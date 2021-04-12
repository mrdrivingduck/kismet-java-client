package iot.mrdrivingduck.kismet.message;

import iot.mrdrivingduck.kismet.annotation.KismetApi;
import iot.mrdrivingduck.kismet.annotation.KismetApiPath;
import iot.mrdrivingduck.kismet.annotation.MessageType;
import iot.mrdrivingduck.kismet.annotation.ResourceKey;

/**
 * Kismet message from kismet.
 *
 * @author Mr Dk.
 * @version 2021/04/12
 */
@MessageType("MSG")
@KismetApi("/messagebus/all_messages.json")
public class MsgMessage extends AbstractKismetMessage {
  private String content;
  private int flags;
  private long time;

  @KismetApiPath("kismet.messagebus.message_string")
  @ResourceKey("kismet.messagebus.message_string")
  public void setContent(String content) {
    this.content = content;
  }

  @KismetApiPath("kismet.messagebus.message_flags")
  @ResourceKey("kismet.messagebus.message_flags")
  public void setFlags(Integer flags) {
    this.flags = flags;
  }

  @KismetApiPath("kismet.messagebus.message_time")
  @ResourceKey("kismet.messagebus.message_time")
  public void setTime(Integer time) {
    this.time = time;
  }

  public String getContent() {
    return content;
  }

  public int getFlags() {
    return flags;
  }

  public long getTime() {
    return time;
  }

  @Override
  public String toString() {
    return "MsgMessage: {" +
      "time=" + time +
      ", content=\"" + content + "\"," +
      "flags=" + flags + "}";
  }
}
