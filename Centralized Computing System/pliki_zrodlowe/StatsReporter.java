import java.util.concurrent.atomic.AtomicInteger;

public class StatsReporter implements Runnable {

    private static AtomicInteger clientCount = new AtomicInteger(0);
    private static AtomicInteger operationCount = new AtomicInteger(0);
    private static AtomicInteger operationSum = new AtomicInteger(0);
    private static AtomicInteger addCount = new AtomicInteger(0);
    private static AtomicInteger subCount = new AtomicInteger(0);
    private static AtomicInteger mulCount = new AtomicInteger(0);
    private static AtomicInteger divCount = new AtomicInteger(0);
    private static AtomicInteger errorCount = new AtomicInteger(0);

    private static AtomicInteger lastClientCount = new AtomicInteger(0);
    private static AtomicInteger lastOperationCount = new AtomicInteger(0);
    private static AtomicInteger lastOperationSum = new AtomicInteger(0);
    private static AtomicInteger lastAddCount = new AtomicInteger(0);
    private static AtomicInteger lastSubCount = new AtomicInteger(0);
    private static AtomicInteger lastMulCount = new AtomicInteger(0);
    private static AtomicInteger lastDivCount = new AtomicInteger(0);
    private static AtomicInteger lastErrorCount = new AtomicInteger(0);

    @Override
    public void run() {

        while (!Thread.interrupted()) {
            try {
                Thread.sleep(10000);
                printStats();
                resetLastStats();
            } catch (InterruptedException e) {
                System.out.println("StatsReporter interrupted");
            }
        }

    }

    public void printStats() {
        System.out.println("===== Global Statistics =====");
        System.out.println("Total New Clients: " + clientCount.get());
        System.out.println("Total Operations: " + operationCount.get());
        System.out.println("  ADD: " + addCount.get());
        System.out.println("  SUB: " + subCount.get());
        System.out.println("  MUL: " + mulCount.get());
        System.out.println("  DIV: " + divCount.get());
        System.out.println("Total Errors: " + errorCount.get());
        System.out.println("Total sum of operations: " + operationSum.get());

        System.out.println();

        System.out.println("===== Last 10 Seconds =====");
        System.out.println("Last New Clients: " + lastClientCount.get());
        System.out.println("Last Operations: " + lastOperationCount.get());
        System.out.println("  ADD: " + lastAddCount.get());
        System.out.println("  SUB: " + lastSubCount.get());
        System.out.println("  MUL: " + lastMulCount.get());
        System.out.println("  DIV: " + lastDivCount.get());
        System.out.println("Last Errors: " + lastErrorCount.get());
        System.out.println("Last sum of operations: " + lastOperationSum.get());

        System.out.println();
    }

    public void resetLastStats() {
        lastClientCount.set(0);
        lastOperationCount.set(0);
        lastOperationSum.set(0);
        lastAddCount.set(0);
        lastSubCount.set(0);
        lastMulCount.set(0);
        lastDivCount.set(0);
        lastErrorCount.set(0);
    }

    public static void incrementClients() {
        clientCount.incrementAndGet();
        lastClientCount.incrementAndGet();
    }

    public static void incrementOperation(String operation, int result) {

        operationCount.incrementAndGet();
        operationSum.addAndGet(result);
        lastOperationCount.incrementAndGet();
        lastOperationSum.addAndGet(result);

        switch (operation) {
            case "ADD":
                addCount.incrementAndGet();
                lastAddCount.incrementAndGet();
                break;
            case "SUB":
                subCount.incrementAndGet();
                lastSubCount.incrementAndGet();
                break;
            case "MUL":
                mulCount.incrementAndGet();
                lastMulCount.incrementAndGet();
                break;
            case "DIV":
                divCount.incrementAndGet();
                lastDivCount.incrementAndGet();
                break;
        }

    }

    public static void incrementError() {
        errorCount.incrementAndGet();
        lastErrorCount.incrementAndGet();
    }
}
