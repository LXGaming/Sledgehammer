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

package io.github.lxgaming.sledgehammer.mixin.forge.common;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ForgeModContainer.class, priority = 1337, remap = false)
public abstract class ForgeModContainerMixin {
    
    /**
     * Thanks covers1624
     */
    @Redirect(
            method = "mappingChanged",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;reloadSearchTrees()V"
            )
    )
    private void onReloadSearchTrees(FMLCommonHandler handler) {
        Sledgehammer.getInstance().getLogger().warn("Skipping reloadSearchTrees");
    }
}