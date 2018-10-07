package commands;

public interface Command {
    String input = null;
    String regexInput = null; // Regex format for comparison purposes
    String regexOutput = null;

    public String getAsRegexString();

    public void execute();
}
