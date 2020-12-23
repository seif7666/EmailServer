package com.cse223.mailserver.library;

import java.io.Serializable;

public class stackImplementation implements Serializable{
	/**
	 * the node class used to implement Stack
	 * consist of two fields the value and pointing to next Node
	 * the constructor of it take one parameter and it is value
	 *
	 */
	private class Node {
		public Object value;
		public Node next=null;
		public Node(Object value){
			this.value=value;
		}
	}
	public Node head=null;
	private int size=0;
	
	/**
	* Removes the element at the top of stack and returns that element.
	*@throws if wrong expression
	* @author sakr
	* @return top of stack element, or through exception if empty
	*/
	
	public Object pop() {
		Object temp;
		// TODO Auto-generated method stub
		if(head==null) {
			throw new RuntimeException("Empty Stack");
		}else {
			temp=head.value;
			head=head.next;
			size--;
		}
		return temp;
	}
	
	/**
	* Get the element at the top of stack without removing it from stack.
	*@throws if wrong expression
	* @author sakr
	* @return top of stack element, or through exception if empty
	*/
	
	public Object peek() {
		// TODO Auto-generated method stub
		Object temp;
		if(head==null) {
			throw new RuntimeException("Empty Stack");
		}else {
			 temp=head.value;
			
		}
		return temp;
	}
	/**
	* Pushes an item onto the top of this stack.
	*
	* @param object
	* to insert
	*/
	
	public void push(Object element) {
		// TODO Auto-generated method stub
		Node newNode=new Node(element);
		if(head==null) {
			newNode.next=null;
			head=newNode;
			size++;
		}else {
			newNode.next=head;
			head=newNode;
			size++;
			
		}
		
	}
	
	
	/**
	* Tests if this stack is empty
	*
	* @return true if stack empty
	*/
	
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if(head==null) return true;
		return false;
	}
	/**
	* Returns the number of elements in the stack.
	*
	* @return number of elements in the stack
	*/
	
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}
	
}
