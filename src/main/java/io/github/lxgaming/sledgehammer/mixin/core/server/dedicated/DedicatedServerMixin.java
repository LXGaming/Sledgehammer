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

package io.github.lxgaming.sledgehammer.mixin.core.server.dedicated;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DedicatedServer.class)
public abstract class DedicatedServerMixin {
    
    @Shadow
    @Final
    private static Logger LOGGER;
    
    @Inject(
            method = "initServer",
            at = @At(
                    value = "RETURN"
            )
    )
    private void onInitServer(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (callbackInfoReturnable.getReturnValue()) {
            LOGGER.info("{} v{} was successfully applied!", Sledgehammer.NAME, Sledgehammer.VERSION);
        }
    }
}