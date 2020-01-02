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

import com.google.common.collect.Sets;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.command.Command;
import io.github.lxgaming.sledgehammer.command.DebugCommand;
import io.github.lxgaming.sledgehammer.command.HelpCommand;
import io.github.lxgaming.sledgehammer.command.InformationCommand;
import io.github.lxgaming.sledgehammer.command.ReloadCommand;
import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.StringUtils;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import net.minecraft.command.ICommandSender;

import java.util.List;
import java.util.Set;

public final class CommandManager {
    
    public static final Set<Command> COMMANDS = Sets.newLinkedHashSet();
    private static final Set<Class<? extends Command>> COMMAND_CLASSES = Sets.newHashSet();
    
    public static void prepare() {
        registerCommand(DebugCommand.class);
        registerCommand(HelpCommand.class);
        registerCommand(InformationCommand.class);
        registerCommand(ReloadCommand.class);
    }
    
    public static boolean execute(ICommandSender commandSender, List<String> arguments) {
        String content = String.join(" ", arguments);
        if (arguments.isEmpty() || StringUtils.isBlank(content)) {
            LocaleAdapter.sendFeedback(commandSender, Locale.COMMAND_BASE, Sledgehammer.ID);
            return false;
        }
        
        Command command = getCommand(arguments);
        if (command == null) {
            LocaleAdapter.sendFeedback(commandSender, Locale.COMMAND_NOT_FOUND);
            return false;
        }
        
        if (StringUtils.isNotBlank(command.getPermission()) && !commandSender.canUseCommand(4, command.getPermission())) {
            LocaleAdapter.sendFeedback(commandSender, Locale.COMMAND_NO_PERMISSION);
            return false;
        }
        
        Sledgehammer.getInstance().debug("Processing {} for {}", content, commandSender.getName());
        
        try {
            command.execute(commandSender, arguments);
            return true;
        } catch (Exception ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while executing {}", Toolbox.getClassSimpleName(command.getClass()), ex);
            LocaleAdapter.sendFeedback(commandSender, Locale.COMMAND_EXCEPTION);
            return false;
        }
    }
    
    public static boolean registerAlias(Command command, String alias) {
        if (StringUtils.containsIgnoreCase(command.getAliases(), alias)) {
            Sledgehammer.getInstance().getLogger().warn("{} is already registered for {}", alias, Toolbox.getClassSimpleName(command.getClass()));
            return false;
        }
        
        command.getAliases().add(alias);
        Sledgehammer.getInstance().getLogger().debug("{} registered for {}", alias, Toolbox.getClassSimpleName(command.getClass()));
        return true;
    }
    
    public static boolean registerCommand(Class<? extends Command> commandClass) {
        Command command = registerCommand(COMMANDS, commandClass);
        if (command != null) {
            Sledgehammer.getInstance().getLogger().debug("{} registered", Toolbox.getClassSimpleName(commandClass));
            return true;
        }
        
        return false;
    }
    
    public static boolean registerCommand(Command parentCommand, Class<? extends Command> commandClass) {
        if (parentCommand.getClass() == commandClass) {
            Sledgehammer.getInstance().getLogger().warn("{} attempted to register itself", Toolbox.getClassSimpleName(parentCommand.getClass()));
            return false;
        }
        
        Command command = registerCommand(parentCommand.getChildren(), commandClass);
        if (command != null) {
            Sledgehammer.getInstance().getLogger().debug("{} registered for {}", Toolbox.getClassSimpleName(commandClass), Toolbox.getClassSimpleName(parentCommand.getClass()));
            return true;
        }
        
        return false;
    }
    
    private static Command registerCommand(Set<Command> commands, Class<? extends Command> commandClass) {
        if (COMMAND_CLASSES.contains(commandClass)) {
            Sledgehammer.getInstance().getLogger().warn("{} is already registered", Toolbox.getClassSimpleName(commandClass));
            return null;
        }
        
        COMMAND_CLASSES.add(commandClass);
        Command command = Toolbox.newInstance(commandClass);
        if (command == null) {
            Sledgehammer.getInstance().getLogger().error("{} failed to initialize", Toolbox.getClassSimpleName(commandClass));
            return null;
        }
        
        if (!command.prepare()) {
            Sledgehammer.getInstance().getLogger().warn("{} failed to prepare", Toolbox.getClassSimpleName(commandClass));
            return null;
        }
        
        if (commands.add(command)) {
            return command;
        }
        
        return null;
    }
    
    public static Command getCommand(Class<? extends Command> commandClass) {
        return getCommand(null, commandClass);
    }
    
    public static Command getCommand(Command parentCommand, Class<? extends Command> commandClass) {
        Set<Command> commands = Sets.newLinkedHashSet();
        if (parentCommand != null) {
            commands.addAll(parentCommand.getChildren());
        } else {
            commands.addAll(COMMANDS);
        }
        
        for (Command command : commands) {
            if (command.getClass() == commandClass) {
                return command;
            }
            
            Command childCommand = getCommand(command, commandClass);
            if (childCommand != null) {
                return childCommand;
            }
        }
        
        return null;
    }
    
    public static Command getCommand(List<String> arguments) {
        return getCommand(null, arguments);
    }
    
    private static Command getCommand(Command parentCommand, List<String> arguments) {
        if (arguments.isEmpty()) {
            return parentCommand;
        }
        
        Set<Command> commands = Sets.newLinkedHashSet();
        if (parentCommand != null) {
            commands.addAll(parentCommand.getChildren());
        } else {
            commands.addAll(COMMANDS);
        }
        
        for (Command command : commands) {
            if (StringUtils.containsIgnoreCase(command.getAliases(), arguments.get(0))) {
                arguments.remove(0);
                return getCommand(command, arguments);
            }
        }
        
        return parentCommand;
    }
}