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

package io.github.lxgaming.sledgehammer.mixin.careerbees.effects;

import com.rwtema.careerbees.effects.EffectAcceleration;
import com.rwtema.careerbees.effects.settings.IEffectSettingsHolder;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.IEffectData;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EffectAcceleration.class, remap = false)
public abstract class EffectAccelerationMixin {
    
    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/fml/common/eventhandler/EventBus;register(Ljava/lang/Object;)V"
            )
    )
    private void onRegister(EventBus eventBus, Object target) {
        // no-op
    }
    
    @Inject(
            method = "canHandleBlock",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onCanHandleBlock(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(false);
    }
    
    @Inject(
            method = "canHandleBlock",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onHandleBlock(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(false);
    }
    
    @Inject(
            method = "doEffectBase",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onDoEffectBase(IBeeGenome genome, IEffectData storedData, IBeeHousing housing, IEffectSettingsHolder settings, CallbackInfoReturnable<IEffectData> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(storedData);
    }
    
    @Inject(
            method = "worldTick",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onWorldTick(CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}