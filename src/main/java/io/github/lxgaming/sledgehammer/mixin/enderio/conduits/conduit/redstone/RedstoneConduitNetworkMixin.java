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

package io.github.lxgaming.sledgehammer.mixin.enderio.conduits.conduit.redstone;

import crazypants.enderio.base.filter.redstone.IInputSignalFilter;
import crazypants.enderio.conduits.conduit.AbstractConduitNetwork;
import crazypants.enderio.conduits.conduit.redstone.IRedstoneConduit;
import crazypants.enderio.conduits.conduit.redstone.RedstoneConduitNetwork;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(value = RedstoneConduitNetwork.class, remap = false)
public abstract class RedstoneConduitNetworkMixin extends AbstractConduitNetwork<IRedstoneConduit, IRedstoneConduit> {
    
    @Shadow
    protected abstract void updateInputsForSource(@Nonnull IRedstoneConduit con, @Nonnull EnumFacing dir);
    
    @Shadow
    protected abstract void notifyConduitNeighbours(@Nonnull IRedstoneConduit con);
    
    public RedstoneConduitNetworkMixin(@Nonnull Class<IRedstoneConduit> implClass, @Nonnull Class<IRedstoneConduit> baseConduitClass) {
        super(implClass, baseConduitClass);
    }
    
    /**
     * @author LX_Gaming
     * @reason Don't tick conduits in chunks that are queued for unload.
     */
    @Overwrite
    public void tickEnd(TickEvent.ServerTickEvent event, @Nullable Profiler profiler) {
        super.tickEnd(event, profiler);
        
        for (IRedstoneConduit con : getConduits()) {
            for (EnumFacing dir : EnumFacing.VALUES) {
                if (dir != null && ((IInputSignalFilter) con.getSignalFilter(dir, false)).shouldUpdate()) {
                    // Sledgehammer start
                    // updateInputsForSource(con, dir);
                    // Sledgehammer end
                    
                    World world = con.getBundle().getBundleworld();
                    BlockPos pos = con.getBundle().getLocation();
                    
                    // Sledgehammer start
                    int chunkX = pos.getX() >> 4;
                    int chunkZ = pos.getZ() >> 4;
                    
                    Chunk chunk = Toolbox.getLoadedChunkWithoutMarkingActive(world, chunkX, chunkZ);
                    if (chunk == null || !chunk.isLoaded() || chunk.unloadQueued) {
                        break;
                    }
                    
                    updateInputsForSource(con, dir);
                    // Sledgehammer end
                    
                    world.notifyNeighborsOfStateChange(pos, world.getBlockState(pos).getBlock(), false);
                    break;
                }
            }
            
            notifyConduitNeighbours(con);
        }
    }
}