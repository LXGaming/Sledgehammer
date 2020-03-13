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

package io.github.lxgaming.sledgehammer.mixin.danknull.network;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import p455w0rd.danknull.init.ModConfig;
import p455w0rd.danknull.network.PacketConfigSync;

@Mixin(value = PacketConfigSync.Handler.class, remap = false)
public abstract class PacketConfigSyncHandlerMixin {
    
    /**
     * @author LX_Gaming
     * @reason Fix NullPointerException
     */
    @Overwrite
    private void handle(PacketConfigSync message, MessageContext ctx) {
        if (ctx.getClientHandler() == null || message.values == null) {
            return;
        }
        
        ModConfig.Options.creativeBlacklist = (String) message.values.getOrDefault(ModConfig.NAME_CREATIVE_BLACKLIST, "");
        ModConfig.Options.creativeWhitelist = (String) message.values.getOrDefault(ModConfig.NAME_CREATIVE_WHITELIST, "");
        ModConfig.Options.oreBlacklist = (String) message.values.getOrDefault(ModConfig.NAME_OREDICT_BLACKLIST, "");
        ModConfig.Options.oreWhitelist = (String) message.values.getOrDefault(ModConfig.NAME_OREDICT_WHITELIST, "");
        ModConfig.Options.disableOreDictMode = (Boolean) message.values.getOrDefault(ModConfig.NAME_DISABLE_OREDICT, false);
    }
}