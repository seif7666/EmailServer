package com.cse223.mailserver.library;

import java.io.Serializable;

public class doubleLinkedList implements ILinkedList,Serializable {
	public dnode head=null;
	public dnode last=null;
	public int size =0;
	public void printLinkekList() {
		dnode i=head;
		if(i==null) {
			System.out.println("Empty linked List");
		}
		else {			//function tom print linkead List
		while(i!=null) {
			System.out.print(i.data);
			i=i.next;
		}
	  }
	}

	@Override
	public void add(int index, Object element) {
		// TODO Auto-generated method stub
		if(index<0) {
			System.out.println("Not Valid");
			return;
		}
		dnode newNode =new dnode(element);  //function to add element with indix 
		dnode i=head;					//if there isn't exisi return null
		dnode detect=head;
		
		 if(size==0&&index!=0) {
			System.out.println("Not Valid");
			return ;
		}
		 if(size==0&&index==0) {
			 add(element);
		 }
		else  if(index==0) {
				newNode.next=head;
				newNode.previous=null;
				head=newNode;
				size++;
			}
		else if(size>index) {
		
		for(int counter =1; counter<index;counter++) {
			i=i.next;
		}
		newNode.next=i.next;
		i.next.previous=newNode;
		newNode.previous=i;
		i.next=newNode;
		size++;
		}
		else if(size==index)add(element);
		else {
			System.out.println("Not Valid");
			return;
		}
	}
	@Override
	public void add(Object element) {
		// TODO Auto-generated method stub
		dnode newNode =new dnode(element);
		dnode i=head;
		dnode detect=head;
		if(size==0) {
			newNode.next=head;		//add in the last
			newNode.previous=null;
			head=last=newNode;
			size++;
		}
		else {
		for(int counter =1; counter<size;counter++) {
			i=i.next;
		}
		newNode.next=i.next;
		last.next=newNode;
		newNode.previous=last;
		last=newNode;
		size++;
		}
	}
	@Override
	public Object get(int index) {
		// TODO Auto-generated method stub
		dnode detect=head;
		dnode i=head;
		if(size==0) {	//get element with indix
			return null;
		}
		else if(index<0) {
			System.out.println("not avalid");
			return null;
		}
		else if(index>=size) {
			System.out.println("not avalid");
			return null;
		}
		else {
			for(int counter =0; counter<index;counter++) {
				i=i.next;
			}
			
			return i.data;
		}
		
	}

	@Override
	public void set(int index, Object element) {
		// TODO Auto-generated method stub
		dnode detect=head;
		dnode i=head;
		if(size==0) { 		//set element with indix
			System.out.println("not avalid");
			return;
		}
		else if(index<0) {
			System.out.println("not avalid");
			return;
		}
		else if(index>=size) {
			System.out.println("not avalid");
			return ;
		}
		else {
			for(int counter =0; counter<index;counter++) {
				i=i.next;
			}
			i.data=element;
			return;
		}
		
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		head=last=null;
		size=0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if(head==null) {
		return true;
		}
		else {
			return false;
		}
		
	}

	@Override
	public void remove(int index) {
		// TODO Auto-generated method stub
		dnode detect=head;
		dnode i=head;
		dnode j;
		dnode next;
		if(size==0) {
			System.out.println("not avalid");
			return ;
		}
		else if(index<0) {
			System.out.println("not avalid");
			return ;
		}
		else if(index==0) {
			head=head.next;
			size--;
			
		}
		else if(index==size-1) {
			last=last.previous;
			last.next=null;
			size--;
		}
		else if(index>=size) {
			System.out.println("not avalid");
			return ;
		}
		else {
			for(int counter =1; counter<index;counter++) {
				i=i.next;
			}
			j=i.next;
			next=j.next;
			next.previous=i;
			i.next=next;
			j.next=null;
			j.previous=null;
			
			size--;
		}
		
		
		
		
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public ILinkedList sublist(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		doubleLinkedList sub =new doubleLinkedList();
		dnode indicator=head;
		int i=0;
		if(fromIndex<0||toIndex<0) {
			return null;
		}
		if(size==0)return null;
		if(fromIndex>=size||toIndex>=size) return null;
		for(int counter =0; counter<=toIndex;counter++) {
			if(counter>=fromIndex&&counter<=toIndex)
				sub.add(indicator.data);
			indicator=indicator.next;
			i++;
		}
		//sub.printLinkekList();
		if(i!=0) {
			return sub;
		}else {
			return null;
		}
		
	}
	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		dnode detect=head;
		detect=head;
		for(int counter =0; counter<size;counter++) {
			if(detect.data==o) {
				return true;
			}
			detect=detect.next;
		}
		return false;
	}

}
