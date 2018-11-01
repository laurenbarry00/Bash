package bash;

import commands.Command;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Message;
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

        if (author.getName().equalsIgnoreCase("AvaIre")) { // Potential test result response
            for (int i = 0; i < Runner.getTestSuiteList().size(); i++) {
                TestSuite suite = Runner.getTestSuiteList().get(i);
                for (int j = 0; j < suite.size(); j++) {
                    TestCase testCase = suite.get(i);
                    String regexPattern = testCase.getOutput();

                    if (message.getEmbeds().size() > 0) {
                        String embedDescription = message.getEmbeds().get(0).getDescription();
                        if (embedDescription.matches(regexPattern)) {
                            testCase.setActualResult(TestResult.PASS);
                        } else {
                            testCase.setActualResult(TestResult.FAIL_UNEXPECTED_OUTPUT);
                        }
                    } else {
                        if (message.getContentDisplay().matches(regexPattern)) {
                            testCase.setActualResult(TestResult.PASS);
                        } else {
                            testCase.setActualResult(TestResult.FAIL_UNEXPECTED_OUTPUT);
                        }
                    }
                }
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
