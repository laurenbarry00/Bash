import java.util.ArrayList;

public class TestSuite {
    private ArrayList<TestCase> cases;

    public void addTestCase(TestCase testCase) {
        if (this.searchSuite(testCase) != -1) {
            cases.add(testCase);
        }
    }

    public boolean removeTestCase(TestCase testCase) {
        if (searchSuite(testCase) != -1) {
            removeTestCase(searchSuite(testCase));
            return true;
        }
        return false;
    }

    public TestCase getTestCase(int index) {
        return cases.get(index);
    }

    public void replaceTestCase(int index, TestCase testCase) {
        cases.set(index, testCase);
    }

    private boolean removeTestCase(int index) {
        try {
            cases.remove(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            Runner.log.error("Tried to remove a test case using an index that was out of bounds!");
            return false;
        }
        return true;
    }

    public int searchSuite(TestCase target) {
        for (int i = 0; i < cases.size(); i++) {
            if (cases.get(i).equals(target)) {
                return i;
            }
        }
        return -1;
    }

    public void runAllTests() {
        // Loop through all test cases and run them. TODO: Implement runAllTests()
    }
}
