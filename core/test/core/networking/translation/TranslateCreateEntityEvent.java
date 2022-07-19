package core.networking.translation;

import core.chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.common.exceptions.SerializationDataMissing;
import core.configuration.ClientConfig;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.attributes.msc.Coordinates;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.incoming.CreateEntityIncomingEventType;
import core.networking.events.types.outgoing.CreateEntityOutgoingEventType;
import core.networking.translation.NetworkDataDeserializer;
import core.networking.translation.NetworkDataSerializer;
import org.junit.Test;

public class TranslateCreateEntityEvent {

  @Test
  public void testTranslateCreateEntityEvent() throws SerializationDataMissing {
    Injector injector = Guice.createInjector(new ClientConfig());
    NetworkDataDeserializer networkDataDeserializer =
        injector.getInstance(NetworkDataDeserializer.class);
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    Coordinates coordinates = new Coordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    Entity entity = entityFactory.createEntity(new Coordinates(0, 0));

    CreateEntityOutgoingEventType outgoing =
        EventTypeFactory.createCreateEntityOutgoingEvent(entity.toNetworkData(), chunkRange);
    CreateEntityIncomingEventType incoming =
        NetworkDataDeserializer.createCreateEntityIncomingEventType(
            NetworkDataSerializer.createCreateEntityOutgoingEventType(outgoing));

    assert outgoing.getChunkRange().equals(incoming.getChunkRange());
    assert outgoing.getEntityData().equals(incoming.getData());
  }
}