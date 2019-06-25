package de.arraying.beproductive.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.arraying.beproductive.Bot;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
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
public enum SQLManager {

    /**
     * The singleton instance.
     */
    INSTANCE;

    private HikariDataSource dataSource;

    /**
     * Sets up the database and its connections.
     * @return True if everything was successful, false otherwise (i.e. credentials incorrect).
     */
    public boolean setup() {
        HikariConfig config = new HikariConfig();
        String ip;
        try {
            ip = resolve();
        } catch(UnknownHostException exception) {
            Bot.INSTANCE.getLogger().info("An error occurred resolving the hostname of the database container.");
            exception.printStackTrace();
            return false;
        }
        String jdbc = String.format("jdbc:mysql://%s:%d/%s", ip, 3306, "bp");
        config.setJdbcUrl(jdbc);
        config.setUsername("help");
        config.setPassword("me");
        config.setMinimumIdle(5);
        config.setLeakDetectionThreshold(3000);
        Bot.INSTANCE.getLogger().info("Using JDBC URL '{}'.", jdbc);
        dataSource = new HikariDataSource(config);
        try {
            new SQLTransaction()
                    .query(SQLQuery.GENERATE_GUILDS, null)
                    .query(SQLQuery.GENERATE_USERS, null)
                    .query(SQLQuery.GENERATE_TASKS, null)
                    .commit();
        } catch(SQLException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Shuts down the data source.
     */
    public void shutdown() {
        if(dataSource != null
                && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    /**
     * Provides a connection
     * @return A non-null SQL connection.
     * @throws SQLException If the connection could not be created such as on connection errors.
     */
    Connection provide() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Resolves the host name to get a valid IP address.
     * JDBC appears to have issues with Docker hostnames, so the IP needs to be resolved beforehand.
     * @return The IP, as a string.
     */
    private String resolve() throws UnknownHostException {
        return InetAddress.getByName("db").getHostAddress();
    }

}
