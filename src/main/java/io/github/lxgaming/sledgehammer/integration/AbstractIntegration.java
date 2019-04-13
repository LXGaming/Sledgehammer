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

package io.github.lxgaming.sledgehammer.integration;

import com.google.common.collect.Sets;
import io.github.lxgaming.sledgehammer.SledgehammerPlatform;

import java.util.Set;

public abstract class AbstractIntegration {
    
    private final Set<String> dependencies = Sets.newLinkedHashSet();
    private SledgehammerPlatform.State state = SledgehammerPlatform.State.INITIALIZATION;
    
    public abstract void execute() throws Exception;
    
    protected final void addDependency(String dependency) {
        getDependencies().add(dependency);
    }
    
    public final Set<String> getDependencies() {
        return dependencies;
    }
    
    public final SledgehammerPlatform.State getState() {
        return state;
    }
    
    protected final void setState(SledgehammerPlatform.State state) {
        this.state = state;
    }
}