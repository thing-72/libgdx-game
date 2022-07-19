package core.networking.translation;

import core.chunk.ChunkFactory;
import core.chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.common.GameStore;
import core.common.events.EventConsumer;
import core.common.events.EventService;
import core.common.exceptions.EntityNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.configuration.ClientConfig;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.attributes.msc.Coordinates;
import core.entity.block.BlockFactory;
import core.networking.translation.NetworkDataDeserializer;
import core.networking.translation.NetworkEventHandler;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import core.networking.events.EventTypeFactory;
import org.junit.Before;
import org.junit.Test;

public class testEntitySerialization {

  NetworkDataDeserializer entitySerializationConverter;

  GameStore gameStore;

  Injector injector;
  EntityFactory entityFactory;
  BlockFactory blockFactory;
  ChunkFactory chunkFactory;

  EventService eventService;
  EventTypeFactory eventTypeFactory;
  NetworkEventHandler networkEventHandler;
  EventConsumer eventConsumer;

  @Before
  public void setup() throws IOException {
    injector = Guice.createInjector(new ClientConfig());
    entityFactory = injector.getInstance(EntityFactory.class);
    entitySerializationConverter = injector.getInstance(NetworkDataDeserializer.class);
    gameStore = injector.getInstance(GameStore.class);
    chunkFactory = injector.getInstance(ChunkFactory.class);
    eventService = injector.getInstance(EventService.class);
    eventTypeFactory = injector.getInstance(EventTypeFactory.class);
    blockFactory = injector.getInstance(BlockFactory.class);
    networkEventHandler = injector.getInstance(NetworkEventHandler.class);
    eventConsumer = injector.getInstance(EventConsumer.class);
    eventConsumer.init();
  }

  @Test
  public void testCreateEntitySerialization() throws SerializationDataMissing {
    Entity entityWrite = entityFactory.createEntity(new Coordinates(2, 3));
    Entity entityRead = entitySerializationConverter.createEntity(entityWrite.toNetworkData());
    assert entityWrite.coordinates.equals(entityRead.coordinates);
    assert entityWrite.getUuid().equals(entityRead.getUuid());
  }

  @Test
  public void testCreateEntityNetworkEvent() throws EntityNotFound, InterruptedException {
    Entity entityWrite = entityFactory.createEntity(new Coordinates(0, 0));
    UUID uuid = entityWrite.getUuid();
    gameStore.addChunk(chunkFactory.create(new ChunkRange(entityWrite.coordinates)));
    networkEventHandler.handleNetworkEvent(
        EventTypeFactory.createCreateEntityOutgoingEvent(
                entityWrite.toNetworkData(), new ChunkRange(entityWrite.coordinates))
            .toNetworkEvent());
    TimeUnit.SECONDS.sleep(1);
    assert uuid.equals(gameStore.getEntity(uuid).getUuid());
  }

  @Test
  public void testBlockWrite() throws EntityNotFound, InterruptedException {
    Entity block = blockFactory.createDirt(new Coordinates(0, 0));
    UUID uuid = block.getUuid();
    gameStore.addChunk(chunkFactory.create(new ChunkRange(block.coordinates)));
    networkEventHandler.handleNetworkEvent(
        EventTypeFactory.createCreateEntityOutgoingEvent(
                block.toNetworkData(), new ChunkRange(block.coordinates))
            .toNetworkEvent());
    TimeUnit.SECONDS.sleep(1);
    assert uuid.equals(gameStore.getEntity(uuid).getUuid());
    assert gameStore.getEntity(uuid).getClass().getName().equals(block.getClass().getName());
  }
}