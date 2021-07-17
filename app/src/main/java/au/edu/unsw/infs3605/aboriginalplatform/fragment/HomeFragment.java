package au.edu.unsw.infs3605.aboriginalplatform.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.adapter.ConversationListAdapter;
import au.edu.unsw.infs3605.aboriginalplatform.adapter.NewsListAdapter;
import au.edu.unsw.infs3605.aboriginalplatform.entity.Conversation;
import au.edu.unsw.infs3605.aboriginalplatform.entity.NewsResult;
import au.edu.unsw.infs3605.aboriginalplatform.http.HttpCallbackModelListener;
import au.edu.unsw.infs3605.aboriginalplatform.http.HttpUtils;

import static au.edu.unsw.infs3605.aboriginalplatform.Constants.TABLE_USER_TOPIC;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private ImageView ivLeft;
    private TextView tvLeft;
    private TextView tvTitle;
    private ImageView ivRight;
    private TextView tvRight;
    private TextView tvConversationsDesc;
    private RecyclerView rvConversations;
    private TextView tvConversations;
    private TextView tvNewsDesc;
    private RecyclerView rvNews;
    private TextView tvNoDateNews;

    private NewsListAdapter mNewsListAdapter;
    private ConversationListAdapter mConversationListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_home, container, false);
        initView(contentView);
        initData();
        return contentView;
    }

    private void initView(View contentView) {
        ivLeft = contentView.findViewById(R.id.ivLeft);
        tvLeft = contentView.findViewById(R.id.tvLeft);
        tvTitle = contentView.findViewById(R.id.tvTitle);
        ivRight = contentView.findViewById(R.id.ivRight);
        tvRight = contentView.findViewById(R.id.tvRight);
        tvConversationsDesc = contentView.findViewById(R.id.tvConversationsDesc);
        rvConversations = contentView.findViewById(R.id.rvConversations);
        tvConversations = contentView.findViewById(R.id.tvConversations);
        tvNewsDesc = contentView.findViewById(R.id.tvNewsDesc);
        rvNews = contentView.findViewById(R.id.rvNews);
        tvNoDateNews = contentView.findViewById(R.id.tvNoDateNews);
    }

    private void initData() {
        mConversationListAdapter = new ConversationListAdapter(new ArrayList<>());
        rvConversations.setAdapter(mConversationListAdapter);

        FirebaseFirestore.getInstance()
                .collection(TABLE_USER_TOPIC)
                .orderBy("publishTime", Query.Direction.DESCENDING)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    List<Conversation> conversationList = new ArrayList<>();
                    if (value != null) {
                        if (value.getDocuments().size() > 3) {
                            for (int i = 0; i < 3; i++) {
                                conversationList.add(value.getDocuments().get(i).toObject(Conversation.class));
                            }
                        } else {
                            conversationList.addAll(value.toObjects(Conversation.class));
                        }
                    }
                    if (conversationList.size() > 0) {
                        mConversationListAdapter.refreshData(conversationList);
                        tvConversations.setVisibility(View.GONE);
                        rvConversations.setVisibility(View.VISIBLE);
                    } else {
                        tvConversations.setVisibility(View.VISIBLE);
                        rvConversations.setVisibility(View.INVISIBLE);
                    }
                });

        mNewsListAdapter = new NewsListAdapter(new ArrayList<>());
        rvNews.setAdapter(mNewsListAdapter);
        //返回对象
        HttpUtils.doGet(getContext(), "https://newsapi.org/v2/everything?q=bitcoin&apiKey=e3b118f3337c4c3cab14d5e19b5b68ec",
                new HttpCallbackModelListener<NewsResult>() {
                    /**
                     * 网络请求成功
                     *
                     * @param response
                     */
                    @Override
                    public void onFinish(NewsResult response) {
                        if (response.getArticles() == null || response.getArticles().size() == 0) {
                            tvNoDateNews.setVisibility(View.VISIBLE);
                            rvNews.setVisibility(View.INVISIBLE);
                        } else {
                            mNewsListAdapter.refreshData(response.getArticles());
                            tvNoDateNews.setVisibility(View.GONE);
                            rvNews.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                }, NewsResult.class);
    }
}