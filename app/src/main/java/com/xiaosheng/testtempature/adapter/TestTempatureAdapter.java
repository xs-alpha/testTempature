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
import com.xiaosheng.testtempature.entity.TestTemEntity;

import java.util.List;

public class TestTempatureAdapter extends ArrayAdapter<TestTemEntity> {
    private int resourceId;
    public TestTempatureAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    public TestTempatureAdapter(Context context, int resource, List<TestTemEntity> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TestTemEntity book = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            viewHolder = new ViewHolder();

            viewHolder.group = (TextView)view.findViewById(R.id.text_id);
            viewHolder.ae1 = (EditText) view.findViewById(R.id.tm_e1);
            viewHolder.ae2 = (EditText)view.findViewById(R.id.tm_e2);
            viewHolder.ae3 = (EditText) view.findViewById(R.id.tm_e3);
            viewHolder.ae4 = (EditText) view.findViewById(R.id.tm_e4);
            viewHolder.ae5 = (EditText) view.findViewById(R.id.tm_e5);
            viewHolder.ae6 = (EditText) view.findViewById(R.id.tm_e6);
            viewHolder.ae7 = (EditText) view.findViewById(R.id.tm_e7);
//            viewHolder.ae8 = (EditText) view.findViewById(R.id.tm_e8);

            viewHolder.at1 = (TextView) view.findViewById(R.id.tm_a1);
            viewHolder.at2 = (TextView) view.findViewById(R.id.tm_a2);
            viewHolder.at3 = (TextView) view.findViewById(R.id.tm_a3);
            viewHolder.at4 = (TextView) view.findViewById(R.id.tm_a4);
            viewHolder.at5 = (TextView) view.findViewById(R.id.tm_a5);
            viewHolder.at6 = (TextView) view.findViewById(R.id.tm_a6);
            viewHolder.at7 = (TextView) view.findViewById(R.id.tm_a7);

            view.setTag(viewHolder);//将viewHolder存储在view中
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag(); //重新获取viewHolder
        }

        viewHolder.group.setText(book.getGroup());
        viewHolder.ae1.setText(String.valueOf(book.getAe1()));
        viewHolder.ae2.setText(String.valueOf(book.getAe2()));
        viewHolder.ae3.setText(String.valueOf(book.getAe3()));
        viewHolder.ae4.setText(String.valueOf(book.getAe4()));
        viewHolder.ae5.setText(String.valueOf(book.getAe5()));
        viewHolder.ae6.setText(String.valueOf(book.getAe6()));
        viewHolder.ae7.setText(String.valueOf(book.getAe7()));
//        viewHolder.ae8.setText(String.valueOf(book.getAe8()));

        viewHolder.at1.setText(String.valueOf(book.getAt1()));
        viewHolder.at2.setText(String.valueOf(book.getAt2()));
        viewHolder.at3.setText(String.valueOf(book.getAt3()));
        viewHolder.at4.setText(String.valueOf(book.getAt4()));
        viewHolder.at5.setText(String.valueOf(book.getAt5()));
        viewHolder.at6.setText(String.valueOf(book.getAt6()));
        viewHolder.at7.setText(String.valueOf(book.getAt7()));
//        viewHolder.at8.setText(String.valueOf(book.getAt8()));

        return view;
    }
    class ViewHolder{
        TextView group;
        TextView at1;
        EditText ae1;
        TextView at2;
        EditText ae2;
        TextView at3;
        EditText ae3;
        TextView at4;
        EditText ae4;
        TextView at5;
        EditText ae5;
        TextView at6;
        EditText ae6;
        TextView at7;
        EditText ae7;
        TextView at8;
        EditText ae8;
    }

}
