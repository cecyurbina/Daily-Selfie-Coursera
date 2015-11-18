package com.dailyselfie.coursera.dailyselfiec.view.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
    boolean inFolder;


    public CustomAdapter(Context context, List<RowData> myList, boolean aInFolder) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);        // only context can also be used
        inFolder = aInFolder;
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
        mViewHolder.ivIcon  = detailI(convertView, R.id.icon, myList.get(position).getImgResId());
        mViewHolder.bigImage  = detailI(convertView, R.id.imageView2,  myList.get(position).getImgResId());
        if (inFolder){
            if (position == 0) {
                mViewHolder.bigImage.setVisibility(View.VISIBLE);
                mViewHolder.tvTitle.setVisibility(View.GONE);
                mViewHolder.tvDesc.setVisibility(View.GONE);
                mViewHolder.ivIcon.setVisibility(View.GONE);

            } else {
                mViewHolder.bigImage.setVisibility(View.GONE);
                mViewHolder.tvTitle.setVisibility(View.VISIBLE);
                mViewHolder.tvDesc.setVisibility(View.VISIBLE);
                mViewHolder.ivIcon.setVisibility(View.VISIBLE);
            }
        }
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
        ImageView bigImage;
    }
}