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

package io.github.lxgaming.sledgehammer;

import io.github.lxgaming.sledgehammer.util.Reference;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import org.apache.commons.lang3.StringUtils;

@Mod(
        modid = Reference.ID,
        name = Reference.NAME,
        version = Reference.VERSION,
        acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS,
        acceptableRemoteVersions = Reference.ACCEPTABLE_REMOTE_VERSIONS,
        certificateFingerprint = Reference.CERTIFICATE_FINGERPRINT
)
public class SledgehammerMod {
    
    @Mod.Instance
    private static SledgehammerMod instance;
    
    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        throw new SecurityException("Certificate Fingerprint Violation Detected!");
    }
    
    @Mod.EventHandler
    public void onConstruction(FMLConstructionEvent event) {
        Sledgehammer.init();
        
        ModContainer modContainer = Loader.instance().activeModContainer();
        if (modContainer != null && StringUtils.equals(modContainer.getModId(), Reference.ID)) {
            modContainer.getMetadata().logoFile = Reference.ID + ".png";
        }
    }
    
    public static SledgehammerMod getInstance() {
        return instance;
    }
}