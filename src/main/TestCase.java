import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONObject;
import java.util.List;

public class TestCase {
    private String input;
    private TestResult expectedResult;
    private TestResult actualResult;

    public TestCase() {
        input = null;
        expectedResult = TestResult.PASS; // Assuming that we'd want tests to pass by default
        actualResult = null;
    }

    public TestCase(String command, TestResult expected, TestResult actual) {
        input = command;
        expectedResult = expected;
        actualResult = actual;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String command) {
        input = command;
    }

    public TestResult getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(TestResult result) {
        expectedResult = result;
    }

    public TestResult getActualResult() {
        return actualResult;
    }

    public void setActualResult(TestResult result) {
        actualResult = result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (TestCase.class.isAssignableFrom(obj.getClass())) { // Other object is a TestCase
            final TestCase other = (TestCase) obj;
            if (this.getInput().equals(other.getInput()) && this.getExpectedResult() == other.getExpectedResult() && this.getActualResult() == other.getActualResult()) { // All three fields must be the same for them to be equal
                return true;
            }
        }

        return false;
    }

    public int runTestCase(JDA api) {
        List<TextChannel> channels = api.getTextChannelsByName("testing", true);
        channels.get(0).sendMessage("test").queue();
        // Send command input to AvaIre as a message TODO: Implement runTestCase()
        return 0; // Returns 1 if successful for use in TestSuite (counting all tests ran successfully)
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        jo.put("input", getInput());
        jo.put("expectedResult", getExpectedResult());
        jo.put("actualResult", getActualResult());
        return jo;
    }
}