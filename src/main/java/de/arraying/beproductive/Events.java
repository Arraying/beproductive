package de.arraying.beproductive;

import de.arraying.beproductive.command.CommandManager;
import de.arraying.beproductive.data.ProductivityManager;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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
public final class Events extends ListenerAdapter {

    /**
     * When the bot is ready.
     * This means all subsystems have been loaded and everything is operational.
     * @param event The event.
     */
    @Override
    public void onReady(ReadyEvent event) {
        if(event.getJDA().getShardInfo() != null) {
            Bot.INSTANCE.getLogger().info("Shard {} operational.", event.getJDA().getShardInfo().getShardId());
        }
    }

    /**
     * When a message has been received, regardless of the type of channel.
     * @param event The event.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot() || (event.getChannelType() != ChannelType.TEXT && event.getChannelType() != ChannelType.PRIVATE)) {
            return;
        }
        CommandManager.INSTANCE.parse(event);
        ProductivityManager.INSTANCE.onMessage(event.getAuthor(), event.getMessage());
    }

}
