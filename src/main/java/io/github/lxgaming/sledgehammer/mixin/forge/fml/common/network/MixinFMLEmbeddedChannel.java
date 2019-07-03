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

package io.github.lxgaming.sledgehammer.mixin.forge.fml.common.network;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.util.NetworkChannelHelper;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = FMLEmbeddedChannel.class, priority = 1337, remap = false)
public abstract class MixinFMLEmbeddedChannel extends EmbeddedChannel {
    
    @Override
    public ChannelFuture writeAndFlush(Object msg) {
        Sledgehammer.getInstance().debug("FMLEmbeddedChannel::writeAndFlush(Object)");
        return NetworkChannelHelper.writeAndFlush(this, msg);
    }
    
    @Override
    public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
        Sledgehammer.getInstance().debug("FMLEmbeddedChannel::writeAndFlush(Object, ChannelPromise)");
        return NetworkChannelHelper.writeAndFlush(this, msg, promise);
    }
}