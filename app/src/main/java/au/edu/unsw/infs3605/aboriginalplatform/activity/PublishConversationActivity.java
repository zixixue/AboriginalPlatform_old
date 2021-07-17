package au.edu.unsw.infs3605.aboriginalplatform.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

import au.edu.unsw.infs3605.aboriginalplatform.Constants;
import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.entity.Conversation;
import au.edu.unsw.infs3605.aboriginalplatform.entity.UserExtendData;
import au.edu.unsw.infs3605.aboriginalplatform.utils.SPUtil;

import static au.edu.unsw.infs3605.aboriginalplatform.Constants.TABLE_USER_TOPIC;
import static au.edu.unsw.infs3605.aboriginalplatform.activity.ConversationListActivity.CONVERSATION_NAME;

public class PublishConversationActivity extends AppCompatActivity implements View.OnClickListener {

    private String conversationName;
    private ImageView ivLeft;
    private TextView tvLeft;
    private TextView tvTitle;
    private ImageView ivRight;
    private TextView tvRight;
    private TextView tvConversation;
    private TextView tvConversationHint;
    private TextView tvConversationTitle;
    private EditText etConversationTitle;
    private TextView tvConversationContent;
    private EditText etConversationContent;
    private ImageView ivAddImg;

    private FirebaseFirestore mDb;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_conversation);
        Intent intent = getIntent();
        if (intent != null) {
            conversationName = intent.getStringExtra(CONVERSATION_NAME);
        }
        ivLeft = findViewById(R.id.ivLeft);
        tvLeft = findViewById(R.id.tvLeft);
        tvTitle = findViewById(R.id.tvTitle);
        ivRight = findViewById(R.id.ivRight);
        tvRight = findViewById(R.id.tvRight);
        tvConversation = findViewById(R.id.tvConversation);
        tvConversationHint = findViewById(R.id.tvConversationHint);
        tvConversationTitle = findViewById(R.id.tvConversationTitle);
        etConversationTitle = findViewById(R.id.etConversationTitle);
        tvConversationContent = findViewById(R.id.tvConversationContent);
        etConversationContent = findViewById(R.id.etConversationContent);
        ivAddImg = findViewById(R.id.ivAddImg);

        ivRight.setVisibility(View.GONE);
        tvRight.setText("Publish");
        tvRight.setVisibility(View.VISIBLE);

        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        ivAddImg.setOnClickListener(this);

        mDb = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        tvConversation.setText(conversationName);

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
                PublishConversationActivity.this.finish();
                break;
            case R.id.tvRight:
                String title = etConversationTitle.getText().toString().trim();
                String content = etConversationContent.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(this, "Conversation title CANNOT BE EMPTY", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(this, "Conversation content CANNOT BE EMPTY", Toast.LENGTH_SHORT).show();
                    return;
                }

                Conversation conversation = new Conversation();
                conversation.setTopic(conversationName);
                conversation.setTitle(title);
                conversation.setContent(content);
                conversation.setUserId(mUser.getUid());
                String userExtendStr = (String) SPUtil.getInstance().getParam(Constants.CACHE_USER, "");
                UserExtendData userExtendData = new Gson().fromJson(userExtendStr, UserExtendData.class);
                conversation.setUserExtendData(userExtendData);
                conversation.setLikeIds(new ArrayList<>());
                conversation.setPublishTime(SystemClock.currentThreadTimeMillis());
                conversation.setCommentNum(0);
                conversation.setClickNum(0);
                mDb.collection(TABLE_USER_TOPIC)
                        .add(conversation)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(PublishConversationActivity.this, "Publish conversation success", Toast.LENGTH_SHORT).show();
                            documentReference.update("id", documentReference.getId());
                            PublishConversationActivity.this.setResult(RESULT_OK);
                            PublishConversationActivity.this.finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(PublishConversationActivity.this, "Publish conversation failed", Toast.LENGTH_SHORT).show());
                break;
            case R.id.ivAddImg:

                break;
            default:
        }

    }
}