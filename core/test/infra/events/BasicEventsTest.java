package infra.events;

import com.google.inject.Guice;
import com.google.inject.Injector;
import modules.App;
import org.junit.Test;

import java.util.HashMap;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

class TestEvent implements Event{
    String changeTo;
    HashMap<String, String> data;
    TestEvent(String changeTo){
        this.data = new HashMap<>();
        data.put("changeTo", changeTo);
    }

    @Override
    public String getType() {
        return "test_event";
    }

    @Override
    public HashMap<String, String> getData() {
        return this.data;
    }
}

public class BasicEventsTest {

    @Test
    public void singleTest(){
        Injector injector = Guice.createInjector(new App());
        EventService eventService = injector.getInstance(EventService.class);
        final String[] changeByEvent = {"before"};
        TestEvent event = new TestEvent("after");
        Consumer<Event> testConsumer = new Consumer<Event>() {
            @Override
            public void accept(Event testEvent) {
                changeByEvent[0] = testEvent.getData().get("changeTo");
            }
        };
        eventService.addListener(event.getType(), testConsumer);
        assertEquals(changeByEvent[0], "before");
        eventService.fireEvent(event);
        assertEquals(changeByEvent[0], "after");
    }
}
