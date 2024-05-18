package com.xiaosheng.testtempature.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xiaosheng.testtempature.R;
import com.xiaosheng.testtempature.entity.TempatureHistory;
import com.xiaosheng.testtempature.entity.TestTemEntity;

import java.util.List;

public class TestTempatureAdapter extends ArrayAdapter<TempatureHistory> {
    private int resourceId;
    public TestTempatureAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    public TestTempatureAdapter(Context context, int resource, List<TempatureHistory> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TempatureHistory book = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            viewHolder = new ViewHolder();

            viewHolder.timestamp = (TextView)view.findViewById(R.id.log_timestamp);
            viewHolder.json = (TextView) view.findViewById(R.id.log_json);
            viewHolder.line = (TextView)view.findViewById(R.id.log_line);
            view.setTag(viewHolder);//将viewHolder存储在view中
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag(); //重新获取viewHolder
        }

        viewHolder.timestamp.setText(String.valueOf(book.getTimestamp()));
        viewHolder.line.setText(String.valueOf(book.getLine()));
        viewHolder.json.setText(String.valueOf(book.getJson()));


        return view;
    }
    class ViewHolder{
        TextView timestamp;
        TextView line;

        TextView json;
    }

}
