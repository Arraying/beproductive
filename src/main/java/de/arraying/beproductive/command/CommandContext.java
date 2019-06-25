package de.arraying.beproductive.command;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.requests.RestAction;

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
public final class CommandContext {

    private final Message message;
    private final MessageChannel channel;
    private final User user;
    private final Guild guild;

    /**
     * Creates a new command context.
     * @param message The message that executed the command.
     */
    CommandContext(Message message) {
        this.message = message;
        this.channel = message.getChannel();
        this.user = message.getAuthor();
        this.guild = message.getGuild();
    }

    /**
     * Checks if the message channel is of the specified type.
     * @param type The type.
     * @return True if it is, false otherwise.
     */
    boolean isNotChannel(ChannelType type) {
        return message.getChannelType() != type;
    }

    /**
     * Replies to the channel with a text message.
     * @param message The message.
     * @return The associated RestAction that can be queued.
     */
    public RestAction<Message> reply(String message) {
        return channel.sendMessage(message);
    }

    /**
     * Replies to the channel with an embed.
     * @param embed The embed.
     * @return The associated RestAction that can be queued.
     */
    public RestAction<Message> reply(MessageEmbed embed) {
        return channel.sendMessage(embed);
    }

    /**
     * Gets the message that executed the command.
     * @return The message.
     */
    Message getMessage() {
        return message;
    }

    /**
     * Gets the channel in which the command was executed in.
     * @return The message channel (text or private).
     */
    public MessageChannel getChannel() {
        return channel;
    }

    /**
     * Gets the user that sent the command.
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the guild in which the command was executed in, if applicable.
     * @return The guild, or null if this was in a private channel.
     */
    public Guild getGuild() {
        return guild;
    }

}
