package de.arraying.beproductive.command;

import de.arraying.beproductive.command.implementations.CommandCommands;
import de.arraying.beproductive.command.implementations.CommandHelp;
import de.arraying.beproductive.command.implementations.CommandPing;
import de.arraying.beproductive.command.implementations.CommandTour;
import de.arraying.beproductive.util.GuildUtil;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
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
public enum CommandManager {

    /**
     * The singleton instance.
     */
    INSTANCE;

    private final Set<Command> commands = new TreeSet<>(Comparator.comparing(Command::getName));

    /**
     * Registers the commands.
     */
    public void register() {
        commands.add(new CommandCommands());
        commands.add(new CommandHelp());
        commands.add(new CommandPing());
        commands.add(new CommandTour());
    }

    /**
     * Attempts to parse the event to see if any commands were executed.
     * @param event The event.
     */
    public void parse(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw().replaceAll(" +", " ");
        String mention = event.getJDA().getSelfUser().getAsMention();
        if(message.startsWith(mention)) {
            message = message.substring(mention.length());
        } else if(event.getChannelType() == ChannelType.TEXT) {
            String prefix = GuildUtil.getPrefix(event.getGuild());
            if(prefix != null && message.startsWith(prefix)) {
                message = message.substring(prefix.length());
            } else {
                return;
            }
        } else {
            return;
        }
        message = message.trim();
        String[] parts = message.split(" ");
        String commandName = parts[0].toLowerCase();
        String[] arguments = new String[parts.length-1];
        System.arraycopy(parts, 1, arguments, 0, arguments.length);
        commands.stream()
                .filter(it -> it.getName().equals(commandName)
                        || it.getAliases().contains(commandName))
                .findFirst().ifPresent(command -> command.preprocess(new CommandContext(event.getMessage())));
    }

    /**
     * Executes the consumer for each command.
     * @param consumer The consumer.
     */
    public void forEach(Consumer<Command> consumer) {
        commands.forEach(consumer);
    }


}
