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
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
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
    private static final Map<Enum, SledgehammerPlatform.State> STATE_MAPPINGS = Maps.newHashMap();
    
    public static void registerMixins() {
        // Client Mixin
        // - Core
        registerClientMixin("core.client.MinecraftMixin", category -> true);
        registerClientMixin("core.network.play.server.SPacketJoinGameMixin", ClientMixinCategory::isWorldTypeLength);
        
        // - Forge
        registerClientMixin("forge.common.ForgeModContainerMixin", ClientMixinCategory::isNukeSearchTree);
        
        // - ImmersiveEngineering
        registerClientMixin("immersiveengineering.client.render.TileRenderWorkbenchMixin", ClientMixinCategory::isImmersiveEngineeringWorkbenchRender);
        
        // Common Mixin
        // - Core
        registerCommonMixin("core.util.text.TextComponentTranslationMixin", CommonMixinCategory::isInvalidTranslation);
        registerCommonMixin("core.util.LazyLoadBaseMixin", CommonMixinCategory::isLazyLoadThreadSafe);
        
        // - Platform
        registerCommonMixin("platform.SledgehammerPlatformMixin_Mod", category ->
                SledgehammerLaunch.isForgeRegistered() && !SledgehammerLaunch.isSpongeRegistered());
        
        registerCommonMixin("platform.SledgehammerPlatformMixin_Plugin", category ->
                SledgehammerLaunch.isSpongeRegistered());
        
        // - PreInit
        registerCommonMixin("forge.fml.common.LoaderMixin", category -> !getModMappings().isEmpty());
        registerCommonMixin("forge.fml.common.MetadataCollectionAccessor", category -> !getModMappings().isEmpty());
        
        // Server Mixin
        // - ActuallyAdditions
        registerServerMixin("actuallyadditions.mod.tile.TileEntityAtomicReconstructorMixin", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isActuallyAdditionsDisruption());
        
        // - CarryOn
        registerServerMixin("carryon.common.event.ItemEventsMixin", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isCarryOnCME());
        
        // - Core
        registerServerMixin("core.advancements.AdvancementManagerMixin_Reload", ServerMixinCategory::isAdvancementReload);
        registerServerMixin("core.advancements.AdvancementManagerMixin_Stacktrace", ServerMixinCategory::isAdvancementStacktrace);
        registerServerMixin("core.advancements.PlayerAdvancementsMixin", category ->
                category.isAdvancementInitialized() && SledgehammerLaunch.isSpongeRegistered());
        
        registerServerMixin("core.block.BlockGrassMixin", ServerMixinCategory::isBlockGrass);
        registerServerMixin("core.block.BlockIceMixin", ServerMixinCategory::isBlockIce);
        registerServerMixin("core.crash.CrashReportMixin", category -> true);
        registerServerMixin("core.entity.EntityMixin_Teleport", category ->
                category.isItemTeleport() && SledgehammerLaunch.isSpongeRegistered());
        
        registerServerMixin("core.item.ItemStackMixin_Exploit", ServerMixinCategory::isItemstackExploit);
        registerServerMixin("core.item.ItemWritableBookMixin", ServerMixinCategory::isLimitBooks);
        registerServerMixin("core.network.NetHandlerPlayServerMixin_Book", ServerMixinCategory::isLimitBooks);
        registerServerMixin("core.network.NetHandlerPlayServerMixin_Event", category ->
                category.isInteractEvents() && SledgehammerLaunch.isSpongeRegistered());
        
        registerServerMixin("core.network.NetworkManagerMixin", ServerMixinCategory::isFlushNetworkOnTick);
        registerServerMixin("core.network.NetworkSystemMixin", ServerMixinCategory::isNetworkSystem);
        registerServerMixin("core.server.management.PlayerChunkMapMixin", ServerMixinCategory::isPlayerChunkMap);
        registerServerMixin("core.server.DedicatedServerMixin", category -> true);
        registerServerMixin("core.tileentity.TileEntityMixin", category ->
                category.isTileEntityStackOverflow() && SledgehammerLaunch.isSpongeRegistered());
        
        registerServerMixin("core.world.biome.BiomeProviderMixin", ServerMixinCategory::isBiomeProvider);
        registerServerMixin("core.world.chunk.storage.AnvilChunkLoaderMixin", category ->
                category.isChunkSaveAlert() || category.isChunkSavePurgeAll() || category.isChunkSavePurgeBlacklist() || category.isChunkSaveShutdown());
        
        registerServerMixin("core.world.chunk.storage.RegionFileChunkBufferMixin", category ->
                category.isChunkSaveAlert() || category.isChunkSavePurgeAll() || category.isChunkSavePurgeBlacklist() || category.isChunkSaveShutdown());
        
        // - Forge
        registerServerMixin("forge.common.ForgeHooksMixin_Advancement", ServerMixinCategory::isAdvancementStacktrace);
        registerServerMixin("forge.entity.passive.EntityVillagerMixin", category ->
                category.isTravelingMerchant() && SledgehammerLaunch.isSpongeRegistered());
        
        registerServerMixin("forge.entity.EntityMixin_Teleport", category ->
                category.isItemTeleport() && SledgehammerLaunch.isSpongeRegistered());
        
        registerServerMixin("forge.fml.common.network.simpleimpl.SimpleChannelHandlerWrapperMixin", ServerMixinCategory::isFlushNetworkOnTick);
        registerServerMixin("forge.fml.common.network.simpleimpl.SimpleNetworkWrapperMixin", ServerMixinCategory::isPacketSpam);
        registerServerMixin("forge.fml.common.network.FMLEmbeddedChannelMixin", ServerMixinCategory::isFlushNetworkOnTick);
        registerServerMixin("forge.fml.common.network.FMLEventChannelMixin", ServerMixinCategory::isFlushNetworkOnTick);
        registerServerMixin("forge.world.storage.WorldInfoMixin", ServerMixinCategory::isCeremonyRain);
        
        // - ProjectRed
        registerServerMixin("projectred.transportation.TransportationSPHMixin", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isProjectRedExploit());
        
        // - Quark
        registerServerMixin("quark.base.module.ModuleLoaderMixin", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isQuarkImprovedSleeping());
        
        // - Reliquary
        registerServerMixin("xreliquary.items.ItemRendingGaleMixin", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isReliquaryItemRendingGale());
        
        // - Ruins
        registerServerMixin("ruins.common.RuinTextLumperMixin", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isRuinsDebug());
        
        // - Sponge
        registerServerMixin("sponge.common.command.WrapperCommandSourceMixin", category ->
                category.isCommandSource() && SledgehammerLaunch.isSpongeRegistered());
        
        registerServerMixin("sponge.common.event.tracking.phase.packet.inventory.BasicInventoryPacketStateMixin", category ->
                category.isInventoryDebug() && SledgehammerLaunch.isSpongeRegistered());
        
        registerServerMixin("sponge.common.event.SpongeCommonEventFactoryMixin", category ->
                category.isInventoryDebug() && SledgehammerLaunch.isSpongeRegistered());
        
        // - TombManyGraves
        registerServerMixin("tombmanygraves.events.CommonEventsMixin", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isTombManyGraves());
        
        // - Topography
        registerServerMixin("topography.event.EventSubscriberMixin", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isTopographyDimensionChange());
    }
    
    public static void registerMods() {
        Sledgehammer.getInstance().getConfig().map(Config::getGeneralCategory).map(GeneralCategory::getModMappings).ifPresent(getModMappings()::putAll);
        registerMod("actuallyadditions", config -> config.getServerMixinCategory().isActuallyAdditionsDisruption());
        registerMod("AS_Ruins", config -> config.getServerMixinCategory().isRuinsDebug());
        registerMod("carryon", config -> config.getServerMixinCategory().isCarryOnCME());
        registerMod("immersiveengineering", config -> config.getClientMixinCategory().isImmersiveEngineeringWorkbenchRender());
        registerMod("projectred-transportation", config -> config.getServerMixinCategory().isProjectRedExploit());
        registerMod("quark", config -> config.getServerMixinCategory().isQuarkImprovedSleeping());
        registerMod("tombmanygraves", config -> config.getServerMixinCategory().isTombManyGraves());
        registerMod("topography", config -> config.getServerMixinCategory().isTopographyDimensionChange());
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
    
    public static Optional<SledgehammerPlatform.State> getStateMapping(Enum state) {
        return Optional.ofNullable(getStateMappings().get(state));
    }
    
    public static Map<String, Boolean> getMixinMappings() {
        return MIXIN_MAPPINGS;
    }
    
    public static Map<String, Boolean> getModMappings() {
        return MOD_MAPPINGS;
    }
    
    public static Map<Enum, SledgehammerPlatform.State> getStateMappings() {
        return STATE_MAPPINGS;
    }
}