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

package io.github.lxgaming.sledgehammer.command;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.manager.LocaleManager;
import io.github.lxgaming.sledgehammer.util.Locale;
import io.github.lxgaming.sledgehammer.util.text.adapter.LocaleAdapter;
import net.minecraft.command.ICommandSender;

import java.util.List;

public class ReloadCommand extends Command {
    
    @Override
    public boolean prepare() {
        addAlias("Reload");
        description("Reloads locale system");
        permission("sledgehammer.reload.base");
        return true;
    }
    
    @Override
    public void execute(ICommandSender commandSender, List<String> arguments) throws Exception {
        if (Sledgehammer.getInstance().reload()) {
            LocaleManager.prepare();
            LocaleAdapter.sendFeedback(commandSender, Locale.COMMAND_RELOAD_SUCCESS);
            return;
        }
        
        LocaleAdapter.sendFeedback(commandSender, Locale.COMMAND_RELOAD_FAILURE);
    }
}