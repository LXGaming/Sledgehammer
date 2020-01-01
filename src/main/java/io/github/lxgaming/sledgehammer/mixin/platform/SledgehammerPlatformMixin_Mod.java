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
import io.github.lxgaming.sledgehammer.util.StringUtils;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(value = SledgehammerPlatform.class, priority = 1337, remap = false)
public abstract class SledgehammerPlatformMixin_Mod {
    
    @Shadow
    private static SledgehammerPlatform instance;
    
    private SledgehammerPlatform.State platform$state;
    
    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        Sledgehammer.getInstance().getLogger().warn("Certificate Fingerprint Violation Detected!");
    }
    
    @Mod.EventHandler
    public void onState(FMLStateEvent event) {
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
        
        MappingManager.STATE_MAPPINGS.put(FMLPreInitializationEvent.class, SledgehammerPlatform.State.PRE_INITIALIZATION);
        MappingManager.STATE_MAPPINGS.put(FMLInitializationEvent.class, SledgehammerPlatform.State.INITIALIZATION);
        MappingManager.STATE_MAPPINGS.put(FMLPostInitializationEvent.class, SledgehammerPlatform.State.POST_INITIALIZATION);
        MappingManager.STATE_MAPPINGS.put(FMLLoadCompleteEvent.class, SledgehammerPlatform.State.LOAD_COMPLETE);
        MappingManager.STATE_MAPPINGS.put(FMLServerAboutToStartEvent.class, SledgehammerPlatform.State.SERVER_ABOUT_TO_START);
        MappingManager.STATE_MAPPINGS.put(FMLServerStartingEvent.class, SledgehammerPlatform.State.SERVER_STARTING);
        MappingManager.STATE_MAPPINGS.put(FMLServerStartedEvent.class, SledgehammerPlatform.State.SERVER_STARTED);
        MappingManager.STATE_MAPPINGS.put(FMLServerStoppingEvent.class, SledgehammerPlatform.State.SERVER_STOPPING);
        MappingManager.STATE_MAPPINGS.put(FMLServerStoppedEvent.class, SledgehammerPlatform.State.SERVER_STOPPED);
        
        IntegrationManager.execute();
        
        ModContainer modContainer = Loader.instance().activeModContainer();
        if (modContainer != null && StringUtils.equals(modContainer.getModId(), Sledgehammer.ID)) {
            modContainer.getMetadata().logoFile = Sledgehammer.ID + ".png";
        }
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    public boolean isLoaded(@Nonnull String id) {
        return Loader.isModLoaded(id);
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    @Nullable
    public Object getContainer() {
        return Loader.instance().getIndexedModList().get(Sledgehammer.ID);
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    @Nullable
    public MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
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