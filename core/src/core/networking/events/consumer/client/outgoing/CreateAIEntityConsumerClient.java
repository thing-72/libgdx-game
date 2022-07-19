package core.networking.events.consumer.client.outgoing;

import com.google.inject.Inject;
import core.common.events.types.CreateAIEntityEventType;
import core.common.events.types.EventType;
import java.util.function.Consumer;
import core.networking.client.ClientNetworkHandle;

public class CreateAIEntityConsumerClient implements Consumer<EventType> {

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    CreateAIEntityEventType realEvent = (CreateAIEntityEventType) eventType;
    clientNetworkHandle.send(realEvent.toNetworkEvent());
  }
}