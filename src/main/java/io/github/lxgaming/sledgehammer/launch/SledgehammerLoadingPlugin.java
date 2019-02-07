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

import io.github.lxgaming.sledgehammer.util.Reference;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
@IFMLLoadingPlugin.Name(value = Reference.NAME)
public class SledgehammerLoadingPlugin implements IFMLLoadingPlugin {
    
    public SledgehammerLoadingPlugin() {
        SledgehammerLaunch.configureClassLoader(Launch.classLoader);
        SledgehammerLaunch.getTweakers().add(0, new SledgehammerTweaker());
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
        File file = Toolbox.cast(data.get("coremodLocation"), File.class);
        String location;
        if (file != null) {
            location = file.getName();
        } else {
            location = "unknown";
        }
        
        // Remove Transformer
        if (CoreModManager.getTransformers().remove(Reference.NAME + " (" + location + ")") == null) {
            // Remove Transformer fallback
            CoreModManager.getTransformers().keySet().removeIf(name -> StringUtils.startsWithAny(name, Reference.NAME, getClass().getSimpleName()));
        }
    }
    
    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}