package infra.networking;

import com.google.inject.Inject;
import infra.chunk.ChunkSubscriptionService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConnectionStore {

  Map<UUID, RequestNetworkEventObserver> connectionMap;

  @Inject
  ChunkSubscriptionService chunkSubscriptionService;

  @Inject
  public ConnectionStore() {
    this.connectionMap = new HashMap<>();
  }

  public void addConnection(UUID uuid, RequestNetworkEventObserver requestNetworkEventObserver) {
    this.connectionMap.put(uuid, requestNetworkEventObserver);
  }

  public void removeConnection(UUID uuid){
    System.out.println("removing "+uuid);
    this.connectionMap.remove(uuid);
    chunkSubscriptionService.removeUUID(uuid);
  }

  public RequestNetworkEventObserver getConnection(UUID uuid) {
    return this.connectionMap.get(uuid);
  }

  public int size() {
    return this.connectionMap.size();
  }
}
