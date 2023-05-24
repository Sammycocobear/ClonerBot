package me.sammy.Events;

import me.sammy.Bot;
import me.sammy.SQLManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class BotLoadInformation implements EventListener {

    @Override
    public void onEvent(GenericEvent event) {
        if (!(event instanceof ReadyEvent)) return;
        for (Guild guild : Bot.builder.getGuilds()){
            String character = SQLManager.getCharacterByGuild(guild.getId());
            Bot.guildCharacterMap.put(guild,character);
            guild.modifyNickname(guild.getMemberById("1108575904147447919"),character).queue();
        }
    }
}
