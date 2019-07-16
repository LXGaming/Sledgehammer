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

package io.github.lxgaming.sledgehammer.mixin.core.network;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.SpongeImplHooks;
import org.spongepowered.common.event.SpongeCommonEventFactory;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;
import org.spongepowered.common.util.VecHelper;

@Mixin(value = NetHandlerPlayServer.class, priority = 137)
public abstract class NetHandlerPlayServerMixin_Event {
    
    @Shadow
    public EntityPlayerMP player;
    
    @Inject(method = "processTryUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getWorld(I)Lnet/minecraft/world/WorldServer;"), cancellable = true)
    private void onProcessTryUseItem(CPacketPlayerTryUseItem packet, CallbackInfo callbackInfo) {
        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
            // Avoids firing a second event
            if (SpongeCommonEventFactory.lastSecondaryPacketTick == SpongeImpl.getServer().getTickCounter()) {
                return;
            }
            
            ItemStack itemStack = this.player.getHeldItem(packet.getHand());
            Sponge.getCauseStackManager().addContext(EventContextKeys.USED_ITEM, ItemStackUtil.snapshotOf(itemStack));
            RayTraceResult rayTraceResult = SpongeImplHooks.rayTraceEyes(this.player, SpongeImplHooks.getBlockReachDistance(this.player));
            
            Vector3d vector3d = null;
            if (rayTraceResult != null) {
                vector3d = VecHelper.toVector3d(rayTraceResult.hitVec);
            }
            
            boolean isCancelled = SpongeCommonEventFactory.callInteractItemEventSecondary(frame, this.player, itemStack, packet.getHand(), vector3d, BlockSnapshot.NONE).isCancelled();
            SpongeImpl.postEvent(SpongeCommonEventFactory.createInteractBlockEventSecondary(this.player, itemStack, vector3d, BlockSnapshot.NONE, Direction.NONE, packet.getHand()));
            if (isCancelled) {
                // Multiple slots may have been changed on the client. Right
                // clicking armor is one example - the client changes it
                // without the server telling it to.
                this.player.sendAllContents(this.player.openContainer, this.player.openContainer.getInventory());
            }
        }
    }
}