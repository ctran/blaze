/*
 * Copyright 2020 Fizzed, Inc.
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
package com.fizzed.blaze.mysql;

import com.fizzed.blaze.Context;
import com.fizzed.blaze.core.BlazeException;
import com.fizzed.blaze.util.ImmutableUri;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MysqlSession implements AutoCloseable {
    static private final Logger log = LoggerFactory.getLogger(MysqlSession.class);

    private final Context context;
    private final ImmutableUri uri;
    private final ImmutableUri redactedUri;
    private final Connection connection;
    private final MysqlInfo info;

    public MysqlSession(Context context, ImmutableUri uri, ImmutableUri redactedUri, Connection connection, MysqlInfo info) {
        this.context = context;
        this.uri = uri;
        this.redactedUri = redactedUri;
        this.connection = connection;
        this.info = info;
    }

    public Context context() {
        return this.context;
    }
    
    public ImmutableUri uri() {
        return this.redactedUri;
    }
    
    public MysqlInfo info() {
        return this.info;
    }

    @Override
    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
            }
            catch (SQLException e) {
                log.warn("", e);
            }
        }
    }
    
    public void createDatabase(String name) {
        this.createDatabase(name, false);
    }
    
    public void createDatabases(Collection<String> names, boolean ifNotExists) {
        names.forEach(v -> this.createDatabase(v, ifNotExists));
    }
    
    public void createDatabase(String name, boolean ifNotExists) {
        try {
            try (Statement stmt = this.connection.createStatement()) {
                stmt.execute("CREATE DATABASE " + (ifNotExists ? "IF NOT EXISTS " : "") + name);
                
                log.info("Created mysql database {}", name);
            }
        }
        catch (SQLException e) {
            throw new BlazeException(e.getMessage(), e);
        }
    }
    
    public void dropDatabase(String name) {
        this.dropDatabase(name, false);
    }
    
    public void dropDatabases(Collection<String> names, boolean ifExists) {
        names.forEach(v -> this.dropDatabase(v, ifExists));
    }
    
    public void dropDatabase(String name, boolean ifExists) {
        try {
            try (Statement stmt = this.connection.createStatement()) {
                stmt.execute("DROP DATABASE " + (ifExists ? "IF EXISTS " : "") + name);
                
                log.info("Dropped mysql database {}", name);
            }
        }
        catch (SQLException e) {
            throw new BlazeException(e.getMessage(), e);
        }
    }

}