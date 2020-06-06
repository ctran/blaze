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
package com.fizzed.blaze.internal;

import com.fizzed.blaze.Config;
import com.fizzed.blaze.Context;
import com.fizzed.blaze.core.ConsolePrompter;
import com.fizzed.blaze.core.MessageOnlyException;
import com.fizzed.blaze.core.Prompter;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of a <code>Context</code>.
 */
public class ContextImpl implements Context {
    static private final Logger log = LoggerFactory.getLogger(ContextImpl.class);
    
    protected Path baseDir;
    protected Path userDir;
    protected Path scriptFile;
    protected Config config;
    protected Logger logger;
    protected Prompter prompter;
    
    public ContextImpl(Path baseDir, Path userDir, Path scriptFile, Config config) {
        this.baseDir = (baseDir != null ? baseDir : Paths.get("."));
        this.userDir = (userDir != null ? userDir : findUserDir());
        this.scriptFile = scriptFile;
        this.config = config;
        this.logger = LoggerFactory.getLogger("script");
        this.prompter = new ConsolePrompter();
    }

    public Prompter getPrompter() {
        return prompter;
    }
    
    public void prompter(Prompter prompter) {
        this.prompter = prompter;
    }
    
    public void baseDir(Path baseDir) {
        this.baseDir = baseDir;
    }
    
    public void userDir(Path userDir) {
        this.userDir = userDir;
    }
    
    public void scriptFile(Path scriptFile) {
        this.scriptFile = scriptFile;
    }
    
    @Override
    public Logger logger() {
        return this.logger;
    }

    @Override
    public Path baseDir() {
        return this.baseDir;
    }
    
    @Override
    public Path withBaseDir(Path path) {
        Objects.requireNonNull(path, "path cannot be null");
        return this.baseDir().resolve(path).normalize();
    }
    
    @Override
    public Path withBaseDir(File file) {
        Objects.requireNonNull(file, "file cannot be null");
        return this.withBaseDir(file.toPath());
    }
    
    @Override
    public Path withBaseDir(String path) {
        Objects.requireNonNull(path, "path cannot be null");
        return this.withBaseDir(Paths.get(path));
    }
    
    @Override
    public Path scriptFile() {
        return this.scriptFile;
    }
    
    @Override
    public Config config() {
        return this.config;
    }

    @Override
    public Path userDir() {
        return this.userDir;
    }
    
    @Override
    public Path withUserDir(Path path) {
        Objects.requireNonNull(path, "path cannot be null");
        return this.userDir().resolve(path).normalize();
    }
    
    @Override
    public Path withUserDir(File file) {
        Objects.requireNonNull(file, "file cannot be null");
        return this.withUserDir(file.toPath());
    }
    
    @Override
    public Path withUserDir(String path) {
        Objects.requireNonNull(path, "path cannot be null");
        return this.withUserDir(Paths.get(path));
    }
    
    @Override
    public void fail(String message) {
        throw new MessageOnlyException(message);
    }
    
    @Override
    public String prompt(String prompt, Object... args) {
        return this.prompter.prompt(prompt, args);
    }
    
    @Override
    public char[] passwordPrompt(String prompt, Object... args) {
        return this.prompter.passwordPrompt(prompt, args);
    }

    static public Path findUserDir() {
        // environment var is better than java "user.home"
        Optional<Path> dir = dirIfExists(System.getenv("HOME"));
        
        if (!dir.isPresent()) {
            String homeDrive = System.getenv("HOMEDRIVE");
            String homePath = System.getenv("HOMEPATH");
            dir = dirIfExists((homeDrive != null ? homeDrive : "") + (homePath != null ? homePath : ""));
            
            if (!dir.isPresent()) {
                dir = dirIfExists(System.getProperty("user.home"));
            }
        }
        
        try {
            return dir.orElseThrow(() -> new IOException("Unable to find user home dir!"));
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
    
    static public Optional<Path> dirIfExists(String path) {
        try {
            if (path == null || path.length() == 0) {
                return Optional.empty();
            }
            
            Path dir = Paths.get(path);

            if (Files.isDirectory(dir)) {
                return Optional.of(dir);
            } else {
                return Optional.empty();
            }
        } catch (Throwable t) {
            return Optional.empty();
        }
    } 
}