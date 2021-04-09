package iot.mrdrivingduck.kismet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describing the API to fetch the message.
 * 
 * @author Mr Dk.
 * @version 2021/04/09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface KismetApi {
  String value();
}
