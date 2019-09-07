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

package io.github.lxgaming.sledgehammer.mixin.storagenetwork.network;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.util.Broadcast;
import io.github.lxgaming.sledgehammer.util.Text;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import mrriegel.storagenetwork.gui.IStorageContainer;
import mrriegel.storagenetwork.network.InsertMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ChatType;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "mrriegel/storagenetwork/network/InsertMessage$1", priority = 1337, remap = false)
public abstract class InsertMessage$1Mixin {
    
    @Shadow(aliases = {"val$player"})
    @Final
    private EntityPlayerMP player;
    
    @Shadow(aliases = {"val$message"})
    @Final
    private InsertMessage message;
    
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "run()V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void onRun(CallbackInfo callbackInfo) {
        if (!(player.openContainer instanceof IStorageContainer)) {
            callbackInfo.cancel();
            return;
        }
        
        ItemStack packetItemStack = ((InsertMessageAccessor) message).accessor$getItemStack();
        if (!ItemStack.areItemStacksEqual(player.inventory.getItemStack(), packetItemStack)) {
            callbackInfo.cancel();
            sledgehammer$disconnect(player, packetItemStack);
        }
    }
    
    private void sledgehammer$disconnect(EntityPlayerMP player, ItemStack itemStack) {
        Sledgehammer.getInstance().getConfig().map(Config::getGeneralCategory).ifPresent(generalCategory -> {
            String message = generalCategory.getMessageCategory().getStorageNetworkExploit();
            if (StringUtils.isBlank(message)) {
                return;
            }
            
            Broadcast broadcast = Broadcast.builder()
                    .message(Toolbox.convertColor(message.replace("[ID]", Toolbox.getResourceLocation(itemStack.getItem()).map(ResourceLocation::toString).orElse("Unknown"))))
                    .type(ChatType.CHAT)
                    .build();
            
            player.connection.disconnect(Text.of(Toolbox.getTextPrefix(), broadcast.getMessage()));
        });
    }
}