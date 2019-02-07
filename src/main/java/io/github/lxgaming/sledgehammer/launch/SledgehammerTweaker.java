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

package io.github.lxgaming.sledgehammer.launch;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.List;

public class SledgehammerTweaker implements ITweaker {
    
    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
    }
    
    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        SledgehammerLaunch.configureEnvironment();
    }
    
    @Override
    public String getLaunchTarget() {
        return null;
    }
    
    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}