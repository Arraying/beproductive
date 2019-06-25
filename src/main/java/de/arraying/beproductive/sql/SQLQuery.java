package de.arraying.beproductive.sql;

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
public enum SQLQuery {

    /**
     * Creates the guilds table where all guild related information is stored.
     */
    GENERATE_GUILDS("CREATE TABLE IF NOT EXISTS `guilds` (`id` BIGINT PRIMARY KEY, `prefix` VARCHAR(32) DEFAULT NULL, `consent_delete` BOOLEAN DEFAULT FALSE, `consent_override` BOOLEAN DEFAULT FALSE);"),

    /**
     * Creates the users table where all user data is stored.
     */
    GENERATE_USERS("CREATE TABLE IF NOT EXISTS `users` (`id` BIGINT PRIMARY KEY, `timezone` VARCHAR(64) NOT NULL, `procrastination` VARCHAR(32) NOT NULL);"),

    /**
     * Creates the tasks table where all the task data is stored.
     */
    GENERATE_TASKS("CREATE TABLE IF NOT EXISTS `tasks` (`id` INT PRIMARY KEY AUTO_INCREMENT, `date` TIMESTAMP NOT NULL, `description` TEXT NOT NULL);"),

    /**
     * Creates the productivity table where all productivity cache is temporarily dumped.
     */
    GENERATE_PRODUCTIVITY("CREATE TABLE IF NOT EXISTS `productivity_dump` (`user` BIGINT PRIMARY KEY, `start` BIGINT NOT NULL, `data` TEXT NOT NULL);"),

    /**
     * Deletes the productivity table.
     */
    UNGENERATE_PRODUCTIVITY("DROP TABLE `productivity_dump`"),

    /**
     * Inserts a guild, thus creating it.
     */
    GUILD_CREATE("INSERT IGNORE INTO `guilds` (`id`) VALUES (?);"),

    /**
     * Gets the guild prefix.
     */
    GUILD_PREFIX("SELECT `prefix` FROM `guilds` WHERE `id`=?;"),

    /**
     * Gets all the guild consent values.
     */
    GUILD_CONSENT("SELECT `consent_delete`, `consent_override` FROM `guilds` WHERE `id`=?;"),

    /**
     * Loads a user by ID.
     */
    USER_LOAD("SELECT * FROM `users` WHERE `id`=?;"),

    /**
     * Saves a user.
     * This will first attempt to insert a new record. If the primary key exists (user is already registered) it will
     * simply replace the old values with the new ones.
     */
    USER_SAVE("INSERT INTO `users` VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE `id`=?, `timezone`=?, `procrastination`=?;"),

    /**
     * Loads all productivity dumps.
     */
    PRODUCTIVITY_LOAD("SELECT * FROM `productivity_dump`"),

    /**
     * Saves a productivity entry to the database.
     */
    PRODUCTIVITY_SAVE("INSERT INTO `productivity_dump` VALUES(?, ?, ?);");

    private final String query;

    /**
     * Creates a new SQL query.
     * @param query The query string.
     */
    SQLQuery(String query) {
        this.query = query;
    }

    /**
     * Gets the query string.
     * @return The query string.
     */
    String getQuery() {
        return query;
    }

}
