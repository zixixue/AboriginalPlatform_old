package au.edu.unsw.infs3605.aboriginalplatform.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.infs3605.aboriginalplatform.R;
import au.edu.unsw.infs3605.aboriginalplatform.fragment.ConversationsFragment;
import au.edu.unsw.infs3605.aboriginalplatform.fragment.HomeFragment;
import au.edu.unsw.infs3605.aboriginalplatform.fragment.MeFragment;


public class MainActivity extends AppCompatActivity {

    private ViewPager2 vp2Content;
    private BottomNavigationView bnvBottom;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp2Content = findViewById(R.id.vp2Content);
        bnvBottom = findViewById(R.id.bnvBottom);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new ConversationsFragment());
        fragmentList.add(new MeFragment());
        bnvBottom.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    return switchFragment(0);
                case R.id.nav_conversation:
                    return switchFragment(1);
                case R.id.nav_me:
                    return switchFragment(2);
                default:
                    return false;
            }
        });
        vp2Content.setUserInputEnabled(false);
        vp2Content.setOffscreenPageLimit(2);
        vp2Content.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            /**
             * This method will be invoked when a new page becomes selected. Animation is not
             * necessarily complete.
             *
             * @param position Position index of the new selected page.
             */
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bnvBottom.getMenu().getItem(position).setChecked(true);
            }
        });
        vp2Content.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        });
    }

    private boolean switchFragment(int position) {
        // 设置为false，就是取消点击item的时候viewpager的滑动效果, 反之则需要滑动, 默认是true
        vp2Content.setCurrentItem(position);
        return true;
    }

}