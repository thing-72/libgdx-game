package old.infra.entity;

import com.google.inject.Inject;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class EntityManager {

  HashMap<UUID, Entity> entityMap;

  @Inject
  EntityManager() {
    this.entityMap = new HashMap();
  }

  public void add(Entity data) {
    this.entityMap.put(data.getID(), data);
  }

  public Entity get(UUID id) {
    return this.entityMap.get(id);
  }

  public Entity[] getAll() {
    return this.entityMap.values().toArray(new Entity[0]);
  }

  public void update(Consumer<Entity> entityConsumer) {
    for (Entity entity : this.entityMap.values()) {
      entityConsumer.accept(entity);
    }
  }

  public void remove(UUID id) {
    this.entityMap.remove(id);
  }

  public void remove(String id) {
    this.entityMap.remove(UUID.fromString(id));
  }
}