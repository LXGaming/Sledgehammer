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
import io.github.lxgaming.sledgehammer.integration.AbstractIntegration;
import io.github.lxgaming.sledgehammer.manager.IntegrationManager;
import io.github.lxgaming.sledgehammer.util.Reference;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(
        id = Reference.ID,
        name = Reference.NAME,
        version = Reference.VERSION,
        description = Reference.DESCRIPTION,
        authors = {Reference.AUTHORS},
        url = Reference.WEBSITE
)
public class SledgehammerPlugin {
    
    @Inject
    private PluginContainer pluginContainer;
    
    @Listener
    public void onConstruction(GameConstructionEvent event) {
        Sledgehammer.init();
        Sledgehammer.getInstance().setPluginContainer(getPluginContainer());
    }
    
    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) {
        Sledgehammer.getInstance().registerIntegrations();
    }
    
    @Listener
    public void onInitialization(GameInitializationEvent event) {
        Sledgehammer.getInstance().registerCommands();
        IntegrationManager.getIntegrations().forEach(AbstractIntegration::run);
    }
    
    @Listener
    public void onLoadComplete(GameLoadCompleteEvent event) {
        Sledgehammer.getInstance().getLogger().info("{} v{} has loaded", Reference.NAME, Reference.VERSION);
    }
    
    @Listener
    public void onStopped(GameStoppedEvent event) {
        Sledgehammer.getInstance().getLogger().info("{} v{} has stopped", Reference.NAME, Reference.VERSION);
    }
    
    private PluginContainer getPluginContainer() {
        return pluginContainer;
    }
}