package de.arraying.beproductive.command.implementations;

import de.arraying.beproductive.Bot;
import de.arraying.beproductive.command.Command;
import de.arraying.beproductive.command.CommandContext;
import de.arraying.beproductive.command.CommandManager;
import de.arraying.beproductive.command.CommandRestriction;
import de.arraying.beproductive.util.CollectionUtil;
import de.arraying.beproductive.util.GuildUtil;
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
public final class CommandCommands extends Command {

    /**
     * Creates a new command.
     */
    public CommandCommands() {
        super("commands", "Shows a list of commands.", CollectionUtil.set(""), Permission.MESSAGE_WRITE, CommandRestriction.UNRESTRICTED);
    }

    /**
     * Sends a list of commands and their respective description.
     * @param context The command context.
     */
    @Override
    protected void execute(CommandContext context) {
        EmbedBuilder embedBuilder = Bot.INSTANCE.generateEmbed();
        embedBuilder.setDescription("Here is a list of commands.");
        String prefix = "@" + context.getChannel().getJDA().getSelfUser().getName() + " ";
        if(context.getGuild() != null) {
            String newPrefix = GuildUtil.getPrefix(context.getGuild());
            if(newPrefix != null) {
                prefix = newPrefix;
            }
        }
        final String finalPrefix = prefix; // What the hell, Java
        CommandManager.INSTANCE.forEach(command -> {
            if(command.isDeveloper()) {
                return;
            }
            String execution;
            switch(command.getRestriction()) {
                case UNRESTRICTED:
                    execution = "This command can be executed anywhere.";
                    break;
                case GUILD:
                    execution = "This command must be executed in a server.";
                    break;
                case PRIVATE:
                    execution = "This command must be executed in direct messages.";
                    break;
                default:
                    execution = "???";
            }
            embedBuilder.addField(finalPrefix + command.getName(), command.getDescription() + "\n*" + execution + "*\n\u200B", false);
        });
        context.reply(embedBuilder.build()).queue();
    }

}
