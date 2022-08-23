/*
 * Copyright 2022 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.pneumaticcraft.common.network;

import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import me.desht.pneumaticcraft.common.advancements.AdvancementTriggers;
import me.desht.pneumaticcraft.common.network.PacketHackingEntityStart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PacketHackingEntityStart.class, remap = false)
public abstract class PacketHackingEntityStartMixin {
    
    @Inject(
            method = "handleServerSide(Lme/desht/pneumaticcraft/common/network/PacketHackingEntityStart;Lnet/minecraft/entity/player/EntityPlayer;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onHandleServerSide(PacketHackingEntityStart message, EntityPlayer player, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
        AdvancementTriggers.ENTITY_HACK.trigger((EntityPlayerMP) player);
        LocaleAdapter.sendMessage(player, Locale.MESSAGE_DISABLED, "Hacking");
    }
}