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

package io.github.lxgaming.sledgehammer.mixin.sponge.common.event;

import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.event.SpongeCauseStackManager;

import java.util.Deque;

@Mixin(value = SpongeCauseStackManager.class, priority = 1337, remap = false)
public abstract class MixinSpongeCauseStackManager implements CauseStackManager {
    
    @Shadow
    @Final
    private Deque<Object> cause;
    
    @Redirect(method = "popCauseFrame", at = @At(value = "FIELD", target = "Lorg/spongepowered/common/event/SpongeCauseStackManager;duplicateCauses:[I", args = "array=set"))
    private void onPopCauseFrame(int[] duplicateCauses, int index, int value) {
        int size = this.cause.size();
        if (duplicateCauses.length > size) {
            duplicateCauses[size] = value;
        }
    }
}