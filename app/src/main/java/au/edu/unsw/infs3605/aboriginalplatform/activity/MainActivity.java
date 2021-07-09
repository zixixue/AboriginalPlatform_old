package au.edu.unsw.infs3605.aboriginalplatform.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.adapter.MainAdapter;


public class MainActivity extends AppCompatActivity {

    private TextView tvTitle;
    private RecyclerView rvContent;
    private MainAdapter mMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        rvContent = (RecyclerView) findViewById(R.id.rvContent);
        mMainAdapter = new MainAdapter(Arrays.asList(getResources().getStringArray(R.array.str_main_category)));
        rvContent.setAdapter(mMainAdapter);
    }

}