package pl.so5dz.aprj2.pubsub.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PubSub {
    private static final Map<String, Topic<?>> topics = new ConcurrentHashMap<>();

    public static <T> Topic<T> topic(String name, Class<T> messageType) {
        var topic = topics.get(name);
        if (topic == null) {
            topic = new Topic<>(messageType);
            topics.put(name, topic);
        }
        topic.assertMessageTypeValid(messageType);
        return (Topic<T>) topic;
    }

    public static void cancelAll() {
        topics.values().forEach(Topic::cancelAll);
    }
}
