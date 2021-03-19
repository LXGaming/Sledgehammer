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

import io.github.lxgaming.sledgehammer.configuration.annotation.Mapping;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class CoreMixinCategory {
    
    @Mapping(value = "core.client.MinecraftMixin_DropItem")
    @Setting(value = "drop-item", comment = "If 'true', prevents items getting dropped from the hotbar when the drop key is pressed while the mouse cursor is over an empty slot.")
    private boolean dropItem = false;
    
    @Mapping(value = "core.util.LazyValueMixin")
    @Setting(value = "lazy-value-thread-safe", comment = "If 'true', makes LazyValue Thread-safe.")
    private boolean lazyValueThreadSafe = false;
    
    @Mapping(value = "core.network.play.ServerPlayNetHandlerMixin_Sleep")
    @Setting(value = "leave-sleep", comment = "If 'true', allows players to exit the sleep screen.")
    private boolean leaveSleep = false;
    
    @Mapping(value = "core.client.GameSettingsMixin")
    @Setting(value = "tutorial", comment = "If 'true', disables Tutorial on startup.")
    private boolean tutorial = false;
    
    public boolean isDropItem() {
        return dropItem;
    }
    
    public boolean isLazyValueThreadSafe() {
        return lazyValueThreadSafe;
    }
    
    public boolean isLeaveSleep() {
        return leaveSleep;
    }
    
    public boolean isTutorial() {
        return tutorial;
    }
}