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

package io.github.lxgaming.sledgehammer.mixin.core.server.management;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.SpongeImplHooks;
import org.spongepowered.common.event.SpongeCommonEventFactory;
import org.spongepowered.common.interfaces.entity.player.IMixinEntityPlayerMP;
import org.spongepowered.common.util.VecHelper;
import org.spongepowered.common.world.WorldUtil;

@Mixin(value = PlayerInteractionManager.class, priority = 137)
public abstract class MixinPlayerInteractionManager {
    
    @Shadow
    public EntityPlayerMP player;
    
    @Inject(method = "onBlockClicked", at = @At(value = "HEAD"), cancellable = true)
    private void onBlockClicked(BlockPos blockPos, EnumFacing facing, CallbackInfo callbackInfo) {
        BlockSnapshot blockSnapshot = WorldUtil.fromNative(this.player.world).createSnapshot(VecHelper.toVector3i(blockPos));
        ItemStack itemStack = this.player.getHeldItemMainhand();
        RayTraceResult rayTraceResult = SpongeImplHooks.rayTraceEyes(this.player, SpongeImplHooks.getBlockReachDistance(this.player));
        
        Vector3d vector3d = null;
        if (rayTraceResult != null) {
            vector3d = VecHelper.toVector3d(rayTraceResult.hitVec);
        }
        
        if (SpongeCommonEventFactory.callInteractItemEventPrimary(this.player, itemStack, EnumHand.MAIN_HAND, vector3d, blockSnapshot).isCancelled()) {
            ((IMixinEntityPlayerMP) this.player).sendBlockChange(blockPos, this.player.world.getBlockState(blockPos));
            // Copied from Forge PlayerInteractionManager
            this.player.world.notifyBlockUpdate(blockPos, this.player.world.getBlockState(blockPos), this.player.world.getBlockState(blockPos), 3);
            callbackInfo.cancel();
        }
    }
}