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

package io.github.lxgaming.sledgehammer.mixin.forge.common;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.common.world.WorldManager;

@Mixin(value = DimensionManager.class, priority = 1337, remap = false)
public abstract class MixinDimensionManager {
    
    /**
     * @author LX_Gaming
     * @reason Fixes https://github.com/SpongePowered/SpongeForge/issues/2173
     */
    @Overwrite
    public static void unregisterDimension(int id) {
        try {
            WorldManager.unregisterDimension(id);
        } catch (IllegalArgumentException ex) {
            Sledgehammer.getInstance().getLogger().error("{}", ex.getMessage());
        }
    }
}