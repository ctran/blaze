/*
 * Copyright 2015 Fizzed, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fizzed.blaze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author joelauer
 */
public interface Config {
    
    static String KEY_COMMAND_EXTS = "blaze.command.exts";
    static String KEY_DEFAULT_TASK = "blaze.default.task";
    static String KEY_DEPENDENCIES = "blaze.dependencies";
    static String KEY_DEPENDENCY_CLEAN = "blaze.dependency.clean";
    
    static String DEFAULT_TASK = "main";
    static Boolean DEFAULT_DEPENDENCY_CLEAN = Boolean.FALSE;
    
    static List<String> DEFAULT_COMMAND_EXTS_UNIX = Arrays.asList("", ".sh");
    static List<String> DEFAULT_COMMAND_EXTS_WINDOWS = Arrays.asList(".exe", ".bat", ".cmd");
    
    String getString(String key);
    
    String getStringOrDie(String key);
    
    String getString(String key, String defaultValue);
    
    Boolean getBoolean(String key);
    
    Boolean getBooleanOrDie(String key);
    
    Boolean getBoolean(String key, Boolean defaultValue);
    
    List<String> getStringList(String key);
    
    List<String> getStringList(String key, List<String> defaultValue);
    
}
