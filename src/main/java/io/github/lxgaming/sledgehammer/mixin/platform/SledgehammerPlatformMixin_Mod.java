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
import io.github.lxgaming.sledgehammer.manager.MappingManager;
import io.github.lxgaming.sledgehammer.util.StringUtils;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SledgehammerPlatform.class, priority = 1337, remap = false)
public abstract class SledgehammerPlatformMixin_Mod {
    
    @Shadow
    private static SledgehammerPlatform instance;
    
    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        Sledgehammer.getInstance().getLogger().warn("Certificate Fingerprint Violation Detected!");
        // throw new SecurityException("Certificate Fingerprint Violation Detected!");
    }
    
    @Mod.EventHandler
    public void onConstruction(FMLConstructionEvent event) {
        instance = Toolbox.cast(this, SledgehammerPlatform.class);
        Sledgehammer.init();
        
        MappingManager.STATE_MAPPINGS.put(LoaderState.CONSTRUCTING, SledgehammerPlatform.State.CONSTRUCTION);
        MappingManager.STATE_MAPPINGS.put(LoaderState.PREINITIALIZATION, SledgehammerPlatform.State.PRE_INITIALIZATION);
        MappingManager.STATE_MAPPINGS.put(LoaderState.INITIALIZATION, SledgehammerPlatform.State.INITIALIZATION);
        MappingManager.STATE_MAPPINGS.put(LoaderState.POSTINITIALIZATION, SledgehammerPlatform.State.POST_INITIALIZATION);
        MappingManager.STATE_MAPPINGS.put(LoaderState.AVAILABLE, SledgehammerPlatform.State.LOAD_COMPLETE);
        MappingManager.STATE_MAPPINGS.put(LoaderState.SERVER_ABOUT_TO_START, SledgehammerPlatform.State.SERVER_ABOUT_TO_START);
        MappingManager.STATE_MAPPINGS.put(LoaderState.SERVER_STARTING, SledgehammerPlatform.State.SERVER_STARTING);
        MappingManager.STATE_MAPPINGS.put(LoaderState.SERVER_STARTED, SledgehammerPlatform.State.SERVER_STARTED);
        MappingManager.STATE_MAPPINGS.put(LoaderState.SERVER_STOPPING, SledgehammerPlatform.State.SERVER_STOPPING);
        MappingManager.STATE_MAPPINGS.put(LoaderState.SERVER_STOPPED, SledgehammerPlatform.State.SERVER_STOPPED);
        
        IntegrationManager.prepare();
        
        ModContainer modContainer = Loader.instance().activeModContainer();
        if (modContainer != null && StringUtils.equals(modContainer.getModId(), Sledgehammer.ID)) {
            modContainer.getMetadata().logoFile = Sledgehammer.ID + ".png";
        }
    }
    
    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        CommandManager.prepare();
    }
    
    @Mod.EventHandler
    public void onState(FMLStateEvent event) {
        IntegrationManager.execute();
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    public Object getContainer() {
        return Loader.instance().getIndexedModList().get(Sledgehammer.ID);
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    public MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    public SledgehammerPlatform.State getState() {
        return MappingManager.getStateMapping(Loader.instance().getLoaderState()).orElse(null);
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    public SledgehammerPlatform.Type getType() {
        return SledgehammerPlatform.Type.FORGE;
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    public boolean isLoaded(String id) {
        return Loader.isModLoaded(id);
    }
}