package me.sammy.ChatHandlers;

import me.sammy.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class Conversation { //All of the chats of a user
    private final List<Chat> conversation = new ArrayList<>();
    private final User user;
    public Conversation(User user, Chat chat){
        this.user = user;
        addChat(chat);
    }

    public User getUser(){
        return user;
    }

    public Conversation addChat( Chat chat){
        if (conversation.size() >= 5){
            conversation.remove(0);
        }
        conversation.add(chat);
        return this;
    }

    public List<Chat> getChats(){
        return conversation;
    }


    public String getFormmatedConversation(Guild guild) {
        StringBuilder returner = new StringBuilder();
        String character = Bot.guildCharacterMap.get(guild);
        for (int i = 0; i < conversation.size(); i++) {
            returner.append("User:").append(conversation.get(i).getMessage()).append("\n");
            returner.append(character).append(":").append(conversation.get(i).getResponse()).append("\n");

        }
        return returner.toString();
    }
}
