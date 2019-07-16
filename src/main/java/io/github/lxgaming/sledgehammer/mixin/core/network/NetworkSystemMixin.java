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

package io.github.lxgaming.sledgehammer.mixin.core.network;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.netty.channel.ChannelFuture;
import net.minecraft.network.NetworkSystem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = NetworkSystem.class, priority = 1337)
public abstract class NetworkSystemMixin {
    
    @Shadow
    private volatile boolean isAlive;
    
    @Shadow
    @Final
    private List<ChannelFuture> endpoints;
    
    /**
     * @author LX_Gaming
     * @reason Fixes potential deadlock on shutdown.
     */
    @Overwrite
    public void terminateEndpoints() {
        this.isAlive = false;
        Sledgehammer.getInstance().getLogger().info("Closing {} Endpoints...", this.endpoints.size());
        
        int failed = 0;
        for (ChannelFuture channelFuture : this.endpoints) {
            if (!channelFuture.channel().close().awaitUninterruptibly(30000L)) {
                failed++;
            }
        }
        
        if (failed > 0) {
            Sledgehammer.getInstance().getLogger().warn("Failed to close {} Endpoints", failed);
        }
        
        Sledgehammer.getInstance().getLogger().info("Closed Endpoints");
    }
}