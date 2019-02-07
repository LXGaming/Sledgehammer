/*
 * Copyright 2018 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.plugin;

import io.github.lxgaming.sledgehammer.launch.SledgehammerLaunch;
import org.apache.commons.lang3.StringUtils;

public class SpongePlugin extends AbstractPlugin {
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!SledgehammerLaunch.isForgeRegistered() && StringUtils.startsWith(mixinClassName, "io.github.lxgaming.sledgehammer.mixin.sponge.mod.")) {
            return false;
        }
        
        return super.shouldApplyMixin(targetClassName, mixinClassName);
    }
}