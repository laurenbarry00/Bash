package bash;

import com.google.gson.Gson;
import commands.Command;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import org.json.JSONObject;
import test.TestSuite;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class EventHandler extends ListenerAdapter {

    /**
     * Executes when the bot has loaded fully.
     * @param event ReadyEvent
     */
    @Override
    @SubscribeEvent
    public void onReady(ReadyEvent event) {
        Runner.log.info("Bash logged into " + event.getJDA().getGuilds().size() + " Discord server(s) upon startup.");
        Runner.log.info("Ping: " + event.getJDA().getPing() + " at " + LocalDateTime.now());
    }

    @Override
    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        if (author.isBot()) {
            try {
                File testSuiteFile = new File("message_log.json");
                if (!testSuiteFile.exists()) {
                    testSuiteFile.createNewFile();
                }
                FileWriter writer = new FileWriter(testSuiteFile);
                if (message.getEmbeds() != null) {
                    JSONObject jo = message.getEmbeds().get(0).toJSONObject();
                    writer.append(jo.toString());
                } else {
                    writer.append(message.getContentRaw());
                }
                writer.flush();
            } catch (IOException e) {
                Runner.getLogger().error("Could not save message " + message.getContentDisplay() + " to JSON file!");
                e.printStackTrace();
            }
        } else { // User is human, who presumably wants to use commands to control Bash.
             List<Command> commandsList = Runner.getCommandsList();
             for (int i = 0; i < commandsList.size(); i++) {
                 Command current = commandsList.get(i);

                 if (message.getContentDisplay().matches(current.getRegexInput())) {
                     current.execute();
                     break;
                 }
             }
        }
    }

    /**
     * Saves TestSuite as JSON upon disconnect from API.
     * @param event DisconnectEvent
     */
    @Override
    @SubscribeEvent
    public void onDisconnect(DisconnectEvent event) {
        Runner.saveJsonTestSuite();
        Runner.getApi().getPresence().setPresence(OnlineStatus.OFFLINE, false);
    }
}