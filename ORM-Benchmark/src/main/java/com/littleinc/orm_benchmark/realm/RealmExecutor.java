package com.littleinc.orm_benchmark.realm;
import android.content.Context;
import android.util.Log;

import com.littleinc.orm_benchmark.BenchmarkExecutable;
import com.littleinc.orm_benchmark.ormlite.*;
import com.littleinc.orm_benchmark.util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


import static com.littleinc.orm_benchmark.util.Util.getRandomString;

/**
 * Created by kgalligan on 6/17/15.
 */
public class RealmExecutor implements BenchmarkExecutable
{

    private static final String TAG = "RealmExecutor";

    //private DataBaseHelper mHelper;
    private Context mContext;

    @Override
    public void init(Context context, boolean useInMemoryDb)
    {
        mContext = context;
        Realm.getInstance(context);

    }

    @Override
    public String getOrmName()
    {
        return "Realm";
    }

    @Override
    public long createDbStructure() throws SQLException
    {
        return 0;
    }

    @Override
    public long writeWholeData() throws SQLException
    {
        List<User> users = new ArrayList<User>(NUM_USER_INSERTS);
        for (int i = 0; i < NUM_USER_INSERTS; i++) {
            User newUser = new User();
            newUser.setId(i);
            newUser.setLastName(getRandomString(10));
            newUser.setFirstName(getRandomString(10));

            users.add(newUser);
        }

        List<Message> messages = new ArrayList<Message>(NUM_MESSAGE_INSERTS);
        for (int i = 0; i < NUM_MESSAGE_INSERTS; i++) {
            Message newMessage = new Message();
            newMessage.setId(i);
            newMessage.setCommandId(i);
            newMessage.setSortedBy(System.nanoTime());
            newMessage.setContent(Util.getRandomString(100));
            newMessage.setClientId(System.currentTimeMillis());
            newMessage
                    .setSenderId(Math.round(Math.random() * NUM_USER_INSERTS));
            newMessage
                    .setChannelId(Math.round(Math.random() * NUM_USER_INSERTS));
            newMessage.setCreatedAt((int) (System.currentTimeMillis() / 1000L));

            messages.add(newMessage);
        }

        Realm realm = Realm.getInstance(mContext);

        long start = System.nanoTime();

        realm.beginTransaction();

        for(User newUser : users)
        {
            User realmUser = realm.createObject(User.class);
            realmUser.setId(newUser.getId());
            realmUser.setFirstName(newUser.getFirstName());
            realmUser.setLastName(newUser.getLastName());
        }

        String userLog = "Done, wrote " + NUM_USER_INSERTS + " users" + (System.nanoTime() - start);

        long messageStart = System.nanoTime();

        for(Message message : messages)
        {
            Message realmMessage = realm.createObject(Message.class);
            realmMessage.setId(message.getId());
            realmMessage.setChannelId(message.getChannelId());
            realmMessage.setClientId(message.getClientId());
            realmMessage.setCommandId(message.getCommandId());
            realmMessage.setContent(message.getContent());
            realmMessage.setCreatedAt(message.getCreatedAt());
            realmMessage.setSenderId(message.getSenderId());
        }

        realm.commitTransaction();

        long totalTime = System.nanoTime() - start;

//        realm.close();

        Log.d(TAG, userLog);
        Log.d(TAG, "Done, wrote " + NUM_MESSAGE_INSERTS + " messages"  + (System.nanoTime() - messageStart));

        return totalTime;
    }

    @Override
    public long readWholeData() throws SQLException
    {
        Realm realm = Realm.getInstance(mContext);

        long start = System.nanoTime();

        RealmQuery<User> userQuery = realm.where(User.class);
        RealmResults<User> userResults = userQuery.findAll();
        for(User user : userResults)
        {
            int id = user.getId();
            String first =  user.getFirstName();
            String last = user.getLastName();
        }

        String userLog = "Read " + NUM_USER_INSERTS + " users in " + (System.nanoTime() - start);

        long messageStart = System.nanoTime();

        RealmQuery <Message> messageQuery = realm.where(Message.class);
        RealmResults<Message> messageResults =messageQuery.findAll();
        for(Message message : messageResults)
        {
            int id = message.getId();
            long channel = message.getChannelId();
            long client = message.getClientId();
            long command = message.getCommandId();
            String content = message.getContent();
            int created = message.getCreatedAt();
            long sender = message.getSenderId();
            double sorted = message.getSortedBy();
        }

        long totalTime = System.nanoTime() - start;

//        realm.close();
        Log.d(TAG, userLog);
        Log.d(TAG,
              "Read " + NUM_MESSAGE_INSERTS + " messages in " + (System.nanoTime() - messageStart));

        return totalTime;
    }

    @Override
    public long readIndexedField() throws SQLException
    {
        return 0;
    }

    @Override
    public long readSearch() throws SQLException
    {
        return 0;
    }

    @Override
    public long dropDb() throws SQLException
    {
        long start = System.nanoTime();
        Realm realm = Realm.getInstance(mContext);

        realm.beginTransaction();
        realm.where(User.class).findAll().clear();
        realm.where(Message.class).findAll().clear();
        realm.commitTransaction();

//        realm.close();
        return System.nanoTime() - start;
    }
}
