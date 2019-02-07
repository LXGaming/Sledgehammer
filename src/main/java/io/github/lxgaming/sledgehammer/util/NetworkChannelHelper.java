/*
 * Minecraft Forge
 * Copyright (c) 2016-2018.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.lxgaming.sledgehammer.util;

import com.google.common.collect.Lists;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.util.List;

/**
 * Copied from <a href="https://github.com/MinecraftForge/MinecraftForge/pull/5126">MinecraftForge Pull Request #5126</a>
 */
public class NetworkChannelHelper implements Runnable {
    
    private static final List<ChannelOutboundInvoker> markedChannels = Lists.newArrayList();
    public static final int flushDelay = Integer.getInteger("fml.network.flushDelay", 50);
    
    static {
        Thread thread = new Thread(new NetworkChannelHelper(), "NetworkChannelFlush");
        thread.setDaemon(true);
        thread.start();
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                NetworkChannelHelper.flushAllChannels();
                Thread.sleep(flushDelay);
            }
        } catch (InterruptedException ex) {
        }
    }
    
    public static ChannelFuture writeAndFlush(ChannelOutboundInvoker channel, Object msg, INetHandler packetListener) {
        if (packetListener == null) {
            return channel.writeAndFlush(msg);
        } else if (packetListener instanceof INetHandlerPlayClient || packetListener instanceof INetHandlerPlayServer) {
            return writeAndFlush(channel, msg);
        } else {
            return channel.writeAndFlush(msg);
        }
    }
    
    public static ChannelFuture writeAndFlush(ChannelOutboundInvoker channel, Object msg) {
        if (isInstantFlushEnabled()) {
            return channel.writeAndFlush(msg);
        } else {
            markForFlush(channel);
            return channel.write(msg);
        }
    }
    
    public static ChannelFuture writeAndFlush(ChannelOutboundInvoker channel, Object msg, ChannelPromise promise) {
        if (isInstantFlushEnabled()) {
            return channel.writeAndFlush(msg, promise);
        } else {
            markForFlush(channel);
            return channel.write(msg, promise);
        }
    }
    
    public static void markForFlush(ChannelOutboundInvoker channel) {
        synchronized (markedChannels) {
            markedChannels.add(channel);
        }
    }
    
    public static void flushAllChannels() {
        synchronized (markedChannels) {
            markedChannels.forEach(ChannelOutboundInvoker::flush);
            markedChannels.clear();
        }
    }
    
    private static boolean isInstantFlushEnabled() {
        return false;
    }
}