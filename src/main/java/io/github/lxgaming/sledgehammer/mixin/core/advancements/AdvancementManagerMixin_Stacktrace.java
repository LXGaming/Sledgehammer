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
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AdvancementManager.class, priority = 1337)
public abstract class AdvancementManagerMixin_Stacktrace {
    
    @Redirect(
            method = "loadCustomAdvancements",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V",
                    remap = false
            )
    )
    private void onCustomAdvancementsError(Logger logger, String message, Throwable throwable) {
        Sledgehammer.getInstance().getLogger().error("{} - {}", message, throwable.getMessage());
    }
    
    @Redirect(
            method = "loadBuiltInAdvancements",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V",
                    remap = false
            )
    )
    private void onBuiltInAdvancementsError(Logger logger, String message, Throwable throwable) {
        Sledgehammer.getInstance().getLogger().error("{} - {}", message, throwable.getMessage());
    }
}