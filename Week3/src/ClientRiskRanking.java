class Client {
    String name;
    int riskScore;
    double accountBalance;

    Client(String name, int riskScore, double accountBalance) {
        this.name = name;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return name + "(" + riskScore + ", ₹" + accountBalance + ")";
    }
}

public class ClientRiskRanking {

    // Bubble Sort - Ascending
    public static void bubbleSortAscending(Client[] clients) {
        int n = clients.length;
        int swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (clients[j].riskScore > clients[j + 1].riskScore) {
                    Client temp = clients[j];
                    clients[j] = clients[j + 1];
                    clients[j + 1] = temp;

                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }

        System.out.println("Bubble Sort (Ascending):");
        for (Client c : clients) {
            System.out.println(c);
        }
        System.out.println("Swaps = " + swaps);
    }

    // Insertion Sort - Descending + Balance
    public static void insertionSortDescending(Client[] clients) {
        for (int i = 1; i < clients.length; i++) {
            Client key = clients[i];
            int j = i - 1;

            while (j >= 0 &&
                    (clients[j].riskScore < key.riskScore ||
                            (clients[j].riskScore == key.riskScore &&
                                    clients[j].accountBalance < key.accountBalance))) {

                clients[j + 1] = clients[j];
                j--;
            }

            clients[j + 1] = key;
        }

        System.out.println("\nInsertion Sort (Descending):");
        for (Client c : clients) {
            System.out.println(c);
        }
    }

    // Top 10 highest risk
    public static void topRiskClients(Client[] clients, int topN) {
        System.out.println("\nTop " + topN + " Highest Risk Clients:");

        for (int i = 0; i < Math.min(topN, clients.length); i++) {
            System.out.println(clients[i]);
        }
    }

    public static void main(String[] args) {
        Client[] clients = {
                new Client("clientC", 80, 50000),
                new Client("clientA", 20, 20000),
                new Client("clientB", 50, 35000)
        };

        // Bubble sort
        Client[] bubbleArray = clients.clone();
        bubbleSortAscending(bubbleArray);

        // Insertion sort
        Client[] insertionArray = clients.clone();
        insertionSortDescending(insertionArray);

        // Top risks
        topRiskClients(insertionArray, 3);
    }
}