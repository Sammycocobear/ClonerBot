package me.sammy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KeyGetter {
    private static String discordToken;
    private static String openaiKey;

    static {
        try {
            Properties properties = new Properties();
            InputStream inputStream = KeyGetter.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            discordToken = properties.getProperty("discord_token");
            openaiKey = properties.getProperty("openai_key");
            inputStream.close();
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
