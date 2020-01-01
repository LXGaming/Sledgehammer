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
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.api.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod(
        modid = Sledgehammer.ID,
        name = Sledgehammer.NAME,
        version = Sledgehammer.VERSION,
        acceptedMinecraftVersions = Sledgehammer.ACCEPTED_VERSIONS,
        acceptableRemoteVersions = Sledgehammer.ACCEPTABLE_REMOTE_VERSIONS,
        certificateFingerprint = Sledgehammer.CERTIFICATE_FINGERPRINT
)
@Plugin(
        id = Sledgehammer.ID,
        name = Sledgehammer.NAME,
        version = Sledgehammer.VERSION,
        description = Sledgehammer.DESCRIPTION,
        authors = {Sledgehammer.AUTHORS},
        url = Sledgehammer.WEBSITE
)
public class SledgehammerPlatform {
    
    private static SledgehammerPlatform instance;
    
    public SledgehammerPlatform() {
        Preconditions.checkState(Sledgehammer.getInstance() != null, "%s has not been initialized!", Sledgehammer.NAME);
        Sledgehammer.getInstance().getLogger().info("{} v{} ({})", Sledgehammer.NAME, Sledgehammer.VERSION, getType());
    }
    
    public static SledgehammerPlatform getInstance() {
        return instance;
    }
    
    public boolean isLoaded(@Nonnull String id) {
        throw new IllegalStateException("isLoaded injection failed");
    }
    
    @Nullable
    public Object getContainer() {
        throw new IllegalStateException("getContainer injection failed");
    }
    
    @Nullable
    public MinecraftServer getServer() {
        throw new IllegalStateException("getServer injection failed");
    }
    
    @Nonnull
    public State getState() {
        throw new IllegalStateException("getState injection failed");
    }
    
    @Nonnull
    public Type getType() {
        throw new IllegalStateException("getType injection failed");
    }
    
    public enum State {
        
        CONSTRUCTION("Construction"),
        
        PRE_INITIALIZATION("Pre Initialization"),
        
        INITIALIZATION("Initialization"),
        
        POST_INITIALIZATION("Post Initialization"),
        
        LOAD_COMPLETE("Load Complete"),
        
        SERVER_ABOUT_TO_START("Server About To Start"),
        
        SERVER_STARTING("Server Starting"),
        
        SERVER_STARTED("Server Started"),
        
        SERVER_STOPPING("Server Stopping"),
        
        SERVER_STOPPED("Server Stopped");
        
        private final String name;
        
        State(String name) {
            this.name = name;
        }
        
        @Nonnull
        public String getName() {
            return name;
        }
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    
    public enum Type {
        
        FORGE("Forge"),
        
        SPONGE("Sponge");
        
        private final String name;
        
        Type(String name) {
            this.name = name;
        }
        
        @Nonnull
        public String getName() {
            return name;
        }
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}