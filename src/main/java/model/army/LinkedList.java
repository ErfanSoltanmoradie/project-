package model.army;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements Serializable,Iterable<T> {

    private class Node{

        private T data;
        private Node link;

        public Node(T data){
            this.data = data;
            this.link = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public LinkedList(){
        head = null;
        tail = null;
        size = 0;
    }

    public void addNodeToTail(T data){
        Node newNode = new Node(data);
        if(head == null){
            head = tail = newNode;
        }else{
            tail.link= newNode;
            tail = newNode;
        }
        size++;
    }

    public void removeNodeFromHead() {
        if (head == null)
            return;

        this.head = head.link;

        if (head == null)
            tail = null;

        size--;
    }

    public T getHead(){
        if(this.head == null){
            return null;
        }
        return head.data;
    }

    public T getTail(){
        if(this.tail == null){
            return null;
        }
        return tail.data;
    }

    public boolean isEmpty(){
        return this.head == null;
    }

    public int size(){
        return this.size;
    }

    @Override
    public Iterator<T> iterator() {

        return new Iterator<>() {

            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {

                if (current == null)
                    throw new NoSuchElementException();

                T data = current.data;
                current = current.link;

                return data;
            }
        };
    }

}