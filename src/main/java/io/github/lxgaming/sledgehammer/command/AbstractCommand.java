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
import io.github.lxgaming.sledgehammer.util.Reference;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractCommand implements CommandCallable {
    
    private final Set<String> aliases = Sets.newLinkedHashSet();
    private final Set<AbstractCommand> children = Sets.newLinkedHashSet();
    private String description;
    private String permission;
    private String usage;
    
    public abstract CommandResult execute(CommandSource commandSource, List<String> arguments);
    
    @Override
    public final CommandResult process(CommandSource commandSource, String arguments) throws CommandException {
        return CommandManager.process(this, commandSource, arguments);
    }
    
    @Override
    public final List<String> getSuggestions(CommandSource commandSource, String arguments, Location<World> targetPosition) throws CommandException {
        return Lists.newArrayList();
    }
    
    @Override
    public final boolean testPermission(CommandSource commandSource) {
        return StringUtils.isBlank(getPermission()) || commandSource.hasPermission(getPermission());
    }
    
    @Override
    public final Optional<Text> getShortDescription(CommandSource commandSource) {
        if (StringUtils.length(getDescription()) > 53) {
            return Optional.of(Text.of(StringUtils.substring(getDescription(), 0, 50), "..."));
        }
        
        return Optional.of(Text.of(StringUtils.defaultIfBlank(getDescription(), "No description provided")));
    }
    
    @Override
    public final Optional<Text> getHelp(CommandSource commandSource) {
        return Optional.of(Text.of(TextColors.BLUE, "Use ", TextColors.GREEN, "/", Reference.ID, " help ", TextColors.BLUE, "to view available commands."));
    }
    
    @Override
    public final Text getUsage(CommandSource commandSource) {
        return null;
    }
    
    protected final void addAlias(String alias) {
        CommandManager.registerAlias(this, alias);
    }
    
    protected final void addChild(Class<? extends AbstractCommand> commandClass) {
        CommandManager.registerCommand(this, commandClass);
    }
    
    public final Optional<String> getPrimaryAlias() {
        for (String alias : getAliases()) {
            if (StringUtils.isNotBlank(alias)) {
                return Optional.of(alias);
            }
        }
        
        return Optional.empty();
    }
    
    public final Set<String> getAliases() {
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
    
    public final String getUsage() {
        return usage;
    }
    
    protected final void setUsage(String usage) {
        this.usage = usage;
    }
}