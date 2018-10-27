package commands;

import bash.Runner;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import test.TestSuite;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// This can't inherit from Command because we need a custom execute() method.
public class RunTestCommand implements Command {
    private final String input = "?runtest <test>";
    private final String regexInput = "\\?(runtest)\\s+\\!\\w+";
    private final String regexOutput = null;

    /**
     * Retrieves the Command input in its display format
     * @return Input as disaplyed normally
     */
    @Override
    public String getInput() {
        return input;
    }

    /**
     * Returns the RegEx pattern for the input
     * @return Output as Regex string
     */
    @Override
    public String getRegexInput() {
        return regexInput;
    }


    /**
     * Returns the RegEx pattern for the output
     * @return Ouput in RegEx format
     */
    @Override
    public String getRegexOutput() {
        return regexOutput;
    }

    /**
     * Runs a specific test suite.
     */
    @Override
    public void execute() {
        TextChannel channel = Runner.getApi().getTextChannelById(492774370125545472L);
        List<Message> history = channel.getHistory().retrievePast(1).complete();
        Message message = history.get(0);
        String suiteName = "";

        Pattern pattern = Pattern.compile("\\!\\w+");
        Matcher matcher = pattern.matcher(message.getContentDisplay());
        while (matcher.find()) {
            suiteName = matcher.group(0);
        }

        for (TestSuite suite : Runner.getTestSuiteList()) {
            if (suiteName.equalsIgnoreCase(suite.getName())) {
                suite.runAllTests(Runner.getApi(), Runner.getLogger());

            }
        }
    }
}
