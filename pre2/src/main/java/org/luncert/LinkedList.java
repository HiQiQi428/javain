package org.luncert;

public class LinkedList<T> {

    public static class Node<T> {
        T data;
        Node<T> next;
        Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
        public String toString() {
            return "[data: " + data + "]";
        }
    }

    Node<T> head;

    Node<T> tail;

    public LinkedList() {}

    public void addTail(T data) {
        Node<T> node = new Node<>(data, null);
        if (tail == null)
            head = node;
        else
            tail.next = node;
        tail = node;
    }

    public void addHead(T data) {
        Node<T> node = new Node<>(data, null);
        if (head == null) {
            head = node;
            tail = node;
        }
        else {
            node.next = head;
            head = node;
        }
    }

    public void reverse() {
        Node<T> node = head, right = head.next, left = null;
        while (right != null) {
            node.next = left;
            
            left = node;
            node = right;
            right = node.next;
        }
        node.next = left;
        tail = head;
        head = node;
    }

    @Override
    public String toString() {
        Node<T> node = head;
        StringBuilder builder = new StringBuilder();
        while (node != null) {
            builder.append(node).append('\n');
            node = node.next;
        }
        return builder.toString();
    }

}