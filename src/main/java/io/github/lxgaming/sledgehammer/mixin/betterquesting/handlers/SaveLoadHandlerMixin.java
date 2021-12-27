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

package io.github.lxgaming.sledgehammer.mixin.betterquesting.handlers;

import betterquesting.core.BetterQuesting;
import betterquesting.handlers.SaveLoadHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SaveLoadHandler.class, remap = false)
public abstract class SaveLoadHandlerMixin {
    
    @Inject(
            method = "loadDatabases",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onLoadDatabases(CallbackInfo callbackInfo) {
        BetterQuesting.logger.info("Loading Databases");
    }
    
    @Inject(
            method = "saveDatabases",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onSaveDatabases(CallbackInfo callbackInfo) {
        BetterQuesting.logger.info("Saving Databases");
    }
    
    @Inject(
            method = "unloadDatabases",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onUnloadDatabases(CallbackInfo callbackInfo) {
        BetterQuesting.logger.info("Unloading Databases");
    }
}