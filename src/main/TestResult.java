public enum TestResult {
    PASS,
    FAIL_UNEXPECTED_OUTPUT,
    FAIL_OTHER;

    // Other types of test results?
    static TestResult getTestResultFromString(String s) {
        TestResult result = null;
        switch(s) {
            case "PASS":
                result = PASS;
                break;
            case "FAIL_UNEXPECTED_OUTPUT":
                result = FAIL_UNEXPECTED_OUTPUT;
                break;
            case "FAIL_OTHER":
                result = FAIL_OTHER;
                break;
        }
        return result;
    }
}
