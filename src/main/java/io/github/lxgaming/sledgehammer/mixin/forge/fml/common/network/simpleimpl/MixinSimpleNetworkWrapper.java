/*
 * Copyright 2018 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.forge.fml.common.network.simpleimpl;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.spongepowered.api.Sponge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SimpleNetworkWrapper.class, priority = 1337, remap = false)
public abstract class MixinSimpleNetworkWrapper {
    
    @Inject(method = "sendToAll", at = @At(value = "HEAD"), cancellable = true)
    private void sendToAll(IMessage message, CallbackInfo callbackInfo) {
        if (Sponge.getPluginManager().isLoaded("lootbags") && message.getClass().getName().startsWith("mal.lootbags.network.message.")) {
            callbackInfo.cancel();
        }
    }
}