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

package io.github.lxgaming.sledgehammer.mixin.matteroverdrive.machines.pattern_storage;

import matteroverdrive.machines.pattern_storage.TileEntityMachinePatternStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntityMachinePatternStorage.class, priority = 1337, remap = false)
public abstract class TileEntityMachinePatternStorageMixin {
    
    @Inject(
            method = "canConnectFromSide",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onCanConnectFromSide(IBlockState blockState, EnumFacing side, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (blockState == null || blockState.getBlock() == Blocks.AIR) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}