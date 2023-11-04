package com.mycompany.stackmgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.NoSuchElementException;

public class StrStack{
    private Stack<String> stack = new Stack<>();
    
    public void addElement(String element) {
        stack.add(element);
    }
    
    public String delElement() {
        if (stack.isEmpty()) {
            throw new NoSuchElementException("The stack is empty.");
        } else {
            String element = stack.get(stack.size() - 1);
            stack.remove(stack.size() - 1);
            return element;
        }
    }
    
    public String top() {
        if (isEmpty()) {
            throw new NoSuchElementException("The stack is empty.");
        } else {
            return stack.get(stack.size() - 1);
        }
    }
    
    public boolean isEmpty() {
        return stack.isEmpty();
    }
    
    public void printStack() {
        for (String element : this.stack) {
            System.out.println(element);
        }
    }
    
    public List<String> getElements() {
        return new ArrayList<>(stack);
    }
    
    public int getSize() {
        return stack.size();
    }
    
    public int search(String element) {
        int index = stack.lastIndexOf(element);
        if (index != -1) {
            return index + 1;
        } else {
            return -1;
        }
    }
    
    public String getByPos(int position) {
        if (position < 1 || position > stack.size()) {
            throw new IndexOutOfBoundsException("Out of bounds position.");
        }
        return stack.get(position - 1);
    }
}