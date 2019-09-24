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
import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = ModEventHandler.class, priority = 1337, remap = false)
public abstract class ModEventHandlerMixin {
    
    @Redirect(method = "harvestBlock",
            at = @At(value = "INVOKE",
                    target = "Lcom/alcatrazescapee/notreepunching/util/HarvestBlockHandler;addExtraDrops(Ljava/util/List;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)V"
            )
    )
    private static void onAddExtraDrops(List<ItemStack> drops, IBlockState state, EntityPlayer player, ItemStack stack) {
        if (stack != null) {
            HarvestBlockHandler.addExtraDrops(drops, state, player, stack);
        } else {
            HarvestBlockHandler.addExtraDrops(drops, state, player, player.getHeldItemMainhand());
        }
    }
    
    @Redirect(method = "harvestBlock",
            at = @At(value = "INVOKE",
                    target = "Lcom/alcatrazescapee/notreepunching/util/HarvestBlockHandler;isInvalidTool(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/block/state/IBlockState;)Z"
            )
    )
    private static boolean onIsInvalidTool(ItemStack stack, EntityPlayer player, IBlockState state) {
        if (stack != null) {
            return HarvestBlockHandler.isInvalidTool(stack, player, state);
        } else {
            return HarvestBlockHandler.isInvalidTool(player.getHeldItemMainhand(), player, state);
        }
    }
}