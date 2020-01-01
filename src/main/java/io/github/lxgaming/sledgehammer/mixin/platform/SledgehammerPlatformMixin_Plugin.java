/*
 * Copyright 2019 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.platform;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.manager.CommandManager;
import io.github.lxgaming.sledgehammer.manager.IntegrationManager;
import io.github.lxgaming.sledgehammer.manager.MappingManager;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.api.GameState;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStateEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SledgehammerPlatform.class, priority = 1337, remap = false)
public abstract class SledgehammerPlatformMixin_Plugin {
    
    @Shadow
    private static SledgehammerPlatform instance;
    
    @Listener
    public void onConstruction(GameConstructionEvent event) {
        instance = Toolbox.cast(this, SledgehammerPlatform.class);
        Sledgehammer.init();
        
        MappingManager.STATE_MAPPINGS.put(GameState.CONSTRUCTION, SledgehammerPlatform.State.CONSTRUCTION);
        MappingManager.STATE_MAPPINGS.put(GameState.PRE_INITIALIZATION, SledgehammerPlatform.State.PRE_INITIALIZATION);
        MappingManager.STATE_MAPPINGS.put(GameState.INITIALIZATION, SledgehammerPlatform.State.INITIALIZATION);
        MappingManager.STATE_MAPPINGS.put(GameState.POST_INITIALIZATION, SledgehammerPlatform.State.POST_INITIALIZATION);
        MappingManager.STATE_MAPPINGS.put(GameState.LOAD_COMPLETE, SledgehammerPlatform.State.LOAD_COMPLETE);
        MappingManager.STATE_MAPPINGS.put(GameState.SERVER_ABOUT_TO_START, SledgehammerPlatform.State.SERVER_ABOUT_TO_START);
        MappingManager.STATE_MAPPINGS.put(GameState.SERVER_STARTING, SledgehammerPlatform.State.SERVER_STARTING);
        MappingManager.STATE_MAPPINGS.put(GameState.SERVER_STARTED, SledgehammerPlatform.State.SERVER_STARTED);
        MappingManager.STATE_MAPPINGS.put(GameState.SERVER_STOPPING, SledgehammerPlatform.State.SERVER_STOPPING);
        MappingManager.STATE_MAPPINGS.put(GameState.SERVER_STOPPED, SledgehammerPlatform.State.SERVER_STOPPED);
        
        IntegrationManager.prepare();
    }
    
    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) {
    }
    
    @Listener
    public void onInitialization(GameInitializationEvent event) {
    }
    
    @Listener
    public void onLoadComplete(GameLoadCompleteEvent event) {
        Sledgehammer.getInstance().getLogger().info("{} v{} has loaded", Sledgehammer.NAME, Sledgehammer.VERSION);
    }
    
    @Listener
    public void onServerStarting(GameStartingServerEvent event) {
        CommandManager.prepare();
    }
    
    @Listener
    public void onStopped(GameStoppedEvent event) {
        Sledgehammer.getInstance().getLogger().info("{} v{} has stopped", Sledgehammer.NAME, Sledgehammer.VERSION);
    }
    
    @Listener
    public void onGameState(GameStateEvent event) {
        IntegrationManager.execute();
    }
    
    /**
     * @author LX_Gaming
     * @reason Sponge compatibility
     */
    @Overwrite
    public Object getContainer() {
        return Sponge.getPluginManager().getPlugin(Sledgehammer.ID).orElse(null);
    }
    
    /**
     * @author LX_Gaming
     * @reason Sponge compatibility
     */
    @Overwrite
    public MinecraftServer getServer() {
        if (Sponge.isServerAvailable()) {
            return (MinecraftServer) Sponge.getServer();
        }
        
        return null;
    }
    
    /**
     * @author LX_Gaming
     * @reason Sponge compatibility
     */
    @Overwrite
    public SledgehammerPlatform.State getState() {
        return MappingManager.getStateMapping(Sponge.getGame().getState()).orElse(null);
    }
    
    /**
     * @author LX_Gaming
     * @reason Sponge compatibility
     */
    @Overwrite
    public SledgehammerPlatform.Type getType() {
        return SledgehammerPlatform.Type.SPONGE;
    }
    
    /**
     * @author LX_Gaming
     * @reason Sponge compatibility
     */
    @Overwrite
    public boolean isLoaded(String id) {
        return Sponge.getPluginManager().isLoaded(id);
    }
}