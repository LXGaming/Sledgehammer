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

package io.github.lxgaming.sledgehammer.integration.sledgehammer;

import io.github.lxgaming.sledgehammer.Sledgehammer;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.command.SledgehammerCommand;
import io.github.lxgaming.sledgehammer.integration.Integration;
import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;

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
        
        ((CommandHandler) server.getCommandManager()).registerCommand(new SledgehammerCommand());
    }
}