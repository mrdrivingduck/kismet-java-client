package iot.mrdrivingduck.kismet.message;

import iot.mrdrivingduck.kismet.annotation.KismetApi;
import iot.mrdrivingduck.kismet.annotation.KismetApiPath;
import iot.mrdrivingduck.kismet.annotation.MessageType;
import iot.mrdrivingduck.kismet.annotation.ResourceKey;
import iot.mrdrivingduck.kismet.util.VendorUtil;

import java.util.Date;

/**
 * Wi-Fi AP data.
 *
 * @author Mr Dk.
 * @version 2021/04/12
 */
@MessageType("BSSID")
@KismetApi("/devices/views/phydot11_accesspoints/last-time/%d/devices.json")
public class BSSIDMessage extends AbstractKismetMessage {
  private String mac;
  private String ssid;
  private String channel;
  private int dataPackets;
  private int encryptedPackets;
  private int llcPackets;
  private String manufacturer;
  private Date firstTime;
  private Date lastTime;
  private int signalDBM;
  private int signalDBMMin;
  private int signalDBMMax;
  private int noiseDBM;
  private int noiseDBMMin;
  private int noiseDBMMax;
  private long dataBytes;
  private String nickname;
  private String cryptType;
  private long checksum;


  @KismetApiPath("kismet.device.base.crypt")
  @ResourceKey("kismet.device.base.crypt")
  public void setCryptType(String cryptType) {
    this.cryptType = cryptType;
  }

  public void setChecksum(long checksum) {
    this.checksum = checksum;
  }

  @KismetApiPath("kismet.device.base.macaddr")
  @ResourceKey("kismet.device.base.macaddr")
  public void setMac(String mac) {
    this.mac = mac;
  }

  @KismetApiPath("kismet.device.base.name")
  @ResourceKey("kismet.device.base.name")
  public void setSsid(String ssid) {
    this.ssid = ssid;
  }

  @KismetApiPath("kismet.device.base.channel")
  @ResourceKey("kismet.device.base.channel")
  public void setChannel(String channel) {
    this.channel = channel;
  }

  @KismetApiPath("kismet.device.base.packets.data")
  @ResourceKey("kismet.device.base.packets.data")
  public void setDataPackets(int dataPackets) {
    this.dataPackets = dataPackets;
  }

  @KismetApiPath("kismet.device.base.packets.crypt")
  @ResourceKey("kismet.device.base.packets.crypt")
  public void setEncryptedPackets(int encryptedPackets) {
    this.encryptedPackets = encryptedPackets;
  }

  @KismetApiPath("kismet.device.base.packets.llc")
  @ResourceKey("kismet.device.base.packets.llc")
  public void setLlcPackets(int llcPackets) {
    this.llcPackets = llcPackets;
  }

  @KismetApiPath("kismet.device.base.macaddr")
  @ResourceKey("kismet.device.base.macaddr")
  public void setManufacturer(String mac) {
    this.manufacturer = VendorUtil.getInstance().getOrganization(mac);
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

  @KismetApiPath("kismet.device.base.datasize")
  @ResourceKey("kismet.device.base.datasize")
  public void setDataBytes(long dataBytes) {
    this.dataBytes = dataBytes;
  }

  @KismetApiPath("kismet.device.base.commonname")
  @ResourceKey("kismet.device.base.commonname")
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  @Override
  public String toString() {
    return "BSSIDMessage: {" +
      "mac:" + mac +
      ", ssid:" + ssid +
      ", crypt:" + cryptType +
      ", manufacturer: " + manufacturer +
      "}";
  }

  public String getCryptType() {
    return cryptType;
  }

  public String getNickname() {
    return nickname;
  }

  public long getDataBytes() {
    return dataBytes;
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

  public String getManufacturer() {
    return manufacturer;
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

  public String getChannel() {
    return channel;
  }

  public String getSsid() {
    return ssid;
  }

  public String getMac() {
    return mac;
  }

  public long getChecksum() {
    return checksum;
  }

}
