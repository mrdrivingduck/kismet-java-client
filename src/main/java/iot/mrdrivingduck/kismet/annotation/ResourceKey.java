package iot.mrdrivingduck.kismet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Key of fields in the JSON object returned by kismet.
 * 
 * @author Mr Dk.
 * @version 2021/04/09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceKey {
  String value();
}
