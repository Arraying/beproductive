package de.arraying.beproductive;

import de.arraying.beproductive.command.CommandManager;
import de.arraying.beproductive.sql.SQLManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.time.Instant;

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
public enum Bot {

    /**
     * The singleton instance.
     */
    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger("Bot");
    private Config config;
    private JDA[] shards;

    /**
     * The entry point of the program. Will start the bot.
     * @param args The runtime arguments, these are ignored.
     */
    public static void main(String[] args) {
        Thread.currentThread().setName("Main");
        Bot.INSTANCE.start();
    }

    /**
     * Starts the bot.
     */
    public void start() {
        logger.info("Starting bot...");
        config = new Config();
        if(!SQLManager.INSTANCE.setup()) {
            logger.info("SQL initialization unsuccessful.");
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("Registering commands...");
        CommandManager.INSTANCE.register();
        shards = new JDA[config.getShards()];
        for(int v = 0; v < config.getShards(); v++) {
            logger.info("Creating shard {} of {}.", v, config.getShards());
            try {
                JDA shard = new JDABuilder(config.getToken())
                        .setAudioEnabled(false)
                        .setGame(Game.of(Game.GameType.WATCHING, "you being productive"))
                        .addEventListener(new Events())
                        .build()
                        .awaitReady();
                shards[v] = shard;
            } catch(InterruptedException | LoginException exception) {
                exception.printStackTrace();
                SQLManager.INSTANCE.shutdown();
            }
        }
        logger.info("Load complete.");
    }

    /**
     * Creates a new embed builder with a few presets.
     * @return The embed builder.
     */
    public EmbedBuilder generateEmbed() {
        return new EmbedBuilder()
                .setTimestamp(Instant.now())
                .setColor(0xF7AF68);
    }

    /**
     * Gets the logger.
     * @return The logger.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Gets the config.
     * @return The config.
     */
    public Config getConfig() {
        return config;
    }

}
