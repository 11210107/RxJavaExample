package com.example.wangzhen.rxjavaexample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wangzhen.rxjavaexample.R;
import com.example.wangzhen.rxjavaexample.domain.GankBeauty;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wangzhen on 17/2/7.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter {


    private Context          mContext;
    private List<GankBeauty> mDatas;
    private OnRecyclerItemClickListener mOnRecyclerItemClickListener;

    public RecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item_layout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRecyclerItemClickListener != null) {
                    mOnRecyclerItemClickListener.onItemClick(view, (int) view.getTag());
                }
            }
        });
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
        GankBeauty gankBeauty = mDatas.get(position);
        Glide.with(mContext).load(gankBeauty.url).into(recyclerViewHolder.mIvImage);
        recyclerViewHolder.mTvImageDes.setText(gankBeauty.createdAt);

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setDatas(List<GankBeauty> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_image)
        ImageView mIvImage;
        @Bind(R.id.tv_image_des)
        TextView  mTvImageDes;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnRecyclerItemClickListener {
        void onItemClick(View view,int position);
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

}
