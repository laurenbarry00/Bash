import commands.Command;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import test.TestSuite;

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
            if (author.getName().equalsIgnoreCase("AvaIre")) { // We only want to respond to AvaIre, other bots don't matter.
                TestSuite suite = Runner.suite;

                for (int i = 0; i < suite.size(); i++) {
                    // Use fancy RegEx to compare expected output String to actual output received here
                }
            } else {
                return; // Ignore all input from bots other than AvaIre
            }
        } else { // User is human, who presumably wants to use commands to control Bash.
             List<Command> commandsList = Runner.getCommandsList();
             for (int i = 0; i < commandsList.size(); i++) {
                 Command current = commandsList.get(i);

                 if (message.toString().matches(current.getAsRegexString())) {
                     current.execute();
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
    }
}