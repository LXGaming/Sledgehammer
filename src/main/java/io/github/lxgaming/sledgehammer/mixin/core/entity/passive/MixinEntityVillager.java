/*
 * Copyright 2017 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.core.entity.passive;

import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerMerchant;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.interfaces.entity.IMixinVillager;

import java.util.Optional;

/**
 * I'm gonna need you to take this IllegalStateException into the bathroom,
 * and I'm gonna need you to put it wayyy up inside your butthole Sponge,
 * Put it wayyy up inside there as far it can fit.
 *
 * @see <a href="https://github.com/SpongePowered/SpongeCommon/commit/87a01798b61f252b01634780de2223853c697c64">Commit 87a0179</a>
 * @see <a href="https://github.com/SpongePowered/SpongeForge/issues/1467">Issue 1467</a>
 */
@Mixin(value = EntityVillager.class, priority = 1001)
public abstract class MixinEntityVillager implements IMixinVillager {
    
    @Shadow
    public abstract EntityPlayer getCustomer();
    
    @Inject(method = "populateBuyingList", at = @At("HEAD"), cancellable = true)
    private void onPopulateBuyingList(CallbackInfo callbackInfo) {
        if (getProfession() != null && !getProfession().getCareers().isEmpty()) {
            return;
        }
        
        callbackInfo.cancel();
        ((Entity) this).remove();
        
        getPlayer().ifPresent(player -> {
            if (((EntityPlayerMP) player).openContainer instanceof ContainerMerchant) {
                ((EntityPlayerMP) player).closeContainer();
                player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.DARK_GRAY, "Entity removed to prevent server crash."));
            }
        });
    }
    
    private Optional<Player> getPlayer() {
        if (getCustomer() == null || !Sponge.isServerAvailable()) {
            return Optional.empty();
        }
        
        return Sponge.getServer().getPlayer(getCustomer().getUniqueID());
    }
}