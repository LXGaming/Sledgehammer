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

import com.google.common.collect.Lists;
import io.github.lxgaming.sledgehammer.manager.CommandManager;
import io.github.lxgaming.sledgehammer.util.Reference;
import io.github.lxgaming.sledgehammer.util.Text;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public class HelpCommand extends AbstractCommand {
    
    public HelpCommand() {
        addAlias("help");
        addAlias("?");
    }
    
    @Override
    public void execute(ICommandSender commandSender, List<String> arguments) {
        Optional<AbstractCommand> abstractCommand = CommandManager.getCommand(Lists.newArrayList(Reference.ID));
        if (!abstractCommand.isPresent()) {
            return;
        }
        
        commandSender.sendMessage(Toolbox.getTextPrefix());
        for (AbstractCommand command : abstractCommand.get().getChildren()) {
            if (command == this || !command.checkPermission(commandSender)) {
                continue;
            }
            
            ITextComponent textComponent = Text.of("");
            textComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + Reference.ID + " " + command.getName()));
            textComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, buildDescription(command)));
            
            textComponent.appendSibling(Text.of(
                    TextFormatting.BLUE, "> ",
                    TextFormatting.GREEN, "/" + Reference.ID + " " + command.getName()
            ));
            
            if (StringUtils.isNotBlank(command.getUsage())) {
                textComponent.appendSibling(Text.of(TextFormatting.GREEN, " " + command.getUsage()));
            }
            
            commandSender.sendMessage(textComponent);
        }
    }
    
    private ITextComponent buildDescription(AbstractCommand command) {
        ITextComponent textComponent = Text.of(
                TextFormatting.AQUA, "Command: ", TextFormatting.DARK_GREEN, StringUtils.capitalize(command.getName()), Text.NEW_LINE,
                TextFormatting.AQUA, "Description: ", TextFormatting.DARK_GREEN, StringUtils.defaultIfBlank(command.getDescription(), "No description provided"), Text.NEW_LINE,
                TextFormatting.AQUA, "Usage: ", TextFormatting.DARK_GREEN, "/" + Reference.ID + " " + command.getName()
        );
        
        if (StringUtils.isNotBlank(command.getUsage())) {
            textComponent.appendSibling(Text.of(TextFormatting.DARK_GREEN, " " + command.getUsage()));
        }
        
        textComponent.appendSibling(Text.of(
                Text.NEW_LINE,
                TextFormatting.AQUA, "Permission: ", TextFormatting.DARK_GREEN, StringUtils.defaultIfBlank(command.getPermission(), "None"),
                Text.NEW_LINE,
                Text.NEW_LINE,
                TextFormatting.GRAY, "Click to auto-complete."
        ));
        
        return textComponent;
    }
}