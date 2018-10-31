package commands;

import bash.Runner;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import test.TestSuite;

import java.awt.*;

public class ListAllTestsCommand implements Command {
    private final String input = "?listalltests";
    private final String regexInput = "\\?(listalltests)";
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
     * Prints out an embed of all the available test suites.
     */
    public void execute() {
        TextChannel channel = Runner.getApi().getTextChannelById(492774370125545472L);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(new Color(43, 104, 174));
        int totalTests = 0;
        for (TestSuite suite : Runner.getTestSuiteList()) {
            embedBuilder.addField(suite.getName(), "Size: " + suite.size(), true);
            totalTests += suite.size();
        }
        embedBuilder.setFooter("Total Tests: " + totalTests, "https://image.freepik.com/free-icon/right-arrow-angle-and-horizontal-down-line-code-signs_318-53994.jpg");
        channel.sendMessage(embedBuilder.build()).queue();
    }
}
