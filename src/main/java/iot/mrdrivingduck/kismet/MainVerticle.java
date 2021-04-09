package iot.mrdrivingduck.kismet;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import iot.mrdrivingduck.kismet.message.AbstractKismetMessage;
import iot.mrdrivingduck.kismet.message.TimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public class MainVerticle extends AbstractVerticle {

  private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    System.setProperty("vertx.logger-delegate-factory-class-name",
      "io.vertx.core.logging.Log4j2LogDelegateFactory");
    System.setProperty("log4j2.skipJansi", "false"); // VM options
    System.setProperty("sun.stdout.encoding", "UTF-8");
    ConfigurationSource source = new ConfigurationSource(Logger.class.getResourceAsStream("/log4j2.xml"));
    Configurator.initialize(null, source);

    KismetClient client = new KismetClient(vertx, 3000, "192.168.2.106", 2501);

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
    client.register(listener);

  }
}
