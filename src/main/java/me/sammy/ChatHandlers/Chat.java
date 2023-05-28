package me.sammy.ChatHandlers;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class Chat {
    private final User user;
    private final Guild guild;
    private final String message;
    private final String response;
    public Chat(User user, Guild guild, String message, String response){
        this.user = user;
        this.guild = guild;
        this.message = message;
        this.response = response.replace("\n","");
    }

    public User getUser() {
        return user;
    }

    public Guild getGuild() {
        return guild;
    }

    public String getMessage() {
        return message;
    }

    public String getResponse() {
        return response;
    }

}
