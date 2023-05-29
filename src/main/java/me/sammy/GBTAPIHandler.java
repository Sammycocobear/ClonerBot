package me.sammy;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import me.sammy.ChatHandlers.Chat;
import me.sammy.ChatHandlers.Conversation;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GBTAPIHandler {
    private static final String URL = "https://api.openai.com/v1/chat/completions";

    public static final String s = "{\n" +
            "  \"model\": \"gpt-3.5-turbo\",\n" +
            "  \"messages\": %s,\n" +
            "  \"temperature\": 0.7\n" +
            "}";



//this code is going to make ms kms
    public static String chat(Conversation conversation, Chat curr) throws Exception{
        String messages = conversation.getFormattedConversation(curr);
        String postBody = String.format(s,messages);

        var thing = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type","application/json")
                .header("Authorization", String.format("Bearer %s",Bot.OPENAI_KEY))
                .POST(HttpRequest.BodyPublishers.ofString(postBody))
                .build();

        var client = HttpClient.newHttpClient();
        var response = client.send(thing, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 429){
            return "RATELIMITEDSAMMYLOOK";
        }
        if (response.statusCode() != 200){
            return null;
        }
        JSONObject jsonObject = new JSONObject(response.body());
        JSONArray choicesArray = jsonObject.getJSONArray("choices");
        JSONObject choiceObject = choicesArray.getJSONObject(0);
        JSONObject messageObject = choiceObject.getJSONObject("message");
        return messageObject.getString("content");
    }
}
