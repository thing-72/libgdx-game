package core.networking.sync;

import core.app.user.User;
import core.chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.common.Clock;
import core.configuration.ClientConfig;
import core.entity.attributes.msc.Coordinates;
import core.networking.sync.SyncService;
import org.junit.Before;
import org.junit.Test;

public class testSyncService {

  Injector clientInjector;
  Clock clock;
  SyncService syncService;
  User user;

  @Before
  public void setup() {
    clientInjector = Guice.createInjector(new ClientConfig());
    clock = clientInjector.getInstance(Clock.class);
    syncService = clientInjector.getInstance(SyncService.class);
    user = clientInjector.getInstance(User.class);
  }

  @Test
  public void testHandshakeLocking() {
    ChunkRange testChunkRange = new ChunkRange(new Coordinates(0, 0));
    clock.tick();
    syncService.lockHandshake(user.getUserID(), testChunkRange, 4);
    assert syncService.isHandshakeLocked(user.getUserID(), testChunkRange);
    clock.tick();
    clock.tick();
    clock.tick();
    assert syncService.isHandshakeLocked(user.getUserID(), testChunkRange);
    clock.tick();
    assert !syncService.isHandshakeLocked(user.getUserID(), testChunkRange);
  }
}