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
public final class CommandHelp extends Command {

    /**
     * Creates a new command.
     */
    public CommandHelp() {
        super("help", "Shows how to use the bot.", CollectionUtil.set("h", "guide"), Permission.MESSAGE_WRITE, CommandRestriction.UNRESTRICTED);
    }

    /**
     * Sends the help embed.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        EmbedBuilder embedBuilder = Bot.INSTANCE.generateEmbed();
        embedBuilder.setDescription("Hey there, let me introduce myself! I am *be productive!*, and I like to think of myself as someone who helps others. " +
                "I'm relatively simple and straightforward to use. I've laid out some commands below to help you get started:");
        String mention = context.getChannel().getJDA().getSelfUser().getAsMention();
        embedBuilder.addField("If you are a user...", "Do '" + mention + " hello' in direct messages to set up your user profile.\n\u200B", false);
        embedBuilder.addField("If you are a server manager...", "Do '" + mention + " config' in a guild channel to set up the bot in your server.\n\u200B", false);
        embedBuilder.addField("To get an overview of features...", "Do '" + mention + " tour', this will give you a basic overview of the bot and its features.\n\u200B", false);
        embedBuilder.addField("In order to see all commands...", "Do '" + mention + " commands' in any channel.", false);
        context.reply(embedBuilder.build()).queue();
    }

}
