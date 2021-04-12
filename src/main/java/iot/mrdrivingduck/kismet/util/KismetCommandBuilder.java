package iot.mrdrivingduck.kismet.util;

import io.vertx.core.json.JsonObject;
import iot.mrdrivingduck.kismet.annotation.KismetApiPath;
import iot.mrdrivingduck.kismet.annotation.ResourceKey;
import iot.mrdrivingduck.kismet.message.AbstractKismetMessage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used for generating kismet command JSON string.
 *
 * @author Mr Dk.
 * @version 2021/04/21
 */
public class KismetCommandBuilder {

  List<List<String>> fields = new ArrayList<>();
  List<List<String>> regex = new ArrayList<>();

  public KismetCommandBuilder(Class<? extends AbstractKismetMessage> msgType) {
    for (Method method : msgType.getMethods()) {
      KismetApiPath path = method.getAnnotation(KismetApiPath.class);
      ResourceKey alias = method.getAnnotation(ResourceKey.class);
      if (path != null && alias != null) {
        fields.add(Arrays.asList(path.value(), alias.value()));
      }
    }
  }

  public void addRegex(String path, String regex) {
    this.regex.add(Arrays.asList(path, regex));
  }

  public String build() {
    return new JsonObject().put("fields", fields).put("regex", regex).toString();
  }
}
