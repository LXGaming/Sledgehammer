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

package io.github.lxgaming.sledgehammer.mixin.core.network;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = NetHandlerPlayServer.class, priority = 137)
public abstract class MixinNetHandlerPlayServer_Book {
    
    @Inject(method = "processCustomPayload",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemWritableBook;isNBTValid(Lnet/minecraft/nbt/NBTTagCompound;)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onIsNBTValid(CPacketCustomPayload packet, CallbackInfo callbackInfo,
                              String channelName, PacketBuffer packetBuffer, ItemStack itemStack) {
        NBTTagCompound compound = itemStack.getTagCompound();
        if (compound != null) {
            // Prevents maliciously crafted packets from bypassing our custom book validation.
            compound.removeTag("resolved");
        }
    }
    
    @Inject(method = "processCustomPayload",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemWrittenBook;validBookTagContents(Lnet/minecraft/nbt/NBTTagCompound;)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onValidBookTagContents(CPacketCustomPayload packet, CallbackInfo callbackInfo,
                                        String channelName, PacketBuffer packetBuffer, ItemStack itemStack) {
        NBTTagCompound compound = itemStack.getTagCompound();
        if (compound != null) {
            // Prevents maliciously crafted packets from bypassing our custom book validation.
            compound.removeTag("resolved");
        }
    }
}