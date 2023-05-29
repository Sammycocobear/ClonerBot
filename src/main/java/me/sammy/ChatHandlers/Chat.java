package me.sammy.ChatHandlers;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class Chat {
    private final User user;
    private final Guild guild;
    private String message;
    private String response;
    public Chat(User user, Guild guild, String message, String response){
        this.user = user;
        this.guild = guild;
        this.message = message;
        this.response = response.replace("\n","");
    }

    public Chat(User user, Guild guild, String message){
        this.user = user;
        this.guild = guild;
        this.message = message;
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

    public void setResponse(String newR) {
        this.response = newR;
    }

    public void setMessage(String newR) {
        this.message = newR;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "user=" + user +
                ", guild=" + guild +
                ", message='" + message + '\'' +
                ", response='" + response + '\'' +
                '}';
    }

    //        this.response = response.replace("\n","");
}
