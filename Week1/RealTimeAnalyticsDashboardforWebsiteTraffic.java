import java.util.*;

class PageEvent {
    String url;
    String userId;
    String source;

    public PageEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

class AnalyticsDashboard {

    // pageUrl -> visit count
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique users
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    private HashMap<String, Integer> trafficSources = new HashMap<>();


    // process event
    public synchronized void processEvent(PageEvent event) {

        // update page views
        pageViews.put(event.url,
                pageViews.getOrDefault(event.url, 0) + 1);

        // update unique visitors
        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        // update traffic source
        trafficSources.put(event.source,
                trafficSources.getOrDefault(event.source, 0) + 1);
    }


    // get top pages
    private List<Map.Entry<String, Integer>> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(Map.Entry.comparingByValue());

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {

            pq.offer(entry);

            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<Map.Entry<String, Integer>> result = new ArrayList<>(pq);
        result.sort((a, b) -> b.getValue() - a.getValue());

        return result;
    }


    // display dashboard
    public void getDashboard() {

        System.out.println("\n===== REAL-TIME DASHBOARD =====");

        System.out.println("\nTop Pages:");

        List<Map.Entry<String, Integer>> topPages = getTopPages();

        int rank = 1;

        for (Map.Entry<String, Integer> page : topPages) {

            int unique = uniqueVisitors.get(page.getKey()).size();

            System.out.println(
                    rank + ". " + page.getKey()
                            + " - " + page.getValue()
                            + " views (" + unique + " unique)"
            );

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        int total = 0;
        for (int count : trafficSources.values()) {
            total += count;
        }

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {

            double percent = (entry.getValue() * 100.0) / total;

            System.out.println(entry.getKey()
                    + ": " + String.format("%.1f", percent) + "%");
        }
    }
}

public class RealTimeAnalyticsDashboardforWebsiteTraffic {

    public static void main(String[] args) throws Exception {

        AnalyticsDashboard dashboard = new AnalyticsDashboard();

        // simulate traffic events
        dashboard.processEvent(new PageEvent("/article/breaking-news", "user_123", "google"));
        dashboard.processEvent(new PageEvent("/article/breaking-news", "user_456", "facebook"));
        dashboard.processEvent(new PageEvent("/sports/championship", "user_222", "direct"));
        dashboard.processEvent(new PageEvent("/sports/championship", "user_333", "google"));
        dashboard.processEvent(new PageEvent("/sports/championship", "user_333", "google"));
        dashboard.processEvent(new PageEvent("/tech/ai-news", "user_999", "google"));
        dashboard.processEvent(new PageEvent("/tech/ai-news", "user_888", "direct"));

        // dashboard refresh every 5 seconds
        while (true) {

            dashboard.getDashboard();

            Thread.sleep(5000);
        }
    }
}