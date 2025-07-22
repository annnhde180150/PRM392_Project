package com.example.homehelperfinder.ui.reports.helper;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.ReportPeriod;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.reports.helper.fragments.HelperEarningsReportFragment;
import com.example.homehelperfinder.ui.reports.helper.fragments.HelperScheduleReportFragment;
import com.example.homehelperfinder.utils.Logger;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * Activity for displaying helper reports with tabbed interface
 * Contains earnings report and schedule analytics
 */
public class HelperReportsActivity extends BaseActivity {
    private static final String TAG = "HelperReportsActivity";

    // UI Components
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private Toolbar toolbar;
    
    // Current period for reports
    private ReportPeriod currentPeriod = ReportPeriod.MONTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_reports);
        
        Logger.d(TAG, "Creating HelperReportsActivity");
        
        initViews();
        setupViewPager();
        setupTabLayout();
        setupMenuNavigation();
        getSupportActionBar();
        setupToolbar();
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupViewPager() {
        ReportsPagerAdapter adapter = new ReportsPagerAdapter(this);
        viewPager.setAdapter(adapter);
        
        // Disable swipe if needed
        viewPager.setUserInputEnabled(true);
    }

    private void setupToolbar() {
        // Keep the toolbar setup for compatibility but it's now hidden
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Helper Report");
        }
    }

    private void setupTabLayout() {
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Thu nhập");
                    tab.setIcon(R.drawable.ic_money);
                    break;
                case 1:
                    tab.setText("Lịch làm việc");
                    tab.setIcon(R.drawable.ic_schedule);
                    break;
            }
        }).attach();
    }



    /**
     * Interface for fragments to listen to period changes
     */
    public interface OnPeriodChangeListener {
        void onPeriodChanged(ReportPeriod period);
    }

    /**
     * Notify all fragments about period change
     */
    private void notifyPeriodChange(ReportPeriod period) {
        Logger.d(TAG, "Period changed to: " + period.getValue());
        
        // Notify all fragments about period change
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof OnPeriodChangeListener) {
                ((OnPeriodChangeListener) fragment).onPeriodChanged(period);
            }
        }
    }

    /**
     * Get current report period
     */
    public ReportPeriod getCurrentPeriod() {
        return currentPeriod;
    }

    /**
     * Set current report period and notify fragments
     */
    public void setCurrentPeriod(ReportPeriod period) {
        if (period != null && !period.equals(currentPeriod)) {
            this.currentPeriod = period;
            notifyPeriodChange(period);
        }
    }

    /**
     * ViewPager adapter for report fragments
     */
    private static class ReportsPagerAdapter extends FragmentStateAdapter {

        public ReportsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new HelperEarningsReportFragment();
                case 1:
                    return new HelperScheduleReportFragment();
                default:
                    return new HelperEarningsReportFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2; // Two tabs: Earnings and Schedule
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "HelperReportsActivity destroyed");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
