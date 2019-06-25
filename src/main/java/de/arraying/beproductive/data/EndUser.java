package de.arraying.beproductive.data;

import de.arraying.beproductive.Bot;
import de.arraying.beproductive.sql.SQLQuery;
import de.arraying.beproductive.sql.SQLTransaction;

import java.sql.SQLException;
import java.util.TimeZone;

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
public final class EndUser {

    // TODO implement user

    private final long id;
    private TimeZone time;
    private EndUserProcrastination procrastination;
    private int rows;

    /**
     * Creates the end user instance.
     * @param id The Discord ID of the user.
     */
    public EndUser(long id) {
        this.id = id;
    }

    /**
     * Saves all of the fields to the database.
     */
    public void save() {
        synchronized(this) {
            try {
                new SQLTransaction()
                        .query(SQLQuery.USER_SAVE, null, id, time, procrastination.toString(), id, time, procrastination.toString())
                        .commit();
            } catch(SQLException exception) {
                Bot.INSTANCE.getLogger().error("Could not save user {}.", id);
                exception.printStackTrace();
            }
        }
    }

    /**
     * Loads the data into the fields.
     */
    public void load() {
        synchronized(this) {
            try {
                this.rows = 0;
                new SQLTransaction()
                        .query(SQLQuery.USER_LOAD, result -> {
                            result.next();
                            this.time = TimeZone.getTimeZone(result.getString("timezone"));
                            this.procrastination = EndUserProcrastination.from(result.getString("procrastination"));
                            rows++;
                        }, id)
                        .commit();
            } catch(SQLException exception) {
                Bot.INSTANCE.getLogger().error("Could not load user {}.", id);
                exception.printStackTrace();
            }
        }
    }

    /**
     * Gets the timezone.
     * @return The timezone.
     */
    public TimeZone getTime() {
        return time;
    }

    /**
     * Sets the timezone.
     * @param time The new timezone.
     */
    public void setTime(TimeZone time) {
        this.time = time;
    }

    /**
     * Gets the user procrastination level.
     * @return The procrastination level.
     */
    public EndUserProcrastination getProcrastination() {
        return procrastination;
    }

    /**
     * Sets the procrastination level.
     * @param procrastination The new procrastination level.
     */
    public void setProcrastination(EndUserProcrastination procrastination) {
        this.procrastination = procrastination;
    }

    /**
     * Gets the number of last loaded by the load method.
     * This should always be 1 or 0. If it is 0, it means the user does not exist.
     * @return 1 or 0.
     */
    public int getRows() {
        return rows;
    }

}
