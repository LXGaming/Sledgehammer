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

package io.github.lxgaming.sledgehammer.configuration.category.mixin;

import io.github.lxgaming.sledgehammer.configuration.annotation.Mapping;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ForgeMixinCategory {
    
    @Mapping(value = "forge.world.WorldMixin_IgnoreForcedChunkTicking")
    @Setting(value = "entity-chunk-churn", comment = "If 'true', reverts Forge-chunkloaded chunk ticking to behave like vanilla (requiring a 5x5 area).")
    private boolean entityChunkChurn = false;
    
    @Mapping(value = "core.network.NetworkManagerMixin")
    @Mapping(value = "forge.fml.common.network.simpleimpl.SimpleChannelHandlerWrapperMixin")
    @Mapping(value = "forge.fml.common.network.FMLEmbeddedChannelMixin")
    @Mapping(value = "forge.fml.common.network.FMLEventChannelMixin")
    @Setting(value = "flush-network-on-tick", comment = "If 'true', reduces network usage by postponing flush.")
    private boolean flushNetworkOnTick = false;
    
    @Mapping(value = "forge.fml.client.FMLClientHandlerMixin")
    @Setting(value = "nuke-search-tree", comment = "If 'true', disables SearchTree reloading (Speeds up server connection process).")
    private boolean nukeSearchTree = false;
    
    @Mapping(value = "forge.fml.client.FMLClientHandlerMixin")
    @Setting(value = "nuke-search-tree-shutdown", comment = "If 'true', disables SearchTree reloading on shutdown.")
    private boolean nukeSearchTreeShutdown = false;
    
    @Mapping(value = "forge.fml.common.network.simpleimpl.SimpleNetworkWrapperMixin")
    @Setting(value = "packet-spam", comment = "If 'true', cancels packets sent by LootBags due to poorly written networking.")
    private boolean packetSpam = false;
    
    @Mapping(value = "core.tileentity.TileEntityMixin_ForgeData")
    @Setting(value = "tile-entity-forge-data", comment = "If 'true', prevents mods from writing CustomTileData, This isn't allowed.")
    private boolean tileEntityForgeData = false;
    
    public boolean isFlushNetworkOnTick() {
        return flushNetworkOnTick;
    }
    
    public boolean isNukeSearchTree() {
        return nukeSearchTree;
    }
    
    public boolean isNukeSearchTreeShutdown() {
        return nukeSearchTreeShutdown;
    }
    
    public boolean isPacketSpam() {
        return packetSpam;
    }
    
    public boolean isTileEntityForgeData() {
        return tileEntityForgeData;
    }
}