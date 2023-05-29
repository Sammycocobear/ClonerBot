package me.sammy.Events;

import me.sammy.ChatHandlers.Chat;
import me.sammy.ChatHandlers.Conversation;
import me.sammy.ChatHandlers.ConversationManager;
import me.sammy.GBTAPIHandler;
import me.sammy.SQLManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;


public class SlashEvent extends ListenerAdapter {

    boolean testMode = false;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (testMode){
            if (!event.getGuild().getId().equals("1112270118706225192")){
                event.reply("This bot is in test mode, wait for sammy to fix some shi up").queue();
                return;
            }
        }

        Guild guild = event.getGuild();
        User user = event.getUser();

        if (event.getName().equals("talk")) {
            if (event.getOption("message") == null) {
                event.reply("You must say something to talk to RuPaul").queue();
                return;
            }
            event.deferReply().queue();

            String prompt = event.getOption("message").getAsString();



            /*
            OpenAiService service = new OpenAiService(Bot.OPENAI_KEY);


            String containsPastMessage = "There is already a conversation going on between you and this user. " +
                    "Try to use the conversation that has already been going on to make the best response to the the prompt given to you by the user." +
                    "However DO NOT INCLUDE ANY PART OF THE PREVIOUS CONVERSATION IN YOUR REPLY. " +
                    "THE CONVERSATION IS STORED, YOU DO NOT NEED TO REPEAT IT PUT THE RESPONSE" +
                    "OR TALK IN CONVERSATION FORMAT. " +
                    "Here is the conversation\n" + message;
            event.deferReply().queue();


            String completionPrompt = String.format("As an expert in imitating human conversations, " +
                            "please respond as %s, the character/personality you are going to clone, " +
                            "to the following input:%s. %s",
                    SQLManager.getCharacter(event.getGuild()),
                    prompt,
                    message != null ? containsPastMessage : "") + "\n\n";

            CompletionRequest completionRequest = CompletionRequest.builder()
                    .prompt(completionPrompt)
                    .model("gbt-3.5-turbo-0301")
                    .temperature(.8)
                    .maxTokens(300)
                    .frequencyPenalty(2.0)
                    .presencePenalty(2.0)
                    .stop(Arrays.asList("ZZZZZZZZZZZZZZZZZZZZZZZZZZ"))
                    .build();



            CompletionResult completionResult = service.createCompletion(completionRequest);
            event.getChannel().sendMessage(completionPrompt).queue();
            event.getChannel().sendMessage(completionRequest.toString()).queue();
            String response = completionResult.getChoices().get(0).getText();
            response = extractSubstring(response);

             */

            Conversation conversation = ConversationManager.getConversation(guild,user);

            Chat currentChat = new Chat(user,guild,prompt);
            String response = null;


            if (conversation == null){
                conversation = new Conversation(guild, user,currentChat);
            }else{
                conversation.addChat(currentChat);
            }

            try {
                response = GBTAPIHandler.chat(conversation,currentChat);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (response == null){
                event.getHook().editOriginal("There has been an unknown error, try repeating it again").queue();
                return;
            }else if (response.equals("RATELIMITEDSAMMYLOOK")){
                event.getHook().editOriginal("The bot is being used too fast, give it a break for a second.").queue();
                return;
            }

            currentChat.setResponse(response);

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
