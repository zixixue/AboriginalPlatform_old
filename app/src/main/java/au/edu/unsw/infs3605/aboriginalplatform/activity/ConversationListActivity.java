package au.edu.unsw.infs3605.aboriginalplatform.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.adapter.ConversationListAdapter;
import au.edu.unsw.infs3605.aboriginalplatform.entity.Conversation;
import au.edu.unsw.infs3605.aboriginalplatform.listener.IOnViewListener;

import static au.edu.unsw.infs3605.aboriginalplatform.Constants.TABLE_USER_TOPIC;

public class ConversationListActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ConversationListActivit";

    public static final String CONVERSATION_NAME = "conversationName";

    private String conversationName;
    private ImageView ivLeft;
    private TextView tvLeft;
    private TextView tvTitle;
    private ImageView ivRight;
    private TextView tvRight;
    private ImageView ivConversationsDesc;
    private TextView tvConversationsDesc;
    private TextView tvConvClickNum;
    private RecyclerView rvConversations;
    private TextView tvNoDataConversations;
    private FloatingActionButton fabPublish;

    private FirebaseFirestore mDb;
    private FirebaseUser mUser;

    private ConversationListAdapter mAdapter;

    public static void newInstance(AppCompatActivity activity, String conversationName) {
        Intent intent = new Intent(activity, ConversationListActivity.class);
        intent.putExtra(CONVERSATION_NAME, conversationName);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        Intent intent = getIntent();
        if (intent != null) {
            conversationName = intent.getStringExtra(CONVERSATION_NAME);
        }
        ivLeft = findViewById(R.id.ivLeft);
        tvLeft = findViewById(R.id.tvLeft);
        tvTitle = findViewById(R.id.tvTitle);
        ivRight = findViewById(R.id.ivRight);
        tvRight = findViewById(R.id.tvRight);
        ivConversationsDesc = findViewById(R.id.ivConversationsDesc);
        tvConversationsDesc = findViewById(R.id.tvConversationsDesc);
        tvConvClickNum = findViewById(R.id.tvConvClickNum);
        rvConversations = findViewById(R.id.rvConversations);
        tvNoDataConversations = findViewById(R.id.tvNoDataConversations);
        fabPublish = findViewById(R.id.fabPublish);

        ivLeft.setOnClickListener(this);
        fabPublish.setOnClickListener(this);

        mDb = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mAdapter = new ConversationListAdapter(new ArrayList<>());
        mAdapter.setOnViewListener(new IOnViewListener<Conversation>() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onViewClick(View view, Conversation conversation, int position) {
                switch (view.getId()) {
                    case R.id.clRoot:
                        break;
                    case R.id.ctvLiked:
                        break;
                    case R.id.tvComment:
                        break;
                    case R.id.tvView:
                        break;
                    default:
                }
            }

            @Override
            public boolean onLongViewClick(View view, Conversation conversation, int position) {
                return false;
            }
        });
        rvConversations.setAdapter(mAdapter);
        mDb.collection(TABLE_USER_TOPIC)
                .whereEqualTo("topic", conversationName)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    List<Conversation> conversationList = new ArrayList<>();
                    if (value != null) {
                        conversationList.addAll(value.toObjects(Conversation.class));
                    }
                    if (conversationList.size() > 0) {
                        mAdapter.refreshData(conversationList);
                        tvNoDataConversations.setVisibility(View.GONE);
                        rvConversations.setVisibility(View.VISIBLE);
                    } else {
                        tvNoDataConversations.setVisibility(View.VISIBLE);
                        rvConversations.setVisibility(View.INVISIBLE);
                    }
                });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                ConversationListActivity.this.finish();
                break;
            case R.id.fabPublish:
                Intent intent = new Intent(ConversationListActivity.this, PublishConversationActivity.class);
                intent.putExtra(CONVERSATION_NAME, conversationName);
                ConversationListActivity.this.startActivity(intent);
                break;
            default:
        }
    }
}