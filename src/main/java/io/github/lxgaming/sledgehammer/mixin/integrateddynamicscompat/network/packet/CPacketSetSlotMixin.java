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

package io.github.lxgaming.sledgehammer.mixin.integrateddynamicscompat.network.packet;

import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.slot.SlotExtended;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamicscompat.network.packet.CPacketSetSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CPacketSetSlot.class, remap = false)
public abstract class CPacketSetSlotMixin {
    
    @Shadow
    private int slot;
    
    @Shadow
    private ItemStack itemStack;
    
    @Inject(
            method = "actionServer",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onActionServer(World world, ServerPlayerEntity player, CallbackInfo callbackInfo) {
        if (!(player.containerMenu instanceof ContainerLogicProgrammerBase)) {
            callbackInfo.cancel();
            LocaleAdapter.disconnect(player, Locale.MESSAGE_INTEGRATED_DYNAMICS_COMPACT_EXPLOIT, Toolbox.getRegistryName(this.itemStack.getItem()));
        }
        
        if (player.containerMenu.slots.size() <= this.slot) {
            callbackInfo.cancel();
            return;
        }
        
        Slot slot = player.containerMenu.getSlot(this.slot);
        if (!(slot instanceof SlotExtended) || !((SlotExtended) slot).isPhantom() || !(slot.container instanceof SimpleInventory)) {
            callbackInfo.cancel();
            LocaleAdapter.disconnect(player, Locale.MESSAGE_INTEGRATED_DYNAMICS_COMPACT_EXPLOIT, Toolbox.getRegistryName(this.itemStack.getItem()));
        }
    }
}