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
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import iot.mrdrivingduck.kismet.annotation.ResourceKey;
import iot.mrdrivingduck.kismet.message.*;
import iot.mrdrivingduck.kismet.util.KismetCommandBuilder;
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

      if (subscriptions.contains(BSSIDMessage.class)) {
        future = future.compose(message -> {
          // deal with previous message
          if (requestingMsgType != null) {
            publishMessage(message, requestingMsgType);
          }

          // requesting for next message
          requestingMsgType = BSSIDMessage.class;
          KismetCommandBuilder kismetCmd = new KismetCommandBuilder(requestingMsgType);
          kismetCmd.addRegex("kismet.device.base.type", "^Wi-Fi AP$");
          return Requester.request(HttpMethod.POST, requestingMsgType, kismetCmd.build(), timestamp);
        });
      }

      if (subscriptions.contains(ClientMessage.class)) {
        future = future.compose(message -> {
          // deal with previous message
          if (requestingMsgType != null) {
            publishMessage(message, requestingMsgType);
          }

          // requesting for next message
          requestingMsgType = ClientMessage.class;
          KismetCommandBuilder kismetCmd = new KismetCommandBuilder(requestingMsgType);
          kismetCmd.addField("kismet.device.base.type", "kismet.device.base.type");
          kismetCmd.addRegex("kismet.device.base.type", "^Wi-Fi Device.*");
          return Requester.request(HttpMethod.POST, requestingMsgType, kismetCmd.build(), timestamp);
        });
      }

      if (subscriptions.contains(AlertMessage.class)) {
        future = future.compose(message -> {
          // deal with previous message
          if (requestingMsgType != null) {
            publishMessage(message, requestingMsgType);
          }

          // requesting for next message
          requestingMsgType = AlertMessage.class;
          KismetCommandBuilder kismetCmd = new KismetCommandBuilder(requestingMsgType);
          return Requester.request(HttpMethod.POST, requestingMsgType, kismetCmd.build(), timestamp);
        });
      }

      if (subscriptions.contains(MsgMessage.class)) {
        future = future.compose(message -> {
          // deal with previous message
          if (requestingMsgType != null) {
            publishMessage(message, requestingMsgType);
          }

          // requesting for next message
          requestingMsgType = MsgMessage.class;
          return Requester.request(HttpMethod.GET, requestingMsgType, null);
        });
      }


      /*
       *
       * more messages to be handled here...
       */

      future.compose(message -> {
        if (requestingMsgType != null) {
          publishMessage(message, requestingMsgType);
        }

        // time message is necessary
        requestingMsgType = TimeMessage.class;
        return Requester.request(HttpMethod.GET, requestingMsgType, null);
      }).compose(message -> {
        if (requestingMsgType != null) {
          publishMessage(message, requestingMsgType);
        }
        TimeMessage time = (TimeMessage) generateMessage(new JsonObject(message), TimeMessage.class);
        timestamp = time.getSec(); // update kismet timestamp

        requestingMsgType = null;
        return Future.succeededFuture();
      }).onFailure(error -> {
        requestingMsgType = null;
        login = false;
        logger.error(error.getMessage(), error);
      });
    });
  }

  private void publishMessage(
    String resource,
    Class<? extends AbstractKismetMessage> msgType) {

    if (resource == null) {
      return;
    }

    JsonArray tempArray;
    if (resource.startsWith("[")) {
      tempArray = new JsonArray(resource);
    } else if (resource.startsWith("{")) {
      tempArray = new JsonArray().add(new JsonObject(resource));
    } else {
      return;
    }

    synchronized (listeners) {
      for (KismetListener listener : listeners) {
        if (listener.getSubscriptions().contains(msgType)) {
          // generate unique message for every subscriber listener
          for (int i = 0; i < tempArray.size(); i++) {
            JsonObject msgJson = tempArray.getJsonObject(i);

            /*
             * Fuck off with kismet's regex filter. It's no use.
             * I have to implement this silly filter.
             */
            if (msgType.equals(ClientMessage.class) &&
              (!msgJson.containsKey("kismet.device.base.type") ||
                msgJson.getString("kismet.device.base.type").equals("Wi-Fi AP"))) {
              continue;
            }

            AbstractKismetMessage msg = generateMessage(msgJson, msgType);
            if (msg != null) {
              listener.onMessage(msg);
            }
          }
        }
      }
    }
  }

  private AbstractKismetMessage generateMessage(
    JsonObject resource,
    Class<? extends AbstractKismetMessage> msgType) {

    AbstractKismetMessage msg = null;
    try {
      msg = (AbstractKismetMessage) msgType.getDeclaredConstructor().newInstance(); // an object

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
        if (key != null && resource.containsKey(key.value())) {
          Class<?>[] paramTypes = method.getParameterTypes();
          Object val = resource.getValue(key.value());
//          logger.warn(val.getClass().getName() + " " + paramTypes[0].getName() + " " + key.value());
          if (paramTypes.length == 1 && val.getClass().equals(paramTypes[0])) {
            method.invoke(msg, resource.getValue(key.value()));  // invoke setter
          }
        }
      }
    } catch (InstantiationException | IllegalAccessException |
      InvocationTargetException | NoSuchMethodException e) {
      logger.error(e.getMessage(), e);
      logger.error(resource);
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
