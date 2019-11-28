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

package io.github.lxgaming.sledgehammer.mixin.bdsandm.network;

import funwayguy.bdsandm.blocks.IStorageBlock;
import funwayguy.bdsandm.network.PacketBdsm;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PacketBdsm.ServerHandler.class, priority = 1337, remap = false)
public abstract class PacketBdsmMixin {
    
    @Redirect(method = "onMessage", at = @At(value = "INVOKE", target = "Lfunwayguy/bdsandm/blocks/IStorageBlock;onPlayerInteract(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/entity/player/EntityPlayerMP;Lnet/minecraft/util/EnumHand;ZZI)V"))
    private void impl$onPlayerInteract(IStorageBlock storageBlock, World world, BlockPos blockPos, IBlockState blockState, EnumFacing enumFacing, EntityPlayerMP entityPlayer, EnumHand enumHand, boolean isHit, boolean altMode, int delay) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            storageBlock.onPlayerInteract(world, blockPos, blockState, enumFacing, entityPlayer, enumHand, isHit, altMode, delay);
        });
    }
}