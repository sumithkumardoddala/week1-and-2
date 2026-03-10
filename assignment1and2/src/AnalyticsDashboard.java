import java.util.*;

public class AnalyticsDashboard {

    // pageUrl -> visit count
    HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique users
    HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    HashMap<String, Integer> trafficSources = new HashMap<>();


    // Process incoming page view event
    public void processEvent(String url, String userId, String source) {

        // Count page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Count traffic sources
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }


    // Get top 10 pages
    public void getTopPages() {

        List<Map.Entry<String, Integer>> list = new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        System.out.println("Top Pages:");

        for (int i = 0; i < Math.min(10, list.size()); i++) {

            String page = list.get(i).getKey();
            int views = list.get(i).getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println((i + 1) + ". " + page +
                    " - " + views + " views (" + unique + " unique)");
        }
    }


    // Display traffic sources
    public void getTrafficSources() {

        int total = 0;

        for (int count : trafficSources.values()) {
            total += count;
        }

        System.out.println("\nTraffic Sources:");

        for (String source : trafficSources.keySet()) {

            int count = trafficSources.get(source);
            double percent = (count * 100.0) / total;

            System.out.printf("%s: %.2f%%\n", source, percent);
        }
    }


    // Display dashboard
    public void getDashboard() {

        getTopPages();
        getTrafficSources();
    }


    // Main method
    public static void main(String[] args) {

        AnalyticsDashboard dashboard = new AnalyticsDashboard();

        dashboard.processEvent("/article/breaking-news", "user_123", "google");
        dashboard.processEvent("/article/breaking-news", "user_456", "facebook");
        dashboard.processEvent("/sports/championship", "user_789", "google");
        dashboard.processEvent("/sports/championship", "user_222", "direct");
        dashboard.processEvent("/sports/championship", "user_333", "google");

        dashboard.getDashboard();
    }
}
