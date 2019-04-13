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
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.command.AbstractCommand;
import io.github.lxgaming.sledgehammer.command.SledgehammerCommand;
import io.github.lxgaming.sledgehammer.util.Text;
import io.github.lxgaming.sledgehammer.util.Toolbox;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class CommandManager {
    
    private static final Set<AbstractCommand> COMMANDS = Sets.newLinkedHashSet();
    private static final Set<Class<? extends AbstractCommand>> COMMAND_CLASSES = Sets.newLinkedHashSet();
    
    public static void register() {
        registerCommand(SledgehammerCommand.class);
    }
    
    public static boolean process(AbstractCommand abstractCommand, ICommandSender commandSender, String[] args) {
        List<String> arguments = getArguments(args);
        AbstractCommand command = getCommand(abstractCommand, arguments).orElse(null);
        if (command == null) {
            commandSender.sendMessage(Text.of(Toolbox.getTextPrefix(), TextFormatting.RED, "Unknown command"));
            return false;
        }
        
        if (!command.checkPermission(commandSender)) {
            commandSender.sendMessage(Text.of(Toolbox.getTextPrefix(), TextFormatting.RED, new TextComponentTranslation("commands.generic.permission")));
            return false;
        }
        
        Sledgehammer.getInstance().debugMessage("Processing {} for {}", command.getName(), commandSender.getName());
        
        try {
            command.execute(commandSender, arguments);
            return true;
        } catch (Exception ex) {
            Sledgehammer.getInstance().getLogger().error("Encountered an error while executing {}", command.getClass().getSimpleName(), ex);
            commandSender.sendMessage(Text.of(Toolbox.getTextPrefix(), TextFormatting.RED, "An error has occurred. Details are available in console."));
            return false;
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
        ((CommandHandler) SledgehammerPlatform.getInstance().getServer().getCommandManager()).registerCommand(command);
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
    
    private static List<String> getArguments(String[] strings) {
        List<String> arguments = Lists.newArrayList();
        for (String string : strings) {
            String argument = Toolbox.filter(string);
            if (StringUtils.isNotBlank(argument)) {
                arguments.add(argument);
            }
        }
        
        return arguments;
    }
    
    public static Set<AbstractCommand> getCommands() {
        return COMMANDS;
    }
    
    private static Set<Class<? extends AbstractCommand>> getCommandClasses() {
        return COMMAND_CLASSES;
    }
}