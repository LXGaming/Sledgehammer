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

package io.github.lxgaming.sledgehammer.configuration.category.general;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MessageCategory {
    
    @Setting(value = "chunk-save", comment = "Sent when a chunk fails to save")
    private String chunkSave = "&fChunk ([X], [Z]) &cfailed to save";
    
    @Setting(value = "item-teleport", comment = "Sent to the player when a thrown item gets deleted")
    private String itemTeleport = "&f[ID] &cwas lost in time and space";
    
    @Setting(value = "move-outside-border", comment = "Sent to the player when attempting to move outside the world border")
    private String moveOutsideBorder = "&cCannot move outside of the world border";
    
    public String getChunkSave() {
        return chunkSave;
    }
    
    public String getItemTeleport() {
        return itemTeleport;
    }
    
    public String getMoveOutsideBorder() {
        return moveOutsideBorder;
    }
}