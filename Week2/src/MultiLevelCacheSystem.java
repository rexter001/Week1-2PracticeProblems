import java.util.*;

class VideoData {
    String videoId;
    String content;

    public VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}

class MultiLevelCache {

    // L1 memory cache
    private LRUCache<String, VideoData> L1;

    // L2 SSD cache
    private LRUCache<String, VideoData> L2;

    // L3 database simulation
    private HashMap<String, VideoData> L3;

    // statistics
    private int l1Hits = 0;
    private int l2Hits = 0;
    private int l3Hits = 0;

    public MultiLevelCache() {

        L1 = new LRUCache<>(10000);
        L2 = new LRUCache<>(100000);
        L3 = new HashMap<>();

        // simulate database videos
        for (int i = 1; i <= 1000; i++) {
            L3.put("video_" + i,
                    new VideoData("video_" + i, "VideoContent" + i));
        }
    }

    public VideoData getVideo(String videoId) {

        long start = System.nanoTime();

        // L1
        if (L1.containsKey(videoId)) {

            l1Hits++;

            System.out.println("L1 Cache HIT (0.5ms)");

            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2
        if (L2.containsKey(videoId)) {

            l2Hits++;

            System.out.println("L2 Cache HIT (5ms)");

            VideoData video = L2.get(videoId);

            // promote to L1
            L1.put(videoId, video);

            System.out.println("Promoted to L1");

            return video;
        }

        System.out.println("L2 Cache MISS");

        // L3 database
        if (L3.containsKey(videoId)) {

            l3Hits++;

            System.out.println("L3 Database HIT (150ms)");

            VideoData video = L3.get(videoId);

            // add to L2
            L2.put(videoId, video);

            return video;
        }

        System.out.println("Video not found");
        return null;
    }

    public void updateVideo(String videoId, String newContent) {

        VideoData video = new VideoData(videoId, newContent);

        L3.put(videoId, video);

        // invalidate caches
        L1.remove(videoId);
        L2.remove(videoId);

        System.out.println("Cache invalidated for " + videoId);
    }

    public void getStatistics() {

        int total = l1Hits + l2Hits + l3Hits;

        System.out.println("\nCache Statistics:");

        if (total == 0) total = 1;

        System.out.println("L1 Hit Rate: "
                + (l1Hits * 100.0 / total) + "%");

        System.out.println("L2 Hit Rate: "
                + (l2Hits * 100.0 / total) + "%");

        System.out.println("L3 Hit Rate: "
                + (l3Hits * 100.0 / total) + "%");

        System.out.println("Overall Requests: " + total);
    }
}

public class MultiLevelCacheSystem {

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        cache.getVideo("video_123");

        System.out.println();

        cache.getVideo("video_123");

        System.out.println();

        cache.getVideo("video_999");

        cache.getStatistics();
    }
}