import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    public DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

class DNSCache {

    private final int MAX_SIZE;

    // LRU cache using LinkedHashMap
    private LinkedHashMap<String, DNSEntry> cache;

    private int hits = 0;
    private int misses = 0;

    public DNSCache(int size) {
        this.MAX_SIZE = size;

        cache = new LinkedHashMap<String, DNSEntry>(size, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > MAX_SIZE;
            }
        };

        startCleanupThread();
    }

    // Resolve domain
    public synchronized String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            System.out.println("Cache HIT → " + entry.ipAddress);
            return entry.ipAddress;
        }

        misses++;

        if (entry != null && entry.isExpired()) {
            cache.remove(domain);
            System.out.println("Cache EXPIRED");
        } else {
            System.out.println("Cache MISS");
        }

        // simulate upstream DNS query
        String ip = queryUpstreamDNS(domain);

        DNSEntry newEntry = new DNSEntry(domain, ip, 10); // TTL = 10 seconds
        cache.put(domain, newEntry);

        return ip;
    }

    // Simulated upstream DNS lookup
    private String queryUpstreamDNS(String domain) {

        try {
            Thread.sleep(100); // simulate network delay
        } catch (InterruptedException e) {}

        Random r = new Random();
        return "172.217." + r.nextInt(255) + "." + r.nextInt(255);
    }

    // Background thread to remove expired entries
    private void startCleanupThread() {

        Thread cleaner = new Thread(() -> {

            while (true) {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {}

                synchronized (this) {

                    Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();

                    while (it.hasNext()) {
                        Map.Entry<String, DNSEntry> entry = it.next();

                        if (entry.getValue().isExpired()) {
                            it.remove();
                        }
                    }
                }
            }

        });

        cleaner.setDaemon(true);
        cleaner.start();
    }

    // Cache statistics
    public void getCacheStats() {

        int total = hits + misses;

        double hitRate = total == 0 ? 0 : ((double) hits / total) * 100;

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }
}

public class DNSCachewithTTL {

    public static void main(String[] args) throws Exception {

        DNSCache cache = new DNSCache(5);

        System.out.println("Resolving google.com");
        System.out.println(cache.resolve("google.com"));

        System.out.println("\nResolving google.com again");
        System.out.println(cache.resolve("google.com"));

        System.out.println("\nResolving openai.com");
        System.out.println(cache.resolve("openai.com"));

        Thread.sleep(11000); // wait for TTL expiry

        System.out.println("\nResolving google.com after TTL");
        System.out.println(cache.resolve("google.com"));

        System.out.println("\nCache Stats:");
        cache.getCacheStats();
    }
}