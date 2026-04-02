import java.util.ArrayList;

public class TransactionAuditSort {

    static class Transaction {
        String id;
        double fee;
        String timestamp;

        Transaction(String id, double fee, String timestamp) {
            this.id = id;
            this.fee = fee;
            this.timestamp = timestamp;
        }
    }

    // Bubble Sort by fee ascending
    public static void bubbleSort(ArrayList<Transaction> transactions) {
        int n = transactions.size();
        int passes = 0;
        int swaps = 0;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            passes++;

            for (int j = 0; j < n - i - 1; j++) {
                if (transactions.get(j).fee > transactions.get(j + 1).fee) {
                    Transaction temp = transactions.get(j);
                    transactions.set(j, transactions.get(j + 1));
                    transactions.set(j + 1, temp);

                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }

        System.out.println("Bubble Sort Result:");
        for (Transaction t : transactions) {
            System.out.println(t.id + " : " + t.fee + " @ " + t.timestamp);
        }

        System.out.println("Passes = " + passes);
        System.out.println("Swaps = " + swaps);
    }

    // Insertion Sort by fee + timestamp
    public static void insertionSort(ArrayList<Transaction> transactions) {
        int shifts = 0;

        for (int i = 1; i < transactions.size(); i++) {
            Transaction key = transactions.get(i);
            int j = i - 1;

            while (j >= 0 &&
                    (transactions.get(j).fee > key.fee ||
                            (transactions.get(j).fee == key.fee &&
                                    transactions.get(j).timestamp.compareTo(key.timestamp) > 0))) {

                transactions.set(j + 1, transactions.get(j));
                j--;
                shifts++;
            }

            transactions.set(j + 1, key);
        }

        System.out.println("\nInsertion Sort Result:");
        for (Transaction t : transactions) {
            System.out.println(t.id + " : " + t.fee + " @ " + t.timestamp);
        }

        System.out.println("Shifts = " + shifts);
    }

    // High fee outlier detection
    public static void highFeeOutliers(ArrayList<Transaction> transactions) {
        boolean found = false;

        System.out.println("\nHigh Fee Outliers (> $50):");

        for (Transaction t : transactions) {
            if (t.fee > 50) {
                System.out.println(t.id + " : " + t.fee);
                found = true;
            }
        }

        if (!found) {
            System.out.println("None");
        }
    }

    public static void main(String[] args) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction("id1", 10.5, "10:00"));
        transactions.add(new Transaction("id2", 25.0, "09:30"));
        transactions.add(new Transaction("id3", 5.0, "10:15"));

        ArrayList<Transaction> bubbleList = new ArrayList<>(transactions);
        bubbleSort(bubbleList);

        ArrayList<Transaction> insertionList = new ArrayList<>(transactions);
        insertionSort(insertionList);

        highFeeOutliers(transactions);
    }
}