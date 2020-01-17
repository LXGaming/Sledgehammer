/*
 * Copyright 2020 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.kubejs.world;

import dev.latvian.kubejs.KubeJSEvents;
import dev.latvian.kubejs.event.EventsJS;
import dev.latvian.kubejs.player.SimplePlayerEventJS;
import dev.latvian.kubejs.script.ScriptManager;
import dev.latvian.kubejs.server.ServerJS;
import dev.latvian.kubejs.server.SimpleServerEventJS;
import dev.latvian.kubejs.world.KubeJSWorldEventHandler;
import dev.latvian.kubejs.world.SimpleWorldEventJS;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = KubeJSWorldEventHandler.class, remap = false)
public abstract class KubeJSWorldEventHandlerMixin {
    
    /**
     * @author LX_Gaming
     * @reason Fixes https://github.com/KubeJS-Mods/KubeJS/issues/5
     */
    @Overwrite
    public static void onServerStopping() {
        // Sledgehammer start
        ServerJS.instance.playerMap.values().removeIf(player -> {
            EventsJS.post(KubeJSEvents.PLAYER_LOGGED_OUT, new SimplePlayerEventJS(player.getPlayerEntity()));
            return true;
        });
        
        ServerJS.instance.worldMap.values().removeIf(world -> {
            EventsJS.post(KubeJSEvents.WORLD_UNLOAD, new SimpleWorldEventJS(world));
            return true;
        });
        
        // Sledgehammer end
        
        ServerJS.instance.updateWorldList();
        
        EventsJS.post(KubeJSEvents.SERVER_UNLOAD, new SimpleServerEventJS(ServerJS.instance));
        ServerJS.instance = null;
        ScriptManager.instance.runtime.remove("server");
    }
}