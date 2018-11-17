package test;

public enum TestResult {
    PASS,
    FAIL_UNEXPECTED_OUTPUT,
    FAIL_OTHER;


    /**
     * Converts string to TestResult enum
     * @param s Input to be converted to enum
     * @return TestResult enum
     */
    public static TestResult getTestResultFromString(String s) {
        TestResult result;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '"') {
                builder.append(s.charAt(i));
            }
        }
        s = builder.toString();
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
            default:
                return null;
        }
        return result;
    }
}
