package test;

import bash.Runner;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.pagination.MessagePaginationAction;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestSuite {
    private String name;
    private ArrayList<TestCase> cases;
    ArrayList<TestCase> passed;
    ArrayList<TestCase> failed;

    /**
     * Default constructor that initializes a new TestSuite object. Initial capacity is 10.
     */
    public TestSuite() {
        this.cases = new ArrayList<>(10);
        passed = new ArrayList<>();
        failed = new ArrayList<>();
    }

    /**
     * Constructor to initialize a TestSuite and automatically add a number of TestCases.
     * @param c A flexible number of TestCases to be initially added.
     */
    public TestSuite(TestCase... c) {
        for (TestCase current : c) {
            cases.add(current);
        }
        passed = new ArrayList<>();
        failed = new ArrayList<>();
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
     * Returns the number of TestCases in the suite
     * @return Size of cases
     */
    public int size() {
        return cases.size();
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
     * Sets the Test Suite's name (the name of the command being tested)
     * @param name Suite name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the Test Suite
     * @return name
     */
    public String getName() {
        return name;
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
     * @param api JDA API
     * @param log SLF4J logger
     */
    public void runAllTests(JDA api, Logger log) {
        int testsRan = 0;
        try {
            for (int i = 0; i < cases.size(); i++) {
                TestCase current = cases.get(i);
                TimeUnit.SECONDS.sleep(3); // for rate limiting
                testsRan += current.execute(api);


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.info("Ran " + testsRan + " out of " + size() + " tests.");
        }

        TextChannel channel = Runner.getApi().getTextChannelById(492774370125545472L);
        channel.sendMessage("Ran " + testsRan + " out of " + size() + " tests.").queue();

        // Clearing out spam!
        MessagePaginationAction history = channel.getIterableHistory();
        int deletedMessages = 0;
        for (Message m : history) {
            if (m.getAuthor().getName().equalsIgnoreCase("AvaIre")) {
                m.delete().queue();
                deletedMessages++;
            } else if (m.getAuthor().getName().equalsIgnoreCase("Bash")) {
                if (m.getEmbeds().size() > 0) {
                    if (m.getEmbeds().get(0).getFooter() != null) {
                        MessageEmbed.Footer foot = m.getEmbeds().get(0).getFooter();
                        if (!foot.getText().equalsIgnoreCase("Bash Test Report")) {
                            m.delete().queue();
                            deletedMessages++;
                        }
                    }
                } else {
                    m.delete().queue();
                    deletedMessages++;
                }
            }
        }
        log.info("Purged " + deletedMessages + " messages after running tests.");
    }

    /**
     * Evaluates whether or not each test passed or failed.
     */
    private void evaluateTests() {
        for (TestCase testCase : cases) {
            if (testCase.getExpectedResult() == TestResult.PASS) {
                passed.add(testCase);
            } else {
                failed.add(testCase);
            }
        }
    }

    /**
     * Formats the test results in an Embed and prints to the text channel.
     */
    private void printTestResults() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(new Color(43, 104, 174));
        builder.setTitle(name + " Test Report");
        builder.setFooter("Bash Test Report", "https://us.123rf.com/450wm/alonastep/alonastep1610/alonastep161000879/66216004-tick-sign-element-green-checkmark-icon-isolated-on-white-background-simple-mark-graphic-design-ok-bu.jpg?ver=6");

        Date timestamp = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        builder.addField("Timestamp", sdf.format(timestamp), false);

        builder.addField("Passed: ", passed.size() + "/" + size(), false);
        for (TestCase testCase : failed) {
            builder.addField("Failed: ", testCase.getInput(), false);
        }
        TextChannel channel = Runner.getApi().getTextChannelById(492774370125545472L); // #testing channel.
        channel.sendMessage(builder.build()).queue();
    }

    /**
     * Evaluates the test results and prints to the text channel.
     */
    public void printReport() {
        evaluateTests();
        printTestResults();
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