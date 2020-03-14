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

package io.github.lxgaming.sledgehammer.integration.astralsorcery;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.util.ASDataSerializers;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;
import io.github.lxgaming.sledgehammer.integration.Integration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.DataSerializerEntry;

public class AstralSorceryIntegration extends Integration {
    
    @Override
    public boolean prepare() {
        addDependency("forge");
        addDependency("astralsorcery");
        state(SledgehammerPlatform.State.PRE_INITIALIZATION);
        return true;
    }
    
    @Override
    public void execute() throws Exception {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onRegisterDataSerializer(RegistryEvent.Register<DataSerializerEntry> event) {
        event.getRegistry().register(new DataSerializerEntry(ASDataSerializers.FLUID).setRegistryName(AstralSorcery.MODID, "serializer_fluid"));
        event.getRegistry().register(new DataSerializerEntry(ASDataSerializers.LONG).setRegistryName(AstralSorcery.MODID, "serializer_long"));
        event.getRegistry().register(new DataSerializerEntry(ASDataSerializers.VECTOR).setRegistryName(AstralSorcery.MODID, "serializer_vec3d"));
    }
}