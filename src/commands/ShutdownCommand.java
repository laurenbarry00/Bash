package commands;

import bash.Runner;
import net.dv8tion.jda.core.OnlineStatus;

public class ShutdownCommand implements Command {
    private final String input = "?shutdown";
    private final String regexInput = "\\?(shutdown)"; // Regex format for comparison purposes
    private final String regexOutput = null; // An output String in the format of display is not necessary, because we only need the output for comparison, hence RegEx

    /**
     * Retrieves the Command input in its display format
     * @return Input as disaplyed normally
     */
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
     * Executes all tests in Bash's TestSuite
     */
    public void execute() {
        Runner.getApi().getPresence().setPresence(OnlineStatus.OFFLINE, true);
        Runner.getApi().shutdownNow();
    }
}