package iot.mrdrivingduck.kismet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describing the relative path to fetch the interested infomation.
 * 
 * @author Mr Dk.
 * @version 2021/04/09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface KismetApiPath {
  String value();
}
