package de.arraying.beproductive.data;

import java.util.HashSet;
import java.util.Set;

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
public final class Productivity {

    private final Set<String> flags = new HashSet<>();
    private final long start;

    /**
     * Creates a new productivity session.
     * @param start The start time.
     */
    public Productivity(long start) {
        this.start = start;
    }

    /**
     * Whether or not the current productivity session has a specific flag.
     * @param flag The flag.
     * @return True if it does, false otherwise.
     */
    public boolean hasFlag(String flag) {
        return flags.contains(flag);
    }

    /**
     * Adds a flag to the productivity session.
     * @param flag The flag.
     */
    public void flag(String flag) {
        flags.add(flag);
    }

    /**
     * Removes a flag from the productivity session.
     * @param flag The flag.
     */
    public void unflag(String flag) {
        flags.remove(flag);
    }

    /**
     * Gets the start time.
     * @return The start time, in milliseconds.
     */
    public long getStart() {
        return start;
    }

    /**
     * Converts all flags to data.
     * @return The data.
     */
    public String toData() {
        return String.join(",", flags);
    }

}
