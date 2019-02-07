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
import io.github.lxgaming.sledgehammer.util.Toolbox;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;

public class HelpCommand extends AbstractCommand {
    
    public HelpCommand() {
        addAlias("help");
        addAlias("?");
    }
    
    @Override
    public CommandResult execute(CommandSource commandSource, List<String> arguments) {
        Optional<AbstractCommand> abstractCommand = CommandManager.getCommand(Lists.newArrayList(Reference.ID));
        if (!abstractCommand.isPresent()) {
            return CommandResult.success();
        }
        
        commandSource.sendMessage(Text.of(Toolbox.getTextPrefix()));
        for (AbstractCommand command : abstractCommand.get().getChildren()) {
            if (command == this || !command.testPermission(commandSource)) {
                continue;
            }
            
            Text.Builder textBuilder = Text.builder();
            textBuilder.onClick(TextActions.suggestCommand("/" + Reference.ID + " " + command.getPrimaryAlias().orElse("unknown")));
            textBuilder.onHover(TextActions.showText(buildDescription(command)));
            textBuilder.append(Text.of(TextColors.BLUE, "> "));
            textBuilder.append(Text.of(TextColors.GREEN, "/", Reference.ID, " ", command.getPrimaryAlias().orElse("unknown")));
            if (StringUtils.isNotBlank(command.getUsage())) {
                textBuilder.append(Text.of(" ", TextColors.GREEN, command.getUsage()));
            }
            
            commandSource.sendMessage(textBuilder.build());
        }
        
        return CommandResult.success();
    }
    
    private Text buildDescription(AbstractCommand command) {
        Text.Builder textBuilder = Text.builder();
        textBuilder.append(Text.of(TextColors.AQUA, "Command: ", TextColors.DARK_GREEN, StringUtils.capitalize(command.getPrimaryAlias().orElse("unknown"))));
        textBuilder.append(Text.NEW_LINE);
        textBuilder.append(Text.of(TextColors.AQUA, "Description: ", TextColors.DARK_GREEN, StringUtils.defaultIfBlank(command.getDescription(), "No description provided")));
        textBuilder.append(Text.NEW_LINE);
        textBuilder.append(Text.of(TextColors.AQUA, "Usage: ", TextColors.DARK_GREEN, "/", Reference.ID, " ", command.getPrimaryAlias().orElse("unknown")));
        if (StringUtils.isNotBlank(command.getUsage())) {
            textBuilder.append(Text.of(" ", TextColors.DARK_GREEN, command.getUsage()));
        }
        
        textBuilder.append(Text.NEW_LINE);
        textBuilder.append(Text.of(TextColors.AQUA, "Permission: ", TextColors.DARK_GREEN, StringUtils.defaultIfBlank(command.getPermission(), "None")));
        textBuilder.append(Text.NEW_LINE);
        textBuilder.append(Text.NEW_LINE);
        textBuilder.append(Text.of(TextColors.GRAY, "Click to auto-complete."));
        return textBuilder.build();
    }
}