package common.events.types;

import static networking.translation.NetworkDataSerializer.createCreateAIEntityEventType;

import entity.attributes.msc.Coordinates;
import java.util.UUID;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkEvent;

public class CreateAIEntityEventType extends EventType implements SerializeNetworkEvent {

  public static String type = "create_ai";

  Coordinates coordinates;
  UUID target;

  public CreateAIEntityEventType(Coordinates coordinates, UUID target) {
    this.coordinates = coordinates;
    this.target = target;
  }

  public UUID getTarget() {
    return target;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  @Override
  public String getEventType() {
    return type;
  }

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    return createCreateAIEntityEventType(this);
  }
}
