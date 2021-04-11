package iot.mrdrivingduck.kismet.util;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public class Logger {
  public static void init() throws Exception {
    System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4j2LogDelegateFactory");
    System.setProperty("log4j2.skipJansi", "false"); // VM options
    System.setProperty("sun.stdout.encoding", "UTF-8");
    ConfigurationSource source = new ConfigurationSource(Logger.class.getResourceAsStream("/log4j2.xml"));
    Configurator.initialize(null, source);
  }
}
