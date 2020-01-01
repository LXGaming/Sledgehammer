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

package io.github.lxgaming.sledgehammer.mixin.core.advancements;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraft.advancements.AdvancementManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AdvancementManager.class, priority = 1337)
public abstract class AdvancementManagerMixin_Reload {
    
    private static boolean sledgehammer$INITIALIZED = false;
    
    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancements/AdvancementManager;reload()V"
            )
    )
    private void onReload(AdvancementManager advancementManager) {
        if (sledgehammer$INITIALIZED) {
            Sledgehammer.getInstance().getLogger().warn("Advancement Reload Cancelled");
            return;
        }
        
        sledgehammer$INITIALIZED = true;
        Sledgehammer.getInstance().getLogger().info("Advancement Reloading...");
        advancementManager.reload();
    }
}