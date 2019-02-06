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

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;

import java.util.List;

public class SledgehammerCommand extends AbstractCommand {
    
    public SledgehammerCommand() {
        addAlias("sledgehammer");
        addAlias("sh");
        addChild(BookCommand.class);
        addChild(DebugCommand.class);
        addChild(HelpCommand.class);
        addChild(InfoCommand.class);
    }
    
    @Override
    public CommandResult execute(CommandSource commandSource, List<String> arguments) {
        getHelp(commandSource).ifPresent(commandSource::sendMessage);
        return CommandResult.success();
    }
}