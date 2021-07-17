package au.edu.unsw.infs3605.aboriginalplatform.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.entity.Conversation;
import au.edu.unsw.infs3605.aboriginalplatform.listener.IOnViewListener;


public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ConversationListViewHolder> {
    private static final String TAG = "ConversationListAdapter";

    private List<Conversation> mData;

    public ConversationListAdapter(List<Conversation> data) {
        this.mData = data;
    }

    private IOnViewListener<Conversation> mIOnViewListener;

    public void setOnViewListener(IOnViewListener<Conversation> onViewListener) {
        this.mIOnViewListener = onViewListener;
    }

    /**
     * 刷新列表数据
     *
     * @param data
     */
    public void refreshData(List<Conversation> data) {
        if (mData.size() > 0) {
            mData.clear();
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public ConversationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_list, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ConversationListViewHolder holder, int position) {
        Conversation conversation = mData.get(position);
        holder.tvUserName.setText(conversation.getUserExtendData().getUserName());
        holder.tvTitle.setText(conversation.getTitle());
        List<String> idList = conversation.getLikeIds();
        holder.ctvLiked.setText(String.valueOf(idList.size()));
        holder.ctvLiked.setChecked(idList.contains(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        holder.tvComment.setText(String.valueOf(conversation.getCommentNum()));
        holder.tvView.setText(String.valueOf(conversation.getClickNum()));
        holder.clRoot.setOnClickListener(v -> {
            if (mIOnViewListener != null) {
                mIOnViewListener.onViewClick(v, conversation, position);
            }
        });
        holder.ctvLiked.setOnClickListener(v -> {
            if (mIOnViewListener != null) {
                mIOnViewListener.onViewClick(v, conversation, position);
            }
        });
        holder.tvComment.setOnClickListener(v -> {
            if (mIOnViewListener != null) {
                mIOnViewListener.onViewClick(v, conversation, position);
            }
        });
        holder.tvView.setOnClickListener(v -> {
            if (mIOnViewListener != null) {
                mIOnViewListener.onViewClick(v, conversation, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class ConversationListViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserImg;
        TextView tvUserName;
        TextView tvTitle;
        CheckedTextView ctvLiked;
        TextView tvComment;
        TextView tvView;
        ConstraintLayout clRoot;

        public ConversationListViewHolder(View itemView) {
            super(itemView);
            ivUserImg = itemView.findViewById(R.id.ivUserImg);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ctvLiked = itemView.findViewById(R.id.ctvLiked);
            tvComment = itemView.findViewById(R.id.tvComment);
            clRoot = itemView.findViewById(R.id.clRoot);
            tvView = itemView.findViewById(R.id.tvView);
        }
    }

}