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

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.GeneralCategory;
import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import net.minecraft.command.CommandSource;

public class DebugCommand extends Command {
    
    private static final String STATE_ARGUMENT = "state";
    
    @Override
    public boolean prepare() {
        addAlias("Debug");
        return true;
    }
    
    @Override
    public void register(LiteralArgumentBuilder<CommandSource> argumentBuilder) {
        argumentBuilder
                .requires(commandSource -> commandSource.hasPermission(4))
                .executes(context -> {
                    return execute(context.getSource());
                })
                .then(argument(STATE_ARGUMENT, BoolArgumentType.bool()).executes(context -> {
                    return execute(context.getSource(), BoolArgumentType.getBool(context, STATE_ARGUMENT));
                }));
    }
    
    private int execute(CommandSource commandSource) {
        return execute(commandSource, null);
    }
    
    private int execute(CommandSource commandSource, Boolean state) {
        GeneralCategory generalCategory = Sledgehammer.getInstance().getConfig().map(Config::getGeneralCategory).orElse(null);
        if (generalCategory == null) {
            LocaleAdapter.sendSuccess(commandSource, Locale.CONFIGURATION_ERROR);
            return 0;
        }
        
        if (state != null) {
            generalCategory.setDebug(state);
        } else {
            generalCategory.setDebug(!generalCategory.isDebug());
        }
        
        if (generalCategory.isDebug()) {
            LocaleAdapter.sendSuccess(commandSource, Locale.COMMAND_DEBUG_ENABLE);
        } else {
            LocaleAdapter.sendSuccess(commandSource, Locale.COMMAND_DEBUG_DISABLE);
        }
        
        return 1;
    }
}