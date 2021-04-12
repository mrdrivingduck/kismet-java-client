package iot.mrdrivingduck.kismet.message;

import iot.mrdrivingduck.kismet.KismetClient;
import iot.mrdrivingduck.kismet.annotation.KismetApi;
import iot.mrdrivingduck.kismet.annotation.KismetApiPath;
import iot.mrdrivingduck.kismet.annotation.MessageType;
import iot.mrdrivingduck.kismet.annotation.ResourceKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Alert message from kismet.
 *
 * @author Mr Dk.
 * @version 2021/04/12
 */
@MessageType("ALERT")
@KismetApi("/alerts/last-time/%d.0/alerts.json")
public class AlertMessage extends AbstractKismetMessage {

  private static final Logger logger = LogManager.getLogger(AlertMessage.class);

  private String source;
  private String destination;
  private String bssid;
  private Date firstTime;
  private Date lastTime;
  private String header;
  private String text;
  private String channel;

  @KismetApiPath("kismet.alert.source_mac")
  @ResourceKey("kismet.alert.source_mac")
  public void setSource(String source) {
    this.source = source;
  }

  @KismetApiPath("kismet.alert.dest_mac")
  @ResourceKey("kismet.alert.dest_mac")
  public void setDestination(String destination) {
    this.destination = destination;
  }

  @KismetApiPath("kismet.alert.transmitter_mac")
  @ResourceKey("kismet.alert.transmitter_mac")
  public void setBssid(String bssid) {
    this.bssid = bssid;
  }

  @KismetApiPath("kismet.alert.timestamp")
  @ResourceKey("kismet.alert.timestamp")
  public void setFirstTime(Double firstTime) {
    this.firstTime = new Date(firstTime.longValue() * 1000);
  }

  @KismetApiPath("kismet.alert.timestamp")
  @ResourceKey("kismet.alert.timestamp")
  public void setLastTime(Double lastTime) {
    this.lastTime = new Date(lastTime.longValue() * 1000);
  }

  @KismetApiPath("kismet.alert.header")
  @ResourceKey("kismet.alert.header")
  public void setHeader(String header) {
    this.header = header;
  }

  @KismetApiPath("kismet.alert.text")
  @ResourceKey("kismet.alert.text")
  public void setText(String text) {
    this.text = text;
  }

  @KismetApiPath("kismet.alert.channel")
  @ResourceKey("kismet.alert.channel")
  public void setChannel(String channel) {
    this.channel = channel;
  }

  @Override
  public String toString() {
    return "AlertMessage: {" +
      "header:" + header +
      ", time:" + firstTime +
      ", source:" + source +
      ", dest:" + destination +
      ", transmitter:" + bssid +
      ", text:\"" + text + "\"}";
  }

  public String getChannel() {
    return channel;
  }

  public String getText() {
    return text;
  }

  public String getHeader() {
    return header;
  }

  public Date getFirstTime() {
    return firstTime;
  }

  public Date getLastTime() {
    return lastTime;
  }

  public String getBssid() {
    return bssid;
  }

  public String getDestination() {
    return destination;
  }

  public String getSource() {
    return source;
  }
}
