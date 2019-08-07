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

package io.github.lxgaming.sledgehammer.mixin.primitivemobs.message;

import net.daveyx0.primitivemobs.message.MessagePrimitiveJumping;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.UUID;

@Mixin(value = MessagePrimitiveJumping.Handler.class, priority = 1337, remap = false)
public abstract class MessagePrimitiveJumpingMixin {
    
    @Inject(method = "onMessage",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/World;getPlayerEntityByUUID(Ljava/util/UUID;)Lnet/minecraft/entity/player/EntityPlayer;"
            ),
            remap = true,
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void onMessage(MessagePrimitiveJumping message, MessageContext ctx, CallbackInfoReturnable<IMessage> callbackInfoReturnable,
                           UUID id, EntityPlayer truePlayer) {
        if (truePlayer == null) {
            callbackInfoReturnable.setReturnValue(null);
        }
    }
}