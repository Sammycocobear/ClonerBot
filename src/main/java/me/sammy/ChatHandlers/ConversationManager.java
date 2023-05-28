package me.sammy.ChatHandlers;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationManager { //All of the conversations in a guild
    private static final Map<Guild, List<Conversation>> conversations = new HashMap();

    public static void addConversation(Guild guild, Conversation conversation){
        if (conversations.containsKey(guild)){
            conversations.get(guild).add(conversation);
            return;
        }

        conversations.put(guild, new ArrayList<>());
        addConversation(guild,conversation); //this will work
    }


    public static void updateConversation(Guild guild, User user, Chat message){
        for (Guild guilds : conversations.keySet()){
            //In the right guild
            if (!guilds.equals(guild)) continue;
            //Is the correct user
            Guild g = guilds;
            for (Conversation convo : conversations.get(g)){
                if (convo.getUser().equals(user)){
                    convo.addChat(message);
                    return;
                }
            }

        }
    }

    public static Conversation getConversation(Guild guild, User user) {
        for (Guild guilds : conversations.keySet()){
            if (!guilds.equals(guild)) continue;
            Guild g = guilds;
            for (Conversation convo : conversations.get(g)){
                if (convo.getUser().equals(user)){
                    return convo;
                }
            }
        }
        return null;
    }

    public static void clearConversations(Guild guild) {
        for (Guild guilds : conversations.keySet()) {
            if (guilds.equals(guild)){
                conversations.get(guilds).clear();
                break;
            }
        }
    }

}
