package iot.mrdrivingduck.kismet;

import java.util.HashSet;
import java.util.Set;

import iot.mrdrivingduck.kismet.message.AbstractKismetMessage;

/**
 * A single listener who can subscribe several types of message in interest.
 * 
 * @author Mr Dk.
 * @version 2021/04/09
 */
public abstract class KismetListener {

  private Set<Class<? extends AbstractKismetMessage>> subscription = new HashSet<>();
  private KismetClient client = null;

  public void setClient(KismetClient client) {
    if (this.client != null) {
      throw new IllegalArgumentException("Listener has been registered to a client.");
    }
    this.client = client;
  }

  public Set<Class<? extends AbstractKismetMessage>> getSubscriptions() {
    return subscription;
  }

  public void subscribe(Class<? extends AbstractKismetMessage> clazz) {
    if (!subscription.contains(clazz)) {
      subscription.add(clazz);
      if (client != null) {
        client.updateSubscriptions();
      }
    }
  }

  public void unsubscribe(Class<? extends AbstractKismetMessage> clazz) {
    if (subscription.contains(clazz)) {
      subscription.remove(clazz);
      if (client != null) {
        client.updateSubscriptions();
      }
    }
  }

  public abstract void onMessage(AbstractKismetMessage message);

  public abstract void onTerminate(String reason);
}
