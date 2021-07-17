package au.edu.unsw.infs3605.aboriginalplatform.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.activity.ConversationListActivity;
import au.edu.unsw.infs3605.aboriginalplatform.activity.LoginActivity;
import au.edu.unsw.infs3605.aboriginalplatform.activity.MainActivity;
import au.edu.unsw.infs3605.aboriginalplatform.adapter.MainAdapter;
import au.edu.unsw.infs3605.aboriginalplatform.listener.IOnViewListener;

/**
 * create an instance of this fragment.
 */
public class ConversationsFragment extends Fragment {
    private static final String TAG = "ConversationsFragment";

    private MainActivity mActivity;
    private Context mContext;

    private TextView tvTitle;
    private TextView tvLogout;
    private RecyclerView rvContent;
    private MainAdapter mMainAdapter;

    public ConversationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_conversations, container, false);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        tvTitle = (TextView) contentView.findViewById(R.id.tvTitle);
        tvLogout = (TextView) contentView.findViewById(R.id.tvLogout);
        rvContent = (RecyclerView) contentView.findViewById(R.id.rvContent);
        mMainAdapter = new MainAdapter(Arrays.asList(getResources().getStringArray(R.array.str_main_category)));
        mMainAdapter.setOnViewListener(new IOnViewListener<String>() {
            @Override
            public void onViewClick(View view, String s, int position) {
                ConversationListActivity.newInstance(mActivity, s);
            }

            @Override
            public boolean onLongViewClick(View view, String s, int position) {
                return false;
            }
        });
        rvContent.setAdapter(mMainAdapter);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(mContext, LoginActivity.class));
                mActivity.finish();
            }
        });

    }
}