/*
 * Copyright 2023 Alex Thomson
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

package io.github.lxgaming.sledgehammer.util;

import io.github.lxgaming.sledgehammer.launch.SledgehammerLaunch;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.util.PrettyPrinter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class PrettyPrinterProxy {
    
    private static final MethodHandle levelMethodHandle;
    private static final MethodHandle logMethodHandle;
    
    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        
        Class<?> parameterClass;
        MethodHandle levelMethodHandleTemporary;
        MethodHandle logMethodHandleTemporary;
        
        try {
            parameterClass = Class.forName("org.spongepowered.asm.logging.Level");
        } catch (Throwable t) {
            parameterClass = Level.class;
        }
        
        try {
            // org.apache.logging.log4j.Level.valueOf(java.lang.String)
            // org.spongepowered.asm.logging.Level.valueOf(java.lang.String)
            Method valueOfMethod = parameterClass.getMethod("valueOf", String.class);
            levelMethodHandleTemporary = lookup.unreflect(valueOfMethod);
            
            // org.spongepowered.asm.util.PrettyPrinter.log(org.apache.logging.log4j.Level)
            // org.spongepowered.asm.util.PrettyPrinter.log(org.spongepowered.asm.logging.Level)
            Method logMethod = PrettyPrinter.class.getMethod("log", parameterClass);
            logMethodHandleTemporary = lookup.unreflect(logMethod);
        } catch (Throwable t) {
            levelMethodHandleTemporary = null;
            logMethodHandleTemporary = null;
        }
        
        levelMethodHandle = levelMethodHandleTemporary;
        logMethodHandle = logMethodHandleTemporary;
    }
    
    public static PrettyPrinter error(PrettyPrinter prettyPrinter) {
        try {
            if (levelMethodHandle == null || logMethodHandle == null) {
                throw new IllegalStateException("MethodHandle is unavailable");
            }
            
            Object levelObject = levelMethodHandle.invoke("ERROR");
            Object object = logMethodHandle.invoke(prettyPrinter, levelObject);
            
            return (PrettyPrinter) object;
        } catch (Throwable t) {
            SledgehammerLaunch.getLogger().error("Encountered an error while pretty printing", t);
            return prettyPrinter;
        }
    }
}