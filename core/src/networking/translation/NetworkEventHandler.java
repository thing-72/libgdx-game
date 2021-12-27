package networking.translation;

import com.google.inject.Inject;
import common.Coordinates;
import common.events.EventConsumer;
import common.events.EventService;
import common.events.types.CreateAIEntityEventType;
import networking.NetworkObjects;
import networking.events.EventTypeFactory;
import networking.events.types.outgoing.SubscriptionOutgoingEventType;

public class NetworkEventHandler extends EventConsumer {

    @Inject
    EventTypeFactory eventTypeFactory;
    @Inject
    EventService eventService;
    @Inject
    NetworkDataDeserializer networkDataDeserializer;

    public NetworkEventHandler() {
        super();
    }

    public void handleNetworkEvent(NetworkObjects.NetworkEvent networkEvent) {
        try {
            String event = networkEvent.getEvent();
            if (event.equals(DataTranslationEnum.CREATE_ENTITY)) {
                eventService.fireEvent(NetworkDataDeserializer.createCreateEntityIncomingEventType(networkEvent));
            } else if (event.equals(DataTranslationEnum.UPDATE_ENTITY)) {
                eventService.fireEvent(NetworkDataDeserializer.createUpdateEntityIncomingEvent(networkEvent));
            } else if (event.equals(SubscriptionOutgoingEventType.type)) {
                eventService.fireEvent(eventTypeFactory.createSubscriptionIncomingEvent(networkEvent));
            } else if (event.equals(DataTranslationEnum.REMOVE_ENTITY)) {
                eventService.queuePostUpdateEvent(NetworkDataDeserializer.createRemoveEntityIncomingEventType(networkEvent));
            } else if (event.equals(DataTranslationEnum.REPLACE_BLOCK)) {
                eventService.queuePostUpdateEvent(networkDataDeserializer.createReplaceBlockIncomingEventType(networkEvent));
            } else if (event.equals(CreateAIEntityEventType.type)) {
                eventService.queuePostUpdateEvent(eventTypeFactory.createAIEntityEventType(new Coordinates(0, 0)));
            } else if (event.equals(DataTranslationEnum.HANDSHAKE)) {
                eventService.fireEvent(NetworkDataDeserializer.createHandshakeIncomingEventType(networkEvent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
