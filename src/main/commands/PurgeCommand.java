package commands;

import bash.Runner;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.pagination.MessagePaginationAction;

public class PurgeCommand implements Command {
    private final String input = "?purge";
    private final String regexInput = "\\?(purge)";
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
     * Attempts to delete all messages in the channel.
     */
    public void execute() {
        TextChannel channel = Runner.getApi().getTextChannelById(492774370125545472L);
        MessagePaginationAction history = channel.getIterableHistory();
        int messagesDeleted = 0;
        for (Message m : history) {
            m.delete().queue();
            messagesDeleted++;
        }
        Runner.getLogger().info("Purged " + messagesDeleted + " messages.");
    }
}
