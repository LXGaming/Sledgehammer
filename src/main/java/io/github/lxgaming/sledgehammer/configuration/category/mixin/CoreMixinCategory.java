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
import io.github.lxgaming.sledgehammer.configuration.annotation.Mapping;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class CoreMixinCategory {
    
    @Mapping(value = "core.advancements.AdvancementManagerMixin_Reload")
    @Setting(value = "advancement-reload", comment = "If 'true', advancements will not be reloaded on world load.")
    private boolean advancementReload = false;
    
    @Mapping(value = "core.advancements.AdvancementManagerMixin_Stacktrace")
    @Mapping(value = "forge.common.ForgeHooksMixin_Advancement")
    @Setting(value = "advancement-stacktrace", comment = "If 'true', prints a single message instead of a stacktrace for advancement errors.")
    private boolean advancementStacktrace = false;
    
    @Mapping(value = "core.world.biome.BiomeProviderMixin")
    @Setting(value = "biome-provider", comment = "If 'true', fixes NullPointerException in BiomeProvider::findBiomePosition.")
    private boolean biomeProvider = false;
    
    @Mapping(value = "core.block.BlockGrassMixin")
    @Setting(value = "block-grass", comment = "If 'true', prevents Grass turning into Dirt.")
    private boolean blockGrass = false;
    
    @Mapping(value = "core.block.BlockIceMixin")
    @Setting(value = "block-ice", comment = "If 'true', prevents Ice turning into Water.")
    private boolean blockIce = false;
    
    @Mapping(value = "core.world.chunk.storage.AnvilChunkLoaderMixin")
    @Mapping(value = "core.world.chunk.storage.RegionFileChunkBufferMixin")
    @Setting(value = "chunk-save-alert", comment = "If 'true', alert players with permission (sledgehammer.broadcast.chunksave) when a chunk fails to save.")
    private boolean chunkSaveAlert = false;
    
    @Setting(value = "chunk-save-blacklist", comment = "Items to remove from chunks.")
    private List<String> chunkSaveBlacklist = Lists.newArrayList("minecraft:writable_book", "minecraft:written_book");
    
    @Mapping(value = "core.world.chunk.storage.AnvilChunkLoaderMixin")
    @Mapping(value = "core.world.chunk.storage.RegionFileChunkBufferMixin")
    @Setting(value = "chunk-save-purge-all", comment = "If 'true', removes all Entities and TileEntities from chunks that fail to save.")
    private boolean chunkSavePurgeAll = false;
    
    @Mapping(value = "core.world.chunk.storage.AnvilChunkLoaderMixin")
    @Mapping(value = "core.world.chunk.storage.RegionFileChunkBufferMixin")
    @Setting(value = "chunk-save-purge-blacklist", comment = "If 'true', removes all blacklisted items from chunks that fail to save.")
    private boolean chunkSavePurgeBlacklist = false;
    
    @Mapping(value = "core.world.chunk.storage.AnvilChunkLoaderMixin")
    @Mapping(value = "core.world.chunk.storage.RegionFileChunkBufferMixin")
    @Setting(value = "chunk-save-shutdown", comment = "If 'true', generates a crash report and safely stops the server if a chunk fails to save.")
    private boolean chunkSaveShutdown = false;
    
    @Mapping(value = "core.entity.EntityTrackerMixin")
    @Setting(value = "entity-tracker", comment = "If 'true', prevents ConcurrentModificationException in EntityTracker.")
    private boolean entityTracker = false;
    
    @Mapping(value = "core.entity.EntityLivingMixin_Despawning")
    @Mapping(value = "core.world.WorldMixin_Despawning")
    @Setting(value = "fix-mobs-not-despawning", comment = "If 'true', avoids MC-2536 by running mob despawning checks in lazy chunks. This will probably break farms which rely on vanilla 1.12 mechanics. You have been warned.")
    private boolean fixMobsNotDespawning = false;
    
    @Mapping(value = "core.entity.EntityLivingMixin_DespawnRanges")
    @Setting(value = "fix-impossible-mob-despawn-range", comment = "If 'true', adjusts the despawn ranges of mobs (32/128 blocks) on low render distances. Requires fix-mobs-not-despawning so that mobs are eligible for despawning. This has no effect on Sponge as it makes these settings configurable.")
    private boolean fixImpossibleMobDespawnRange = false;
    
    @Mapping(value = "core.network.play.server.SPacketChunkDataMixin")
    @Setting(value = "get-update-tag-crash", comment = "If 'true', adds TileEntity data to the crash report from calls to 'getUpdateTag' that fail.")
    private boolean getUpdateTagCrash = false;
    
    @Mapping(value = "core.util.text.TextComponentTranslationMixin")
    @Setting(value = "invalid-translation", comment = "If 'true', prevents crash due to invalid translation keys.")
    private boolean invalidTranslation = false;
    
    @Mapping(value = "core.item.ItemStackMixin_Exploit")
    @Setting(value = "itemstack-exploit", comment = "If 'true', fixes MC-134716 - Player kick exploit.")
    private boolean itemstackExploit = false;
    
    @Mapping(value = "core.util.LazyLoadBaseMixin")
    @Setting(value = "lazyload-thread-safe", comment = "If 'true', makes LazyLoad Thread-safe (Should fix MC-68381).")
    private boolean lazyLoadThreadSafe = false;
    
    @Mapping(value = "core.network.NetHandlerPlayServerMixin_Sleep")
    @Setting(value = "leave-sleep", comment = "If 'true', allows players to exit the sleep screen.")
    private boolean leaveSleep = false;
    
    @Mapping(value = "core.item.ItemWritableBookMixin")
    @Mapping(value = "core.network.NetHandlerPlayServerMixin_Book")
    @Setting(value = "limit-books", comment = "If 'true', enforces limits for books to 50 pages with 255 characters for each.")
    private boolean limitBooks = false;
    
    @Mapping(value = "core.network.NetworkSystemMixin")
    @Setting(value = "network-system", comment = "If 'true', fixes potential deadlock on shutdown.")
    private boolean networkSystem = false;
    
    @Mapping(value = "core.block.BlockDynamicLiquidMixin")
    @Mapping(value = "core.block.BlockLiquidAccessor")
    @Setting(value = "optimize-liquid-drain", comment = "If 'true', fixes depth calculation that causes unnecessary block updates.")
    private boolean optimizeLiquidDrain = false;
    
    @Mapping(value = "core.server.management.PlayerChunkMapMixin")
    @Setting(value = "player-chunk-map", comment = "If 'true', prevents ConcurrentModificationException in PlayerChunkMap.")
    private boolean playerChunkMap = false;
    
    @Mapping(value = "core.client.renderer.RenderGlobalMixin")
    @Setting(value = "premature-culling", comment = "If 'true', fixes MC-88176 (entities culled too aggresively at subchunk boundaries).")
    private boolean prematureCulling = false;
    
    @Mapping(value = "core.client.renderer.EntityRendererMixin")
    @Setting(value = "speed-up-chunk-rendering", comment = "If 'true', changes the renderer's target FPS to be 30 (matching 1.15-pre1) instead of the maximum FPS in game options. This helps with chunks not loading on the client.")
    private boolean speedUpChunkRendering = false;
    
    @Mapping(value = "core.world.WorldMixin_ChunkUnload")
    @Setting(value = "tile-entity-chunk-unload", comment = "If 'true', prevents unloading TileEntities from loading chunks")
    private boolean tileEntityChunkUnload = false;
    
    @Mapping(value = "core.network.play.server.SPacketJoinGameMixin")
    @Setting(value = "world-type-length", comment = "If 'true', increases the maximum length for a WorldType name in SPacketJoinGame packet.")
    private boolean worldTypeLength = false;
    
    public boolean isAdvancementReload() {
        return advancementReload;
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
    
    public boolean isEntityTracker() {
        return entityTracker;
    }
    
    public boolean isFixMobsNotDespawning() {
        return fixMobsNotDespawning;
    }
    
    public boolean isFixImpossibleMobDespawnRange() {
        return fixImpossibleMobDespawnRange;
    }
    
    public boolean isGetUpdateTagCrash() {
        return getUpdateTagCrash;
    }
    
    public boolean isInvalidTranslation() {
        return invalidTranslation;
    }
    
    public boolean isItemstackExploit() {
        return itemstackExploit;
    }
    
    public boolean isLazyLoadThreadSafe() {
        return lazyLoadThreadSafe;
    }
    
    public boolean isLeaveSleep() {
        return leaveSleep;
    }
    
    public boolean isLimitBooks() {
        return limitBooks;
    }
    
    public boolean isNetworkSystem() {
        return networkSystem;
    }
    
    public boolean isOptimizeLiquidDrain() {
        return optimizeLiquidDrain;
    }
    
    public boolean isPlayerChunkMap() {
        return playerChunkMap;
    }
    
    public boolean isTileEntityChunkUnload() {
        return tileEntityChunkUnload;
    }
    
    public boolean isWorldTypeLength() {
        return worldTypeLength;
    }
}
