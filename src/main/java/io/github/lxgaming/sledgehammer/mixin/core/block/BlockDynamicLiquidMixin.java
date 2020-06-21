/*
 * Copyright 2020 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.core.block;

import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BlockDynamicLiquid.class)
public abstract class BlockDynamicLiquidMixin extends BlockLiquid {
    
    protected BlockDynamicLiquidMixin(Material materialIn) {
        super(materialIn);
    }
    
    @Redirect(
            method = "updateTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockDynamicLiquid;getDepth(Lnet/minecraft/block/state/IBlockState;)I"
            )
    )
    private int onGetDepth(BlockDynamicLiquid blockDynamicLiquid, IBlockState blockState) {
        int depth = ((BlockLiquidAccessor) blockDynamicLiquid).accessor$getDepth(blockState);
        // If the block above is falling then our depth will be the max which is 15.
        if (depth >= 8) {
            return 15;
        }
        
        return depth;
    }
}