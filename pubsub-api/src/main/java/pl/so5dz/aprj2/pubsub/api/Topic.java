package pl.so5dz.aprj2.pubsub.api;

import java.util.ArrayList;
import java.util.List;

public class Topic<T> {
    private final Class<T> messageType;
    private final List<Subscription<T>> subscriptions;

    public Topic(Class<T> messageType) {
        this.subscriptions = new ArrayList<>();
        this.messageType = messageType;
    }

    public void publish(T message) {
        subscriptions.forEach(subscription -> subscription.publish(message));
    }

    public Subscription<T> subscribe() {
        var subscription = new Subscription<>(this);
        subscriptions.add(subscription);
        return subscription;
    }

    public void cancelAll() {
        subscriptions.forEach(Subscription::cancel);
    }

    void onCancelled(Subscription<T> subscription) {
        subscriptions.remove(subscription);
    }

    void assertMessageTypeValid(Class<?> messageType) {
        if (!this.messageType.isAssignableFrom(messageType)) {
            throw new IllegalArgumentException(
                    "Message type " + messageType + " is not assignable to " + this.messageType);
        }
    }
}
