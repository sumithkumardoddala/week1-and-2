import java.util.*;

public class Socialmedia {

    // username -> userId
    HashMap<String, Integer> usernameMap = new HashMap<>();

    // username -> attempt count
    HashMap<String, Integer> attemptCount = new HashMap<>();

    // Check username availabilit
    public boolean checkAvailability(String username) {

        // Track attempt frequency
        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);

        return !usernameMap.containsKey(username);
    }

    // Register a new user
    public void registerUser(String username, int userId) {
        usernameMap.put(username, userId);
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String newName = username + i;

            if (!usernameMap.containsKey(newName)) {
                suggestions.add(newName);
            }
        }

        String dotName = username.replace("_", ".");
        if (!usernameMap.containsKey(dotName)) {
            suggestions.add(dotName);
        }

        String officialName = username + "_official";
        if (!usernameMap.containsKey(officialName)) {
            suggestions.add(officialName);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String result = "";
        int max = 0;

        for (String key : attemptCount.keySet()) {

            if (attemptCount.get(key) > max) {
                max = attemptCount.get(key);
                result = key;
            }
        }

        return result;
    }

    public static void main(String[] args) {

        Socialmedia sm = new Socialmedia();

        sm.registerUser("john_doe", 1001);
        sm.registerUser("alex23", 1002);

        System.out.println("Check john_doe: " + sm.checkAvailability("john_doe"));
        System.out.println("Check jane_smith: " + sm.checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe:");
        System.out.println(sm.suggestAlternatives("john_doe"));

        System.out.println("Most attempted username: " + sm.getMostAttempted());
    }
}