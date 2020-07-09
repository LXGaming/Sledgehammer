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

import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.command.SledgehammerCommand;
import io.github.lxgaming.sledgehammer.integration.Integration;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class CommandIntegration_Client extends Integration {
    
    @Override
    public boolean prepare() {
        addDependency("forge");
        state(SledgehammerPlatform.State.LOAD_COMPLETE);
        return FMLCommonHandler.instance().getSide() == Side.CLIENT;
    }
    
    @Override
    public void execute() throws Exception {
        ClientCommandHandler.instance.registerCommand(new SledgehammerCommand());
    }
}