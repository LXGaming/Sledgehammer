/*
 * Copyright 2021 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.projectred.integration;

import mrtjp.projectred.integration.RedstoneGatePart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = RedstoneGatePart.class, remap = false)
public abstract class RedstoneGatePartMixin {
    
    @ModifyArg(
            method = "getRedstoneInput",
            at = @At(
                    value = "INVOKE",
                    target = "Lmrtjp/projectred/integration/RedstoneGatePart;calcMaxSignal(IZZ)I"
            ),
            index = 1
    )
    private boolean alwaysUseStrongSignal(boolean strong) {
        return true;
    }
}