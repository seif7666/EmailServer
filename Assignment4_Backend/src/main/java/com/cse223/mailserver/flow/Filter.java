package com.cse223.mailserver.flow;

import com.cse223.mailserver.library.doubleLinkedList;
import com.cse223.mailserver.library.singlyLinkedList;
import com.cse223.mailserver.library.stackImplementation;

import java.text.SimpleDateFormat;
import java.util.Date;



public class Filter {
	private String attribute;
    public Filter(String attribute){
    	this.attribute = attribute;
    	}
    
    public doubleLinkedList binarySearchEmails1(doubleLinkedList dl , String key){
        stackImplementation stack = new stackImplementation();
        stack.push(0);
        stack.push(dl.size-1);
        int mid = 0;
        singlyLinkedList stateOne = new singlyLinkedList();
        singlyLinkedList stateTwo = new singlyLinkedList();
        doubleLinkedList list = new doubleLinkedList();
        while(!stack.isEmpty()){
            int high = (int)stack.pop();
            int low = (int)stack.pop();
            mid = (low + high) / 2;
            stateOne.add(attribute.equals("Sender") && key.compareToIgnoreCase(((Message)dl.get(mid)).getHeader().getSender()) < 0);
            stateOne.add(attribute.equals("Subject") && key.compareToIgnoreCase(((Message)dl.get(mid)).getHeader().getSubject()) < 0);
            stateTwo.add(attribute.equals("Sender") && key.compareToIgnoreCase(((Message)dl.get(mid)).getHeader().getSender()) > 0);
            stateTwo.add(attribute.equals("Subject") && key.compareToIgnoreCase(((Message)dl.get(mid)).getHeader().getSubject()) > 0);
            if(low > high)  return null;
            if(stateOne.contains(true)){
                stack.push(low);
                stack.push(mid-1);
            }
            else if(stateTwo.contains(true)){
                stack.push(mid+1);
                stack.push(high);
            }
            else {
                list.add((Message)dl.get(mid));
                int j = mid;
                while (attribute.equals("Sender") && (j != dl.size-1) && key.compareToIgnoreCase(((Message)dl.get(++j)).getHeader().getSender()) == 0)
                    list.add((Message)dl.get(j));
                while (attribute.equals("Subject") && (j != dl.size-1) && key.compareToIgnoreCase(((Message)dl.get(++j)).getHeader().getSubject()) == 0)
                    list.add((Message)dl.get(j));
                j = mid;
                while (attribute.equals("Sender") && (j != 0) && key.compareToIgnoreCase(((Message)dl.get(--j)).getHeader().getSender()) == 0)
                    list.add((Message)dl.get(j));
                while (attribute.equals("Subject") && (j != 0) && key.compareToIgnoreCase(((Message)dl.get(--j)).getHeader().getSubject()) == 0)
                    list.add((Message)dl.get(j));
            }
            stateOne.clear();
            stateTwo.clear();
        }
        return list;
    }
    public doubleLinkedList binarySearchEmails2(doubleLinkedList dl , Date key){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        stackImplementation stack = new stackImplementation();
        stack.push(0);
        stack.push(dl.size-1);
        int mid = 0;
        doubleLinkedList list = new doubleLinkedList();
        while (!stack.isEmpty()){
            int high = (int)stack.pop();
            int low = (int)stack.pop();
            mid = (low + high) / 2;
            if(low > high)  return null;
            if(simpleDateFormat.format(key).compareTo(simpleDateFormat.format(((Message)dl.get(mid)).getTime())) > 0){
                stack.push(low);
                stack.push(mid-1);
            }
            else if(simpleDateFormat.format(key).compareTo(simpleDateFormat.format(((Message)dl.get(mid)).getTime())) < 0){
                stack.push(mid+1);
                stack.push(high);
            }
            else {
                list.add((Message)dl.get(mid));
                int j = mid;
                while ((j != dl.size-1) && simpleDateFormat.format(key).compareTo(simpleDateFormat.format(((Message)dl.get(++j)).getTime()))==0)
                    list.add((Message)dl.get(j));
                j = mid;
                while ((j != 0) && simpleDateFormat.format(key).compareTo(simpleDateFormat.format(((Message)dl.get(--j)).getTime()))==0)
                    list.add((Message)dl.get(j));
            }
        }
        return list;
    }
    public doubleLinkedList binarySearchEmails3(doubleLinkedList dl ,String key){
        doubleLinkedList list = new doubleLinkedList();
        Sort sort = new Sort("");
        for (int i = 0; i < dl.size; i++) {
          //  ((Message)dl.get(i)).savedReceivers = sort.sorting(((Message)dl.get(i)).savedReceivers);
           // if(binarySearchStrings(((Message)dl.get(i)).savedReceivers , key))
           //     list.add((Message)dl.get(i));
        }
        return list;
    }
    public boolean binarySearchStrings(doubleLinkedList dl ,String key){
        stackImplementation stack = new stackImplementation();
        stack.push(0);
        stack.push(dl.size-1);
        int mid = 0;
        while (!stack.isEmpty()){
            int high = (int)stack.pop();
            int low = (int)stack.pop();
            mid = (low + high) / 2;
            if(low > high)  return false;
            if(key.compareToIgnoreCase((String)dl.get(mid)) < 0){
                stack.push(low);
                stack.push(mid-1);
            }
            else if(key.compareToIgnoreCase((String)dl.get(mid)) > 0){
                stack.push(mid+1);
                stack.push(high);
            }
        }
        return true;
    }
    public doubleLinkedList linearSearchBody(doubleLinkedList dl ,String key){
        doubleLinkedList list = new doubleLinkedList();
        for (int i = 0; i < dl.size(); i++) {
            if(((Message)dl.get(i)).getBody().getBody().contains(key)){
                list.add((Message)dl.get(i));
            }
        }
        return list;
    }

}
