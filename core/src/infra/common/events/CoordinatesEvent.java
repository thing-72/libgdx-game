package infra.common.events;

import com.google.inject.Inject;
import infra.common.Coordinates;

public class CoordinatesEvent extends Event {

  public static String type = "coordinates_event";

  Coordinates coordinates;

  @Inject
  public CoordinatesEvent(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public String getType() {
    return type;
  }
}
