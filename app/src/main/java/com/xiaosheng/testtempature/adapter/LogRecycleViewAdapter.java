package com.xiaosheng.testtempature.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaosheng.testtempature.R;
import com.xiaosheng.testtempature.entity.TempatureHistory;

import java.util.List;

public class LogRecycleViewAdapter extends RecyclerView.Adapter<LogRecycleViewAdapter.ViewHolder>  {

    private List<TempatureHistory> list;
    private OnItemClickListener listener;

    public LogRecycleViewAdapter(List<TempatureHistory> list) {
        this.list = list;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (null== list){
            return;
        }
        TempatureHistory log = list.get(position);
        holder.timestamp.setText( log.getTimestamp());
        holder.line.setText( String.valueOf(log.getLine()));
        holder.json.setText( log.getJson());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null==list){
            return 0;
        }
        return list.size();
    }



     class ViewHolder extends RecyclerView.ViewHolder {
        TextView timestamp;
        TextView line;

        TextView json;

        public ViewHolder(View itemView) {
            super(itemView);
            this.timestamp = itemView.findViewById(R.id.log_timestamp);
            this.json = itemView.findViewById(R.id.log_json);
            this.line = itemView.findViewById(R.id.log_line);
        }
    }
}
