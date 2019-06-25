package de.arraying.beproductive.command;

import de.arraying.beproductive.Bot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.TextChannel;

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
public abstract class Command {

    private final String name;
    private final Set<String> aliases;
    private final String description;
    private final Permission permission;
    private final CommandRestriction restriction;
    protected boolean developer = false;

    /**
     * Creates a new command.
     * @param name The name.
     * @param description The description.
     * @param aliases The aliases.
     * @param permission The permission.
     * @param restriction The command restriction.
     */
    public Command(String name, String description, Set<String> aliases, Permission permission, CommandRestriction restriction) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.permission = permission;
        this.restriction = restriction;
    }

    /**
     * The actual command implementation.
     * @param context The command context.
     */
    protected abstract void execute(CommandContext context);

    /**
     * Pre-processes the command.
     * This double checks that the conditions for the command are met (i.e. perms or restrictions).
     * If everything is met, the actual command method gets invoked.
     * @param context The command context.
     */
    final void preprocess(CommandContext context) {
        Bot.INSTANCE.getLogger().info("Started processing command {} for user {}.", name, context.getUser().getId());
        if(developer && !Bot.INSTANCE.getConfig().getDevelopers().contains(context.getUser().getId())) {
            context.reply("This command is restricted to the bot developers.").queue();
            return;
        }
        switch(restriction) {
            case PRIVATE:
                if(context.isNotChannel(ChannelType.PRIVATE)) {
                    context.reply("This command needs to be run in direct messages.").queue();
                    return;
                }
            case GUILD:
                if(context.isNotChannel(ChannelType.TEXT)) {
                    context.reply("This command needs to be run in a guild text channel.").queue();
                    return;
                }
        }
        if(context.getChannel() instanceof TextChannel) {
            TextChannel textChannel = (TextChannel) context.getChannel();
            if(!context.getMessage().getMember().hasPermission(textChannel, permission)) {
                context.reply("You do not have the required permission (" + permission.getName() + ") to execute this command.").queue();
                return;
            }
        }
        execute(context);
        Bot.INSTANCE.getLogger().info("Executed the command {} for user {}.", name, context.getUser().getId());
    }

    /**
     * Gets the command name.
     * @return The command name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the command aliases.
     * @return The command aliases.
     */
    Set<String> getAliases() {
        return aliases;
    }

    /**
     * Gets the command description.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the command restriction.
     * @return The command restriction.
     */
    public CommandRestriction getRestriction() {
        return restriction;
    }

    /**
     * Whether or not the command is strictly for developers.
     * @return True if it is, false otherwise.
     */
    public boolean isDeveloper() {
        return developer;
    }

}
