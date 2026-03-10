import java.util.*;
public class PlagiarismDetector {
    HashMap<String, Set<String>> ngramIndex = new HashMap<>();
    HashMap<String, List<String>> documentNgrams = new HashMap<>();

    int N = 5;
    public void addDocument(String docId, String text) {

        List<String> ngrams = extractNgrams(text);
        documentNgrams.put(docId, ngrams);

        for (String gram : ngrams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }
    }
    private List<String> extractNgrams(String text) {

        List<String> result = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            result.add(gram.toString().trim());
        }

        return result;
    }
    public void analyzeDocument(String docId, String text) {

        List<String> grams = extractNgrams(text);

        System.out.println("Extracted " + grams.size() + " n-grams");

        HashMap<String, Integer> matchCount = new HashMap<>();


        for (String gram : grams) {

            if (ngramIndex.containsKey(gram)) {

                for (String existingDoc : ngramIndex.get(gram)) {

                    matchCount.put(existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }
        for (String existingDoc : matchCount.keySet()) {

            int matches = matchCount.get(existingDoc);
            double similarity = (matches * 100.0) / grams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with " + existingDoc);

            System.out.printf("Similarity: %.2f%%\n", similarity);

            if (similarity > 50) {
                System.out.println("PLAGIARISM DETECTED\n");
            } else if (similarity > 10) {
                System.out.println("Suspicious\n");
            }
        }
    }
    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        detector.addDocument("essay_089.txt",
                "machine learning is a method of data analysis that automates analytical model building");

        detector.addDocument("essay_092.txt",
                "machine learning is a method of data analysis that automates analytical model building using algorithms");

        detector.analyzeDocument("essay_123.txt",
                "machine learning is a method of data analysis that automates analytical model building");
    }
}
