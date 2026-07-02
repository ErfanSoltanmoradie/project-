package model.army;

import java.time.Instant;

public class ArmyQueue {

    private final LinkedList<ArmyType> queue;
    private Instant lastFinishTime;

    public ArmyQueue() {
        queue = new LinkedList<>();
    }

    public void enqueue(ArmyType type) {
        queue.addNodeToTail(type);
    }

    public void dequeue() {
        queue.removeNodeFromHead();
    }

    public ArmyType peek() {
        return queue.getHead();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public Instant getLastFinishTime() {
        return lastFinishTime;
    }

    public void setLastFinishTime(Instant lastFinishTime) {
        this.lastFinishTime = lastFinishTime;
    }
}
