package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.events.Event;
import infra.networking.NetworkObjects;

public class CreateEntityOutgoingEvent extends Event {

  NetworkObjects.NetworkData entityData;

  @Inject
  CreateEntityOutgoingEvent(@Assisted NetworkObjects.NetworkData entityData) {
    this.entityData = entityData;
  }

  public NetworkObjects.NetworkEvent getNetworkEvent() {
    return NetworkObjects.NetworkEvent.newBuilder()
        .setData(this.entityData)
        .setEvent("create_entity")
        .build();
  }

  public String getType() {
    return null;
  }
}