package iot.mrdrivingduck.kismet.message;

import iot.mrdrivingduck.kismet.annotation.KismetApi;
import iot.mrdrivingduck.kismet.annotation.KismetApiPath;
import iot.mrdrivingduck.kismet.annotation.MessageType;
import iot.mrdrivingduck.kismet.annotation.ResourceKey;

/**
 * Timestamp information provided by kismet.
 *
 * @author Mr Dk.
 * @version 2021/04/09
 */
@MessageType("TIMESTAMP")
@KismetApi("/system/timestamp.json")
public class TimeMessage extends AbstractKismetMessage {
  private long sec;
  private long usec;

  @KismetApiPath("kismet.system.timestamp.sec")
  @ResourceKey("kismet.system.timestamp.sec")
  public void setSec(Integer sec) {
    this.sec = sec;
  }

  @KismetApiPath("kismet.system.timestamp.usec")
  @ResourceKey("kismet.system.timestamp.usec")
  public void setUsec(Integer usec) {
    this.usec = usec;
  }

  public long getSec() {
    return sec;
  }

  public long getUsec() {
    return usec;
  }

  @Override
  public String toString() {
    return new StringBuilder("TimeMessage: {")
      .append("sec=")
      .append(sec)
      .append(", usec=")
      .append(usec)
      .append('}')
      .toString();
  }
}
