package iot.mrdrivingduck.kismet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import iot.mrdrivingduck.kismet.annotation.ResourceKey;
import iot.mrdrivingduck.kismet.message.AbstractKismetMessage;
import iot.mrdrivingduck.kismet.message.TimeMessage;
import iot.mrdrivingduck.kismet.util.Requester;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A kismet Java client.
 *
 * @author Mr Dk.
 * @version 2021/04/09
 */
public class KismetClient {

  private static final Logger logger = LogManager.getLogger(KismetClient.class);

  private long timestamp = 0L; // timestamp in kismet
  private boolean login = false;

  private final AtomicBoolean running = new AtomicBoolean(true);
  private long timerID;

  private final List<KismetListener> listeners;
  private final Set<Class<? extends AbstractKismetMessage>> subscriptions;

  private Class<? extends AbstractKismetMessage> requestingMsgType;

  public KismetClient(final Vertx vertx, long period, String host, int port) {
    listeners = new ArrayList<>();
    subscriptions = new HashSet<>();

    Requester.init(vertx, host, port);
    timerID = vertx.setPeriodic(period, timeout -> {
      if (!running.get()) {
        // onTerminate all listeners
        for (KismetListener listener : listeners) {
          listener.onTerminate("Client killed.");
        }
        vertx.cancelTimer(timerID); // cancel current timer
        vertx.close();
        return;
      }

      Future<String> future = Future.succeededFuture();

      if (!login) {
        future = future
          .compose(empty -> Requester.login("username", "password"))
          .compose(v -> {
            login = true;
            return Future.succeededFuture();
          });
      }

      if (subscriptions.contains(TimeMessage.class)) {
        future = future.compose(message -> {
          // deal with previous message
          if (requestingMsgType != null) {
            publishMessage(message, requestingMsgType);
          }

          // requesting for next message
          requestingMsgType = TimeMessage.class;
          return Requester.request(HttpMethod.GET, TimeMessage.class);
        });
      }

      /**
       *
       * more messages to be handled here...
       */

      // time message is necessary
      future.compose(message -> {
        if (requestingMsgType != null) {
          publishMessage(message, requestingMsgType);
        }

        requestingMsgType = TimeMessage.class;
        return Requester.request(HttpMethod.GET, TimeMessage.class);
      }).compose(message -> {
        TimeMessage time = (TimeMessage) generateMessage(message, TimeMessage.class);
        timestamp = time.getSec(); // update kismet timestamp

        requestingMsgType = null;
        return Future.succeededFuture();
      }).onFailure(error -> {
        requestingMsgType = null;
        login = false;
      });
    });
  }

  private void publishMessage(
    String resource,
    Class<? extends AbstractKismetMessage> msgType) {
    synchronized (listeners) {
      for (KismetListener listener : listeners) {
        if (listener.getSubscriptions().contains(msgType)) {
          // generate unique message for every subscriber listener
          listener.onMessage(generateMessage(resource, msgType));
        }
      }
    }
  }

  private AbstractKismetMessage generateMessage(
    String resource,
    Class<? extends AbstractKismetMessage> msgType) {

    JsonObject json = new JsonObject(resource);
    AbstractKismetMessage msg = null;
    try {
      msg = (AbstractKismetMessage) msgType.newInstance(); // an object

      Method[] methods = msgType.getMethods();
      Arrays.sort(methods, new Comparator<Method>() {
        @Override
        public int compare(Method m, Method n) {
          return m.getName().compareTo(n.getName());
        }
      });

      for (Method method : methods) {
        ResourceKey key = method.getAnnotation(ResourceKey.class);
        // whether the method is a valid setter of message class
        if (key != null && json.containsKey(key.value()) &&
          method.getParameterTypes().length == 1) {
          method.invoke(msg, json.getValue(key.value())); // invoke setter
        }
      }
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }

    return msg;
  }

  public synchronized void register(KismetListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
      listener.setClient(this);
      updateSubscriptions();
    }
  }

  public synchronized void unregister(KismetListener listener) {
    if (listeners.contains(listener)) {
      listeners.remove(listener);
      listener.setClient(null);
      updateSubscriptions();
    }
  }

  public void updateSubscriptions() {
    subscriptions.clear();
    for (KismetListener listener : listeners) {
      subscriptions.addAll(listener.getSubscriptions());
    }
  }

  public void close() {
    if (running.get()) {
      running.set(false);
    }
  }
}
