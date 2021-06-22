package configuration;

import infra.app.Game;
import infra.app.GameScreen;
import infra.app.UpdateLoop;
import infra.app.client.ClientGame;
import infra.app.client.ClientGameScreen;
import infra.app.client.ClientUpdateLoop;
import infra.common.events.EventConsumer;
import infra.common.render.BaseCamera;
import infra.networking.client.ClientNetworkHandle;
import infra.networking.consumer.ClientEventConsumer;

public class ClientConfig extends MainConfig {
  @Override
  protected void configure() {
    super.configure();
    bind(EventConsumer.class).to(ClientEventConsumer.class).asEagerSingleton();
    bind(ClientNetworkHandle.class).asEagerSingleton();
    bind(UpdateLoop.class).to(ClientUpdateLoop.class).asEagerSingleton();
    bind(Game.class).to(ClientGame.class).asEagerSingleton();
    bind(BaseCamera.class).asEagerSingleton();
    bind(GameScreen.class).to(ClientGameScreen.class).asEagerSingleton();
  }
}
