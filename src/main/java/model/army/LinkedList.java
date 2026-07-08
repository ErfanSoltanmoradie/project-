package model.army;

public class LinkedList<T>{

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
}