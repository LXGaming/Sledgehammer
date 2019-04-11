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

package io.github.lxgaming.sledgehammer.manager;

import com.google.common.collect.Maps;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.GeneralCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ClientMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.CommonMixinCategory;
import io.github.lxgaming.sledgehammer.configuration.category.mixin.ServerMixinCategory;
import io.github.lxgaming.sledgehammer.launch.SledgehammerLaunch;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class MappingManager {
    
    private static final Map<String, Boolean> MIXIN_MAPPINGS = Maps.newHashMap();
    private static final Map<String, Boolean> MOD_MAPPINGS = Maps.newHashMap();
    
    public static void registerMixins() {
        // Client Mixin
        // - Core
        registerClientMixin("core.client.MixinMinecraft", category -> true);
        
        // - Forge
        registerClientMixin("forge.common.MixinForgeModContainer", ClientMixinCategory::isNukeSearchTree);
        
        // Common Mixin
        // - Platform
        registerCommonMixin("platform.MixinSledgehammerPlatform_Mod", category -> SledgehammerLaunch.isForgeRegistered() && !SledgehammerLaunch.isSpongeRegistered());
        registerCommonMixin("platform.MixinSledgehammerPlatform_Plugin", category -> SledgehammerLaunch.isSpongeRegistered());
        
        // - PreInit
        registerCommonMixin("forge.fml.common.MixinLoader", category -> !getModMappings().isEmpty());
        registerCommonMixin("forge.fml.common.MixinMetadataCollection", category -> !getModMappings().isEmpty());
        
        // Server Mixin
        // - ActuallyAdditions
        registerServerMixin("actuallyadditions.mod.tile.MixinTileEntityAtomicReconstructor", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isActuallyAdditionsDisruption());
        
        // - Core
        registerServerMixin("core.advancements.MixinAdvancementManager", ServerMixinCategory::isAdvancementStacktrace);
        registerServerMixin("core.block.MixinBlockGrass", ServerMixinCategory::isBlockGrass);
        registerServerMixin("core.block.MixinBlockIce", ServerMixinCategory::isBlockIce);
        registerServerMixin("core.crash.MixinCrashReport", category -> true);
        registerServerMixin("core.entity.MixinEntity_Teleport", ServerMixinCategory::isItemTeleport);
        registerServerMixin("core.item.MixinItemStack_Exploit", ServerMixinCategory::isItemstackExploit);
        registerServerMixin("core.item.MixinItemWritableBook", ServerMixinCategory::isLimitBooks);
        registerServerMixin("core.network.MixinNetHandlerPlayServer_Book", ServerMixinCategory::isLimitBooks);
        registerServerMixin("core.network.MixinNetHandlerPlayServer_Event", ServerMixinCategory::isInteractEvents);
        registerServerMixin("core.network.MixinNetworkManager", ServerMixinCategory::isFlushNetworkOnTick);
        registerServerMixin("core.network.MixinNetworkSystem", ServerMixinCategory::isNetworkSystem);
        registerServerMixin("core.server.management.MixinPlayerChunkMap", ServerMixinCategory::isPlayerChunkMap);
        registerServerMixin("core.server.MixinDedicatedServer", category -> true);
        registerServerMixin("core.world.biome.MixinBiomeProvider", ServerMixinCategory::isBiomeProvider);
        registerServerMixin("core.world.chunk.storage.MixinAnvilChunkLoader", category ->
                category.isChunkSaveAlert() || category.isChunkSavePurgeAll() || category.isChunkSavePurgeBlacklist() || category.isChunkSaveShutdown());
        registerServerMixin("core.world.chunk.storage.MixinRegionFileChunkBuffer", category ->
                category.isChunkSaveAlert() || category.isChunkSavePurgeAll() || category.isChunkSavePurgeBlacklist() || category.isChunkSaveShutdown());
        
        // - Forge
        registerServerMixin("forge.common.MixinForgeHooks_Advancement", ServerMixinCategory::isAdvancementStacktrace);
        registerServerMixin("forge.entity.passive.MixinEntityVillager", ServerMixinCategory::isTravelingMerchant);
        registerServerMixin("forge.entity.MixinEntity_Teleport", ServerMixinCategory::isItemTeleport);
        registerServerMixin("forge.fml.common.network.simpleimpl.MixinSimpleChannelHandlerWrapper", ServerMixinCategory::isFlushNetworkOnTick);
        registerServerMixin("forge.fml.common.network.simpleimpl.MixinSimpleNetworkWrapper", ServerMixinCategory::isPacketSpam);
        registerServerMixin("forge.fml.common.network.MixinFMLEmbeddedChannel", ServerMixinCategory::isFlushNetworkOnTick);
        registerServerMixin("forge.fml.common.network.MixinFMLEventChannel", ServerMixinCategory::isFlushNetworkOnTick);
        registerServerMixin("forge.world.storage.MixinWorldInfo", ServerMixinCategory::isCeremonyRain);
        
        // - Quark
        registerServerMixin("quark.base.module.MixinModuleLoader", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isQuarkImprovedSleeping());
        
        // - Reliquary
        registerServerMixin("xreliquary.items.MixinItemRendingGale", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isReliquaryItemRendingGale());
        
        // - Ruins
        registerServerMixin("ruins.common.MixinRuinTextLumper", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isRuinsDebug());
        
        // - Sponge
        registerServerMixin("sponge.common.event.tracking.phase.packet.inventory.MixinBasicInventoryPacketState", ServerMixinCategory::isInventoryDebug);
        registerServerMixin("sponge.common.event.MixinSpongeCommonEventFactory", ServerMixinCategory::isInventoryDebug);
    }
    
    public static void registerMods() {
        Sledgehammer.getInstance().getConfig().map(Config::getGeneralCategory).map(GeneralCategory::getModMappings).ifPresent(getModMappings()::putAll);
        registerMod("actuallyadditions", config -> config.getServerMixinCategory().isActuallyAdditionsDisruption());
        registerMod("AS_Ruins", config -> config.getServerMixinCategory().isRuinsDebug());
        registerMod("quark", config -> config.getServerMixinCategory().isQuarkImprovedSleeping());
        registerMod("xreliquary", config -> config.getServerMixinCategory().isReliquaryItemRendingGale());
    }
    
    private static void registerClientMixin(String mixin, Function<ClientMixinCategory, Boolean> function) {
        registerMixin(mixin, config -> function.apply(config.getClientMixinCategory()));
    }
    
    private static void registerCommonMixin(String mixin, Function<CommonMixinCategory, Boolean> function) {
        registerMixin(mixin, config -> function.apply(config.getCommonMixinCategory()));
    }
    
    private static void registerServerMixin(String mixin, Function<ServerMixinCategory, Boolean> function) {
        registerMixin(mixin, config -> function.apply(config.getServerMixinCategory()));
    }
    
    private static void registerMixin(String mixin, Function<Config, Boolean> function) {
        getMixinMappings().put(mixin, Sledgehammer.getInstance().getConfig().map(function).orElse(false));
    }
    
    private static void registerMod(String mod, Function<Config, Boolean> function) {
        if (Sledgehammer.getInstance().getConfig().map(function).orElse(false)) {
            getModMappings().put(mod, true);
        }
    }
    
    public static Optional<Boolean> getMixinMapping(String mixin) {
        return Optional.ofNullable(getMixinMappings().get(StringUtils.removeStart(mixin, "io.github.lxgaming.sledgehammer.mixin.")));
    }
    
    public static Optional<Boolean> getModMapping(String mod) {
        return Optional.ofNullable(getModMappings().get(mod));
    }
    
    public static Map<String, Boolean> getMixinMappings() {
        return MIXIN_MAPPINGS;
    }
    
    public static Map<String, Boolean> getModMappings() {
        return MOD_MAPPINGS;
    }
}