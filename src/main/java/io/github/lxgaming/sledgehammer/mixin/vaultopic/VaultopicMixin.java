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

package io.github.lxgaming.sledgehammer.mixin.vaultopic;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wolforce.vaultopic.Vaultopic;
import wolforce.vaultopic.net.VTOpenVice;
import wolforce.vaultopic.net.VTRecipe;

@Mixin(value = Vaultopic.class, remap = false)
public abstract class VaultopicMixin {
    
    @Inject(
            method = "onOpenVice",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private static void onOpenVice(VTOpenVice message, MessageContext ctx, CallbackInfo callbackInfo) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        if (!player.server.isCallingFromMinecraftThread()) {
            player.server.addScheduledTask(() -> Vaultopic.onOpenVice(message, ctx));
            callbackInfo.cancel();
        }
    }
    
    @Inject(
            method = "onRecipe",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private static void onRecipe(VTRecipe message, MessageContext ctx, CallbackInfo callbackInfo) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        if (!player.server.isCallingFromMinecraftThread()) {
            player.server.addScheduledTask(() -> Vaultopic.onRecipe(message, ctx));
            callbackInfo.cancel();
        }
    }
}