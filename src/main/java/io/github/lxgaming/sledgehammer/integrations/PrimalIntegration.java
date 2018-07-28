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

package io.github.lxgaming.sledgehammer.integrations;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nmd.primal.core.api.PrimalAPI;
import nmd.primal.core.api.events.FlakeEvent;
import nmd.primal.core.common.helper.PlayerHelper;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.CauseStackManager;

public class PrimalIntegration extends AbstractIntegration {
    
    public PrimalIntegration() {
        addDependency("forge");
        addDependency("primal");
    }
    
    @Override
    public boolean prepareIntegration() {
        MinecraftForge.EVENT_BUS.register(this);
        return true;
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFlake(FlakeEvent event) {
        event.setCanceled(true);
        EntityPlayerMP entityPlayer = Sponge.getCauseStackManager().getCurrentCause().first(EntityPlayerMP.class).orElse(null);
        if (entityPlayer == null) {
            return;
        }
        
        for (ItemStack output : event.getRecipe().getOutput()) {
            if (!PrimalAPI.randomCheck(event.getRecipe().getFailure_chance())) {
                continue;
            }
            
            // ItemStack needs to be copied otherwise we are decreasing the stack size for the FlakeRecipe which breaks future interactions
            ItemStack itemStack = output.copy();
            
            // Added Math.max to ensure an IllegalArgumentException doesn't happen
            itemStack.setCount(PrimalAPI.getRandom().nextInt(1, Math.max(2, itemStack.getCount() + 1)));
            
            // Debugging
            Sledgehammer.getInstance().debugMessage("{} got {}x {}", entityPlayer.getName(), itemStack.getCount(), itemStack.getItem().getRegistryName().toString());
            PlayerHelper.spawnItemInAir(event.getWorld(), event.getPos(), itemStack);
        }
        
        entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
        PlayerHelper.applyBlockStat(entityPlayer, event.getWorld().getBlockState(event.getPos()).getBlock(), 0.002F);
    }
    
    @SubscribeEvent(receiveCanceled = true)
    public void onPlayerInteractLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
            frame.pushCause(event.getEntityPlayer());
        }
    }
}