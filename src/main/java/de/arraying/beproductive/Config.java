package de.arraying.beproductive;

import java.util.function.Function;

/**
 * Copyright 2019 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class Config {

    private final String token;
    private final int shards;
    private final String developers;

    /**
     * Creates the config.
     */
    Config() {
        this.token = getVariable("TOKEN", String::toString, "123");
        this.shards = getVariable("SHARDS", Integer::parseInt, 1);
        this.developers = getVariable("DEVELOPERS", String::toString, "");
    }

    /**
     * Gets the bot token.
     * @return The token used to log into Discord.
     */
    String getToken() {
        return token;
    }

    /**
     * Gets the total number of shards.
     * @return A number > 0.
     */
    int getShards() {
        return shards;
    }

    /**
     * Gets all developers.
     * This will possibly return a list delimited by characters. The entire string needs a contains check in order to see if
     * a developer is contained within it.
     * @return A list of developers.
     */
    public String getDevelopers() {
        return developers;
    }

    /**
     * Gets an environment variable.
     * @param name The name of the environment variable.
     * @param mapper The mapper function.
     * @param fallback The fallback default value.
     * @param <T> The type of the target variable.
     * @return The variable in the form of the target variable.
     */
    private <T> T getVariable(String name, Function<String, T> mapper, T fallback) {
        String raw = System.getenv(name);
        if(raw == null) {
            return fallback;
        }
        return mapper.apply(raw);
    }

}
