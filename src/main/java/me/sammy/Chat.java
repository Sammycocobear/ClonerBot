package me.sammy;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private static final List<Chat> chats = new ArrayList<>();
    private final User user;
    private final Guild guild;
    private final String message;

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

    public static List<Chat> getChats(){
        return chats;
    }

    public static void addChat(Chat chat){
        chats.add(chat);
    }

    public static void removeChat(int i){
        chats.remove(i);
    }

    public static String getMessageFromUserInGuild(Guild guild, User user){
        for (Chat chat : chats){
            if (chat.getGuild().equals(guild) && chat.getUser().equals(user)){
                return chat.getMessage();
            }
        }
        return null;
    }
}
