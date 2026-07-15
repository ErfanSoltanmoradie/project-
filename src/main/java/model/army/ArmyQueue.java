package model.army;

import java.io.Serializable;
import java.util.Iterator;

public class ArmyQueue implements Serializable,Iterable<QueuedArmy> {

    private final LinkedList<QueuedArmy> queue;
    private boolean training;

    public ArmyQueue() {
        queue = new LinkedList<>();
        training = false;
    }

    public void enqueue(QueuedArmy queuedArmy) {
        queue.addNodeToTail(queuedArmy);
    }

    public void dequeue() {
        queue.removeNodeFromHead();
    }

    public QueuedArmy peek() {
        return queue.getHead();
    }

    public QueuedArmy getLast() {
        return queue.getTail();
    }


    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public boolean isTraining() {
        return training;
    }

    public void setTraining(boolean training) {
        this.training = training;
    }

    @Override
    public Iterator<QueuedArmy> iterator() {
        return queue.iterator();
    }
}
