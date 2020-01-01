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

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.configuration.Config;
import io.github.lxgaming.sledgehammer.configuration.category.GeneralCategory;
import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.StringUtils;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import net.minecraft.command.ICommandSender;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;

public class DebugCommand extends Command {
    
    @Override
    public boolean prepare() {
        addAlias("Debug");
        permission("sledgehammer.debug");
        return true;
    }
    
    @Override
    public void execute(ICommandSender commandSender, List<String> arguments) throws Exception {
        GeneralCategory generalCategory = Sledgehammer.getInstance().getConfig().map(Config::getGeneralCategory).orElse(null);
        if (generalCategory == null) {
            LocaleAdapter.sendFeedback(commandSender, Locale.CONFIGURATION_ERROR);
            return;
        }
        
        Boolean state;
        if (!arguments.isEmpty()) {
            String argument = arguments.remove(0);
            if (StringUtils.isNotBlank(argument)) {
                state = BooleanUtils.toBooleanObject(argument);
                if (state == null) {
                    LocaleAdapter.sendFeedback(commandSender, Locale.PARSE_BOOLEAN_ERROR, argument);
                }
            } else {
                state = null;
            }
        } else {
            state = null;
        }
        
        if (state != null) {
            generalCategory.setDebug(state);
        } else {
            generalCategory.setDebug(!generalCategory.isDebug());
        }
        
        if (generalCategory.isDebug()) {
            LocaleAdapter.sendFeedback(commandSender, Locale.COMMAND_DEBUG_ENABLE);
        } else {
            LocaleAdapter.sendFeedback(commandSender, Locale.COMMAND_DEBUG_DISABLE);
        }
    }
}