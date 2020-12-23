package com.cse223.mailserver.library;

import java.io.Serializable;

public class queueLinkedListImplementation implements Serializable{
	private int counter=0; //number of element in queue
	private class Node {
		public Object value; 	//value of enqueued item
		public Node next=null;		//class node to use it in make nodes for implementation queue
		public Node(Object value){
			this.value=value;
		}
	}
	Node front=null; //to point to first element
	Node rear=null;  //to point to last element
	/**
	* Inserts an item at the queue front
	* @param item
	* @author team
	* @return void
	*/
	
	public void enqueue(Object item) {
		// TODO Auto-generated method stub
		Node newNode= new Node(item);	
		newNode.next=null;
		if(isEmpty()) {
			front=rear=newNode;		//special case if the first element added
		}else {
			rear.next=newNode;
			rear=newNode;
		}
		counter++;
		
	}
	/**
	* Removes the object at the queue rear and returns it.
	* @throws if empty queue
	* @author team
	* @return dequeued element
	*/
	
	public Object dequeue() {
		// TODO Auto-generated method stub
		Object temp;
		if(isEmpty())throw new RuntimeException("Empty queue"); //Exception if empty
		if(front==rear) {
			temp=front.value;
			front=rear=null; //special case if we deleted last element
		}
		else {
			temp=front.value;
			front=front.next;
		}
		counter--;
		return temp;
	}
	/**
	* Tests if this queue is empty.
	* author team
	* @return boolean true if empty false if non empty
	*/
	
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if(front==null) return true; //FUNCTION TO KNOW IF EMPTY
		else
		return false;
	}
	/**
	* Returns the number of elements in the queue
	* author team
	* @return size of queue
	*/
	
	public int size() {
		// TODO Auto-generated method stub //return size of queue
		return this.counter;
	}

	

}
