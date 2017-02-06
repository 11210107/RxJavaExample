package com.example.wangzhen.rxjavaexample.adapter;

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
 * Created by wangzhen on 17/2/3.
 */
public class MapItemListAdapter extends RecyclerView.Adapter {

    List<GankBeauty> datas;
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //普通Item
    private static final int TYPE_ITEM        = 0;
    //底部FooterItem
    private static final int TYPE_FOOTER_ITEM = 1;
    //上拉加载更多的状态,默认是0
    private static int footer_item_status = 0;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
            return new MapItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_footer_item, parent, false);
            return new FooterItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MapItemViewHolder) {
            MapItemViewHolder mapItemViewHolder = (MapItemViewHolder) holder;
            GankBeauty cardImage = datas.get(position);
            Glide.with(mapItemViewHolder.itemView.getContext()).load(cardImage.url).into(mapItemViewHolder.mIvImage);
            mapItemViewHolder.mTvDescription.setText(cardImage.createdAt);
        } else if (holder instanceof FooterItemViewHolder) {
            FooterItemViewHolder footerItemViewHolder = (FooterItemViewHolder) holder;
            switch (footer_item_status) {
                case PULLUP_LOAD_MORE:
                    footerItemViewHolder.mTvFooterItemDes.setText(R.string.pullup_load_more);
                    break;
                case LOADING_MORE:
                    footerItemViewHolder.mTvFooterItemDes.setText(R.string.loading_more);
                    break;
            }
        }

    }


    //判断是普通的Item还是FooterItem
    @Override
    public int getItemViewType(int position) {
        //最后一个item设置成FooterItem
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER_ITEM;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size() + 1;
    }

    public void setDatas(List<GankBeauty> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void addAllDatas(List<GankBeauty> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    //正常Item布局的ViewHolder
    static class MapItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_image)
        ImageView mIvImage;
        @Bind(R.id.tv_description)
        TextView  mTvDescription;

        public MapItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //FooterItem的ViewHolder
    static class FooterItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_footer_item_des)
        TextView mTvFooterItemDes;
        public FooterItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void changeLoadMoreStatus(int status) {
        footer_item_status = status;
        notifyDataSetChanged();
    }

}
