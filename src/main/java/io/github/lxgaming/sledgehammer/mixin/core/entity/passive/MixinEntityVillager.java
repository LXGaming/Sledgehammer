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

import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.interfaces.entity.IMixinVillager;

/**
 * I'm gonna need you to take this IllegalStateException into the bathroom,
 * and I'm gonna need you to put it wayyy up inside your butthole Sponge,
 * Put it wayyy up inside there as far it can fit.
 *
 * @see <a href="https://github.com/SpongePowered/SpongeCommon/commit/87a01798b61f252b01634780de2223853c697c64">Commit 87a0179</a>
 * @see <a href="https://github.com/SpongePowered/SpongeForge/issues/1467">Issue 1467</a>
 */
@Mixin(value = EntityVillager.class, priority = 1337)
public abstract class MixinEntityVillager extends EntityAgeable implements IMixinVillager {
    
    @Shadow
    public abstract EntityPlayer getCustomer();
    
    @Shadow
    private int careerId;
    
    public MixinEntityVillager(World worldIn, int careerId) {
        super(worldIn);
        this.careerId = careerId;
    }
    
    @Inject(method = "populateBuyingList", at = @At(value = "HEAD"), cancellable = true)
    private void onPopulateBuyingListHead(CallbackInfo callbackInfo) {
        if (getProfession() != null && !getProfession().getCareers().isEmpty()) {
            return;
        }
        
        Entity entity = (Entity) this;
        
        callbackInfo.cancel();
        entity.remove();
        Sledgehammer.getInstance().debugMessage("Entity {} at {} was removed by {}", entity.getType().getId(), entity.getLocation().toString(), getClass().getSimpleName());
    }
}