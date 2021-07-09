package com.bayi.rerobot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bayi.rerobot.R;
import com.tobot.slam.data.LocationBean;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class SourceListAdapter extends RecyclerView.Adapter<SourceListAdapter.MyViewHolder> {

    private Context context;
    private List<LocationBean> list;

    public SourceListAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_source_list_item, parent, false);
        return new MyViewHolder(view);
    }

    private int selectedPosition = -1;
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPosition == holder.getAdapterPosition());
        if (selectedPosition == position) {
            holder.sourceName.setTextColor(Color.parseColor("#ffffff"));
            holder.rootLayout.setBackgroundResource(R.drawable.area_click1);
        } else {
            holder.sourceName.setTextColor(Color.parseColor("#ffffff"));
            holder.rootLayout.setBackgroundResource(R.drawable.area_normal);
        }
        holder.sourceName.setText(list.get(position).getLocationNumber());
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
                if (onSelectedListener != null) {
                    onSelectedListener.onSelected(selectedPosition,list.get(selectedPosition));
                }
            }
        });
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener{
        /**
         * 监听选中事件
         * @param position 选中的item下标
         *
         */
        void onSelected(int position, LocationBean bean);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rootLayout;
        TextView sourceName;
        MyViewHolder(View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            sourceName = itemView.findViewById(R.id.sourceName);
        }
    }

    public void setData(List<LocationBean> list) {
        this.list = list;
        selectedPosition = -1;
        this.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

