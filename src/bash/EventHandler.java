package bash;

import commands.Command;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import test.TestCase;
import test.TestResult;
import test.TestSuite;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

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

    /**
     * EventHandler for messages received
     * @param event MessageReceivedEvent
     */
    @Override
    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();

        if (author.getName().equalsIgnoreCase("AvaIre")) { // // author.getName().equalsIgnoreCase("AvaIre")
            String id = message.getId();
            List<Message> history = event.getChannel().getHistoryBefore(id, 1).complete().getRetrievedHistory(); // Gets the message directly before the received message
            Message testMessage = history.get(0); // Message containing the test
            String testText;
            if (testMessage.getEmbeds().size() > 0) {
                testText = testMessage.getEmbeds().get(0).getDescription();
            } else {
                testText = testMessage.getContentRaw();
            }

            TestCase thisTestCase = null;
            for (TestSuite suite : Runner.getTestSuiteList()) {
                for (int i = 0; i < suite.size(); i++) {
                    TestCase current = suite.get(i);
                    if (current.getInput().equals(testText)) { // Identified which test case is being executed
                        thisTestCase = current;
                    }
                }
            }

            if (testText.matches(thisTestCase.getOutput())) {
                thisTestCase.setActualResult(TestResult.PASS);
            } else {
                thisTestCase.setActualResult(TestResult.FAIL_UNEXPECTED_OUTPUT);
            }
        } else { // User is human, who presumably wants to use commands to control Bash.
             List<Command> commandsList = Runner.getCommandsList();
             for (Command current : commandsList) {
                 if (message.getContentDisplay().matches(current.getRegexInput())) {
                     Runner.getLogger().info(current.getInput() + " executed by " + author.getName() + "#" + author.getDiscriminator());
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
        Runner.getApi().getPresence().setPresence(OnlineStatus.OFFLINE, false);
    }
}
