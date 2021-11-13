package networking.events.consumer.server.incoming;

import app.GameController;
import chunk.ChunkRange;
import chunk.ChunkSubscriptionService;
import com.google.inject.Inject;
import common.events.EventType;
import entity.Entity;
import entity.EntitySerializationConverter;
import generation.ChunkGenerationManager;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.server.ServerNetworkHandle;

import java.util.UUID;
import java.util.function.Consumer;

public class CreateEntityIncomingConsumerServer implements Consumer<EventType> {

  @Inject GameController gameController;
  @Inject EntitySerializationConverter entitySerializationConverter;
  @Inject ChunkSubscriptionService chunkSubscriptionService;
  @Inject ServerNetworkHandle serverNetworkHandle;
  @Inject ChunkGenerationManager chunkGenerationManager;

  @Override
  public void accept(EventType eventType) {
    CreateEntityIncomingEventType realEvent = (CreateEntityIncomingEventType) eventType;
    Entity entity =
        gameController.triggerCreateEntity(
            entitySerializationConverter.createEntity(realEvent.getData()));
    chunkGenerationManager.registerActiveEntity(
        entity, UUID.fromString(realEvent.networkEvent.getUser()));

    for (UUID uuid :
        chunkSubscriptionService.getSubscriptions(new ChunkRange(entity.coordinates))) {
      if (uuid.equals(realEvent.getUser())) continue;
      serverNetworkHandle.send(uuid, realEvent.networkEvent);
    }
  }
}