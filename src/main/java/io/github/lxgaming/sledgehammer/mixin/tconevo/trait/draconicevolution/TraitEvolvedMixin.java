/*
 * Copyright 2023 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.tconevo.trait.draconicevolution;

import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import xyz.phanta.tconevo.trait.draconicevolution.ModifierDraconic;
import xyz.phanta.tconevo.trait.draconicevolution.TraitEvolved;

@Mixin(value = TraitEvolved.class, remap = false)
public abstract class TraitEvolvedMixin {
    
    @Redirect(
            method = "applyEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lxyz/phanta/tconevo/trait/draconicevolution/ModifierDraconic;apply(Lnet/minecraft/nbt/NBTTagCompound;)V"
            )
    )
    private void onApply(ModifierDraconic mod, NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        if (!TinkerUtil.hasTrait(rootCompound, mod.identifier)) {
            mod.apply(rootCompound);
        }
    }
}