/*
 * Copyright 2018 Alex Thomson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lxgaming.sledgehammer;

import com.google.inject.Inject;
import io.github.lxgaming.sledgehammer.commands.SledgehammerCommand;
import io.github.lxgaming.sledgehammer.integrations.ForgeIntegration;
import io.github.lxgaming.sledgehammer.integrations.MistIntegration;
import io.github.lxgaming.sledgehammer.integrations.PrimalIntegration;
import io.github.lxgaming.sledgehammer.integrations.SpongeIntegration;
import io.github.lxgaming.sledgehammer.managers.CommandManager;
import io.github.lxgaming.sledgehammer.managers.IntegrationManager;
import io.github.lxgaming.sledgehammer.util.Reference;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(
        id = Reference.PLUGIN_ID,
        name = Reference.PLUGIN_NAME,
        version = Reference.PLUGIN_VERSION,
        description = Reference.DESCRIPTION,
        authors = {Reference.AUTHORS},
        url = Reference.WEBSITE
)
public class SledgehammerPlugin {
    
    @Inject
    private PluginContainer pluginContainer;
    
    @Listener
    public void onGameConstruction(GameConstructionEvent event) {
        Sledgehammer.init();
        Sledgehammer.getInstance().setPluginContainer(getPluginContainer());
    }
    
    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
        CommandManager.registerCommand(SledgehammerCommand.class);
        IntegrationManager.registerIntegration(ForgeIntegration.class);
        IntegrationManager.registerIntegration(MistIntegration.class);
        IntegrationManager.registerIntegration(PrimalIntegration.class);
        IntegrationManager.registerIntegration(SpongeIntegration.class);
    }
    
    @Listener
    public void onGameLoadComplete(GameLoadCompleteEvent event) {
        Sledgehammer.getInstance().getLogger().info("{} v{} has started.", Reference.PLUGIN_NAME, Reference.PLUGIN_VERSION);
    }
    
    @Listener
    public void onGameStopping(GameStoppingEvent event) {
        Sledgehammer.getInstance().getLogger().info("{} v{} has stopped.", Reference.PLUGIN_NAME, Reference.PLUGIN_VERSION);
    }
    
    private PluginContainer getPluginContainer() {
        return pluginContainer;
    }
}