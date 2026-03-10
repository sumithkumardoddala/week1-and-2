import java.util.*;
public class MultiLevelCache {
    int L1_SIZE = 10000;
    int L2_SIZE = 100000;
    LinkedHashMap<String, String> L1;
    LinkedHashMap<String, String> L2;
    HashMap<String, String> database = new HashMap<>();
    HashMap<String, Integer> accessCount = new HashMap<>();
    int L1Hits = 0;
    int L2Hits = 0;
    int L3Hits = 0;
    public MultiLevelCache() {
        L1 = new LinkedHashMap<String, String>(L1_SIZE, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > L1_SIZE;
            }
        };
        L2 = new LinkedHashMap<String, String>(L2_SIZE, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > L2_SIZE;
            }
        };
    }
    public String getVideo(String videoId) {
        if (L1.containsKey(videoId)) {
            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }
        System.out.println("L1 Cache MISS");
        if (L2.containsKey(videoId)) {
            L2Hits++;
            System.out.println("L2 Cache HIT (5ms)");
            String video = L2.get(videoId);
            L1.put(videoId, video);
            System.out.println("Promoted to L1");
            return video;
        }
        System.out.println("L2 Cache MISS");
        if (database.containsKey(videoId)) {
            L3Hits++;
            System.out.println("L3 Database HIT (150ms)");
            String video = database.get(videoId);
            L2.put(videoId, video);
            accessCount.put(videoId,
                    accessCount.getOrDefault(videoId, 0) + 1);
            return video;
        }
        System.out.println("Video not found");
        return null;
    }
    public void addVideo(String videoId, String data) {
        database.put(videoId, data);
    }
    public void getStatistics() {
        int total = L1Hits + L2Hits + L3Hits;
        double L1Rate = total == 0 ? 0 : (L1Hits * 100.0 / total);
        double L2Rate = total == 0 ? 0 : (L2Hits * 100.0 / total);
        double L3Rate = total == 0 ? 0 : (L3Hits * 100.0 / total);

        System.out.println("\nCache Statistics:");
        System.out.println("L1 Hit Rate: " + L1Rate + "%");
        System.out.println("L2 Hit Rate: " + L2Rate + "%");
        System.out.println("L3 Hit Rate: " + L3Rate + "%");
    }
    public static void main(String[] args) {
        MultiLevelCache cache = new MultiLevelCache();
        cache.addVideo("video_123", "Video Data A");
        cache.addVideo("video_999", "Video Data B");
        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_999");
        cache.getStatistics();
    }
}
