# Kismet-java-client ![image](https://img.shields.io/badge/vert.x-4.0.3-purple.svg)

🌉 A Java client for [*kismet*](https://github.com/kismetwireless/kismet) RESTful API.

## Environments

This application was generated using [Vert.x starter](http://start.vertx.io).

- *kismet* release: 2020-12
- Java 8+

## Building as Independent Application

To launch your tests:

```bash
./mvnw clean test
```

To package your application:

```bash
./mvnw clean package
```

To run your application:

```bash
./mvnw clean compile exec:java
```

## Running as Dependency

You can run it as an independent *Vert.x verticle*. These code can be added in `start()` callback of *Vert.x verticle*.

```java
/*
 * STEP 1: Initialize a KismetClient object, it knows how to connect and
 * request kismet server.
 */
KismetClient client = new KismetClient(
    vertx,            // Vert.x instance
    3000,             // interval to request kismet server (millisecond)
    "192.168.2.106",  // IP address of kismet server
    2501,             // port of kismet server
    "username",       // username of kismet server (configured on Web UI)
    "password"        // password of kismet server (configured on Web UI)
);

/*
 * STEP 2: Initialize a KismetListener object, it knows how to deal with
 * the subscribed messages from kismet server.
 */
KismetListener listener = new KismetListener() {
    @Override
    public void onMessage(AbstractKismetMessage message) {
        // deal with kismet message
    }

    @Override
    public void onTerminate(String reason) {
        // invoked when client exit.
    }
};

/*
 * STEP 3: Subscribe the kismet message you are interested on listener.
 */
listener.subscribe(TimeMessage.class);
listener.subscribe(BSSIDMessage.class);
listener.subscribe(ClientMessage.class);
listener.subscribe(AlertMessage.class);
listener.subscribe(MsgMessage.class);

/*
 * STEP 4: Register the listener to the client. The client will begin to
 * request messages which are subscribed by listeners.
 */
client.register(listener);
// client.close();
```

## Kismet Message Extension

Kismet messages are user-defined data objects which gather several fields being interested by users. For example, if you are interested on the information of a Wi-Fi AP, you can implement a kismet message called `APMessage`, with some fields you can retrieve from kismet server, like SSID, MAC address, etc.

A kismet message class must inherent from class `AbstractKismetMessage`, and add the following necessary information:

1. Fields which you are interested in, together with their getters / setters
2. Annotations on the field setter describing how to fetch the field data from responses
3. Annotations on class describing which end point to fetch the information from kismet server

Here is an example on how to implement your own `APMessage`:

```java
@MessageType("AP")  // message type
@KismetApi("/devices/views/phydot11_accesspoints/devices.json")  // end point of kismet server to request
public class APMessage extends AbstractKismetMessage {
    
    // fields
    private String ssid;
    private int signal;
    
    // getters
    public String getSsid() { return ssid; }
    public String getSignal() { return signal; }
    
    // setters
    
    /*
     * @KismetApiPath tells kismet server the field you are interested.
     * @ResourceKey is the key to fetch this field from the respond JSON.
     *
     * For example, you tell kismet server you want to get "kismet.device.base.name",
     * then, kismet server will response a JSON object with key "kismet.device.base.name".
     */
    @KismetApiPath("kismet.device.base.name")
    @ResourceKey("kismet.device.base.name")
    public void setSsid(String ssid) { this.ssid = ssid; }
    
    /*
     * Things are a little bit different here. The field you are interested
     * are in a specific JSON object of kismet's response:
     * 
     * {
     *   "kismet.device.base.signal": {
     *       "kismet.common.signal.last_signal": -58
     *   }
     * }
     *
     * Here, @KismetApiPath specifies the detailed field you want to fetch,
     * and kismet will response a field aliased with @ResourceKey:
     * 
     * {
     *   "kismet.common.signal.last_signal": -58
     * }
     *
     * Then kismet client can get the field from response JSON by @ResourceKey.
     */
    @KismetApiPath("kismet.device.base.signal/kismet.common.signal.last_signal")
    @ResourceKey("kismet.common.signal.last_signal")
    public void setSignal(int signal) { this.signal = signal; }
}
```

**Please comfort the rules ahead**, because kismet client use **reflection** of message class to generate message.

Also, to see the entire JSON object respond by end point of kismet server, see [`kismet_data/`](kismet_data/).

## Help

### Kismet

* [WebUI REST endpoints](https://www.kismetwireless.net/docs/devel/webui_rest/endpoints/)
* [Field Specifications](https://www.kismetwireless.net/docs/devel/webui_rest/commands/#field-specifications)
* [Logins & sessions](https://www.kismetwireless.net/docs/devel/webui_rest/logins/)

### Vert.x

* [Vert.x Documentation](https://vertx.io/docs/)
* [Vert.x Stack Overflow](https://stackoverflow.com/questions/tagged/vert.x?sort=newest&pageSize=15)
* [Vert.x User Group](https://groups.google.com/forum/?fromgroups#!forum/vertx)
* [Vert.x Gitter](https://gitter.im/eclipse-vertx/vertx-users)

---

