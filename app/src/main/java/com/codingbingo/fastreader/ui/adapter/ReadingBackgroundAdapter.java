package com.codingbingo.fastreader.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.manager.SettingManager;
import com.codingbingo.fastreader.model.eventbus.StyleChangeEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/4/12.
 * <p>
 * 阅读背景设置
 */

public class ReadingBackgroundAdapter extends RecyclerView.Adapter<ReadingBackgroundAdapter.ViewHolder> {

    private Context mContext;
    private List<String> backgroundColorList;

    public ReadingBackgroundAdapter(Context mContext, List<String> backgroundColorList) {
        this.mContext = mContext;
        this.backgroundColorList = backgroundColorList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.read_background_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GradientDrawable gradientDrawable = (GradientDrawable) holder.readBackgroundColor.getBackground();
        final String color = backgroundColorList.get(position);
        gradientDrawable.setColor(Color.parseColor(color));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingManager.getInstance().setReadMode(false);
                SettingManager.getInstance().setReadBackground(color);

                EventBus.getDefault().post(new StyleChangeEvent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return backgroundColorList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View readBackgroundColor;

        public ViewHolder(View itemView) {
            super(itemView);

            readBackgroundColor = itemView.findViewById(R.id.read_background_color);
        }
    }
}
