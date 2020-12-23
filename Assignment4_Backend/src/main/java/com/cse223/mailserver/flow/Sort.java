package com.cse223.mailserver.flow;

import com.cse223.mailserver.library.doubleLinkedList;
import com.cse223.mailserver.library.stackImplementation;

import java.util.HashSet;
import java.util.Set;


public class Sort {
	private String attributeOfSorting="";
    public Sort(String attributeOfSorting) {
        this.attributeOfSorting=attributeOfSorting;
    }
    
    public doubleLinkedList sorting(doubleLinkedList  s  ) {
        // TODO Auto-generated method stub
        stackImplementation stack = new stackImplementation();
        stack.push(0);
        stack.push(s.size());
        while (!stack.isEmpty()) {
            int End = (int) stack.pop();
            int start = (int) stack.pop();
            if (End - start < 2) {
                continue;
            }
            int p = start + ((End - start) / 2);
            if(!attributeOfSorting.equals("")) p = partitionOne(s, p, start, End);
            else  p = partitionTwo(s, p, start, End);
            stack.push(p + 1);
            stack.push(End);
            stack.push(start);
            stack.push(p);
        }
        return s;
    }
    private int partitionOne(doubleLinkedList s, int pos, int start, int end) {
        int l = start;
        int h = end - 2;
        MessageCreator q = (MessageCreator) s.get(pos);
        MessageCreator piv = q;
        Set<Boolean> validConditions = new HashSet<Boolean>();
        Set<Boolean> inverseValidConditions = new HashSet<Boolean>();
        swap(s, pos, end - 1 ,true);
        while (l < h) {
            MessageCreator r = (MessageCreator) s.get(l);
            MessageCreator t = (MessageCreator) s.get(h);
            
            validConditions.add((attributeOfSorting.equals(Constants.SENDER))&&(r.getHeader().getSender()).compareToIgnoreCase(piv.getHeader().getSender())<0);
            validConditions.add((attributeOfSorting.equals(Constants.SUBJECT))&&(r.getHeader().getSubject()).compareToIgnoreCase(piv.getHeader().getSubject())<0);
            validConditions.add((attributeOfSorting.equals(Constants.BODY))&&(r.getBody().getBody()).compareToIgnoreCase(piv.getBody().getBody())<0);
           
            inverseValidConditions.add((attributeOfSorting.equals(Constants.SENDER))&&(t.getHeader().getSender()).compareToIgnoreCase(piv.getHeader().getSender())>=0);
            inverseValidConditions.add((attributeOfSorting.equals(Constants.SUBJECT))&&(t.getHeader().getSubject()).compareToIgnoreCase(piv.getHeader().getSubject())>=0);
            inverseValidConditions.add((attributeOfSorting.equals(Constants.BODY))&&(t.getBody().getBody()).compareToIgnoreCase(piv.getBody().getBody())>=0);
            if (validConditions.contains(true)) {
                l++;
            } else if (inverseValidConditions.contains(true)) {
                h--;
            } else {
                swap(s, l, h ,true);
            }
            validConditions.clear();
            inverseValidConditions.clear();
        }
        int index = h;
        MessageCreator t = (MessageCreator) s.get(h);
        validConditions.add((attributeOfSorting.equals(Constants.SENDER))&&(t.getHeader().getSender()).compareToIgnoreCase(piv.getHeader().getSender())<0);
        validConditions.add((attributeOfSorting.equals(Constants.SUBJECT))&&(t.getHeader().getSubject()).compareToIgnoreCase(piv.getHeader().getSubject())<0);
        validConditions.add((attributeOfSorting.equals(Constants.BODY))&&(t.getBody().getBody()).compareToIgnoreCase(piv.getBody().getBody())<0);
        if (validConditions.contains(true)) {
            index++;
        }
        swap(s,end - 1,index ,true);
        return index;
    }
    private void swap(doubleLinkedList s, int i, int j ,boolean flag) {
        if(flag) {
            MessageCreator temp = (MessageCreator) s.get(i);
            s.set(i,s.get(j));
            s.set(j,temp);
        } else {
            String temp = (String) s.get(i);
            s.set(i,s.get(j));
            s.set(j,temp);
        }
    }
    private int partitionTwo(doubleLinkedList s, int pos, int start, int end) {
        int l = start;
        int h = end - 2;
        String q = (String) s.get(pos);
        String piv = q;
        swap(s, pos, end - 1 ,false);
        while (l < h) {
            String  r = (String) s.get(l);
            String t = (String) s.get(h);
            if ((r).compareToIgnoreCase(piv) < 0) {
                l++;
            } else if ((t).compareToIgnoreCase(piv) >= 0) {
                h--;
            } else {
                swap(s, l, h ,false);
            }
        }
        int index = h;
        String t = (String) s.get(h);
        if (t.compareToIgnoreCase(piv) < 0) {
            index++;
        }
        swap(s, end - 1, index ,false);
        return index;
    }

}
