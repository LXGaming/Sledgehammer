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

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import net.minecraft.command.CommandSource;

public class SledgehammerCommand extends Command {
    
    @Override
    public boolean prepare() {
        addAlias("sledgehammer");
        addChild(DebugCommand.class);
        addChild(InformationCommand.class);
        addChild(ReloadCommand.class);
        return true;
    }
    
    @Override
    public void register(LiteralArgumentBuilder<CommandSource> argumentBuilder) {
        argumentBuilder
                .executes(context -> {
                    return execute(context.getSource());
                });
    }
    
    private int execute(CommandSource commandSource) {
        LocaleAdapter.sendSuccess(commandSource, Locale.COMMAND_BASE,
                Sledgehammer.ID
        );
        
        return 1;
    }
}