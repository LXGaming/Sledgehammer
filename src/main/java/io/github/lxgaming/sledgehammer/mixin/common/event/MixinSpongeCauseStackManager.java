/*
 * Copyright 2017 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.common.event;

import org.spongepowered.api.Sponge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "org.spongepowered.common.event.SpongeCauseStackManager", priority = 1001, remap = false)
public class MixinSpongeCauseStackManager {
    
    @Inject(method = "enforceMainThread", at = @At("HEAD"), cancellable = true)
    private void onEnforceMainThread(CallbackInfo callbackInfo) {
        if (!(Sponge.isServerAvailable() && !(Sponge.getServer().isMainThread() || Thread.currentThread().getName().equals("Server Shutdown Thread")))) {
            callbackInfo.cancel();
        }
    }
}