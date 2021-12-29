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

package io.github.lxgaming.sledgehammer.launch;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
@IFMLLoadingPlugin.Name(value = Sledgehammer.NAME)
@IFMLLoadingPlugin.SortingIndex(value = Integer.MIN_VALUE)
public class SledgehammerLoadingPlugin implements IFMLLoadingPlugin {
    
    public SledgehammerLoadingPlugin() {
        if (!SledgehammerLaunch.isMixinTweakerPresent()) {
            SledgehammerLaunch.getLogger().error("------------------------- ERROR -------------------------");
            SledgehammerLaunch.getLogger().error("An attempt to initialize {} was made but the MixinTweaker is not present", Sledgehammer.NAME);
            SledgehammerLaunch.getLogger().error("This indicates that Mixin has been initialized incorrectly");
            SledgehammerLaunch.getLogger().error("------------------------- ERROR -------------------------");
        }
        
        if (SledgehammerLaunch.isStateTweakerPresent()) {
            SledgehammerLaunch.getLogger().error("------------------------- ERROR -------------------------");
            SledgehammerLaunch.getLogger().error("An attempt to initialize {} was made by the EnvironmentStateTweaker", Sledgehammer.NAME);
            SledgehammerLaunch.getLogger().error("This indicates that Mixin has been initialized incorrectly");
            SledgehammerLaunch.getLogger().error("------------------------- ERROR -------------------------");
        }
        
        if (!SledgehammerLaunch.isSledgehammerTweakerPresent()) {
            SledgehammerLaunch.getLogger().debug("Initializing {} from {}", SledgehammerTweaker.class.getSimpleName(), getClass().getSimpleName());
            new SledgehammerTweaker();
        }
    }
    
    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    @Override
    public String getModContainerClass() {
        return null;
    }
    
    @Override
    public String getSetupClass() {
        return null;
    }
    
    @Override
    public void injectData(Map<String, Object> data) {
        File file = (File) data.get("coremodLocation");
        String location;
        if (file != null) {
            location = file.getName();
        } else {
            location = "unknown";
        }
        
        // Remove Transformer
        if (CoreModManager.getTransformers().remove(Sledgehammer.NAME + " (" + location + ")") == null) {
            // Remove Transformer fallback
            CoreModManager.getTransformers().keySet().removeIf(name -> name.startsWith(Sledgehammer.NAME) || name.startsWith(getClass().getSimpleName()));
        }
    }
    
    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}