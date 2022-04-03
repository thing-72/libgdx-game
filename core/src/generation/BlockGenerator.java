package generation;

import app.GameController;
import com.google.inject.Inject;
import common.Coordinates;
import common.exceptions.ChunkNotFound;
import entity.Entity;

public class BlockGenerator {

  @Inject GameController gameController;

  @Inject
  BlockGenerator() {}

  public Entity generate(Coordinates coordinates) throws ChunkNotFound {
    if (coordinates.getY() > 0) {
      return gameController.createSkyBlock(coordinates);
    } else if (Math.random() < 0.1) {
      return gameController.createStoneBlock(coordinates);
    } else {
      return gameController.createDirtBlock(coordinates);
    }
  }
}
