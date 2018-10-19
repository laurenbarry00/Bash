package bash;

import commands.Command;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
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

    @Override
    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        if (author.isBot()) {
            JDA jda = Runner.getApi();
            TextChannel channel = jda.getTextChannelById(492774370125545472L);
            MessageHistory mh = channel.getHistory();
            List<Message> messageList = mh.getRetrievedHistory();
            for (TestSuite suite : Runner.getTestSuiteList()) {
                for (int i = 0; i < suite.size(); i++) {
                    TestCase testCase = suite.get(i);
                    for (Message m : messageList) {
                        if (m.getContentDisplay().equalsIgnoreCase(testCase.getInput())) {
                            for (int j = 0; j < messageList.size(); j++) {
                                if (m.getContentDisplay().matches(testCase.getOutput())) {
                                    testCase.setExpectedResult(TestResult.PASS);
                                }
                            }
                        }
                    }
                }
                suite.evaluateTests();
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
        Runner.getApi().getPresence().setPresence(OnlineStatus.OFFLINE, false);
    }
}