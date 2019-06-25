package de.arraying.beproductive.data;

import de.arraying.beproductive.Bot;
import de.arraying.beproductive.sql.SQLQuery;
import de.arraying.beproductive.sql.SQLTransaction;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
public enum ProductivityManager {

    // TODO implement productivity

    /**
     * The singleton instance.
     */
    INSTANCE;

    private final Map<Long, Productivity> productivities = new HashMap<>();
    private boolean loaded = false;

    /**
     * Loads the dump into memory and deletes the table.
     */
    public void dumpLoad() {
        synchronized(this) {
            if(loaded) {
                return;
            }
            try {
                new SQLTransaction()
                        .query(SQLQuery.PRODUCTIVITY_LOAD, result -> {
                            while(result.next()) {
                                long id = result.getLong("id");
                                long start = result.getLong("start");
                                Productivity productivity = new Productivity(start);
                                Arrays.stream(result.getString("data")
                                        .split(","))
                                        .forEach(productivity::flag);
                                productivities.put(id, productivity);
                            }
                        })
                        .query(SQLQuery.UNGENERATE_PRODUCTIVITY, null)
                        .commit();
                Bot.INSTANCE.getLogger().info("Loaded productivity dump.");
            } catch(SQLException exception) {
                Bot.INSTANCE.getLogger().error("Could not load productivity dump.");
                exception.printStackTrace();
            }
            loaded = true;
        }
    }

    /**
     * Creates the table and loads the memory into the database.
     */
    public void dumpSave() {
        synchronized(this) {
            if(!loaded) {
                return;
            }
            try {
                SQLTransaction transaction = new SQLTransaction()
                        .query(SQLQuery.GENERATE_PRODUCTIVITY, null);
                for(Map.Entry<Long, Productivity> entry : productivities.entrySet()) {
                    transaction.query(SQLQuery.PRODUCTIVITY_SAVE, null, entry.getKey(), entry.getValue().getStart(), entry.getValue().toData());
                }
                transaction.commit();
                Bot.INSTANCE.getLogger().info("Saved productivity dump ({} total).", productivities.size());
            } catch(SQLException exception) {
                Bot.INSTANCE.getLogger().error("Could not save productivity dump.");
                exception.printStackTrace();
            }
            loaded = false;
        }
    }

    /**
     * Whether or not the user is in a session.
     * @param user The user.
     * @return True if they are, false otherwise.
     */
    public boolean isInSession(User user) {
        synchronized(this) {
            return productivities.containsKey(user.getIdLong());
        }
    }

    /**
     * Starts a new productivity session for the user.
     * @param user The user.
     * @param productivity The session data.
     */
    public void add(User user, Productivity productivity) {
        synchronized(this) {
            if(!productivities.containsKey(user.getIdLong())) {
                productivities.put(user.getIdLong(), productivity);
            }
        }
    }

    /**
     * Removes the productivity session from the user.
     * @param user The user.
     */
    public void remove(User user) {
        synchronized(this) {
            productivities.remove(user.getIdLong());
        }
    }

    /**
     * When a user sends a message in a guild.
     * @param user The user.
     * @param message The message.
     */
    public void onMessage(User user, Message message) {
        if(!isInSession(user)) {
            return;
        }
        Productivity productivity = productivities.get(user.getIdLong());
        try {
            new SQLTransaction()
                    .query(SQLQuery.GUILD_CONSENT, result -> {
                        result.next();
                        if(result.getBoolean("consent_delete")
                                && productivity.hasFlag("delete")) {
                            message.delete().queue();
                        }
                        if(result.getBoolean("consent_override")
                                && productivity.hasFlag("override")) {
                            message.getTextChannel().putPermissionOverride(message.getMember())
                                    .setDeny(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE)
                                    .queue();
                        }
                    }, message.getGuild().getIdLong())
                    .commit();
        } catch(SQLException exception) {
            exception.printStackTrace();
        }
    }

}
