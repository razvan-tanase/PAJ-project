public final class Test {
    private static final String IN_FILE = "in/basic_2.json";
    private static final String OUT_FILE = "results.out";
    private static final String[] mainArgv = new String[2];

    public static void main(final String[] argv) throws Exception {
        mainArgv[0] = IN_FILE;
        mainArgv[1] = OUT_FILE;

        Main.main(mainArgv);

        System.exit(0);
    }
}