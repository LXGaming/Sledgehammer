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

package io.github.lxgaming.sledgehammer.util.world.chunk;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class EmptyChunk extends Chunk {
    
    public EmptyChunk(World worldIn, int x, int z) {
        super(worldIn, x, z);
    }
    
    @Override
    public boolean isAtLocation(int x, int z) {
        return this.x == x && this.z == z;
    }
    
    @Override
    public int getHeightValue(int x, int z) {
        return 0;
    }
    
    @Override
    protected void generateHeightMap() {
        // no-op
    }
    
    @Override
    public void generateSkylightMap() {
        // no-op
    }
    
    @Override
    public int getBlockLightOpacity(BlockPos pos) {
        return 255;
    }
    
    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return Blocks.AIR.getDefaultState();
    }
    
    @Override
    public int getLightFor(EnumSkyBlock type, BlockPos pos) {
        return type.defaultLightValue;
    }
    
    @Override
    public void setLightFor(EnumSkyBlock type, BlockPos pos, int value) {
        // no-op
    }
    
    @Override
    public int getLightSubtracted(BlockPos pos, int amount) {
        return 0;
    }
    
    @Override
    public void addEntity(Entity entityIn) {
        // no-op
    }
    
    @Override
    public void removeEntity(Entity entityIn) {
        // no-op
    }
    
    @Override
    public void removeEntityAtIndex(Entity entityIn, int index) {
        // no-op
    }
    
    @Override
    public boolean canSeeSky(BlockPos pos) {
        return false;
    }
    
    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos, EnumCreateEntityType creationMode) {
        return null;
    }
    
    @Override
    public void addTileEntity(TileEntity tileEntityIn) {
        // no-op
    }
    
    @Override
    public void addTileEntity(BlockPos pos, TileEntity tileEntityIn) {
        // no-op
    }
    
    @Override
    public void removeTileEntity(BlockPos pos) {
        // no-op
    }
    
    @Override
    public void onLoad() {
        // no-op
    }
    
    @Override
    public void onUnload() {
        // no-op
    }
    
    @Override
    public void markDirty() {
        // no-op
    }
    
    @Override
    public void getEntitiesWithinAABBForEntity(@Nullable Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill, Predicate<? super Entity> filter) {
        // no-op
    }
    
    @Override
    public <T extends Entity> void getEntitiesOfTypeWithinAABB(Class<? extends T> entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate<? super T> filter) {
        // no-op
    }
    
    @Override
    public boolean needsSaving(boolean p_76601_1_) {
        return false;
    }
    
    @Override
    public Random getRandomWithSeed(long seed) {
        return new Random(this.getWorld().getSeed() + (long) (this.x * this.x * 4987142) + (long) (this.x * 5947611) + (long) (this.z * this.z) * 4392871L + (long) (this.z * 389711) ^ seed);
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public boolean isEmptyBetween(int startY, int endY) {
        return true;
    }
}