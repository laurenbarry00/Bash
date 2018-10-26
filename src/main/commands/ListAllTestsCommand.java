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

    public String getInput() {
        return input;
    }

    public String getRegexInput() {
        return regexInput;
    }

    public String getRegexOutput() {
        return regexOutput;
    }

    public void execute() {
        TextChannel channel = Runner.getApi().getTextChannelById(492774370125545472L);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(new Color(43, 104, 174));
        for (TestSuite suite : Runner.getTestSuiteList()) {
            embedBuilder.addField(suite.getName(), "Size: " + suite.size(), true);
        }
        embedBuilder.setFooter("Use `?runtest` <name> to run a specific test!", "https://i.imgur.com/evWcc3F.png");
        channel.sendMessage(embedBuilder.build()).queue();
    }
}
