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

package io.github.lxgaming.sledgehammer.mixin.logisticspipes.world;

import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import network.rs485.logisticspipes.world.DoubleCoordinates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = DoubleCoordinates.class, remap = false)
public abstract class DoubleCoordinatesMixin {
    
    @Shadow
    public abstract BlockPos getBlockPos();
    
    /**
     * @author LX_Gaming
     * @reason Prevent loading chunks
     */
    @Overwrite
    public boolean blockExists(World world) {
        BlockPos blockPos = getBlockPos();
        int chunkX = blockPos.getX() >> 4;
        int chunkZ = blockPos.getZ() >> 4;
        
        Chunk chunk = Toolbox.getLoadedChunkWithoutMarkingActive(world, chunkX, chunkZ);
        if (chunk == null || !chunk.isLoaded() || chunk.unloadQueued) {
            return false;
        }
        
        IBlockState blockState = chunk.getBlockState(blockPos);
        return !blockState.getBlock().isAir(blockState, world, blockPos);
    }
}