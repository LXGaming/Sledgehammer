/*
 * Copyright 2021 Alex Thomson
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
public class BetterQuestingMixinCategory {
    
    @Mapping(value = "betterquesting.handlers.SaveLoadHandlerMixin", dependencies = {"betterquesting"})
    @Setting(value = "debug", comment = "For debugging purposes")
    private boolean debug = false;
    
    @Mapping(value = "betterquesting.api2.utils.BQThreadedIOMixin", dependencies = {"betterquesting"})
    @Mapping(value = "betterquesting.core.BetterQuestingMixin", dependencies = {"betterquesting"})
    @Setting(value = "await-io", comment = "If 'true', waits for enqueued tasks to complete")
    private boolean awaitIO = false;
    
    public boolean isDebug() {
        return debug;
    }
    
    public boolean isAwaitIO() {
        return awaitIO;
    }
}