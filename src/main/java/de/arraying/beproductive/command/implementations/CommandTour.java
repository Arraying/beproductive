package de.arraying.beproductive.command.implementations;

import de.arraying.beproductive.Bot;
import de.arraying.beproductive.command.Command;
import de.arraying.beproductive.command.CommandContext;
import de.arraying.beproductive.command.CommandRestriction;
import de.arraying.beproductive.util.CollectionUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

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
public final class CommandTour extends Command {

    /**
     * Creates a new command.
     */
    public CommandTour() {
        super("tour", "Shows the bot features.", CollectionUtil.set("t"), Permission.MESSAGE_WRITE, CommandRestriction.UNRESTRICTED);
    }

    /**
     * Sends the highlights of the bot.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        // TODO complete this
        EmbedBuilder embedBuilder = Bot.INSTANCE.generateEmbed();
        embedBuilder.addField("Productivity Sessions", "todo", false);
        embedBuilder.addField("Task Management", "todo", false);
        context.reply(embedBuilder.build()).queue();
    }

}
