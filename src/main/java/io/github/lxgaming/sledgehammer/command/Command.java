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

import com.google.common.collect.Sets;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.github.lxgaming.sledgehammer.manager.CommandManager;
import net.minecraft.command.CommandSource;

import java.util.Set;

public abstract class Command {
    
    private final Set<String> aliases = Sets.newLinkedHashSet();
    private final Set<Command> children = Sets.newLinkedHashSet();
    
    public abstract boolean prepare();
    
    public abstract void register(LiteralArgumentBuilder<CommandSource> argumentBuilder);
    
    public static <T> RequiredArgumentBuilder<CommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
    
    public static LiteralArgumentBuilder<CommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
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
}