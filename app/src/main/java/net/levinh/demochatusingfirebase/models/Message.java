package net.levinh.demochatusingfirebase.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

/**
 * Created by levinhtxbt@gmail.com on 13/09/2016.
 */
@IgnoreExtraProperties
public class Message {

    private String name;
    private String message;
    private long time;
    @Exclude
    private String displayName;

    public Message() {
    }

    public Message(String name, String message, long time) {
        this.name = name;
        this.message = message;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            Message message = ((Message) obj);
            return message.time == time;
        }
        return false;
    }
}
