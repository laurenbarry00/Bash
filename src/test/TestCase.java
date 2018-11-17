package test;

import com.google.gson.JsonObject;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

import java.util.List;

public class TestCase {
    private String caseName;
    private String input;
    private String output;
    private TestResult expectedResult;
    private TestResult actualResult;

    /**
     * Default constructor with randomized UUID. Default expected result is TestResult.PASS
     */
    public TestCase() {
        input = null;
        expectedResult = TestResult.PASS; // Assuming that we'd want tests to pass by default
        actualResult = null;
    }

    /**
     * Overloaded constructor to manually set all fields.
     * @param title Command name
     * @param output Regex pattern to match output
     * @param command Input to be executed upon case being run
     * @param expected The expected result of the TestCase
     * @param actual The actual result of the TestCase
     */
    public TestCase(String title, String command, String output, TestResult expected, TestResult actual) {
        caseName = title;
        input = command;
        this.output = output;
        expectedResult = expected;
        actualResult = actual;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseName() {
        return caseName;
    }

    /**
     * Returns a TestCase's command input
     * @return input
     */
    public String getInput() {
        return input;
    }

    /**
     * Modifies the TestCase input to be command
     * @param command The TestCase input
     */
    public void setInput(String command) {
        input = command;
    }

    /**
     * Retrieves the string of the expected output
     * @return expected output (as string)
     */
    public String getOutput() {
        return output;
    }

    /**
     * Set a string as the expected output
     * @param output String expected output
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * Retrieves the TestCase expected result
     * @return expectedResult
     */
    public TestResult getExpectedResult() {
        return expectedResult;
    }

    /**
     * Sets the TestCase's expected result
     * @param result Expected result enum
     */
    public void setExpectedResult(TestResult result) {
        expectedResult = result;
    }

    /**
     * Retrieves the TestResult's actual result
     * @return actualResult
     */
    public TestResult getActualResult() {
        return actualResult;
    }

    /**
     * Sets the TestCase's actual result
     * @param result Enum of actual result
     */
    public void setActualResult(TestResult result) {
        actualResult = result;
    }

    /**
     * Compares an object to the TestCase
     * @param obj The item being compared
     * @return true if the item is a TestCase with the same value for all fields
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (TestCase.class.isAssignableFrom(obj.getClass())) { // Other object must be a TestCase
            final TestCase other = (TestCase) obj;
            if (this.getInput().equals(other.getInput()) && this.getExpectedResult() == other.getExpectedResult() && this.getActualResult() == other.getActualResult()) { // All three fields must be the same for them to be equal
                return true;
            }
        }

        return false;
    }

    /**
     * Runs the TestCase by sending a command to the server's #testing channel.
     * @param api JDA instance
     * @return 0 if successful
     */
    public int execute(JDA api) {
        List<TextChannel> channels = api.getTextChannelsByName("testing", true);
        channels.get(0).sendMessage(getInput()).queue();
        MessageAction action = channels.get(0).sendMessage(getInput());
        if (action == null) { // sendMessage() returns a a MessageAction upon success
            return 0;
        }

        return 1; // Returns 1 if successful - for use in TestSuite (counting all tests ran successfully)
    }

    /**
     * Converts the TestCase to a JSONObject
     * @return JSONObject with the structure and values of the TestCase
     */
    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.addProperty("input", getInput());
        jo.addProperty("expectedResult", getExpectedResult().toString());
        jo.addProperty("actualResult", getActualResult().toString());
        return jo;
    }

}