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

package io.github.lxgaming.sledgehammer.mixin.sponge.common.event;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.event.SpongeCommonEventFactory;

@Mixin(value = SpongeCommonEventFactory.class, priority = 1337, remap = false)
public abstract class SpongeCommonEventFactoryMixin {
    
    @SuppressWarnings("AmbiguousMixinSledgehammer.")
    @Redirect(
            method = "captureTransaction",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;)V"
            )
    )
    private static void onCaptureTransactionWarn(Logger logger, String message) {
        Sledgehammer.getInstance().debug(message);
    }
}