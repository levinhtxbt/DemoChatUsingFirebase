package net.levinh.demochatusingfirebase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;

import net.levinh.demochatusingfirebase.R;
import net.levinh.demochatusingfirebase.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by levinhtxbt@gmail.com on 13/09/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHoler> {
    private static final int RECEIVE = 1;
    private static final int SEND = 2;
    Context mContext;
    int mLayoutID;
    List<Message> mListMessage;
    OnItemClickListener callback;
    String user;

    public RecyclerViewAdapter(Context context, int layoutResourceID, OnItemClickListener callback) {
        this(context, layoutResourceID, null, callback);
    }

    public RecyclerViewAdapter(Context context, int layoutResourceID, List<Message> list, OnItemClickListener callback) {
        this.mContext = context;
        this.mLayoutID = layoutResourceID;
        if (mListMessage != null) {
            setMessage(mListMessage);
        }
        this.callback = callback;
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("Adapter", "RecyclerViewAdapter: " + user);
    }

    @Override
    public ItemViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == SEND) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview, parent, false);
        } else
            view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview2, parent, false);
        return new ItemViewHoler(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHoler holder, int position) {
        final Message data = getItem(position);
        holder.bindData(data);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onItemClick(data);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mListMessage.get(position).getName().equalsIgnoreCase(user) ?
                SEND : RECEIVE;
    }


    public Message getItem(int position) {
        return mListMessage == null ? null : mListMessage.get(position);
    }

    @Override
    public int getItemCount() {
        return mListMessage == null ?
                0 : mListMessage.size();
    }

    public void setMessage(List<Message> list) {
        if (list != null) {
            mListMessage = list;
            notifyDataSetChanged();
        }
    }

    public List<Message> getMessage() {
        return mListMessage;
    }

    public void pushMessage(Message message) {
        if (mListMessage == null) {
            mListMessage = new ArrayList<>();
        }
        mListMessage.add(message);
        notifyItemInserted(mListMessage.size() - 1);
    }

    public void removeMessage(Message msg) {
        int position = mListMessage.indexOf(msg);
        mListMessage.remove(msg);
        notifyItemRemoved(position);
    }

    public void editMessage(Message msg) {
        int position = mListMessage.indexOf(msg);
        Message _msg = mListMessage.get(position);
        _msg.setMessage(msg.getMessage());
        notifyItemChanged(position);
    }

}
