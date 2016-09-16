package net.levinh.demochatusingfirebase.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.levinh.demochatusingfirebase.R;
import net.levinh.demochatusingfirebase.models.Message;

/**
 * Created by levinhtxbt@gmail.com on 13/09/2016.
 */

public class ItemViewHoler extends RecyclerView.ViewHolder {

    TextView lblAuthor;
    TextView lblMessage;

    public ItemViewHoler(View itemView) {
        super(itemView);
        lblAuthor = (TextView) itemView.findViewById(R.id.lblAuthor);
        lblMessage = (TextView) itemView.findViewById(R.id.lblMessage);
    }

    public void bindData(Message message) {
        lblAuthor.setText(message.getDisplayName());
        lblMessage.setText(message.getMessage());
    }
}
