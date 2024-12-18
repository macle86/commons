/*
 * Copyright (C) 2020 Seomse Inc.
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
package io.runon.commons.example;

import io.runon.commons.config.Config;
import io.runon.commons.config.ConfigSet;

/**
 * config test
 * @author macle
 */
public class ConfigExample {
    public static void main(String[] args) {
        ConfigSet.IS_SYSTEM_PROPERTIES_USE = true;

        System.out.println(Config.getConfig("application.jdbc.type"));
    }
}
