package com.littleinc.orm_benchmark.realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by kgalligan on 6/17/15.
 */
public class User extends RealmObject
{
    private int    id;
    private String lastName;
    private String firstName;

    public int getId()
    {
        return id;
    }

    public void setId(int mId)
    {
        this.id = mId;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String mLastName)
    {
        this.lastName = mLastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String mFirstName)
    {
        this.firstName = mFirstName;
    }
}
