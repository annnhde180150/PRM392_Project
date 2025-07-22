package com.example.homehelperfinder.ui.reports;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.ReportPeriod;
import com.example.homehelperfinder.ui.reports.fragments.BookingReportFragment;
import com.example.homehelperfinder.ui.reports.fragments.FavoriteHelpersReportFragment;
import com.example.homehelperfinder.ui.reports.fragments.SpendingReportFragment;
import com.example.homehelperfinder.utils.Logger;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * Main activity for customer reports with tabs for different report types
 */
public class CustomerReportsActivity extends AppCompatActivity {

    private static final String TAG = "CustomerReportsActivity";

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Spinner periodSpinner;
    private ReportsPagerAdapter pagerAdapter;

    // Report period change listener interface
    public interface OnPeriodChangeListener {
        void onPeriodChanged(ReportPeriod period);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reports);

        initViews();
        setupToolbar();
        setupPeriodSpinner();
        setupViewPager();
        setupTabLayout();

        Logger.d(TAG, "CustomerReportsActivity created successfully");
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        periodSpinner = findViewById(R.id.periodSpinner);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Báo cáo khách hàng");
        }
    }

    private void setupPeriodSpinner() {
        // Create adapter for period spinner
        String[] periods = new String[ReportPeriod.values().length];
        for (int i = 0; i < ReportPeriod.values().length; i++) {
            periods[i] = ReportPeriod.values()[i].getDisplayName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, periods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(adapter);

        // Set default to MONTH
        periodSpinner.setSelection(ReportPeriod.MONTH.ordinal());

        // Set listener for period changes
        periodSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                ReportPeriod selectedPeriod = ReportPeriod.values()[position];
                notifyPeriodChange(selectedPeriod);
                Logger.d(TAG, "Period changed to: " + selectedPeriod.getDisplayName());
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupViewPager() {
        pagerAdapter = new ReportsPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
    }

    private void setupTabLayout() {
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Đặt chỗ");
                    tab.setIcon(R.drawable.ic_booking);
                    break;
                case 1:
                    tab.setText("Chi tiêu");
                    tab.setIcon(R.drawable.ic_money);
                    break;
                case 2:
                    tab.setText("Helper yêu thích");
                    tab.setIcon(R.drawable.ic_favorite);
                    break;
            }
        }).attach();
    }

    private void notifyPeriodChange(ReportPeriod period) {
        // Notify all fragments about period change
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof OnPeriodChangeListener) {
                ((OnPeriodChangeListener) fragment).onPeriodChanged(period);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                    return new BookingReportFragment();
                case 1:
                    return new SpendingReportFragment();
                case 2:
                    return new FavoriteHelpersReportFragment();
                default:
                    return new BookingReportFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3; // Three tabs: Booking, Spending, Favorite Helpers
        }
    }

    /**
     * Get current selected period
     */
    public ReportPeriod getCurrentPeriod() {
        int position = periodSpinner.getSelectedItemPosition();
        return ReportPeriod.values()[position];
    }
}
