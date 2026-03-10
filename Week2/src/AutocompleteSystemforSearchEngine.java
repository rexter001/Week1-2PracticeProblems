import java.util.*;

class TrieNode {

    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEnd = false;
}

class AutocompleteSystem {

    private TrieNode root = new TrieNode();

    // query -> frequency
    private HashMap<String, Integer> frequencyMap = new HashMap<>();


    // insert query into trie
    public void addQuery(String query, int freq) {

        frequencyMap.put(query,
                frequencyMap.getOrDefault(query, 0) + freq);

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEnd = true;
    }


    // search suggestions for prefix
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c))
                return new ArrayList<>();

            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();

        dfs(node, prefix, results);

        // min heap for top 10
        PriorityQueue<String> pq =
                new PriorityQueue<>((a, b) ->
                        frequencyMap.get(a) - frequencyMap.get(b));

        for (String query : results) {

            pq.offer(query);

            if (pq.size() > 10)
                pq.poll();
        }

        List<String> topResults = new ArrayList<>(pq);

        topResults.sort((a, b) ->
                frequencyMap.get(b) - frequencyMap.get(a));

        return topResults;
    }


    // DFS to collect words
    private void dfs(TrieNode node, String word, List<String> list) {

        if (node.isEnd)
            list.add(word);

        for (char c : node.children.keySet()) {

            dfs(node.children.get(c), word + c, list);
        }
    }


    // update frequency after search
    public void updateFrequency(String query) {

        addQuery(query, 1);

        System.out.println(
                query + " → Frequency: " + frequencyMap.get(query));
    }
}


public class AutocompleteSystemforSearchEngine {

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.addQuery("java tutorial", 1234567);
        system.addQuery("javascript", 987654);
        system.addQuery("java download", 456789);
        system.addQuery("java 21 features", 100);
        system.addQuery("java interview questions", 90000);


        System.out.println("Suggestions for 'jav':");

        List<String> results = system.search("jav");

        int rank = 1;

        for (String r : results) {

            System.out.println(rank + ". " + r);
            rank++;
        }

        System.out.println();

        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
    }
}