package core.chunk;

import static org.junit.Assert.fail;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.chunk.Chunk;
import core.chunk.ChunkFactory;
import core.chunk.ChunkRange;
import core.common.exceptions.EntityNotFound;
import core.configuration.ClientConfig;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.attributes.msc.Coordinates;
import core.entity.block.Block;
import core.entity.block.BlockFactory;
import org.junit.Test;

public class testChunk {

  @Test
  public void testGetBlock() throws EntityNotFound {
    Injector injector = Guice.createInjector(new ClientConfig());
    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);
    Chunk chunk = chunkFactory.create(new ChunkRange(new Coordinates(0, 0)));
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);
    BlockFactory blockFactory = injector.getInstance(BlockFactory.class);
    Entity entity = entityFactory.createEntity(new Coordinates(0, 0));
    assert entity.coordinates.equals(new Coordinates(0, 0));
    chunk.addEntity(entity);
    try {
      assert chunk.getBlock(new Coordinates(0, 0)) == null;
      fail();
    } catch (Exception ignored) {
    }
    Block dirtBlock = blockFactory.createDirt(new Coordinates(0, 0));
    assert dirtBlock.coordinates.equals(new Coordinates(0, 0));
    chunk.addEntity(dirtBlock);
    assert chunk.getBlock(new Coordinates(0, 0)).equals(dirtBlock);
  }

  @Test
  public void testChunkEquals() {
    Injector injector = Guice.createInjector(new ClientConfig());
    ChunkFactory chunkFactory = injector.getInstance(ChunkFactory.class);
    Chunk chunk1 = chunkFactory.create(new ChunkRange(new Coordinates(0, 0)));
    Chunk chunk2 = chunkFactory.create(new ChunkRange(new Coordinates(0, 0)));
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    for (int i = 0; i < 10; i++) {
      Entity entity = entityFactory.createEntity(new Coordinates(0, 0));
      chunk1.addEntity(entity);
      assert !chunk1.equals(chunk2);
      chunk2.addEntity(entity);
    }

    assert chunk1.equals(chunk2);
  }
}