package com.example.wangzhen.rxjavaexample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wangzhen.rxjavaexample.R;
import com.example.wangzhen.rxjavaexample.domain.CardImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangzhen on 17/1/17.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter {

    List<CardImage> datas;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder viewHolder = (ImageViewHolder) holder;
        CardImage cardImage = datas.get(position);
        viewHolder.description.setText(cardImage.description);
        Glide.with(holder.itemView.getContext()).load(cardImage.image_url).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void setDatas(List<CardImage> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_image)
        ImageView imageView;
        @Bind(R.id.tv_description)
        TextView  description;
        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
