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

package io.github.lxgaming.sledgehammer.configuration.category.mixin;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ClientMixinCategory {
    
    @Setting(value = "nuke-search-tree", comment = "Disable SearchTree reloading (Speeds up server connection process)")
    private boolean nukeSearchTree = true;
    
    @Setting(value = "world-type-length", comment = "Increase the maximum length for a WorldType name in SPacketJoinGame")
    private boolean worldTypeLength = false;
    
    public boolean isNukeSearchTree() {
        return nukeSearchTree;
    }
    
    public boolean isWorldTypeLength() {
        return worldTypeLength;
    }
}