package de.arraying.beproductive.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

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
public final class SQLTransaction {

    private final Connection connection;

    /**
     * Gets the connection and prepares for a transaction.
     */
    public SQLTransaction() throws SQLException {
        this.connection = SQLManager.INSTANCE.provide();
        connection.setAutoCommit(false);
    }

    /**
     * Modifies the connection to do tasks such as setting the timezone.
     * @param consumer The consumer.
     * @return The instance, for chaining.
     */
    public SQLTransaction modify(Consumer<Connection> consumer) {
        consumer.accept(connection);
        return this;
    }

    /**
     * Adds another query to the transaction.
     * @param query The query.
     * @param resultHandler The result handler.
     * @param parameters The values for any parameters.
     * @return The instance, for chaining.
     * @throws SQLException If there is an SQL error.
     */
    public SQLTransaction query(SQLQuery query, SQLResultHandler resultHandler, Object... parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query.getQuery());
        for(int j = 0; j < parameters.length; j++) {
            statement.setObject(j + 1, parameters[j]);
        }
        if(resultHandler != null) {
            ResultSet resultSet = statement.executeQuery();
            resultHandler.handle(resultSet);
        } else {
            statement.execute();
        }
        return this;
    }

    /**
     * Commits the SQL transaction.
     * Don't call this if you have commitment issues.
     */
    public void commit() throws SQLException {
        try(Connection connection = this.connection) {
            connection.commit();
        }
    }

}
