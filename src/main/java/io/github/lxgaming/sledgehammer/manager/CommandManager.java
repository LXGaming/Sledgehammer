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

package io.github.lxgaming.sledgehammer.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.SledgehammerPlugin;
import io.github.lxgaming.sledgehammer.command.AbstractCommand;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class CommandManager {
    
    private static final Set<AbstractCommand> COMMANDS = Sets.newLinkedHashSet();
    private static final Set<Class<? extends AbstractCommand>> COMMAND_CLASSES = Sets.newLinkedHashSet();
    
    public static CommandResult process(AbstractCommand abstractCommand, CommandSource commandSource, String message) {
        List<String> arguments = getArguments(message).map(Lists::newArrayList).orElse(null);
        if (arguments == null) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Failed to collect arguments"));
            return CommandResult.empty();
        }
        
        AbstractCommand command = getCommand(abstractCommand, arguments).orElse(null);
        if (command == null) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Unknown command"));
            return CommandResult.empty();
        }
        
        if (!command.testPermission(commandSource)) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "You do not have permission to execute this command"));
            return CommandResult.empty();
        }
        
        Sledgehammer.getInstance().debugMessage("Processing {} for {}", command.getPrimaryAlias().orElse("Unknown"), commandSource.getName());
        
        try {
            return command.execute(commandSource, arguments);
        } catch (Exception ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while executing {}", command.getClass().getSimpleName(), ex);
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "An error has occurred. Details are available in console."));
            return CommandResult.empty();
        }
    }
    
    public static boolean registerCommand(Class<? extends AbstractCommand> commandClass) {
        if (getCommandClasses().contains(commandClass)) {
            Sledgehammer.getInstance().getLogger().warn("{} is already registered", commandClass.getSimpleName());
            return false;
        }
        
        getCommandClasses().add(commandClass);
        AbstractCommand command = Toolbox.newInstance(commandClass).orElse(null);
        if (command == null) {
            Sledgehammer.getInstance().getLogger().error("{} failed to initialize", commandClass.getSimpleName());
            return false;
        }
        
        getCommands().add(command);
        Sponge.getCommandManager().register(SledgehammerPlugin.getInstance().getPluginContainer(), command, command.getAliases().toArray(new String[0]));
        Sledgehammer.getInstance().debugMessage("{} registered", commandClass.getSimpleName());
        return true;
    }
    
    public static boolean registerAlias(AbstractCommand command, String alias) {
        if (Toolbox.containsIgnoreCase(command.getAliases(), alias)) {
            Sledgehammer.getInstance().getLogger().warn("{} is already registered for {}", alias, command.getClass().getSimpleName());
            return false;
        }
        
        command.getAliases().add(alias);
        Sledgehammer.getInstance().debugMessage("{} registered for {}", alias, command.getClass().getSimpleName());
        return true;
    }
    
    public static boolean registerCommand(AbstractCommand parentCommand, Class<? extends AbstractCommand> commandClass) {
        if (parentCommand.getClass() == commandClass) {
            Sledgehammer.getInstance().getLogger().warn("{} attempted to register itself", parentCommand.getClass().getSimpleName());
            return false;
        }
        
        if (getCommandClasses().contains(commandClass)) {
            Sledgehammer.getInstance().getLogger().warn("{} is already registered", commandClass.getSimpleName());
            return false;
        }
        
        getCommandClasses().add(commandClass);
        AbstractCommand command = Toolbox.newInstance(commandClass).orElse(null);
        if (command == null) {
            Sledgehammer.getInstance().getLogger().error("{} failed to initialize", commandClass.getSimpleName());
            return false;
        }
        
        parentCommand.getChildren().add(command);
        Sledgehammer.getInstance().debugMessage("{} registered for {}", commandClass.getSimpleName(), parentCommand.getClass().getSimpleName());
        return true;
    }
    
    public static Optional<AbstractCommand> getCommand(List<String> arguments) {
        return getCommand(null, arguments);
    }
    
    private static Optional<AbstractCommand> getCommand(AbstractCommand parentCommand, List<String> arguments) {
        Set<AbstractCommand> commands = Sets.newLinkedHashSet();
        if (parentCommand != null) {
            commands.addAll(parentCommand.getChildren());
        } else {
            commands.addAll(getCommands());
        }
        
        if (arguments.isEmpty() || commands.isEmpty()) {
            return Optional.ofNullable(parentCommand);
        }
        
        for (AbstractCommand command : commands) {
            if (Toolbox.containsIgnoreCase(command.getAliases(), arguments.get(0))) {
                arguments.remove(0);
                return getCommand(command, arguments);
            }
        }
        
        return Optional.ofNullable(parentCommand);
    }
    
    private static Optional<String[]> getArguments(String message) {
        return Optional.ofNullable(StringUtils.split(Toolbox.filter(message), " "));
    }
    
    public static Set<AbstractCommand> getCommands() {
        return COMMANDS;
    }
    
    private static Set<Class<? extends AbstractCommand>> getCommandClasses() {
        return COMMAND_CLASSES;
    }
}