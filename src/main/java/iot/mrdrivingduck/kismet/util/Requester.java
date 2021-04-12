package iot.mrdrivingduck.kismet.util;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import iot.mrdrivingduck.kismet.annotation.KismetApi;
import iot.mrdrivingduck.kismet.message.AbstractKismetMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Making requests to fetch kismet messages.
 *
 * @author Mr Dk.
 * @version 2021/04/09
 */
public class Requester {

  private static final Logger logger = LogManager.getLogger(Requester.class);

  private static Vertx vertx;
  private static WebClientOptions options;
  private static List<String> cookies;

  public static void init(final Vertx vertx, String host, int port) {
    Requester.vertx = vertx;
    Requester.options = new WebClientOptions()
      .setDefaultHost(host)
      .setDefaultPort(port);
    logger.info(new StringBuilder("Requester initialized with: { host: ")
      .append(host).append(", port: ").append(port).append(" }")
    );
  }

  public static Future<Void> login(String username, String password) {
    return WebClient.create(vertx, options)
      .request(HttpMethod.GET, "/session/check_setup_ok")
      .addQueryParam("user", username)
      .addQueryParam("password", password)
      .send()
      .onFailure(error -> {
        logger.error(error.getMessage());
      })
      .compose(response -> {
        if (response.statusCode() == 200) {
          if (response.cookies() != null) {
            cookies = response.cookies();
          }
          logger.info("Login success with cookie: " + response.cookies().size());
          return Future.succeededFuture();
        }
        logger.error("Login failed with " + response.statusCode() + ": " + response.statusMessage());
        return Future.failedFuture(response.statusMessage());
      });
  }

  public static Future<String> request(
    HttpMethod httpMethod,
    Class<? extends AbstractKismetMessage> msgType,
    Object... uriParams) {

    String uri = String.format(msgType.getAnnotation(KismetApi.class).value(), uriParams);
    WebClient client = WebClient.create(vertx, options);
    HttpRequest<Buffer> request = client.request(httpMethod, uri);

    if (cookies != null) {
      for (String cookie : cookies) {
        request.putHeader("Cookie", cookie);
      }
    }

    return request.send()
      .onFailure(error -> {
        logger.error(new StringBuilder("Request kismet failed because of ").append(error.getMessage()));
      })
      .compose(response -> {
        if (response.statusCode() == 200) {
          if (response.cookies() != null) {
            cookies = response.cookies();
          }
          return Future.succeededFuture(response.bodyAsString());
        }
        return Future.failedFuture(new StringBuilder("Request kismet failed with ")
          .append(response.statusCode()).append(": ").append(response.statusMessage()).toString());
      });
  }
}
