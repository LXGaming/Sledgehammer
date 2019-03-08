/*
 * Copyright 2017 Alex Thomson
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

package io.github.lxgaming.sledgehammer;

import com.google.common.collect.Maps;
import io.github.lxgaming.sledgehammer.command.SledgehammerCommand;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.Configuration;
import io.github.lxgaming.sledgehammer.configuration.category.IntegrationCategory;
import io.github.lxgaming.sledgehammer.configuration.category.MixinCategory;
import io.github.lxgaming.sledgehammer.integration.BotaniaIntegration;
import io.github.lxgaming.sledgehammer.integration.ForgeIntegration;
import io.github.lxgaming.sledgehammer.integration.MistIntegration;
import io.github.lxgaming.sledgehammer.integration.PrimalIntegration;
import io.github.lxgaming.sledgehammer.integration.SpongeIntegration_Border;
import io.github.lxgaming.sledgehammer.integration.SpongeIntegration_Death;
import io.github.lxgaming.sledgehammer.integration.SpongeIntegration_Phase;
import io.github.lxgaming.sledgehammer.launch.SledgehammerLaunch;
import io.github.lxgaming.sledgehammer.manager.CommandManager;
import io.github.lxgaming.sledgehammer.manager.IntegrationManager;
import io.github.lxgaming.sledgehammer.util.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.common.launch.SpongeLaunch;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Sledgehammer {
    
    private static Sledgehammer instance;
    private final Logger logger = LogManager.getLogger(Reference.NAME);
    private final Configuration configuration = new Configuration(SpongeLaunch.getConfigDir().resolve(Reference.ID + ".conf"));
    private final Map<String, Function<MixinCategory, Boolean>> mixinMappings = Maps.newHashMap();
    private final Map<String, Boolean> modMappings = Maps.newHashMap();
    private PluginContainer pluginContainer;
    
    private Sledgehammer() {
        instance = this;
    }
    
    public static boolean init() {
        if (getInstance() != null) {
            return false;
        }
        
        Sledgehammer sledgehammer = new Sledgehammer();
        sledgehammer.getConfiguration().loadConfiguration();
        sledgehammer.registerMods();
        sledgehammer.registerMixins();
        sledgehammer.getConfiguration().saveConfiguration();
        return true;
    }
    
    protected void registerCommands() {
        CommandManager.registerCommand(SledgehammerCommand.class);
    }
    
    protected void registerIntegrations() {
        IntegrationManager.registerIntegration(BotaniaIntegration.class, IntegrationCategory::isBotania);
        IntegrationManager.registerIntegration(ForgeIntegration.class, IntegrationCategory::isForge);
        IntegrationManager.registerIntegration(MistIntegration.class, IntegrationCategory::isMist);
        IntegrationManager.registerIntegration(PrimalIntegration.class, IntegrationCategory::isPrimal);
        IntegrationManager.registerIntegration(SpongeIntegration_Border.class, IntegrationCategory::isSpongeBorder);
        IntegrationManager.registerIntegration(SpongeIntegration_Death.class, IntegrationCategory::isSpongeDeath);
        IntegrationManager.registerIntegration(SpongeIntegration_Phase.class, IntegrationCategory::isSpongePhase);
    }
    
    protected void registerMixins() {
        // Mixin ActuallyAdditions
        getMixinMappings().put("actuallyadditions.mod.tile.MixinTileEntityAtomicReconstructor", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isActuallyAdditionsDisruption());
        
        // Mixin Core
        getMixinMappings().put("core.advancements.MixinAdvancementManager", MixinCategory::isAdvancementStacktrace);
        getMixinMappings().put("core.block.MixinBlockGrass", MixinCategory::isBlockGrass);
        getMixinMappings().put("core.block.MixinBlockIce", MixinCategory::isBlockIce);
        getMixinMappings().put("core.crash.MixinCrashReport", category -> true);
        getMixinMappings().put("core.entity.MixinEntity_Teleport", MixinCategory::isItemTeleport);
        getMixinMappings().put("core.item.MixinItemStack_Exploit", MixinCategory::isItemstackExploit);
        getMixinMappings().put("core.item.MixinItemWritableBook", MixinCategory::isLimitBooks);
        getMixinMappings().put("core.network.MixinNetHandlerPlayServer_Book", MixinCategory::isLimitBooks);
        getMixinMappings().put("core.network.MixinNetHandlerPlayServer_Event", MixinCategory::isInteractEvents);
        getMixinMappings().put("core.network.MixinNetworkManager", MixinCategory::isFlushNetworkOnTick);
        getMixinMappings().put("core.network.MixinNetworkSystem", MixinCategory::isNetworkSystem);
        getMixinMappings().put("core.server.management.MixinPlayerChunkMap", MixinCategory::isPlayerChunkMap);
        getMixinMappings().put("core.server.MixinDedicatedServer", category -> true);
        getMixinMappings().put("core.world.biome.MixinBiomeProvider", MixinCategory::isBiomeProvider);
        getMixinMappings().put("core.world.chunk.storage.MixinAnvilChunkLoader", category ->
                category.isChunkSaveAlert() || category.isChunkSavePurgeAll() || category.isChunkSavePurgeBlacklist() || category.isChunkSaveShutdown());
        getMixinMappings().put("core.world.chunk.storage.MixinRegionFileChunkBuffer", category ->
                category.isChunkSaveAlert() || category.isChunkSavePurgeAll() || category.isChunkSavePurgeBlacklist() || category.isChunkSaveShutdown());
        
        // Mixin Forge
        getMixinMappings().put("forge.common.MixinForgeHooks_Advancement", MixinCategory::isAdvancementStacktrace);
        getMixinMappings().put("forge.entity.passive.MixinEntityVillager", MixinCategory::isTravelingMerchant);
        getMixinMappings().put("forge.entity.MixinEntity_Teleport", MixinCategory::isItemTeleport);
        getMixinMappings().put("forge.fml.common.network.simpleimpl.MixinSimpleChannelHandlerWrapper", MixinCategory::isFlushNetworkOnTick);
        getMixinMappings().put("forge.fml.common.network.simpleimpl.MixinSimpleNetworkWrapper", MixinCategory::isPacketSpam);
        getMixinMappings().put("forge.fml.common.network.MixinFMLEmbeddedChannel", MixinCategory::isFlushNetworkOnTick);
        getMixinMappings().put("forge.fml.common.network.MixinFMLEventChannel", MixinCategory::isFlushNetworkOnTick);
        getMixinMappings().put("forge.world.storage.MixinWorldInfo", MixinCategory::isCeremonyRain);
        
        // Mixin PreInit
        getMixinMappings().put("forge.fml.common.MixinLoader", category -> !getModMappings().isEmpty());
        getMixinMappings().put("forge.fml.common.MixinMetadataCollection", category -> !getModMappings().isEmpty());
        
        // Mixin Quark
        getMixinMappings().put("quark.base.module.MixinModuleLoader", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isQuarkImprovedSleeping());
        
        // Mixin Reliquary
        getMixinMappings().put("xreliquary.items.MixinItemRendingGale", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isReliquaryItemRendingGale());
        
        // Mixin Ruins
        getMixinMappings().put("ruins.common.MixinRuinTextLumper", category ->
                SledgehammerLaunch.isForgeRegistered() && category.isRuinsDebug());
        
        // Mixin Sponge
        getMixinMappings().put("sponge.common.event.tracking.phase.packet.inventory.MixinBasicInventoryPacketState", MixinCategory::isInventoryDebug);
        getMixinMappings().put("sponge.common.event.MixinSpongeCommonEventFactory", MixinCategory::isInventoryDebug);
    }
    
    protected void registerMods() {
        getConfig().map(Config::getModMappings).ifPresent(getModMappings()::putAll);
        addModMapping("actuallyadditions", config -> config.getMixinCategory().isActuallyAdditionsDisruption());
        addModMapping("AS_Ruins", config -> config.getMixinCategory().isRuinsDebug());
        addModMapping("quark", config -> config.getMixinCategory().isQuarkImprovedSleeping());
        addModMapping("xreliquary", config -> config.getMixinCategory().isReliquaryItemRendingGale());
    }
    
    public void addModMapping(String id, Function<Config, Boolean> function) {
        if (getConfig().map(function).orElse(false)) {
            getModMappings().put(id, true);
        }
    }
    
    public void debugMessage(String format, Object... arguments) {
        if (getConfig().map(Config::isDebug).orElse(false)) {
            getLogger().info(format, arguments);
        }
    }
    
    public static Sledgehammer getInstance() {
        return instance;
    }
    
    public Logger getLogger() {
        return logger;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
    
    public Optional<Config> getConfig() {
        if (getConfiguration() != null) {
            return Optional.ofNullable(getConfiguration().getConfig());
        }
        
        return Optional.empty();
    }
    
    public Map<String, Function<MixinCategory, Boolean>> getMixinMappings() {
        return mixinMappings;
    }
    
    public Optional<Boolean> getMixinMapping(String id) {
        Function<MixinCategory, Boolean> mixinMapping = getMixinMappings().get(StringUtils.removeStart(id, "io.github.lxgaming.sledgehammer.mixin."));
        if (mixinMapping != null) {
            return getConfig().map(Config::getMixinCategory).map(mixinMapping);
        }
        
        return Optional.empty();
    }
    
    public Map<String, Boolean> getModMappings() {
        return modMappings;
    }
    
    public Optional<Boolean> getModMapping(String id) {
        return Optional.ofNullable(getModMappings().get(id));
    }
    
    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }
    
    protected void setPluginContainer(PluginContainer pluginContainer) {
        this.pluginContainer = pluginContainer;
    }
}