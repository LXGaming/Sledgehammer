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

package io.github.lxgaming.sledgehammer.mixin.journeymap.client.task.multi;

import journeymap.client.task.multi.TaskController;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TaskController.class, remap = false)
public abstract class TaskControllerMixin {
    
    @Redirect(
            method = "performTasks",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/apache/logging/log4j/Logger;debug(Ljava/lang/String;)V"
            )
    )
    private void onPerformTasks(Logger logger, String message) {
        // no-op
    }
}