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
import io.github.lxgaming.sledgehammer.command.SledgehammerCommand;
import io.github.lxgaming.sledgehammer.util.StringUtils;
import io.github.lxgaming.sledgehammer.util.Toolbox;

import java.util.Set;

public final class CommandManager {
    
    public static final Set<Command> COMMANDS = Sets.newLinkedHashSet();
    private static final Set<Class<? extends Command>> COMMAND_CLASSES = Sets.newHashSet();
    
    public static void prepare() {
        registerCommand(SledgehammerCommand.class);
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
}