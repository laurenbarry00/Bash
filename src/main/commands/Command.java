package commands;

public interface Command {

    String getInput();

    String getRegexInput();

    String getRegexOutput();

    void execute();
}
