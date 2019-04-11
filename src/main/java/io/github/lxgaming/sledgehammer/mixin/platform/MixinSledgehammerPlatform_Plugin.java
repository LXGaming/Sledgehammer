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
import io.github.lxgaming.sledgehammer.util.Reference;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStateEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SledgehammerPlatform.class, priority = 1337, remap = false)
public abstract class MixinSledgehammerPlatform_Plugin {
    
    @Shadow
    private static SledgehammerPlatform instance;
    
    @Listener
    public void onConstruction(GameConstructionEvent event) {
        instance = Toolbox.cast(this, SledgehammerPlatform.class);
        Sledgehammer.init();
        IntegrationManager.register();
    }
    
    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) {
    }
    
    @Listener
    public void onInitialization(GameInitializationEvent event) {
        CommandManager.register();
    }
    
    @Listener
    public void onLoadComplete(GameLoadCompleteEvent event) {
        Sledgehammer.getInstance().getLogger().info("{} v{} has loaded", Reference.NAME, Reference.VERSION);
    }
    
    @Listener
    public void onStopped(GameStoppedEvent event) {
        Sledgehammer.getInstance().getLogger().info("{} v{} has stopped", Reference.NAME, Reference.VERSION);
    }
    
    @Listener
    public void onGameState(GameStateEvent event) {
        IntegrationManager.process();
    }
    
    /**
     * @author LX_Gaming
     * @reason Sponge compatibility
     */
    @Overwrite
    public Object getContainer() {
        return Sponge.getPluginManager().getPlugin(Reference.ID).orElse(null);
    }
    
    /**
     * @author LX_Gaming
     * @reason Sponge compatibility
     */
    @Overwrite
    public SledgehammerPlatform.Type getType() {
        return SledgehammerPlatform.Type.SPONGE;
    }
}