package entity.pathfinding;

import chunk.Chunk;
import chunk.ChunkFactory;
import chunk.ChunkRange;
import chunk.world.WorldWrapper;
import chunk.world.exceptions.BodyNotFound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Inject;
import entity.Entity;
import entity.EntityFactory;
import entity.attributes.msc.Coordinates;
import entity.block.Block;
import entity.block.BlockFactory;
import entity.block.EmptyBlock;
import entity.block.SolidBlock;
import entity.controllers.actions.EntityAction;
import entity.controllers.actions.EntityActionFactory;
import java.util.Map;

public class RelativeActionEdgeGenerator {

  WorldWrapper worldWrapper;
  Chunk chunk;
  Entity entity;

  @Inject EntityActionFactory entityActionFactory;
  @Inject BlockFactory blockFactory;
  @Inject ChunkFactory chunkFactory;
  @Inject EntityFactory entityFactory;

  RelativeActionEdgeGenerator() {}

  public RelativeActionEdge generateRelativeActionEdge(
      EntityStructure rootEntityStructure, RelativeVertex fromRelativeVertex, String actionKey)
      throws BodyNotFound {
    this.setupWorld(rootEntityStructure, fromRelativeVertex);

    EntityAction entityAction;

    if (actionKey.equals("left"))
      entityAction = entityActionFactory.createHorizontalMovementAction(-5);
    else if (actionKey.equals("right"))
      entityAction = entityActionFactory.createHorizontalMovementAction(5);
    else if (actionKey.equals("jump"))
      entityAction = entityActionFactory.createJumpMovementAction();
    else if (actionKey.equals("stop"))
      entityAction = entityActionFactory.createStopMovementAction();
    else return null;

    worldWrapper.applyBody(
        entity,
        (Body body) -> {
          entityAction.apply(body);
        });

    worldWrapper.tick();

    EntityStructure newEntityStructure = rootEntityStructure.copy();

    Vector2 rootPosition = worldWrapper.getPosition(entity);

    RelativeCoordinates newRelativeCoordinates = new RelativeCoordinates(rootPosition);

    // set empty bottom left
    newEntityStructure.registerRelativeEntity(newRelativeCoordinates, EmptyBlock.class);
    // set empty bottom right
    newEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(rootPosition.cpy().add(Entity.staticWidth, 0)), EmptyBlock.class);
    // set empty top left
    newEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(rootPosition.cpy().add(0, Entity.staticHeight)), EmptyBlock.class);
    // set empty top right
    newEntityStructure.registerRelativeEntity(
        new RelativeCoordinates(rootPosition.cpy().add(Entity.staticWidth, Entity.staticHeight)),
        EmptyBlock.class);

    RelativeVertex toRelativeVertex =
        new RelativeVertex(
            newEntityStructure, newRelativeCoordinates, worldWrapper.getVelocity(entity));

    worldWrapper.applyWorld(
        (World world) -> {
          world.dispose();
        });
    System.gc();

    return new RelativeActionEdge(fromRelativeVertex, toRelativeVertex, actionKey);
  }

  private void setupWorld(EntityStructure entityStructure, RelativeVertex relativeVertex)
      throws BodyNotFound {
    this.worldWrapper = new WorldWrapper(new ChunkRange(new Coordinates(0, 0)));
    this.chunk = chunkFactory.create(new ChunkRange(new Coordinates(0, 0)));

    for (Map.Entry<RelativeCoordinates, Class<? extends Entity>> relativeBlockMapEntry :
        entityStructure.getRelativeEntityMapEntrySet()) {
      Class<? extends Entity> entityClass = relativeBlockMapEntry.getValue();
      RelativeCoordinates blockRelativeCoordinates = relativeBlockMapEntry.getKey();
      if (entityClass.isInstance(SolidBlock.class)) {
        Block block =
            blockFactory.createDirt(
                blockRelativeCoordinates.applyRelativeCoordinates(new Coordinates(0, 0)));
        block.addWorld(chunk);
      }
    }

    this.entity =
        entityFactory.createEntity(
            relativeVertex
                .getRelativeCoordinates()
                .applyRelativeCoordinates(new Coordinates(0, 0)));
    worldWrapper.addEntity(entity.addWorld(chunk));
    worldWrapper.setVelocity(entity, relativeVertex.velocity);
  }
}
