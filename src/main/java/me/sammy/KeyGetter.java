package me.sammy;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class KeyGetter {
    private static String discordToken;
    private static String openaiKey;

    static {
        try {
            Properties properties = new Properties();
            FileInputStream configFile = new FileInputStream("src/main/java/config.properties");
            System.out.println(configFile);
            properties.load(configFile);
            discordToken = properties.getProperty("discord_token");
            openaiKey = properties.getProperty("openai_key");
            configFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDiscordToken() {
        return discordToken;
    }

    public static String getOpenaiKey() {
        return openaiKey;
    }

}

