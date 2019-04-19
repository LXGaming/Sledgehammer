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

import com.google.common.collect.Lists;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class ServerMixinCategory {
    
    @Setting(value = "actuallyadditions-disruption", comment = "Disable Disruption lens from ActuallyAdditions")
    private boolean actuallyAdditionsDisruption = false;
    
    @Setting(value = "advancement-stacktrace", comment = "Print a single message instead of a stacktrace for advancement errors")
    private boolean advancementStacktrace = true;
    
    @Setting(value = "biome-provider", comment = "Fix NullPointerException in BiomeProvider::findBiomePosition")
    private boolean biomeProvider = false;
    
    @Setting(value = "block-grass", comment = "Prevent Grass turning into Dirt")
    private boolean blockGrass = false;
    
    @Setting(value = "block-ice", comment = "Prevent Ice turning into Water")
    private boolean blockIce = false;
    
    @Setting(value = "ceremony-rain", comment = "Prevent Totemic from changing the weather")
    private boolean ceremonyRain = false;
    
    @Setting(value = "chunk-save-alert", comment = "Alert players with permission (sledgehammer.broadcast.chunksave) when a chunk fails to save")
    private boolean chunkSaveAlert = false;
    
    @Setting(value = "chunk-save-blacklist", comment = "Items to remove from chunks")
    private List<String> chunkSaveBlacklist = Lists.newArrayList("minecraft:writable_book", "minecraft:written_book");
    
    @Setting(value = "chunk-save-purge-all", comment = "Remove all Entities and TileEntities from chunks that fail to save")
    private boolean chunkSavePurgeAll = false;
    
    @Setting(value = "chunk-save-purge-blacklist", comment = "Remove all blacklisted items from chunks that fail to save")
    private boolean chunkSavePurgeBlacklist = false;
    
    @Setting(value = "chunk-save-shutdown", comment = "Generate a crash report and safely stops the server if a chunk fails to save")
    private boolean chunkSaveShutdown = false;
    
    @Setting(value = "command-source", comment = "Fixes ClassCastException in WrapperCommandSource (Sponge only)")
    private boolean commandSource = false;
    
    @Setting(value = "flush-network-on-tick", comment = "Reduce Network usage by postponing flush")
    private boolean flushNetworkOnTick = false;
    
    @Setting(value = "interact-events", comment = "Fix https://github.com/SpongePowered/SpongeCommon/issues/2013 (Sponge only)")
    private boolean interactEvents = false;
    
    @Setting(value = "inventory-debug", comment = "Redirect inventory debugging messages added by Sponge (Sponge only)")
    private boolean inventoryDebug = false;
    
    @Setting(value = "itemstack-exploit", comment = "Fix MC-134716 - Player kick exploit")
    private boolean itemstackExploit = false;
    
    @Setting(value = "item-teleport", comment = "Prevent or delete any items that attempt to teleport across dimensions (Sponge only)")
    private boolean itemTeleport = false;
    
    @Setting(value = "item-teleport-whitelist", comment = "Don't prevent these items from teleporting (Sponge only)")
    private List<String> itemTeleportWhitelist = Lists.newArrayList("draconicevolution:ender_energy_manipulator");
    
    @Setting(value = "limit-books", comment = "Limit books to 50 pages with 255 characters for each")
    private boolean limitBooks = false;
    
    @Setting(value = "network-system", comment = "Fix potential deadlock on shutdown")
    private boolean networkSystem = true;
    
    @Setting(value = "packet-spam", comment = "Cancel spammy packets")
    private boolean packetSpam = false;
    
    @Setting(value = "player-chunk-map", comment = "Prevent ConcurrentModificationException in PlayerChunkMap")
    private boolean playerChunkMap = false;
    
    @Setting(value = "quark-improved-sleeping", comment = "Disable Improved Sleeping from Quark")
    private boolean quarkImprovedSleeping = false;
    
    @Setting(value = "reliquary-item-rending-gale", comment = "Fix https://github.com/P3pp3rF1y/Reliquary/issues/370")
    private boolean reliquaryItemRendingGale = false;
    
    @Setting(value = "ruins-debug", comment = "Redirect ruins debugging messages")
    private boolean ruinsDebug = false;
    
    @Setting(value = "tile-entity-stack-overflow", comment = "Prevent StackOverflow on writeToNBT (Sponge only)")
    private boolean tileEntityStackOverflow = false;
    
    @Setting(value = "tomb-many-graves", comment = "Disable TombManyGraves functionality")
    private boolean tombManyGraves = false;
    
    @Setting(value = "traveling-merchant", comment = "Fix https://github.com/Daveyx0/PrimitiveMobs/issues/59 (Sponge only)")
    private boolean travelingMerchant = false;
    
    public boolean isActuallyAdditionsDisruption() {
        return actuallyAdditionsDisruption;
    }
    
    public boolean isAdvancementStacktrace() {
        return advancementStacktrace;
    }
    
    public boolean isBiomeProvider() {
        return biomeProvider;
    }
    
    public boolean isBlockGrass() {
        return blockGrass;
    }
    
    public boolean isBlockIce() {
        return blockIce;
    }
    
    public boolean isCeremonyRain() {
        return ceremonyRain;
    }
    
    public boolean isChunkSaveAlert() {
        return chunkSaveAlert;
    }
    
    public List<String> getChunkSaveBlacklist() {
        return chunkSaveBlacklist;
    }
    
    public boolean isChunkSavePurgeAll() {
        return chunkSavePurgeAll;
    }
    
    public boolean isChunkSavePurgeBlacklist() {
        return chunkSavePurgeBlacklist;
    }
    
    public boolean isChunkSaveShutdown() {
        return chunkSaveShutdown;
    }
    
    public boolean isCommandSource() {
        return commandSource;
    }
    
    public boolean isFlushNetworkOnTick() {
        return flushNetworkOnTick;
    }
    
    public boolean isInteractEvents() {
        return interactEvents;
    }
    
    public boolean isInventoryDebug() {
        return inventoryDebug;
    }
    
    public boolean isItemstackExploit() {
        return itemstackExploit;
    }
    
    public boolean isItemTeleport() {
        return itemTeleport;
    }
    
    public List<String> getItemTeleportWhitelist() {
        return itemTeleportWhitelist;
    }
    
    public boolean isLimitBooks() {
        return limitBooks;
    }
    
    public boolean isNetworkSystem() {
        return networkSystem;
    }
    
    public boolean isPacketSpam() {
        return packetSpam;
    }
    
    public boolean isPlayerChunkMap() {
        return playerChunkMap;
    }
    
    public boolean isQuarkImprovedSleeping() {
        return quarkImprovedSleeping;
    }
    
    public boolean isReliquaryItemRendingGale() {
        return reliquaryItemRendingGale;
    }
    
    public boolean isRuinsDebug() {
        return ruinsDebug;
    }
    
    public boolean isTileEntityStackOverflow() {
        return tileEntityStackOverflow;
    }
    
    public boolean isTombManyGraves() {
        return tombManyGraves;
    }
    
    public boolean isTravelingMerchant() {
        return travelingMerchant;
    }
}