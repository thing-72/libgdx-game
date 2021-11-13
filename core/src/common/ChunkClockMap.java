package common;

import chunk.Chunk;
import chunk.ChunkRange;
import com.google.inject.Inject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ChunkClockMap {

  Map<ChunkRange, Chunk> map;

  @Inject
  ChunkClockMap() {
    this.map = new ConcurrentHashMap<>();
  }

  void add(Chunk chunk) {
    this.map.put(chunk.chunkRange, chunk);
  }

  Chunk get(ChunkRange chunkRange) {
    return this.map.get(chunkRange);
  }

  public List<ChunkRange> getChunkRangeList() {
    return new LinkedList<>(this.map.keySet());
  }

  List<Callable<Chunk>> getChunksOnTick(Tick tick) {
    return this.map.values().stream()
        .filter(chunk -> chunk.updateTick.time == tick.time)
        .collect(Collectors.toList());
  }
}