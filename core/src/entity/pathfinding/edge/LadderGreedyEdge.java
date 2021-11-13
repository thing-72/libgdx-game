package entity.pathfinding.edge;

import app.GameController;
import common.Coordinates;
import entity.Entity;
import entity.misc.Ladder;
import entity.pathfinding.EntityStructure;
import entity.pathfinding.PathGameStoreOverride;
import entity.pathfinding.RelativePathNode;
import entity.pathfinding.RelativeVertex;

public class LadderGreedyEdge extends HorizontalGreedyEdge {
  GameController gameController;

  public LadderGreedyEdge(
      GameController gameController,
      EntityStructure entityStructure,
      RelativeVertex from,
      RelativeVertex to) {
    super(entityStructure, from, to);
    this.gameController = gameController;
  }

  @Override
  public double getCost() {
    return 2;
  }

  @Override
  public void appendPathGameStoreOverride(
      PathGameStoreOverride pathGameStoreOverride, Coordinates sourceCoordinates) {

    pathGameStoreOverride.registerEntityTypeOverride(
        Ladder.class, this.applyTransition(sourceCoordinates));
  }

  @Override
  public EdgeStepper getEdgeStepper(Entity entity, RelativePathNode relativePathNode) {
    return new LadderEdgeStepper(this.gameController);
  }
}

class LadderEdgeStepper extends HorizontalEdgeStepper {
  GameController gameController;

  public LadderEdgeStepper(GameController gameController) {
    this.gameController = gameController;
  }

  @Override
  public void follow(Entity entity, RelativePathNode relativePathNode) throws Exception {
    this.gameController.createLadder(relativePathNode.getEndPosition());
    super.follow(entity, relativePathNode);

    String actionKey;
    if (relativePathNode.getEndPosition().getYReal() > entity.coordinates.getYReal()) {
      actionKey = "climbUp";
    } else if (relativePathNode.getEndPosition().getYReal() < entity.coordinates.getYReal()) {
      actionKey = "climbDown";
    } else return;

    entity.entityController.applyAction(actionKey, entity.getBody());
  }
}