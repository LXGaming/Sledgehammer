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

package io.github.lxgaming.sledgehammer.command;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.manager.CommandManager;
import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.StringUtils;
import io.github.lxgaming.sledgehammer.util.text.EmptyTextComponent;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import io.github.lxgaming.sledgehammer.util.text.adapter.TextAdapter;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.List;

public class HelpCommand extends Command {
    
    @Override
    public boolean prepare() {
        addAlias("Help");
        addAlias("?");
        return true;
    }
    
    @Override
    public void execute(ICommandSender commandSender, List<String> arguments) throws Exception {
        LocaleAdapter.sendFeedback(commandSender, Locale.GENERAL_PREFIX);
        for (Command command : CommandManager.COMMANDS) {
            if (command == this || (StringUtils.isNotBlank(command.getPermission()) && !commandSender.canUseCommand(4, command.getPermission()))) {
                continue;
            }
            
            EmptyTextComponent rootTextComponent = new EmptyTextComponent();
            rootTextComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + Sledgehammer.ID + " " + String.join(" ", command.getPath()).toLowerCase()));
            rootTextComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, buildDescription(command)));
            rootTextComponent.appendSibling(new TextComponentString("> ")
                    .setStyle(new Style().setColor(TextFormatting.BLUE))
            );
            rootTextComponent.appendSibling(new TextComponentString("/" + Sledgehammer.ID + " " + String.join(" ", command.getPath()).toLowerCase())
                    .setStyle(new Style().setColor(TextFormatting.GREEN))
            );
            
            if (StringUtils.isNotBlank(command.getUsage())) {
                rootTextComponent.appendSibling(new TextComponentString(" " + command.getUsage())
                        .setStyle(new Style().setColor(TextFormatting.GREEN))
                );
            }
            
            TextAdapter.sendFeedback(commandSender, rootTextComponent);
        }
    }
    
    private ITextComponent buildDescription(Command command) {
        EmptyTextComponent rootTextComponent = new EmptyTextComponent();
        rootTextComponent.appendSibling(new TextComponentString("Command: ")
                .setStyle(new Style().setColor(TextFormatting.AQUA))
        );
        rootTextComponent.appendSibling(new TextComponentString(command.getPrimaryAlias().orElse("unknown"))
                .setStyle(new Style().setColor(TextFormatting.DARK_GREEN))
        );
        
        rootTextComponent.appendSibling(new TextComponentString("\n"));
        rootTextComponent.appendSibling(new TextComponentString("Description: ")
                .setStyle(new Style().setColor(TextFormatting.AQUA))
        );
        rootTextComponent.appendSibling(new TextComponentString(StringUtils.defaultIfEmpty(command.getDescription(), "No description provided"))
                .setStyle(new Style().setColor(TextFormatting.DARK_GREEN))
        );
        
        rootTextComponent.appendSibling(new TextComponentString("\n"));
        rootTextComponent.appendSibling(new TextComponentString("Usage: ")
                .setStyle(new Style().setColor(TextFormatting.AQUA))
        );
        rootTextComponent.appendSibling(new TextComponentString("/" + Sledgehammer.ID + " " + String.join(" ", command.getPath()).toLowerCase())
                .setStyle(new Style().setColor(TextFormatting.DARK_GREEN))
        );
        
        if (StringUtils.isNotBlank(command.getUsage())) {
            rootTextComponent.appendSibling(new TextComponentString(" " + command.getUsage())
                    .setStyle(new Style().setColor(TextFormatting.DARK_GREEN))
            );
        }
        
        rootTextComponent.appendSibling(new TextComponentString("\n"));
        rootTextComponent.appendSibling(new TextComponentString("Permission: ")
                .setStyle(new Style().setColor(TextFormatting.AQUA))
        );
        rootTextComponent.appendSibling(new TextComponentString(StringUtils.defaultIfEmpty(command.getPermission(), "None"))
                .setStyle(new Style().setColor(TextFormatting.DARK_GREEN))
        );
        
        rootTextComponent.appendSibling(new TextComponentString("\n"));
        rootTextComponent.appendSibling(new TextComponentString("\n"));
        rootTextComponent.appendSibling(new TextComponentString("Click to auto-complete.")
                .setStyle(new Style().setColor(TextFormatting.GRAY))
        );
        
        return rootTextComponent;
    }
}