package com.bayi.rerobot.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.R;
import com.bayi.rerobot.bean.ChatMsg;

import java.util.List;

/**
 * Created by tongzn
 * on 2021/1/5
 */
public class danganAdapter extends RecyclerView.Adapter<danganAdapter.ViewHolder>{
    private List<ChatMsg> mMsgList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rihgtMsg;

        public ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rihgtMsg = (TextView) view.findViewById(R.id.right_msg);
        }
    }

    public danganAdapter(List<ChatMsg> msgList) {
        mMsgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dangan_chat_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMsg msg = mMsgList.get(position);
        if (!msg.isLeft()) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getMsg());
        } else  {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rihgtMsg.setText(msg.getMsg());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
