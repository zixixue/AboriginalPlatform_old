package au.edu.unsw.infs3605.aboriginalplatform.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.entity.NewsResult;
import au.edu.unsw.infs3605.aboriginalplatform.listener.IOnViewListener;


public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder> {
    private static final String TAG = "MainAdapter";

    private List<NewsResult.News> mData;

    public NewsListAdapter(List<NewsResult.News> data) {
        this.mData = data;
    }

    private IOnViewListener<NewsResult.News> mIOnViewListener;

    public void setOnViewListener(IOnViewListener<NewsResult.News> onViewListener) {
        this.mIOnViewListener = onViewListener;
    }

    /**
     * 刷新列表数据
     *
     * @param data
     */
    public void refreshData(List<NewsResult.News> data) {
        if (mData.size() > 0) {
            mData.clear();
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_news, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewsListViewHolder holder, int position) {
        NewsResult.News news = mData.get(position);
        holder.tvTitle.setText(news.getTitle());
        holder.tvContent.setText(news.getContent());
        holder.tvAuthor.setText("Author: " + news.getAuthor());
        holder.tvDate.setText("Publish Date: " + news.getPublishedAt());
        Glide.with(holder.cvRoot.getContext())
                .load(news.getUrlToImage())
                .placeholder(R.mipmap.ic_img_placeholder)
                .into(holder.ivImg);
        holder.cvRoot.setOnClickListener(v -> {
            if (mIOnViewListener != null) {
                mIOnViewListener.onViewClick(v, news, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void initView() {

    }


    public static class NewsListViewHolder extends RecyclerView.ViewHolder {
        CardView cvRoot;
        ImageView ivImg;
        TextView tvTitle;
        TextView tvContent;
        TextView tvAuthor;
        TextView tvDate;

        public NewsListViewHolder(View itemView) {
            super(itemView);
            cvRoot = itemView.findViewById(R.id.cvRoot);
            ivImg = itemView.findViewById(R.id.ivImg);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

}