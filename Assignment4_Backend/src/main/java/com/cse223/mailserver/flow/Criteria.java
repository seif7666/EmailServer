package com.cse223.mailserver.flow;

import java.util.ArrayList;

public interface Criteria {
    public ArrayList<? extends Message> filterCriteria (ArrayList<? extends Message> messages);
}
