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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.lxgaming.sledgehammer.manager.CommandManager;
import io.github.lxgaming.sledgehammer.util.StringUtils;
import net.minecraft.command.ICommandSender;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class Command {
    
    private final Set<String> aliases = Sets.newLinkedHashSet();
    private final Set<Command> children = Sets.newLinkedHashSet();
    private Command parentCommand;
    private String description;
    private String permission;
    private String usage;
    
    public abstract boolean prepare();
    
    public abstract void execute(ICommandSender commandSender, List<String> arguments) throws Exception;
    
    public final Optional<String> getPrimaryAlias() {
        for (String alias : getAliases()) {
            if (StringUtils.isNotBlank(alias)) {
                return Optional.of(alias);
            }
        }
        
        return Optional.empty();
    }
    
    public final List<String> getPath() {
        List<String> paths = Lists.newArrayList();
        if (parentCommand != null) {
            paths.addAll(parentCommand.getPath());
        }
        
        getPrimaryAlias().ifPresent(paths::add);
        return paths;
    }
    
    protected final void addAlias(String alias) {
        CommandManager.registerAlias(this, alias);
    }
    
    public final Set<String> getAliases() {
        return aliases;
    }
    
    protected final void addChild(Class<? extends Command> commandClass) {
        CommandManager.registerCommand(this, commandClass);
    }
    
    public final Set<Command> getChildren() {
        return children;
    }
    
    public final Command getParentCommand() {
        return parentCommand;
    }
    
    public final void parentCommand(Command parentCommand) {
        Preconditions.checkState(this.parentCommand == null, "ParentCommand is already set");
        this.parentCommand = parentCommand;
    }
    
    public final String getDescription() {
        return description;
    }
    
    protected final void description(String description) {
        Preconditions.checkState(this.description == null, "Description is already set");
        this.description = description;
    }
    
    public final String getPermission() {
        return permission;
    }
    
    protected final void permission(String permission) {
        Preconditions.checkState(this.permission == null, "Permission is already set");
        this.permission = permission;
    }
    
    public final String getUsage() {
        return usage;
    }
    
    protected final void usage(String usage) {
        Preconditions.checkState(this.usage == null, "Usage is already set");
        this.usage = usage;
    }
}