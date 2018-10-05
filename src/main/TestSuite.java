import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestSuite {
    private ArrayList<TestCase> cases;


    /**
     * Default constructor that initializes a new TestSuite object. Initial capacity is 10.
     */
    public TestSuite() {
        this.cases = new ArrayList<>(10);
    }

    /**
     * Constructor to initialize a TestSuite and automatically add a number of TestCases.
     * @param c A flexible number of TestCases to be initially added.
     */
    public TestSuite(TestCase... c) {
        for (TestCase current : c) {
            cases.add(current);
        }
    }

    /**
     * Adds TestCases to the TestSuite object, while checking for duplicates.
     * @param c A flexible number of TestCases to be added.
     */
    public void add(TestCase... c) {
        for (TestCase current : c) {
            if (search(current) == -1) {
                this.cases.add(current);
            }
        }

    }

    /**
     * Removes a specified TestCase from the TestSuite. Searches the TestSuite and then removes based on the object's index.
     * @param testCase TestCase to be removed from the TestSuite.
     * @return True if the TestCase was located and successfully removed.
     */
    public boolean remove(TestCase testCase) {
        int index = search(testCase);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    /**
     * Internal private method to remove a TestCase based on its index.
     * @param index The index at which the item to be removed is located.
     */
    private void remove(int index) {
        cases.remove(index);
    }

    /**
     * Finds and retrieves a TestCase object for a given index.
     * @param index The index of the object that is being retrieved.
     * @return Returns the TestCase object found.
     */
    public TestCase get(int index) {
        return cases.get(index);
    }

    /**
     * Replaces a TestCase at a given interval with a different TestCase.
     * @param index The index we are replacing.
     * @param testCase The new TestCase that will be placed into the TestSuite.
     */
    public void replace(int index, TestCase testCase) {
        cases.set(index, testCase);
    }

    /**
     * Searches for a given TestCase and returns the index of its location.
     * @param target The TestCase that is being searched for.
     * @return The index at which the target TestCase is found. Returns -1 if not found.
     */
    public int search(TestCase target) {
        for (int i = 0; i < cases.size(); i++) {
            if (cases.get(i).equals(target)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Loops through all TestCases in the TestSuite and executes them. Counts and confirms the number of tests executed.
     */
    public void runAllTests() {
        int testsRan = 0;
        for (int i = 0; i < cases.size(); i++) {
            TestCase current = cases.get(i);
            testsRan += current.execute(Runner.api);
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.addField("Tests Ran:", String.valueOf(testsRan), false);
        builder.setTimestamp(LocalDateTime.now()); // Display the number of tests run after completion

        List<TextChannel> channels = Runner.api.getTextChannelsByName("testing", true);
        channels.get(0).sendMessage(builder.build()).queue();
        Runner.log.info("Successfully ran " + testsRan + "  out of " + cases.size() + " test cases in suite."); // Log the number of tests run
    }

    /**
     * Converts the current TestSuite to a JSONObject, to enable storage and retrieval of TestSuites from JSON files.
     * @return JSONObject of the TestSuite
     */
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        for (int i = 0; i < cases.size(); i++) {
            TestCase current = cases.get(i);
            jo.put(current.getInput(), current.toJsonObject());
        }
        return jo;
    }
}
