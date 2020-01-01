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
import io.github.lxgaming.sledgehammer.manager.IntegrationManager;
import io.github.lxgaming.sledgehammer.manager.MappingManager;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStateEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(value = SledgehammerPlatform.class, priority = 1337, remap = false)
public abstract class SledgehammerPlatformMixin_Plugin {
    
    @Shadow
    private static SledgehammerPlatform instance;
    
    private SledgehammerPlatform.State platform$state;
    
    @Listener
    public void onGameState(GameStateEvent event) {
        MappingManager.getStateMapping(event.getClass()).ifPresent(this::platform$setState);
    }
    
    @Listener
    public void onLoadComplete(GameLoadCompleteEvent event) {
        Sledgehammer.getInstance().getLogger().info("{} v{} has loaded", Sledgehammer.NAME, Sledgehammer.VERSION);
    }
    
    @Listener
    public void onStopped(GameStoppedEvent event) {
        Sledgehammer.getInstance().getLogger().info("{} v{} has stopped", Sledgehammer.NAME, Sledgehammer.VERSION);
    }
    
    @Inject(
            method = "<init>",
            at = @At(
                    value = "RETURN"
            )
    )
    private void onInit(CallbackInfo callbackInfo) {
        instance = Toolbox.cast(this, SledgehammerPlatform.class);
        this.platform$state = SledgehammerPlatform.State.CONSTRUCTION;
        
        Sledgehammer.init();
        
        MappingManager.STATE_MAPPINGS.put(GamePreInitializationEvent.class, SledgehammerPlatform.State.PRE_INITIALIZATION);
        MappingManager.STATE_MAPPINGS.put(GameInitializationEvent.class, SledgehammerPlatform.State.INITIALIZATION);
        MappingManager.STATE_MAPPINGS.put(GamePostInitializationEvent.class, SledgehammerPlatform.State.POST_INITIALIZATION);
        MappingManager.STATE_MAPPINGS.put(GameLoadCompleteEvent.class, SledgehammerPlatform.State.LOAD_COMPLETE);
        MappingManager.STATE_MAPPINGS.put(GameAboutToStartServerEvent.class, SledgehammerPlatform.State.SERVER_ABOUT_TO_START);
        MappingManager.STATE_MAPPINGS.put(GameStartingServerEvent.class, SledgehammerPlatform.State.SERVER_STARTING);
        MappingManager.STATE_MAPPINGS.put(GameStartedServerEvent.class, SledgehammerPlatform.State.SERVER_STARTED);
        MappingManager.STATE_MAPPINGS.put(GameStoppingServerEvent.class, SledgehammerPlatform.State.SERVER_STOPPING);
        MappingManager.STATE_MAPPINGS.put(GameStoppedServerEvent.class, SledgehammerPlatform.State.SERVER_STOPPED);
        
        IntegrationManager.execute();
    }
    
    /**
     * @author LX_Gaming
     * @reason Sponge compatibility
     */
    @Overwrite
    public boolean isLoaded(@Nonnull String id) {
        return Sponge.getPluginManager().isLoaded(id);
    }
    
    /**
     * @author LX_Gaming
     * @reason Sponge compatibility
     */
    @Overwrite
    @Nullable
    public Object getContainer() {
        return Sponge.getPluginManager().getPlugin(Sledgehammer.ID).orElse(null);
    }
    
    /**
     * @author LX_Gaming
     * @reason Sponge compatibility
     */
    @Overwrite
    @Nullable
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
    @Nonnull
    public SledgehammerPlatform.State getState() {
        return this.platform$state;
    }
    
    private void platform$setState(SledgehammerPlatform.State state) {
        if (state == null) {
            return;
        }
        
        Sledgehammer.getInstance().getLogger().debug("State: {} -> {}", this.platform$state.getName(), state.getName());
        this.platform$state = state;
        
        IntegrationManager.execute();
    }
    
    /**
     * @author LX_Gaming
     * @reason Sponge compatibility
     */
    @Overwrite
    @Nonnull
    public SledgehammerPlatform.Type getType() {
        return SledgehammerPlatform.Type.SPONGE;
    }
}