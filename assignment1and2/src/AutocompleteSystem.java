import java.util.*;

public class AutocompleteSystem {

    // Trie Node
    class TrieNode {
        HashMap<Character, TrieNode> children = new HashMap<>();
        List<String> queries = new ArrayList<>();
    }

    TrieNode root = new TrieNode();

    // query -> frequency
    HashMap<String, Integer> frequencyMap = new HashMap<>();


    // Insert query into Trie
    public void insertQuery(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            node.queries.add(query);
        }
    }


    // Update search frequency
    public void updateFrequency(String query) {

        int freq = frequencyMap.getOrDefault(query, 0) + 1;
        frequencyMap.put(query, freq);

        insertQuery(query);

        System.out.println(query + " → Frequency: " + freq);
    }


    // Search suggestions for prefix
    public void search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c)) {
                System.out.println("No suggestions");
                return;
            }

            node = node.children.get(c);
        }

        List<String> candidates = node.queries;

        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> frequencyMap.get(b) - frequencyMap.get(a));

        pq.addAll(candidates);

        System.out.println("Suggestions:");

        for (int i = 1; i <= 10 && !pq.isEmpty(); i++) {

            String q = pq.poll();
            System.out.println(i + ". " + q + " (" +
                    frequencyMap.get(q) + " searches)");
        }
    }


    // Main method
    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java 21 features");

        system.search("jav");
    }
}
