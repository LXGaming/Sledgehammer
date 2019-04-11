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
import io.github.lxgaming.sledgehammer.util.Reference;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SledgehammerPlatform.class, priority = 1337, remap = false)
public abstract class MixinSledgehammerPlatform_Mod {
    
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
        
        ModContainer modContainer = Loader.instance().activeModContainer();
        if (modContainer != null && StringUtils.equals(modContainer.getModId(), Reference.ID)) {
            modContainer.getMetadata().logoFile = Reference.ID + ".png";
        }
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    public Object getContainer() {
        return Loader.instance().getIndexedModList().get(Reference.ID);
    }
    
    /**
     * @author LX_Gaming
     * @reason Forge compatibility
     */
    @Overwrite
    public SledgehammerPlatform.Type getType() {
        return SledgehammerPlatform.Type.FORGE;
    }
}