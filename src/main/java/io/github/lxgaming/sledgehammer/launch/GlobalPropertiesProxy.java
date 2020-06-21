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

package io.github.lxgaming.sledgehammer.launch;

import org.spongepowered.asm.launch.GlobalProperties;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class GlobalPropertiesProxy {
    
    private static final MethodHandle ofMethodHandle;
    private static final MethodHandle getMethodHandle;
    private static final MethodHandle putMethodHandle;
    
    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        
        Class<?> keyClass;
        MethodHandle ofMethodHandleTemporary;
        MethodHandle getMethodHandleTemporary;
        MethodHandle putMethodHandleTemporary;
        
        try {
            // org.spongepowered.asm.launch.GlobalProperties.Keys.of(java.lang.String)
            Method ofMethod = GlobalProperties.Keys.class.getMethod("of", String.class);
            
            // Mixin 0.8
            ofMethodHandleTemporary = lookup.unreflect(ofMethod);
            keyClass = GlobalProperties.Keys.class;
        } catch (Throwable throwable) {
            // Mixin 0.7
            ofMethodHandleTemporary = null;
            keyClass = String.class;
        }
        
        try {
            // org.spongepowered.asm.launch.GlobalProperties.get(java.lang.String, java.lang.Object)
            // org.spongepowered.asm.launch.GlobalProperties.get(org.spongepowered.asm.launch.GlobalProperties.Keys, java.lang.Object)
            Method getMethod = GlobalProperties.class.getMethod("get", keyClass, Object.class);
            getMethodHandleTemporary = lookup.unreflect(getMethod);
            
            // org.spongepowered.asm.launch.GlobalProperties.put(java.lang.String, java.lang.Object)
            // org.spongepowered.asm.launch.GlobalProperties.put(org.spongepowered.asm.launch.GlobalProperties.Keys, java.lang.Object)
            Method putMethod = GlobalProperties.class.getMethod("put", keyClass, Object.class);
            putMethodHandleTemporary = lookup.unreflect(putMethod);
        } catch (Throwable throwable) {
            getMethodHandleTemporary = null;
            putMethodHandleTemporary = null;
        }
        
        ofMethodHandle = ofMethodHandleTemporary;
        getMethodHandle = getMethodHandleTemporary;
        putMethodHandle = putMethodHandleTemporary;
    }
    
    public static <T> T get(String key) {
        return get(key, null);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, T defaultValue) {
        try {
            if (getMethodHandle == null) {
                throw new IllegalStateException("GetMethodHandle is unavailable");
            }
            
            Object keyObject = getOrCreateKey(key);
            Object object = getMethodHandle.invoke(keyObject, defaultValue);
            if (object != null) {
                return (T) object;
            }
            
            return defaultValue;
        } catch (Throwable ex) {
            SledgehammerLaunch.getLogger().error("Encountered an error while getting {}: {}", key, ex);
            return defaultValue;
        }
    }
    
    public static void put(String key, Object value) {
        try {
            if (putMethodHandle == null) {
                throw new IllegalStateException("PutMethodHandle is unavailable");
            }
            
            Object keyObject = getOrCreateKey(key);
            putMethodHandle.invoke(keyObject, value);
        } catch (Throwable throwable) {
            SledgehammerLaunch.getLogger().error("Encountered an error while putting {}: {}", key, throwable);
        }
    }
    
    private static Object getOrCreateKey(String name) throws Throwable {
        if (ofMethodHandle != null) {
            // Mixin 0.8
            return ofMethodHandle.invoke(name);
        }
        
        // Mixin 0.7
        return name;
    }
}