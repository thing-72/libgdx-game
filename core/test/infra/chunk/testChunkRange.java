package infra.chunk;

import infra.common.Coordinates;
import org.junit.Test;

public class testChunkRange {
  @Test
  public void testHashEqual() {
    ChunkRange chunkRange1 = new ChunkRange(new Coordinates(0, 0));
    ChunkRange chunkRange2 = new ChunkRange(new Coordinates(0, 0));
    System.out.println(chunkRange1);
    System.out.println(chunkRange2);
    assert chunkRange1.equals(chunkRange2);
  }

  @Test
  public void testRelative() {
    ChunkRange chunkRange1 = new ChunkRange(new Coordinates(0, 0));
    assert chunkRange1.getLeft().equals(new ChunkRange(new Coordinates(-1, 0)));
    assert chunkRange1.getRight().equals(new ChunkRange(new Coordinates(ChunkRange.size, 0)));
    assert chunkRange1.getDown().equals(new ChunkRange(new Coordinates(0, -ChunkRange.size)));
    assert chunkRange1.getUp().equals(new ChunkRange(new Coordinates(0, ChunkRange.size)));
  }
}