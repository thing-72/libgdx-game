package networking.translation;

import app.user.UserID;
import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import configuration.ClientConfig;
import java.util.LinkedList;
import java.util.List;
import networking.events.types.incoming.SubscriptionIncomingEventType;
import networking.events.types.outgoing.SubscriptionOutgoingEventType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class testSubscriptionSerialization {

  Injector injector;

  @Before
  public void setup() {
    injector = Guice.createInjector(new ClientConfig());
  }

  @Test
  public void testHandleSubscriptionEvent() {
    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(0, 1)));
    chunkRangeList.add(new ChunkRange(new Coordinates(-2, 1)));
    SubscriptionOutgoingEventType subscriptionOutgoingEvent =
        new SubscriptionOutgoingEventType(chunkRangeList);
    UserID userID = UserID.createUserID();

    SubscriptionIncomingEventType subscriptionIncomingEvent =
        new SubscriptionIncomingEventType(
            subscriptionOutgoingEvent.toNetworkEvent().toBuilder()
                .setUser(userID.toString())
                .build());

    Assert.assertEquals(
        subscriptionOutgoingEvent.getChunkRangeList(),
        subscriptionIncomingEvent.getChunkRangeList());

    Assert.assertEquals(subscriptionIncomingEvent.getUserID(), userID);
  }
}
