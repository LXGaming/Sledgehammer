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

package io.github.lxgaming.sledgehammer.mixin.tombmanygraves.events;

import com.m4thg33k.tombmanygraves.events.CommonEvents;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CommonEvents.class, priority = 1337, remap = false)
public abstract class CommonEventsMixin {
    
    @Inject(method = "onPlayerDeath", at = @At(value = "INVOKE"))
    private void onPlayerDeath(CallbackInfo callbackInfo) {
        callbackInfo.cancel();
        Sledgehammer.getInstance().debug("TombManyGraves was blocked");
    }
}