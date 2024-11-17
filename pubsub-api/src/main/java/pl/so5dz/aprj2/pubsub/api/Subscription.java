package pl.so5dz.aprj2.pubsub.api;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Subscription<T> {
    private final Thread subscribingThread;

    private final Topic<T> topic;
    private final BlockingQueue<T> queue;
    private boolean cancelled = false;

    public Subscription(Topic<T> topic) {
        this.topic = topic;
        queue = new LinkedBlockingQueue<>();
        subscribingThread = Thread.currentThread();
    }

    void publish(T message) {
        queue.add(message);
    }

    public T awaitMessage() {
        if (cancelled) {
            throw new IllegalStateException("Subscription is cancelled");
        }
        try {
            return queue.take();
        } catch (InterruptedException e) {
            cancel();
            return null;
        }
    }

    public void cancel() {
        cancelled = true;
        topic.onCancelled(this);
        subscribingThread.interrupt();
    }
}
