import java.util.*;

public class RateLimiter {

    class TokenBucket {
        int tokens;
        int maxTokens;
        double refillRate; // tokens per second
        long lastRefillTime;

        TokenBucket(int maxTokens, double refillRate) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.refillRate = refillRate;
            this.lastRefillTime = System.currentTimeMillis();
        }
    }

    // clientId -> token bucket
    HashMap<String, TokenBucket> clients = new HashMap<>();

    int LIMIT = 1000; // requests per hour
    double REFILL_RATE = LIMIT / 3600.0;
    public synchronized boolean checkRateLimit(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            bucket = new TokenBucket(LIMIT, REFILL_RATE);
            clients.put(clientId, bucket);
        }

        refill(bucket);

        if (bucket.tokens > 0) {

            bucket.tokens--;

            System.out.println("Allowed (" + bucket.tokens + " requests remaining)");
            return true;

        } else {

            System.out.println("Denied (0 requests remaining)");
            return false;
        }
    }
    private void refill(TokenBucket bucket) {

        long now = System.currentTimeMillis();
        double seconds = (now - bucket.lastRefillTime) / 1000.0;

        int tokensToAdd = (int) (seconds * bucket.refillRate);

        if (tokensToAdd > 0) {

            bucket.tokens = Math.min(bucket.maxTokens, bucket.tokens + tokensToAdd);
            bucket.lastRefillTime = now;
        }
    }
    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("No requests made yet.");
            return;
        }

        int used = bucket.maxTokens - bucket.tokens;

        System.out.println("{used: " + used +
                ", limit: " + bucket.maxTokens +
                ", remaining: " + bucket.tokens + "}");
    }
    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        for (int i = 0; i < 5; i++) {
            limiter.checkRateLimit("abc123");
        }

        limiter.getRateLimitStatus("abc123");
    }
}
