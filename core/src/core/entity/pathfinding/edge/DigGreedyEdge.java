package core.entity.pathfinding.edge;

import static core.app.screen.GameScreen.pathDebugRender;

import core.app.game.GameController;
import core.chunk.world.exceptions.BodyNotFound;
import com.badlogic.gdx.graphics.Color;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EdgeStepperException;
import core.common.exceptions.EntityNotFound;
import core.entity.attributes.msc.Coordinates;
import core.entity.block.Block;
import core.entity.block.BlockFactory;
import core.entity.block.SkyBlock;
import core.entity.pathfinding.EntityStructure;
import core.entity.pathfinding.PathGameStoreOverride;
import core.entity.pathfinding.RelativeCoordinates;
import core.entity.pathfinding.RelativePathNode;
import core.entity.pathfinding.RelativeVertex;
import core.entity.Entity;

public class DigGreedyEdge extends HorizontalGreedyEdge {
  GameController gameController;
  GameStore gameStore;
  BlockFactory blockFactory;
  RelativeCoordinates digPosition;

  public DigGreedyEdge(
      GameController gameController,
      GameStore gameStore,
      BlockFactory blockFactory,
      EntityStructure entityStructure,
      RelativeVertex position,
      RelativeCoordinates digPosition) {
    super(entityStructure, position, position);
    this.gameController = gameController;
    this.gameStore = gameStore;
    this.blockFactory = blockFactory;
    this.digPosition = digPosition;
  }

  @Override
  public double getCost() {
    return 3;
  }

  public EdgeStepper getEdgeStepper(Entity entity, RelativePathNode relativePathNode) {
    return new DigEdgeStepper(
        this.gameController, this.gameStore, this.blockFactory, this.digPosition);
  }

  @Override
  public void appendPathGameStoreOverride(
      PathGameStoreOverride pathGameStoreOverride, Coordinates sourceCoordinates) {

    pathGameStoreOverride.registerEntityTypeOverride(
        SkyBlock.class, this.digPosition.applyRelativeCoordinates(sourceCoordinates));
  }

  @Override
  public void render(Coordinates position) {
    pathDebugRender.setColor(Color.YELLOW);
    super.render(position);
  }
}

class DigEdgeStepper extends HorizontalEdgeStepper {
  GameController gameController;
  GameStore gameStore;
  BlockFactory blockFactory;
  RelativeCoordinates digPosition;

  public DigEdgeStepper(
      GameController gameController,
      GameStore gameStore,
      BlockFactory blockFactory,
      RelativeCoordinates digPosition) {
    this.gameController = gameController;
    this.gameStore = gameStore;
    this.blockFactory = blockFactory;
    this.digPosition = digPosition;
  }

  @Override
  public void follow(Entity entity, RelativePathNode relativePathNode)
      throws EdgeStepperException, ChunkNotFound, BodyNotFound {
    Block targetBlock =
        blockFactory.createSky(
            this.digPosition.applyRelativeCoordinates(relativePathNode.startPosition));
    try {
      this.gameController.replaceBlock(
          this.gameStore.getBlock(
              this.digPosition.applyRelativeCoordinates(relativePathNode.startPosition)),
          targetBlock);
    } catch (EntityNotFound e) {
      throw new EdgeStepperException(e.toString());
    }
    super.follow(entity, relativePathNode);
  }
}