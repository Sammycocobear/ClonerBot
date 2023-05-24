package me.sammy.Events;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import me.sammy.Bot;
import me.sammy.SQLManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.HashMap;
import java.util.Map;


public class SlashEvent extends ListenerAdapter {

    private Map<User,String> pastMessage = new HashMap<>();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("talk")) {
            if (event.getOption("message") == null) {
                event.reply("You must say something to talk to RuPaul").queue();
                return;
            }

            String containsPastMessage = " there has been a past message in this conversation, " +
                    "you might want to use it to decide a response to this message so the conversation can be more fluid. " +
                    "Here is the past message: " + pastMessage.getOrDefault(event.getUser(), "");

            OpenAiService service = new OpenAiService(Bot.OPENAI_KEY);

            CompletionRequest completionRequest = CompletionRequest.builder()
                    .prompt(String.format("As an expert in imitating human conversations, " +
                                    "please respond as %s, the character/personality you want to clone, " +
                                    "to the following input:%s%s",
                            SQLManager.getCharacter(event.getGuild()),
                            event.getOption("message"),
                            pastMessage.containsKey(event.getUser()) ? containsPastMessage : ""))
                    .model("text-davinci-003")
                    .maxTokens(200)
                    .build();

            event.deferReply().queue();
            event.getHook().editOriginal(service.createCompletion(completionRequest).getChoices().get(0).getText()).queue();

            pastMessage.put(event.getUser(), event.getOption("message").getAsString());
        } else if (event.getName().equals("clone")) {
            OptionMapping cloner = event.getOption("cloner");
            if (cloner == null) {
                event.reply("You must say something to talk to RuPaul").queue();
                return;
            }

            String clonerNickname = cloner.getAsString().substring(0, Math.min(32, cloner.getAsString().length()));
            event.getGuild().modifyNickname(event.getGuild().getMemberById("1108575904147447919"), clonerNickname).queue();

            SQLManager.setCharacter(event.getGuild().getId(), event.getOption("cloner").getAsString());
            event.reply(String.format("Set the character as %s", SQLManager.getCharacter(event.getGuild()))).queue();
        }
    }



}
