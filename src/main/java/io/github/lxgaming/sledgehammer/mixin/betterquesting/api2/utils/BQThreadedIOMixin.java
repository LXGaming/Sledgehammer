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

package io.github.lxgaming.sledgehammer.mixin.betterquesting.api2.utils;

import betterquesting.api2.utils.BQThreadedIO;
import betterquesting.core.BetterQuesting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Mixin(value = BQThreadedIO.class, remap = false)
public abstract class BQThreadedIOMixin {
    
    @Shadow
    private ExecutorService exService;
    
    /**
     * @author LX_Gaming
     * @reason Wait for enqueued tasks to complete
     */
    @Overwrite
    public void shutdown() {
        try {
            BetterQuesting.logger.info("Shutting down BQThreadedIO...");
            exService.shutdown();
            if (!exService.awaitTermination(60L, TimeUnit.SECONDS)) {
                throw new InterruptedException();
            }
            
            BetterQuesting.logger.info("Successfully terminated threads, continuing with shutdown process...");
        } catch (Exception ex) {
            BetterQuesting.logger.error("Failed to terminate threads, continuing with shutdown process...");
        }
    }
}