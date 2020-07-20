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

package io.github.lxgaming.sledgehammer.mixin.fluxnetworks.network;

import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sonar.flux.api.IFluxItemGui;
import sonar.flux.common.item.ItemConfigurator;
import sonar.flux.network.PacketUpdateGuiItem;

@Mixin(value = PacketUpdateGuiItem.Handler.class, remap = false)
public abstract class PacketUpdateGuiItem_HandlerMixin {
    
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(
            method = "lambda$onMessage$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;setTagCompound(Lnet/minecraft/nbt/NBTTagCompound;)V",
                    remap = true
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private static void onMessage(MessageContext ctx, PacketUpdateGuiItem message, CallbackInfo callbackInfo,
                                  EntityPlayer player, ItemStack stack, ItemStack held) {
        callbackInfo.cancel();
        if (!(stack.getItem() instanceof IFluxItemGui)) {
            LocaleAdapter.disconnect((EntityPlayerMP) player, Locale.MESSAGE_FLUX_NETWORKS_EXPLOIT, Toolbox.getResourceLocation(stack.getItem()).map(ResourceLocation::toString).orElse("Unknown"));
            return;
        }
        
        NBTTagCompound configsTagCompound = stack.getSubCompound(ItemConfigurator.CONFIGS_TAG);
        if (configsTagCompound != null) {
            held.setTagInfo(ItemConfigurator.CONFIGS_TAG, configsTagCompound);
        }
        
        NBTTagCompound disabledTagCompound = stack.getSubCompound(ItemConfigurator.DISABLED_TAG);
        if (disabledTagCompound != null) {
            held.setTagInfo(ItemConfigurator.DISABLED_TAG, disabledTagCompound);
        }
    }
}