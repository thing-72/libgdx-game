package infra.entity.collision;

import com.badlogic.gdx.physics.box2d.*;
import com.google.inject.Inject;

public class EntityContactListener implements ContactListener {

  @Inject CollisionService collisionService;

  @Inject
  public EntityContactListener() {
  }

  public void beginContact(Contact contact) {
    Fixture fixtureA = contact.getFixtureA();
    Fixture fixtureB = contact.getFixtureB();
    Object objectA = fixtureA.getUserData();
    Object objectB = fixtureB.getUserData();
    if (objectA == null || objectB == null) return;
    this.collisionService.handleBeginCollision(
        new CollisionPair(objectA.getClass(), objectB.getClass()), objectA, objectB);
  }

  @Override
  public void endContact(Contact contact) {
    Fixture fixtureA = contact.getFixtureA();
    Fixture fixtureB = contact.getFixtureB();
    Object objectA = fixtureA.getUserData();
    Object objectB = fixtureB.getUserData();
    if (objectA == null || objectB == null) return;
    this.collisionService.handleEndCollision(
        new CollisionPair(objectA.getClass(), objectB.getClass()), objectA, objectB);
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {}

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {}
}