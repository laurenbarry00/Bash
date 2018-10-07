package test;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class TestCase {
    private UUID caseUUID;
    private String input;
    private String output;
    private TestResult expectedResult;
    private TestResult actualResult;

    /**
     * Default constructor with randomized UUID. Default expected result is TestResult.PASS
     */
    public TestCase() {
        caseUUID = UUID.randomUUID();
        input = null;
        expectedResult = TestResult.PASS; // Assuming that we'd want tests to pass by default
        actualResult = null;
    }

    /**
     * Overloaded constructor to manually set all fields.
     * @param uuid The TestCase's UUID
     * @param command Input to be executed upon case being run
     * @param expected The expected result of the TestCase
     * @param actual The actual result of the TestCase
     */
    public TestCase(UUID uuid, String command, TestResult expected, TestResult actual) {
        caseUUID = uuid;
        input = command;
        expectedResult = expected;
        actualResult = actual;
    }

    /**
     * Returns a TestCase's UUID
     * @return uuid
     */
    public UUID getUUID() {
        return caseUUID;
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
     * @return 1 if successful, 0 if not
     */
    public int execute(JDA api) {
        List<TextChannel> channels = api.getTextChannelsByName("testing", true);
        MessageAction action = channels.get(0).sendMessage(getInput());
        if (action != null) { // sendMessage() returns a a MessageAction upon success
            return 1;
        }

        return 0; // Returns 1 if successful - for use in TestSuite (counting all tests ran successfully)
    }

    /**
     * Converts the TestCase to a JSONObject
     * @return JSONObject with the structure and values of the TestCase
     */
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        jo.put("uuid", caseUUID.toString());
        jo.put("input", getInput());
        jo.put("expectedResult", getExpectedResult());
        jo.put("actualResult", getActualResult());
        return jo;
    }

}