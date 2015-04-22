package com.students.routingmstu;

/**
 * Created by mosin on 22.04.2015.
 */
public class Point {
    public int Id;
    public String ShortName;
    public String FullName;
    public boolean IsImportant;
    public boolean IsVisited;
    public int Weight;

    @Override
    public String toString() {
        return ShortName;
    }
}
