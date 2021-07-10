package au.edu.unsw.infs3605.aboriginalplatform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.adapter.MainAdapter;


public class MainActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvLogout;
    private RecyclerView rvContent;
    private MainAdapter mMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        rvContent = (RecyclerView) findViewById(R.id.rvContent);
        mMainAdapter = new MainAdapter(Arrays.asList(getResources().getStringArray(R.array.str_main_category)));
        rvContent.setAdapter(mMainAdapter);
        tvLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                MainActivity.this.finish();
            }
        });
    }

}