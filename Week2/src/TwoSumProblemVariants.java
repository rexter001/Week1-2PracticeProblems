import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    long timestamp;
    String account;

    public Transaction(int id, int amount, String merchant, long timestamp, String account) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.timestamp = timestamp;
        this.account = account;
    }
}

class TransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();


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

                System.out.println("Two-Sum Match: "
                        + "(id:" + other.id + ", id:" + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }


    // Two-Sum within 1 hour window
    public void findTwoSumWithTimeWindow(int target, long windowMillis) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                if (Math.abs(t.timestamp - other.timestamp) <= windowMillis) {

                    System.out.println("Two-Sum within time window: "
                            + "(id:" + other.id + ", id:" + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }


    // Duplicate detection
    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.println("Duplicate Transactions Detected:");

                for (Transaction t : list) {
                    System.out.println("Transaction ID: " + t.id + " Account: " + t.account);
                }
            }
        }
    }


    // K-Sum
    public void findKSum(int k, int target) {

        List<Integer> amounts = new ArrayList<>();
        for (Transaction t : transactions)
            amounts.add(t.amount);

        kSumHelper(amounts, new ArrayList<>(), k, target, 0);
    }

    private void kSumHelper(List<Integer> nums, List<Integer> current, int k, int target, int start) {

        if (k == 0 && target == 0) {
            System.out.println("K-Sum Match: " + current);
            return;
        }

        if (k == 0 || target < 0)
            return;

        for (int i = start; i < nums.size(); i++) {

            current.add(nums.get(i));

            kSumHelper(nums, current, k - 1,
                    target - nums.get(i), i + 1);

            current.remove(current.size() - 1);
        }
    }
}


public class TwoSumProblemVariants {

    public static void main(String[] args) {

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        long now = System.currentTimeMillis();

        analyzer.addTransaction(new Transaction(1, 500, "StoreA", now, "acc1"));
        analyzer.addTransaction(new Transaction(2, 300, "StoreB", now + 1000, "acc2"));
        analyzer.addTransaction(new Transaction(3, 200, "StoreC", now + 2000, "acc3"));
        analyzer.addTransaction(new Transaction(4, 500, "StoreA", now + 3000, "acc4"));

        System.out.println("Two-Sum target=500");
        analyzer.findTwoSum(500);

        System.out.println("\nTwo-Sum with time window");
        analyzer.findTwoSumWithTimeWindow(500, 3600000);

        System.out.println("\nDuplicate Detection");
        analyzer.detectDuplicates();

        System.out.println("\nK-Sum k=3 target=1000");
        analyzer.findKSum(3, 1000);
    }
}