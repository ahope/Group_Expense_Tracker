package edu.neu.mad_sea.lishawang.groupexpensetracker.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    private String groupTitle;
    private String groupDescription;
    private List<String> groupMembers;
    private List<Expense> groupExpenses;

    public Group(String groupTitle, String groupDescription, List<String> groupMembers) {
        this.groupTitle = groupTitle;
        this.groupDescription = groupDescription;
        this.groupMembers = groupMembers;
        this.groupExpenses = new ArrayList<>();
    }

    public Group(String groupTitle, String groupDescription, List<String> groupMembers,
                 List<Expense> expenseList) {
        this.groupTitle = groupTitle;
        this.groupDescription = groupDescription;
        this.groupMembers = groupMembers;
        this.groupExpenses = expenseList;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public List<Expense> getGroupExpenses() {
        return groupExpenses;
    }

    public void setGroupExpenses(List<Expense> groupExpenses) {
        this.groupExpenses = groupExpenses;
    }
}
