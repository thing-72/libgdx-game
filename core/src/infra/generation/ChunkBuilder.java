package infra.generation;

import com.google.inject.Inject;
import infra.app.GameController;
import infra.chunk.Chunk;
import infra.chunk.ChunkFactory;
import infra.chunk.ChunkRange;
import infra.common.Coordinates;
import infra.common.GameStore;
import infra.entity.Entity;

import java.util.concurrent.Callable;

public class ChunkBuilder implements Callable<Chunk> {

  ChunkFactory chunkFactory;

  GameStore gameStore;

  BlockGenerator blockGenerator;

  GameController gameController;

  ChunkRange chunkRange;

  @Inject
  ChunkBuilder(
      ChunkFactory chunkFactory,
      GameStore gameStore,
      BlockGenerator blockGenerator,
      GameController gameController,
      ChunkRange chunkRange) {
    this.chunkFactory = chunkFactory;
    this.gameStore = gameStore;
    this.blockGenerator = blockGenerator;
    this.gameController = gameController;
    this.chunkRange = chunkRange;
  }

  @Override
  public Chunk call() throws Exception {
    try {
      Chunk chunk;
      if (this.gameStore.getChunk(this.chunkRange) == null) {
        chunk = this.chunkFactory.create(this.chunkRange);
        this.gameStore.addChunk(chunk);
      } else {
        chunk = this.gameStore.getChunk(this.chunkRange);
      }
      for (int i = chunkRange.bottom_x; i < chunkRange.top_x; i++) {
        for (int j = chunkRange.bottom_y; j < chunkRange.top_y; j++) {
          Entity block = blockGenerator.generate(new Coordinates(i, j));
          gameController.createEntity(block);
        }
      }

      return chunk;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
