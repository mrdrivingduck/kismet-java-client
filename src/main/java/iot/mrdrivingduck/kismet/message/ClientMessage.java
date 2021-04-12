package iot.mrdrivingduck.kismet.message;

import iot.mrdrivingduck.kismet.annotation.KismetApi;
import iot.mrdrivingduck.kismet.annotation.KismetApiPath;
import iot.mrdrivingduck.kismet.annotation.MessageType;
import iot.mrdrivingduck.kismet.annotation.ResourceKey;
import iot.mrdrivingduck.kismet.util.VendorUtil;

import java.util.Date;

/**
 * Wi-Fi client data.
 *
 * @author Mr Dk.
 * @version 2021/04/12
 */
@MessageType("CLIENT")
@KismetApi("/devices/views/phy-IEEE802.11/last-time/%d/devices.json")
public class ClientMessage extends AbstractKismetMessage {
  private String bssid;
  private String mac;
  private Date lastTime;
  private Date firstTime;
  private int signalDBM;
  private int signalDBMMin;
  private int signalDBMMax;
  private int noiseDBM;
  private int noiseDBMMin;
  private int noiseDBMMax;
  private String nickname;
  private int llcPackets;
  private int dataPackets;
  private int encryptedPackets;
  private long dataSize;
  private String manufactor;

  @KismetApiPath("kismet.device.base.macaddr")
  @ResourceKey("kismet.device.base.macaddr")
  public void setManufactor(String mac) {
    this.manufactor = VendorUtil.getInstance().getOrganization(mac);
  }

  @KismetApiPath("kismet.device.base.macaddr")
  @ResourceKey("kismet.device.base.macaddr")
  public void setMac(String mac) {
    this.mac = mac;
  }

  @KismetApiPath("dot11.device/dot11.device.last_bssid")
  @ResourceKey("dot11.device.last_bssid")
  public void setBssid(String bssid) {
    this.bssid = bssid;
  }

  @KismetApiPath("kismet.device.base.packets.data")
  @ResourceKey("kismet.device.base.packets.data")
  public void setDataPackets(int dataPackets) {
    this.dataPackets = dataPackets;
  }

  @KismetApiPath("kismet.device.base.packets.data")
  @ResourceKey("kismet.device.base.packets.data")
  public void setEncryptedPackets(int encryptedPackets) {
    this.encryptedPackets = encryptedPackets;
  }

  @KismetApiPath("kismet.device.base.packets.llc")
  @ResourceKey("kismet.device.base.packets.llc")
  public void setLlcPackets(int llcPackets) {
    this.llcPackets = llcPackets;
  }

  @KismetApiPath("kismet.device.base.first_time")
  @ResourceKey("kismet.device.base.first_time")
  public void setFirstTime(long firstTime) {
    this.firstTime = new Date(firstTime);
  }

  @KismetApiPath("kismet.device.base.last_time")
  @ResourceKey("kismet.device.base.last_time")
  public void setLastTime(long lastTime) {
    this.lastTime = new Date(lastTime);
  }

  @KismetApiPath("kismet.device.base.signal/kismet.common.signal.last_signal")
  @ResourceKey("kismet.common.signal.last_signal")
  public void setSignalDBM(int signalDBM) {
    this.signalDBM = signalDBM;
  }

  @KismetApiPath("kismet.device.base.signal/kismet.common.signal.min_signal")
  @ResourceKey("kismet.common.signal.min_signal")
  public void setSignalDBMMin(int signalDBMMin) {
    this.signalDBMMin = signalDBMMin;
  }

  @KismetApiPath("kismet.device.base.signal/kismet.common.signal.max_signal")
  @ResourceKey("kismet.common.signal.max_signal")
  public void setSignalDBMMax(int signalDBMMax) {
    this.signalDBMMax = signalDBMMax;
  }

  @KismetApiPath("kismet.device.base.signal/kismet.common.signal.last_noise")
  @ResourceKey("kismet.common.signal.last_noise")
  public void setNoiseDBM(int noiseDBM) {
    this.noiseDBM = noiseDBM;
  }

  @KismetApiPath("kismet.device.base.signal/kismet.common.signal.min_noise")
  @ResourceKey("kismet.common.signal.min_noise")
  public void setNoiseDBMMin(int noiseDBMMin) {
    this.noiseDBMMin = noiseDBMMin;
  }

  @KismetApiPath("kismet.device.base.signal/kismet.common.signal.max_noise")
  @ResourceKey("kismet.common.signal.max_noise")
  public void setNoiseDBMMax(int noiseDBMMax) {
    this.noiseDBMMax = noiseDBMMax;
  }

  @KismetApiPath("kismet.device.base.packets.data")
  @ResourceKey("kismet.device.base.packets.data")
  public void setDataSize(long dataSize) {
    this.dataSize = dataSize;
  }

  @KismetApiPath("kismet.device.base.commonname")
  @ResourceKey("kismet.device.base.commonname")
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  @Override
  public String toString() {
    return "ClientMessage: {" +
      "mac:" + mac +
      ", bssid:" + bssid +
      ", manufacture:" + manufactor +
      "}";
  }

  public String getManufactor() {
    return manufactor;
  }

  public String getNickname() {
    return nickname;
  }

  public long getDataSize() {
    return dataSize;
  }

  public int getNoiseDBMMax() {
    return noiseDBMMax;
  }

  public int getNoiseDBMMin() {
    return noiseDBMMin;
  }

  public int getNoiseDBM() {
    return noiseDBM;
  }

  public int getSignalDBMMax() {
    return signalDBMMax;
  }

  public int getSignalDBMMin() {
    return signalDBMMin;
  }

  public int getSignalDBM() {
    return signalDBM;
  }

  public Date getLastTime() {
    return lastTime;
  }

  public Date getFirstTime() {
    return firstTime;
  }

  public int getLlcPackets() {
    return llcPackets;
  }

  public int getEncryptedPackets() {
    return encryptedPackets;
  }

  public int getDataPackets() {
    return dataPackets;
  }

  public String getBssid() {
    return bssid;
  }

  public String getMac() {
    return mac;
  }
}
