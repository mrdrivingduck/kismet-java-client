package iot.mrdrivingduck.kismet;

import iot.mrdrivingduck.kismet.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    iot.mrdrivingduck.kismet.util.Logger.init();

    KismetClient client = new KismetClient(
      vertx, 3000,
      "192.168.2.106", 2501,
      "username", "password");

    KismetListener listener = new KismetListener() {
      @Override
      public void onMessage(AbstractKismetMessage message) {
        logger.info(message);
      }

      @Override
      public void onTerminate(String reason) {
        logger.warn(reason);
      }
    };

    listener.subscribe(TimeMessage.class);
    listener.subscribe(BSSIDMessage.class);
    listener.subscribe(ClientMessage.class);
    listener.subscribe(AlertMessage.class);
    listener.subscribe(MsgMessage.class);

    client.register(listener);
//    client.close();
  }
}
