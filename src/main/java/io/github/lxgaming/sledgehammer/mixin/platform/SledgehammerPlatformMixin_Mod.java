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

package io.github.lxgaming.sledgehammer.mixin.platform;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.manager.CommandManager;
import io.github.lxgaming.sledgehammer.manager.IntegrationManager;
import io.github.lxgaming.sledgehammer.manager.LocaleManager;
import io.github.lxgaming.sledgehammer.manager.MappingManager;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(value = SledgehammerPlatform.class, remap = false)
public abstract class SledgehammerPlatformMixin_Mod {
    
    @Shadow
    private static SledgehammerPlatform instance;
    
    private SledgehammerPlatform.State platform$state;
    
    public void onModLifecycle(ModLifecycleEvent event) {
        MappingManager.getStateMapping(event.getClass()).ifPresent(this::platform$setState);
    }
    
    public void onServerLifecycle(ServerLifecycleEvent event) {
        MappingManager.getStateMapping(event.getClass()).ifPresent(this::platform$setState);
    }
    
    @Inject(
            method = "<init>",
            at = @At(
                    value = "RETURN"
            )
    )
    private void onInit(CallbackInfo callbackInfo) {
        instance = Toolbox.cast(this, SledgehammerPlatform.class);
        this.platform$state = SledgehammerPlatform.State.CONSTRUCTION;
        
        Sledgehammer.init();
        CommandManager.prepare();
        IntegrationManager.prepare();
        LocaleManager.prepare();
        
        MappingManager.STATE_MAPPINGS.put(FMLCommonSetupEvent.class, SledgehammerPlatform.State.COMMON_SETUP);
        MappingManager.STATE_MAPPINGS.put(FMLClientSetupEvent.class, SledgehammerPlatform.State.CLIENT_SETUP);
        MappingManager.STATE_MAPPINGS.put(FMLDedicatedServerSetupEvent.class, SledgehammerPlatform.State.DEDICATED_SERVER_SETUP);
        MappingManager.STATE_MAPPINGS.put(FMLLoadCompleteEvent.class, SledgehammerPlatform.State.LOAD_COMPLETE);
        MappingManager.STATE_MAPPINGS.put(FMLServerAboutToStartEvent.class, SledgehammerPlatform.State.SERVER_ABOUT_TO_START);
        MappingManager.STATE_MAPPINGS.put(FMLServerStartingEvent.class, SledgehammerPlatform.State.SERVER_STARTING);
        MappingManager.STATE_MAPPINGS.put(FMLServerStartedEvent.class, SledgehammerPlatform.State.SERVER_STARTED);
        MappingManager.STATE_MAPPINGS.put(FMLServerStoppingEvent.class, SledgehammerPlatform.State.SERVER_STOPPING);
        MappingManager.STATE_MAPPINGS.put(FMLServerStoppedEvent.class, SledgehammerPlatform.State.SERVER_STOPPED);
        
        IntegrationManager.execute();
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModLifecycle);
        MinecraftForge.EVENT_BUS.addListener(this::onServerLifecycle);
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    public boolean isLoaded(@Nonnull String id) {
        return ModList.get().isLoaded(id);
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    @Nullable
    public Object getContainer() {
        return ModList.get().getModContainerById(Sledgehammer.ID);
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    @Nullable
    public MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    @Nonnull
    public SledgehammerPlatform.State getState() {
        return this.platform$state;
    }
    
    private void platform$setState(SledgehammerPlatform.State state) {
        if (state == null) {
            return;
        }
        
        Sledgehammer.getInstance().getLogger().debug("State: {} -> {}", this.platform$state.getName(), state.getName());
        this.platform$state = state;
        
        IntegrationManager.execute();
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    @Nonnull
    public SledgehammerPlatform.Type getType() {
        return SledgehammerPlatform.Type.FORGE;
    }
}