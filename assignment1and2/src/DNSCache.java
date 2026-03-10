import java.util.*;

public class DNSCache {

    class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime;

        DNSEntry(String domain, String ipAddress, int ttlSeconds) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
    private LinkedHashMap<String, DNSEntry> cache;

    private int capacity;
    private int hits = 0;
    private int misses = 0;

    public DNSCache(int capacity) {

        this.capacity = capacity;

        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {

            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.capacity;
            }
        };
    }
    public String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null) {

            if (!entry.isExpired()) {
                hits++;
                System.out.println("Cache HIT");
                return entry.ipAddress;
            } else {
                cache.remove(domain);
                System.out.println("Cache EXPIRED");
            }
        }

        misses++;
        System.out.println("Cache MISS");

        String ip = queryUpstreamDNS(domain);
        cache.put(domain, new DNSEntry(domain, ip, 300));

        return ip;
    }
    private String queryUpstreamDNS(String domain) {

        Random r = new Random();
        return "172.217.14." + r.nextInt(255);
    }
    public void getCacheStats() {

        int total = hits + misses;

        double hitRate = (total == 0) ? 0 : (hits * 100.0 / total);

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }
    public static void main(String[] args) throws InterruptedException {
        DNSCache dns = new DNSCache(5);
        System.out.println("IP: " + dns.resolve("google.com"));
        System.out.println("IP: " + dns.resolve("google.com"));
        Thread.sleep(2000);
        System.out.println("IP: " + dns.resolve("example.com"));
        System.out.println("IP: " + dns.resolve("google.com"));

        dns.getCacheStats();
    }
}
