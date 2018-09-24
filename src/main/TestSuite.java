import org.json.JSONObject;

import java.util.ArrayList;

public class TestSuite {
    private ArrayList<TestCase> cases;


    public TestSuite() {
        this.cases = new ArrayList<>(10);
    }

    public TestSuite(TestCase... c) {
        for (TestCase current : c) {
            cases.add(current);
        }
    }

    public void add(TestCase... c) {
        for (TestCase current : c) {
            if (search(current) == -1) {
                this.cases.add(current);
            }
        }

    }

    public boolean remove(TestCase testCase) {
        if (search(testCase) != -1) {
            remove(search(testCase));
            return true;
        }
        return false;
    }

    private boolean remove(int index) {
        cases.remove(index);
        return true;
    }

    public TestCase get(int index) {
        return cases.get(index);
    }

    public void replace(int index, TestCase testCase) {
        cases.set(index, testCase);
    }

    public int search(TestCase target) {
        for (int i = 0; i < cases.size(); i++) {
            if (cases.get(i).equals(target)) {
                return i;
            }
        }

        return -1;
    }

    public void runAllTests() {
        int testsRan = 0;
        for (int i = 0; i < cases.size(); i++) {
            TestCase current = cases.get(i);
            testsRan += current.runTestCase(Runner.api);
        }
        Runner.log.info("Successfully ran " + testsRan + "  out of " + cases.size() + " test cases in suite.");
    }

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();
        for (int i = 0; i < cases.size(); i++) {
            TestCase current = cases.get(i);
            jo.put(current.getInput(), current.toJsonObject());
        }
        return jo;
    }
}
