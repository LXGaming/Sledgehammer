/*
 * Copyright 2020 Alex Thomson
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

package io.github.lxgaming.sledgehammer.integration;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.command.Command;
import io.github.lxgaming.sledgehammer.manager.CommandManager;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class CommandIntegration extends Integration {
    
    @Override
    public boolean prepare() {
        state(SledgehammerPlatform.State.SERVER_STARTING);
        return true;
    }
    
    @Override
    public void execute() throws Exception {
        MinecraftServer server = SledgehammerPlatform.getInstance().getServer();
        if (server == null) {
            Sledgehammer.getInstance().getLogger().warn("Server is unavailable");
            return;
        }
        
        CommandDispatcher<CommandSource> dispatcher = server.getCommandManager().getDispatcher();
        if (dispatcher.getRoot() == null) {
            Sledgehammer.getInstance().getLogger().warn("RootCommandNode is unavailable");
            return;
        }
        
        for (Command command : CommandManager.COMMANDS) {
            List<CommandNode<CommandSource>> commandNodes = register(command);
            for (CommandNode<CommandSource> commandNode : commandNodes) {
                dispatcher.getRoot().addChild(commandNode);
            }
        }
    }
    
    private List<CommandNode<CommandSource>> register(Command command) {
        List<CommandNode<CommandSource>> commandNodes = Lists.newArrayList();
        for (String alias : command.getAliases()) {
            LiteralArgumentBuilder<CommandSource> argumentBuilder = Command.literal(alias);
            command.register(argumentBuilder);
            commandNodes.add(argumentBuilder.build());
        }
        
        for (Command childCommand : command.getChildren()) {
            List<CommandNode<CommandSource>> childCommandNodes = register(childCommand);
            addChildren(commandNodes, childCommandNodes);
        }
        
        return commandNodes;
    }
    
    private void addChildren(List<CommandNode<CommandSource>> parentCommandNodes, List<CommandNode<CommandSource>> childCommandNodes) {
        if (parentCommandNodes.isEmpty() || childCommandNodes.isEmpty()) {
            return;
        }
        
        for (CommandNode<CommandSource> parentCommandNode : parentCommandNodes) {
            for (CommandNode<CommandSource> childCommandNode : childCommandNodes) {
                parentCommandNode.addChild(childCommandNode);
            }
        }
    }
}