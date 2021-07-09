package au.edu.unsw.infs3605.aboriginalplatform.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.listener.IOnViewListener;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private static final String TAG = "MainAdapter";

    private List<String> mData;

    public MainAdapter(List<String> data) {
        this.mData = data;
    }

    private IOnViewListener<String> mIOnViewListener;

    public void setOnViewListener(IOnViewListener<String> onViewListener) {
        this.mIOnViewListener = onViewListener;
    }

    /**
     * 刷新列表数据
     *
     * @param data
     */
    public void refreshData(List<String> data) {
        if (mData.size() > 0) mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_card, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String category = mData.get(position);
        holder.tvName.setText(category);
        int width = holder.cvRoot.getMeasuredWidth();
        holder.cvRoot.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = holder.cvRoot.getLayoutParams();
                Log.i(TAG, "run: " + layoutParams.width);
                layoutParams.height = holder.cvRoot.getMeasuredWidth();
                holder.cvRoot.setLayoutParams(layoutParams);
            }
        });
        holder.cvRoot.setOnClickListener(v -> {
            if (mIOnViewListener != null) {
                mIOnViewListener.onViewClick(v, category, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MainViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CardView cvRoot;

        public MainViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            cvRoot = (CardView) itemView.findViewById(R.id.cvRoot);
        }
    }

}