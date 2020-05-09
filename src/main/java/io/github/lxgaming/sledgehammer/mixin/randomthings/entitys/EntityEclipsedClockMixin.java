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

package io.github.lxgaming.sledgehammer.mixin.randomthings.entitys;

import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import lumien.randomthings.entitys.EntityEclipsedClock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityEclipsedClock.class, remap = false)
public abstract class EntityEclipsedClockMixin {
    
    @Inject(
            method = "func_184230_a",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onProcessInitialInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(true);
        LocaleAdapter.sendMessage(player, Locale.MESSAGE_DISABLED, "Eclipsed Clock");
    }
}