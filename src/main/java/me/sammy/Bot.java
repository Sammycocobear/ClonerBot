package me.sammy;

import me.sammy.Events.BotLoadInformation;
import me.sammy.Events.SlashEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Bot {
    public static final String OPENAI_KEY = KeyGetter.getOpenaiKey();
    public static Map<Guild,String> guildCharacterMap = new HashMap<>();
    public static  Connection connection = null;
    public static JDA builder;

    public static void main(String[] args) {
        builder = JDABuilder.createLight(KeyGetter.getDiscordToken())
        .addEventListeners(new SlashEvent(), new BotLoadInformation())
        .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
        .setBulkDeleteSplittingEnabled(false)
        .enableIntents(GatewayIntent.GUILD_MEMBERS) // Enable the GUILD_MEMBERS intent
        .setActivity(Activity.watching("Cloning your favorite people!"))
        .build();

        builder.updateCommands().addCommands(
                Commands.slash("talk","talk to the chracter like they are actually there!").addOption(OptionType.STRING, "message","the message", true),
                Commands.slash("clone","set the clone").addOption(OptionType.STRING, "cloner","the clone", true)
        ).queue();

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + Paths.get("").toAbsolutePath().toString() + "/database.db");
            SQLManager.createTable();

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }


    }


}
