package com.cse223.mailserver.flow;


import java.util.ArrayList;

public class andFilter  implements Criteria{
    private Criteria mySubject;
    private Criteria reciever;

    public andFilter(Criteria Subject,Criteria reciever) {
        mySubject=Subject;
        this.reciever=reciever;
    }

    @Override
    public ArrayList<? extends Message> filterCriteria(ArrayList<? extends Message> messages) {
        ArrayList<? extends Message> subjectCriteria = mySubject.filterCriteria(messages);
        return reciever.filterCriteria(subjectCriteria) ;
    }
}
