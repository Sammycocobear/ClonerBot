package me.sammy.Events;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import me.sammy.Bot;
import me.sammy.ChatHandlers.Chat;
import me.sammy.ChatHandlers.Conversation;
import me.sammy.ChatHandlers.ConversationManager;
import me.sammy.SQLManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.ArrayList;
import java.util.Arrays;


public class SlashEvent extends ListenerAdapter {

    OpenAiService service = new OpenAiService(Bot.OPENAI_KEY);

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        User user = event.getUser();

        if (event.getName().equals("talk")) {
            if (event.getOption("message") == null) {
                event.reply("You must say something to talk to RuPaul").queue();
                return;
            }

            String prompt = event.getOption("message").getAsString();

            Conversation conversation = ConversationManager.getConversation(guild,user);
            String message = null;
            if (conversation != null){ //there is a conversation for this user in this guild loaded
                message = conversation.getFormmatedConversation(guild);
            }



            String containsPastMessage = "There is already a conversation going on between you and this user. " +
                    "Try to use the conversation that has already been going on to make the best response to the the prompt given to you by the user." +
                    "However DO NOT INCLUDE ANY PART OF THE PREVIOUS CONVERSATION IN YOUR REPLY. " +
                    "THE CONVERSATION IS STORED IN DATA AND YOU DO NOT NEED TO REPEAT IT PUT THE RESPONSE!!!" +
                    "OR TALK IN CONVERSATION FORMAT. " +
                    "Here is the conversation\n" + message;
            event.deferReply().queue();
            
            CompletionRequest completionRequest = CompletionRequest.builder()
                    .prompt(String.format("As an expert in imitating human conversations, " +
                                    "please respond as %s, the character/personality you are going to clone, " +
                                    "to the following input:%s. %s",
                            SQLManager.getCharacter(event.getGuild()),
                            prompt,
                            message != null ? containsPastMessage : ""))
                    .model("text-davinci-003")
                    .maxTokens(200)
                    .temperature(.8)
                    .stop(Arrays.asList("SAUHNASDFUIJBDNIHASJFBIHDASJKBFGIHSDGBVSHDfv"))
                    .frequencyPenalty(2.0)
                    .presencePenalty(2.0)
                    .build();


            System.out.println(String.format("As an expert in imitating human conversations, " +
                            "please respond as %s, the character/personality you are going to clone, " +
                            "to the following input:%s. %s",
                    SQLManager.getCharacter(event.getGuild()),
                    prompt,
                    message != null ? containsPastMessage : ""));

            CompletionResult completionResult = service.createCompletion(completionRequest);
            System.out.println(completionResult);
            String response = completionResult.getChoices().get(0).getText();
            System.out.println(response);
            response = extractSubstring(response);
            System.out.println(response);
            if (response.isEmpty()){
                event.getHook().editOriginal("There has been an unkown error, try repeating it again").queue();
                return;
            }

            if (message == null){
                Chat chat = new Chat(user,guild,prompt,response);
                ConversationManager.addConversation(guild,new Conversation(user,chat));
            }else{
                conversation.addChat(new Chat(user,guild,prompt,response));
            }
            event.getHook().editOriginal(response).queue();

        } else if (event.getName().equals("clone")) {
            OptionMapping clone = event.getOption("cloner");
            if (clone == null) {
                event.reply("You must say something to talk to RuPaul").queue();
                return;
            }
            String cloner = event.getOption("cloner").getAsString();

            event.deferReply().queue();

            String clonerNickname = cloner.substring(0, Math.min(32, cloner.length()));
            event.getGuild().modifyNickname(event.getGuild().getMemberById("1108575904147447919"), clonerNickname).queue();

            SQLManager.setCharacter(event.getGuild().getId(), event.getOption("cloner").getAsString());

            //Clears all data
            ConversationManager.clearConversations(guild);

            event.getHook().editOriginal(String.format("Set the character as %s", SQLManager.getCharacter(event.getGuild()))).queue();
        }
    }

    public static String extractSubstring(String input) {
        int startIndex = input.indexOf(":");
        startIndex = input.indexOf(":",startIndex + 1);
        if (startIndex >= 0 && startIndex < input.length() - 1) {
            return input.substring(startIndex + 1).trim();
        } else {
            return input;
        }
    }



}
