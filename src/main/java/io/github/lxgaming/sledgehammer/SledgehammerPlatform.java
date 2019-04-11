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

import com.google.common.base.Preconditions;
import io.github.lxgaming.sledgehammer.util.Reference;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.api.plugin.Plugin;

@Mod(
        modid = Reference.ID,
        name = Reference.NAME,
        version = Reference.VERSION,
        acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS,
        acceptableRemoteVersions = Reference.ACCEPTABLE_REMOTE_VERSIONS,
        certificateFingerprint = Reference.CERTIFICATE_FINGERPRINT
)
@Plugin(
        id = Reference.ID,
        name = Reference.NAME,
        version = Reference.VERSION,
        description = Reference.DESCRIPTION,
        authors = {Reference.AUTHORS},
        url = Reference.WEBSITE
)
public class SledgehammerPlatform {
    
    private static SledgehammerPlatform instance;
    
    public SledgehammerPlatform() {
        Preconditions.checkState(Sledgehammer.getInstance() != null, "%s has not been initialized!", Reference.NAME);
        Sledgehammer.getInstance().getLogger().info("{} v{} ({})", Reference.NAME, Reference.VERSION, getType());
    }
    
    public static SledgehammerPlatform getInstance() {
        return instance;
    }
    
    public Object getContainer() {
        throw new IllegalStateException(String.format("%s injection failed", Reference.NAME));
    }
    
    public Type getType() {
        throw new IllegalStateException(String.format("%s injection failed", Reference.NAME));
    }
    
    public enum Type {
        
        FORGE("Forge"),
        
        SPONGE("Sponge");
        
        private final String name;
        
        Type(String name) {
            this.name = name;
        }
        
        public boolean isForge() {
            return this == FORGE;
        }
        
        public boolean isSponge() {
            return this == SPONGE;
        }
        
        public String getName() {
            return name;
        }
        
        @Override
        public String toString() {
            return getName();
        }
    }
}