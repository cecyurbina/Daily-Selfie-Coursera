package com.dailyselfie.coursera.dailyselfiec.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dailyselfie.coursera.dailyselfiec.R;

import com.dailyselfie.coursera.dailyselfiec.view.ui.RowData;

import java.util.List;

/**
 * Created by root on 17/11/15.
 */
public class CustomAdapter extends BaseAdapter {

    List<RowData> myList;
    LayoutInflater inflater;
    Context context;


    public CustomAdapter(Context context, List<RowData> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);        // only context can also be used
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public RowData getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_list, null);
            mViewHolder = new MyViewHolder();
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.tvTitle = detail(convertView, R.id.firstLine, myList.get(position).getTitle());
        mViewHolder.tvDesc  = detail(convertView, R.id.secondLine,  myList.get(position).getDescription());
        mViewHolder.ivIcon  = detailI(convertView, R.id.icon,  myList.get(position).getImgResId());

        return convertView;
    }

    // or you can try better way
    private TextView detail(View v, int resId, String text) {
        TextView tv = (TextView) v.findViewById(resId);
        tv.setText(text);
        return tv;
    }

    private ImageView detailI(View v, int resId, String icon) {
        ImageView iv = (ImageView) v.findViewById(resId);
        //iv.setImageResource(icon);
        Bitmap bMap = BitmapFactory.decodeFile(icon);
        iv.setImageBitmap(bMap);
        return iv;
    }

    private class MyViewHolder {
        TextView tvTitle, tvDesc;
        ImageView ivIcon;
    }
}