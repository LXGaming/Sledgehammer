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

package io.github.lxgaming.sledgehammer.configuration.category;

import com.google.common.collect.Lists;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class MixinCategory {
    
    @Setting(value = "actuallyadditions-disruption", comment = "Disabled Disruption lens from ActuallyAdditions")
    private boolean actuallyAdditionsDisruption = false;
    
    @Setting(value = "advancement-stacktrace", comment = "Prints a single message instead of a stacktrace for advancement errors")
    private boolean advancementStacktrace = true;
    
    @Setting(value = "biome-provider", comment = "Fixed NullPointerException in findBiomePosition method")
    private boolean biomeProvider = false;
    
    @Setting(value = "block-grass", comment = "Prevents Grass turning into Dirt")
    private boolean blockGrass = false;
    
    @Setting(value = "block-ice", comment = "Prevents Ice turning into Water")
    private boolean blockIce = false;
    
    @Setting(value = "ceremony-rain", comment = "Prevents Totemic from changing the weather")
    private boolean ceremonyRain = false;
    
    @Setting(value = "chunk-save-alert", comment = "Alerts players with permission (sledgehammer.broadcast.chunksave) when a chunk fails to save")
    private boolean chunkSaveAlert = false;
    
    @Setting(value = "chunk-save-blacklist", comment = "Items to removed from chunks")
    private List<String> chunkSaveBlacklist = Lists.newArrayList("minecraft:writable_book", "minecraft:written_book");
    
    @Setting(value = "chunk-save-purge-all", comment = "Removes all Entities and TileEntities from chunks that fail to save")
    private boolean chunkSavePurgeAll = false;
    
    @Setting(value = "chunk-save-purge-blacklist", comment = "Removes all blacklisted items from chunks that fail to save")
    private boolean chunkSavePurgeBlacklist = false;
    
    @Setting(value = "chunk-save-shutdown", comment = "Generates a crash report and safely stops the server if a chunk fails to save")
    private boolean chunkSaveShutdown = false;
    
    @Setting(value = "flush-network-on-tick", comment = "Reduce Network usage by postponing flush")
    private boolean flushNetworkOnTick = false;
    
    @Setting(value = "interact-events", comment = "Fixes https://github.com/SpongePowered/SpongeCommon/issues/2013")
    private boolean interactEvents = false;
    
    @Setting(value = "inventory-debug", comment = "Redirects inventory debugging messages")
    private boolean inventoryDebug = false;
    
    @Setting(value = "itemstack-exploit", comment = "Fixes MC-134716")
    private boolean itemstackExploit = false;
    
    @Setting(value = "item-teleport", comment = "Prevents or deletes any items that attempt to teleport across dimensions")
    private boolean itemTeleport = false;
    
    @Setting(value = "item-teleport-whitelist", comment = "Don't prevent these items from teleporting")
    private List<String> itemTeleportWhitelist = Lists.newArrayList("draconicevolution:ender_energy_manipulator");
    
    @Setting(value = "limit-books", comment = "Limits books to 50 pages with 255 characters for each")
    private boolean limitBooks = false;
    
    @Setting(value = "network-system", comment = "Fixes potential deadlock on shutdown")
    private boolean networkSystem = true;
    
    @Setting(value = "nuke-search-tree", comment = "Fixes potential deadlock on shutdown")
    private boolean nukeSearchTree = true;
    
    @Setting(value = "packet-spam", comment = "Cancels spammy packets")
    private boolean packetSpam = false;
    
    @Setting(value = "player-chunk-map", comment = "Prevents ConcurrentModificationException in PlayerChunkMap")
    private boolean playerChunkMap = false;
    
    @Setting(value = "quark-improved-sleeping", comment = "Disables Improved Sleeping from Quark")
    private boolean quarkImprovedSleeping = false;
    
    @Setting(value = "reliquary-item-rending-gale", comment = "Fixes https://github.com/P3pp3rF1y/Reliquary/issues/370")
    private boolean reliquaryItemRendingGale = false;
    
    @Setting(value = "ruins-debug", comment = "Redirects ruins debugging messages")
    private boolean ruinsDebug = false;
    
    @Setting(value = "traveling-merchant", comment = "Fixes https://github.com/Daveyx0/PrimitiveMobs/issues/59")
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
    
    public boolean isNukeSearchTree() {
        return nukeSearchTree;
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
    
    public boolean isTravelingMerchant() {
        return travelingMerchant;
    }
}