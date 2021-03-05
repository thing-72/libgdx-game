package networking.server;

import com.google.inject.Inject;
import infra.entity.EntityManager;
import infra.entity.factories.EntityFactory;
import networking.server.connetion.ConnectionStore;
import networking.server.observers.CreateObserver;
import networking.server.observers.RemoveObserver;
import networking.server.observers.UpdateObserver;

public class ServerObserverFactory {

    EntityManager entityManager;
    ConnectionStore connectionStore;
    EntityFactory entityFactory;

    @Inject
    ServerObserverFactory(EntityManager entityManager, ConnectionStore connectionStore, EntityFactory entityFactory){
        this.entityManager = entityManager;
        this.connectionStore = connectionStore;
        this.entityFactory  = entityFactory;
    }

    public CreateObserver createCreateObserver(){
        return new CreateObserver(this.entityManager, this.connectionStore, this.entityFactory);
    }

    public UpdateObserver createUpdateObserver(){
        return new UpdateObserver(this.entityManager, this.connectionStore);
    }

    public RemoveObserver createRemoveObserver(){
        return new RemoveObserver(this.entityManager, this.connectionStore);
    }

}
