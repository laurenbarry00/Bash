import net.dv8tion.jda.core.JDA;

public class TestCase {
    private static String input;
    private static TestResult expectedResult;
    private static TestResult actualResult;

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

    public void runTestCase(JDA api) {

        // Send command input to AvaIre as a message TODO: Implement runTestCase()
    }
}