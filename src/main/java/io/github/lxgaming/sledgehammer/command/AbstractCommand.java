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
import com.google.common.collect.Sets;
import io.github.lxgaming.sledgehammer.manager.CommandManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractCommand implements ICommand {
    
    private final List<String> aliases = Lists.newArrayList();
    private final Set<AbstractCommand> children = Sets.newLinkedHashSet();
    private String description;
    private String permission;
    private String usage;
    
    public abstract void execute(ICommandSender commandSender, List<String> arguments);
    
    @Override
    public final void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        CommandManager.process(this, sender, args);
    }
    
    @Override
    public final boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return checkPermission(sender);
    }
    
    public final boolean checkPermission(ICommandSender sender) {
        return StringUtils.isBlank(getPermission()) || sender.canUseCommand(4, getPermission());
    }
    
    @Override
    public final List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return Lists.newArrayList();
    }
    
    @Override
    public final boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
    
    protected final void addAlias(String alias) {
        CommandManager.registerAlias(this, alias);
    }
    
    protected final void addChild(Class<? extends AbstractCommand> commandClass) {
        CommandManager.registerCommand(this, commandClass);
    }
    
    @Override
    public final String getName() {
        return getAliases().stream().filter(StringUtils::isNotBlank).findFirst().orElseThrow(IllegalStateException::new);
    }
    
    @Override
    public final List<String> getAliases() {
        return aliases;
    }
    
    public final Set<AbstractCommand> getChildren() {
        return children;
    }
    
    public final String getDescription() {
        return description;
    }
    
    protected final void setDescription(String description) {
        this.description = description;
    }
    
    public final String getPermission() {
        return permission;
    }
    
    protected final void setPermission(String permission) {
        this.permission = permission;
    }
    
    @Override
    public final String getUsage(ICommandSender sender) {
        return getUsage();
    }
    
    public final String getUsage() {
        return usage;
    }
    
    protected final void setUsage(String usage) {
        this.usage = usage;
    }
    
    @Override
    public int compareTo(ICommand o) {
        return Objects.compare(getName(), o.getName(), String::compareTo);
    }
}