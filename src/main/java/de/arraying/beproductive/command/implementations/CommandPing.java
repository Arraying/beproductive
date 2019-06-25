package de.arraying.beproductive.command.implementations;

import de.arraying.beproductive.command.Command;
import de.arraying.beproductive.command.CommandContext;
import de.arraying.beproductive.command.CommandRestriction;
import net.dv8tion.jda.core.Permission;

import java.util.HashSet;

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
public final class CommandPing extends Command {

    /**
     * Creates a new command.
     */
    public CommandPing() {
        super("ping", "Returns the WebSocket latency.", new HashSet<>(), Permission.MESSAGE_WRITE, CommandRestriction.UNRESTRICTED);
    }

    /**
     * Sends the average WebSocket latency. This may be accurate.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        context.reply(String.format("The average ping is currently %d milliseconds.", context.getUser().getJDA().getPing())).queue();
    }

}
