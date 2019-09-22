/*
 * Copyright 2019 Alex Thomson
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

package io.github.lxgaming.sledgehammer.mixin.notreepunching;

import com.alcatrazescapee.notreepunching.ModEventHandler;
import com.alcatrazescapee.notreepunching.common.capability.IPlayerItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ModEventHandler.class, priority = 1337, remap = false)
public abstract class ModEventHandlerMixin {
    
    /**
     * no-tree-punching doesn't check if the ItemStack contained in the Capability is null.
     */
    @Redirect(method = "harvestBlock",
            at = @At(value = "INVOKE",
                    target = "Lcom/alcatrazescapee/notreepunching/common/capability/IPlayerItem;getStack()Lnet/minecraft/item/ItemStack;"
            )
    )
    private static ItemStack onGetStack(IPlayerItem playerItem) {
        ItemStack itemStack = playerItem.getStack();
        if (itemStack != null) {
            return itemStack;
        }
        
        return ItemStack.EMPTY;
    }
}