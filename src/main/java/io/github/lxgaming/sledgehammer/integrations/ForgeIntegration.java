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
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.List;

public class ForgeIntegration extends AbstractIntegration {
    
    public ForgeIntegration() {
        addDependency("forge");
    }
    
    @Override
    public boolean prepareIntegration() {
        MinecraftForge.EVENT_BUS.register(this);
        return true;
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onCommand(CommandEvent event) {
        if (!(event.getSender() instanceof EntityPlayer)) {
            return;
        }
        
        EntityPlayer entityPlayer = (EntityPlayer) event.getSender();
        List<String> arguments = Toolbox.newArrayList(event.getCommand().getName());
        Collections.addAll(arguments, event.getParameters());
        if (arguments.isEmpty()) {
            return;
        }
        
        Sledgehammer.getInstance().debugMessage("{} ran the command: /{}", entityPlayer.getName(), String.join(" ", arguments));
        if (!entityPlayer.canUseCommand(4, String.join(".", arguments))) {
            event.setCanceled(true);
            TextComponentTranslation textComponentTranslation = new TextComponentTranslation("commands.generic.permission");
            textComponentTranslation.getStyle().setColor(TextFormatting.RED);
            entityPlayer.sendMessage(textComponentTranslation);
            Sledgehammer.getInstance().getLogger().info("{} was denied access to /{}", entityPlayer.getName(), String.join(" ", arguments));
        }
    }
}