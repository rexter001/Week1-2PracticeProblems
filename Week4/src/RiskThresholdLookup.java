public class RiskThresholdLookup {

    // ---------------- LINEAR SEARCH ----------------
    public static void linearSearch(int[] risks, int target) {
        int comparisons = 0;
        boolean found = false;

        for (int i = 0; i < risks.length; i++) {
            comparisons++;

            if (risks[i] == target) {
                System.out.println("Linear Search: Found at index " + i);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Linear Search: Not found");
        }

        System.out.println("Comparisons = " + comparisons);
    }

    // ---------------- BINARY FLOOR + CEILING ----------------
    public static void binaryFloorCeiling(int[] risks, int target) {
        int low = 0;
        int high = risks.length - 1;
        int comparisons = 0;

        Integer floor = null;
        Integer ceiling = null;
        int insertionPoint = 0;

        while (low <= high) {
            comparisons++;

            int mid = (low + high) / 2;

            if (risks[mid] == target) {
                floor = risks[mid];
                ceiling = risks[mid];
                insertionPoint = mid;
                break;
            }
            else if (risks[mid] < target) {
                floor = risks[mid];
                low = mid + 1;
            }
            else {
                ceiling = risks[mid];
                high = mid - 1;
            }
        }

        if (floor == null || ceiling == null) {
            insertionPoint = low;
        }

        System.out.println("\nBinary Search:");
        System.out.println("Floor = " + floor);
        System.out.println("Ceiling = " + ceiling);
        System.out.println("Insertion Point = " + insertionPoint);
        System.out.println("Comparisons = " + comparisons);
    }

    public static void main(String[] args) {
        int[] risks = {10, 25, 50, 100};
        int target = 30;

        linearSearch(risks, target);
        binaryFloorCeiling(risks, target);
    }
}
