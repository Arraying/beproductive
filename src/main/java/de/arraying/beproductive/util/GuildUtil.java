package de.arraying.beproductive.util;

import de.arraying.beproductive.sql.SQLQuery;
import de.arraying.beproductive.sql.SQLResultHandler;
import de.arraying.beproductive.sql.SQLTransaction;
import net.dv8tion.jda.core.entities.Guild;

import java.sql.ResultSet;
import java.sql.SQLException;

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
public final class GuildUtil {

    /**
     * Gets the prefix for the guild.
     * @param guild The guild.
     * @return The prefix, or null if there was an error.
     */
    public static String getPrefix(Guild guild) {
        try {
            Prefix prefix = new Prefix();
            new SQLTransaction()
                    .query(SQLQuery.GUILD_CREATE, null, guild.getIdLong())
                    .query(SQLQuery.GUILD_PREFIX, prefix, guild.getIdLong())
                    .commit();
            return prefix.prefix;
        } catch(SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * The prefix retrieval wrapper.
     */
    private static final class Prefix implements SQLResultHandler {

        private String prefix;

        /**
         * Handles the result set execution.
         * @param resultSet The result set provided by JDBC, may be null.
         */
        @Override
        public void handle(ResultSet resultSet) {
            try {
                resultSet.next();
                prefix = resultSet.getString("prefix");
            } catch(SQLException exception) {
                exception.printStackTrace();
            }
        }

    }

}
