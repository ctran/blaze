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
package com.fizzed.blaze.local;

import com.fizzed.blaze.Context;
import com.fizzed.blaze.system.Exec;
import com.fizzed.blaze.system.ExecSession;

public class LocalSession implements ExecSession {

    private final Context context;

    public LocalSession(Context context) {
        this.context = context;
    }

    @Override
    public Exec newExec() {
        return new LocalExec(context);
    }

}