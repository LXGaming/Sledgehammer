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

package io.github.lxgaming.sledgehammer.mixin.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.server.entity.MMBossInfoServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntitySenses;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MMBossInfoServer.class, remap = false)
public abstract class MMBossInfoServerMixin {
    
    @Redirect(
            method = "func_186760_a",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/EntitySenses;canSee(Lnet/minecraft/entity/Entity;)Z",
                    remap = true
            )
    )
    private boolean onAddPlayer(EntitySenses entitySenses, Entity entity) {
        return false;
    }
}