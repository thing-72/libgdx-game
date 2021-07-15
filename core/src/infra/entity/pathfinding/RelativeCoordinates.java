package infra.entity.pathfinding;

import com.badlogic.gdx.math.Vector2;
import infra.common.Coordinates;
import infra.entity.Entity;

public class RelativeCoordinates {
  float relativeX;
  float relativeY;

  public RelativeCoordinates(float relativeX, float relativeY) {
    this.relativeX = relativeX;
    this.relativeY = relativeY;
  }

  public RelativeCoordinates(Vector2 vector2) {
    this.relativeX = vector2.x / Entity.coordinatesScale;
    this.relativeY = vector2.y / Entity.coordinatesScale;
  }

  public float getRelativeX() {
    return relativeX;
  }

  public float getRelativeY() {
    return relativeY;
  }

  public RelativeCoordinates round() {
    return new RelativeCoordinates((int) this.relativeX, (int) this.relativeY);
  }

  public Coordinates applyRelativeCoordinates(Coordinates coordinates) {
    return new Coordinates(
        coordinates.getXReal() + this.relativeX, coordinates.getYReal() + this.relativeY);
  }

  public RelativeCoordinates getUp() {
    return new RelativeCoordinates(this.getRelativeX(), this.getRelativeY() + 1);
  }

  public RelativeCoordinates getDown() {
    return new RelativeCoordinates(this.getRelativeX(), this.getRelativeY() - 1);
  }

  public RelativeCoordinates getLeft() {
    return new RelativeCoordinates(this.getRelativeX() - 1, this.getRelativeY());
  }

  public RelativeCoordinates getRight() {
    return new RelativeCoordinates(this.getRelativeX() + 1, this.getRelativeY());
  }

  public boolean equalBase(RelativeCoordinates other) {
    return (int) this.relativeX == (int) other.relativeX
        && (int) this.relativeY == (int) other.relativeY;
  }

  public RelativeCoordinates add(RelativeCoordinates other) {
    return new RelativeCoordinates(
        this.relativeX + other.relativeX, this.relativeY + other.relativeY);
  }

  public RelativeCoordinates sub(RelativeCoordinates other) {
    return new RelativeCoordinates(
        this.relativeX - other.relativeX, this.relativeY - other.relativeY);
  }

  @Override
  public int hashCode() {
    return (this.getRelativeX() + "," + this.getRelativeY()).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    RelativeCoordinates other = (RelativeCoordinates) obj;
    return this.getRelativeX() == other.getRelativeX()
        && this.getRelativeY() == other.getRelativeY();
  }

  @Override
  public String toString() {
    return "RelativeCoordinates{" + "relativeX=" + relativeX + ", relativeY=" + relativeY + '}';
  }

  public Vector2 toVector2() {
    return new Vector2(
        this.relativeX * Entity.coordinatesScale, this.relativeY * Entity.coordinatesScale);
  }
}