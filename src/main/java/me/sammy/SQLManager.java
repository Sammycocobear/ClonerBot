package me.sammy;

import net.dv8tion.jda.api.entities.Guild;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLManager {

    public static void createTable() {
        Statement statement = null;
        try {
            statement = Bot.connection.createStatement();

            // Create the table
            String sql = "CREATE TABLE IF NOT EXISTS discord_storage (" +
                    "guild TEXT NOT NULL," +
                    "character TEXT NOT NULL)";
            statement.executeUpdate(sql);

            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    public static String getCharacterByGuild(String guild) {
        String character = "RuPaul";
        try {
            Statement statement = Bot.connection.createStatement();

            // Retrieve the character based on the guild
            String query = "SELECT character FROM discord_storage WHERE guild = '" + guild + "'";
            ResultSet resultSet = statement.executeQuery(query);

            // Check if there's a result and retrieve the character
            if (resultSet.next()) {
                character = resultSet.getString("character");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return character;
    }



    public static void setCharacter(String guild, String character) {
        try {
            String query = String.format("SELECT COUNT(*) AS count FROM discord_storage WHERE guild = '%s'", guild);
            Statement statement = Bot.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            int count = resultSet.getInt("count");
            resultSet.close();
            statement.close();

            if (count > 0) {
                // Guild exists, update the character
                query = String.format("UPDATE discord_storage SET character = '%s' WHERE guild = '%s'", character, guild);
                statement = Bot.connection.createStatement();
                statement.executeUpdate(query);
                statement.close();
            } else {
                // Guild does not exist, insert a new record
                query = String.format("INSERT INTO discord_storage (guild, character) VALUES ('%s', '%s')", guild, character);
                statement = Bot.connection.createStatement();
                statement.executeUpdate(query);
                statement.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        Bot.guildCharacterMap.put(Bot.builder.getGuildById(guild),character);
    }

    public static String getCharacter(Guild guild){
        return Bot.guildCharacterMap.getOrDefault(guild,"RuPaul");
    }

}
