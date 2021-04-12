package iot.mrdrivingduck.kismet.message;

/**
 * Wi-Fi client device in the white list.
 *
 * @author Mr Dk.
 * @version 2021/04/12
 */
public class WLClientMessage {
  private int type;
  private String bssid;
  private String mac;
  private String nickname;
  private String personname;
  private String telephone;

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }


  public String getBssid() {
    return bssid;
  }

  public void setBssid(String bssid) {
    this.bssid = bssid;
  }


  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }


  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getPersonname() {
    return personname;
  }

  public void setPersonname(String personname) {
    this.personname = personname;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  @Override
  public String toString() {
    return new StringBuilder("WLCLIENTMessage{")
      .append("BSSID=").append(bssid)
      .append(", SSID=").append(mac)
      .append(", NICKNAME=").append(nickname)
      .append(", PERSONNAME=").append(personname)
      .append('}').toString();
  }
}
