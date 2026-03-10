import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    int time; // minutes from start of day

    Transaction(int id, int amount, String merchant, String account, int time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

 class FinancialAnalyzer {

    List<Transaction> transactions = new ArrayList<>();


    // Add transaction
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }


    // Classic Two-Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                System.out.println("TwoSum Pair: (" +
                        other.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }


    // Two-Sum within 1 hour (60 minutes)
    public void findTwoSumTimeWindow(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                if (Math.abs(t.time - other.time) <= 60) {

                    System.out.println("TimeWindow Pair: (" +
                            other.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }


    // Duplicate detection
    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.print("Duplicate detected: ");

                for (Transaction t : list) {
                    System.out.print("ID " + t.id + " ");
                }

                System.out.println();
            }
        }
    }


    // K-Sum (simple recursive)
    public void findKSum(int k, int target) {
        kSumHelper(transactions, k, target, new ArrayList<>(), 0);
    }

    private void kSumHelper(List<Transaction> list, int k, int target,
                            List<Integer> current, int start) {

        if (k == 0 && target == 0) {
            System.out.println("K-Sum: " + current);
            return;
        }

        if (k == 0 || target < 0) return;

        for (int i = start; i < list.size(); i++) {

            current.add(list.get(i).id);

            kSumHelper(list, k - 1,
                    target - list.get(i).amount,
                    current, i + 1);

            current.remove(current.size() - 1);
        }
    }


    public static void main(String[] args) {

        FinancialAnalyzer system = new FinancialAnalyzer();

        system.addTransaction(new Transaction(1, 500, "StoreA", "acc1", 600));
        system.addTransaction(new Transaction(2, 300, "StoreB", "acc2", 615));
        system.addTransaction(new Transaction(3, 200, "StoreC", "acc3", 630));
        system.addTransaction(new Transaction(4, 500, "StoreA", "acc4", 650));

        System.out.println("Two-Sum:");
        system.findTwoSum(500);

        System.out.println("\nTwo-Sum within 1 hour:");
        system.findTwoSumTimeWindow(500);

        System.out.println("\nDuplicates:");
        system.detectDuplicates();

        System.out.println("\nK-Sum (k=3, target=1000):");
        system.findKSum(3, 1000);
    }
}
