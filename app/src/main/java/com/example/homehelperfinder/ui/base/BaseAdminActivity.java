package com.example.homehelperfinder.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.ui.admin.AdminAnalyticsActivity;
import com.example.homehelperfinder.ui.admin.AdminRequestsActivity;
import com.example.homehelperfinder.ui.admin.HelperApplicationsActivity;
import com.example.homehelperfinder.ui.editProfile.AdminEditProfileActivity;
import com.example.homehelperfinder.ui.profileManagement.ProfileManagementActivity;
import com.example.homehelperfinder.utils.Logger;

/**
 * Base activity for all admin activities with shared bottom navigation
 */
public abstract class BaseAdminActivity extends BaseActivity {
    
    private static final String TAG = "BaseAdminActivity";
    
    // Admin page types
    public enum AdminPage {
        ANALYTICS,
        USERS,
        APPLICATIONS,
        REQUESTS,
        PROFILE
    }
    
    // Bottom navigation views
    private LinearLayout navAnalytics, navUsers, navApplications, navRequests, navAdminProfile;
    private TextView tvNavAnalytics, tvNavUsers, tvNavApplications, tvNavRequests, tvNavAdminProfile;
    private ImageView ivNavAnalytics, ivNavUsers, ivNavApplications, ivNavRequests, ivNavAdminProfile;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        // Create a wrapper layout that includes the activity content and bottom navigation
        LinearLayout wrapperLayout = new LinearLayout(this);
        wrapperLayout.setOrientation(LinearLayout.VERTICAL);
        wrapperLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        // Inflate the activity layout
        View activityContent = LayoutInflater.from(this).inflate(layoutResID, wrapperLayout, false);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
        activityContent.setLayoutParams(contentParams);
        wrapperLayout.addView(activityContent);

        // Add bottom navigation
        View bottomNav = LayoutInflater.from(this)
                .inflate(R.layout.layout_admin_bottom_navigation, wrapperLayout, false);
        wrapperLayout.addView(bottomNav);

        // Set the wrapper as content view
        super.setContentView(wrapperLayout);

        // Setup navigation
        setupAdminBottomNavigation(bottomNav);
    }
    
    /**
     * Child activities must implement this to specify their page type
     */
    protected abstract AdminPage getCurrentPage();
    
    /**
     * Setup the admin bottom navigation
     */
    private void setupAdminBottomNavigation(View bottomNav) {
        initBottomNavViews(bottomNav);
        setupBottomNavClickListeners();
        setCurrentPageSelected();
    }
    
    /**
     * Initialize bottom navigation views
     */
    private void initBottomNavViews(View bottomNav) {
        navAnalytics = bottomNav.findViewById(R.id.nav_analytics);
        navUsers = bottomNav.findViewById(R.id.nav_users);
        navApplications = bottomNav.findViewById(R.id.nav_applications);
        navRequests = bottomNav.findViewById(R.id.nav_requests);
        navAdminProfile = bottomNav.findViewById(R.id.nav_admin_profile);
        
        tvNavAnalytics = bottomNav.findViewById(R.id.tv_nav_analytics);
        tvNavUsers = bottomNav.findViewById(R.id.tv_nav_users);
        tvNavApplications = bottomNav.findViewById(R.id.tv_nav_applications);
        tvNavRequests = bottomNav.findViewById(R.id.tv_nav_requests);
        tvNavAdminProfile = bottomNav.findViewById(R.id.tv_nav_admin_profile);
        
        ivNavAnalytics = bottomNav.findViewById(R.id.iv_nav_analytics);
        ivNavUsers = bottomNav.findViewById(R.id.iv_nav_users);
        ivNavApplications = bottomNav.findViewById(R.id.iv_nav_applications);
        ivNavRequests = bottomNav.findViewById(R.id.iv_nav_requests);
        ivNavAdminProfile = bottomNav.findViewById(R.id.iv_nav_admin_profile);
    }
    
    /**
     * Setup click listeners for bottom navigation
     */
    private void setupBottomNavClickListeners() {
        navAnalytics.setOnClickListener(v -> navigateToPage(AdminPage.ANALYTICS));
        navUsers.setOnClickListener(v -> navigateToPage(AdminPage.USERS));
        navApplications.setOnClickListener(v -> navigateToPage(AdminPage.APPLICATIONS));
        navRequests.setOnClickListener(v -> navigateToPage(AdminPage.REQUESTS));
        navAdminProfile.setOnClickListener(v -> navigateToPage(AdminPage.PROFILE));
    }
    
    /**
     * Navigate to the specified admin page
     */
    private void navigateToPage(AdminPage page) {
        // Don't navigate if already on the current page
        if (page == getCurrentPage()) {
            return;
        }
        
        Intent intent = null;
        
        switch (page) {
            case ANALYTICS:
                intent = new Intent(this, AdminAnalyticsActivity.class);
                break;
            case USERS:
                intent = new Intent(this, ProfileManagementActivity.class);
                break;
            case APPLICATIONS:
                intent = new Intent(this, HelperApplicationsActivity.class);
                break;
            case REQUESTS:
                intent = new Intent(this, AdminRequestsActivity.class);
                break;
            case PROFILE:
                intent = new Intent(this, AdminEditProfileActivity.class);
                break;
        }
        
        if (intent != null) {
            startActivity(intent);
            // Add transition animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            Logger.d(TAG, "Navigated to admin page: " + page.name());
        }
    }
    
    /**
     * Set the current page as selected in bottom navigation
     */
    private void setCurrentPageSelected() {
        // Reset all to unselected state
        resetAllNavItems();
        
        // Set current page as selected
        AdminPage currentPage = getCurrentPage();
        switch (currentPage) {
            case ANALYTICS:
                setNavItemSelected(navAnalytics, tvNavAnalytics, ivNavAnalytics);
                break;
            case USERS:
                setNavItemSelected(navUsers, tvNavUsers, ivNavUsers);
                break;
            case APPLICATIONS:
                setNavItemSelected(navApplications, tvNavApplications, ivNavApplications);
                break;
            case REQUESTS:
                setNavItemSelected(navRequests, tvNavRequests, ivNavRequests);
                break;
            case PROFILE:
                setNavItemSelected(navAdminProfile, tvNavAdminProfile, ivNavAdminProfile);
                break;
        }
    }
    
    /**
     * Reset all navigation items to unselected state
     */
    private void resetAllNavItems() {
        setNavItemUnselected(navAnalytics, tvNavAnalytics, ivNavAnalytics);
        setNavItemUnselected(navUsers, tvNavUsers, ivNavUsers);
        setNavItemUnselected(navApplications, tvNavApplications, ivNavApplications);
        setNavItemUnselected(navRequests, tvNavRequests, ivNavRequests);
        setNavItemUnselected(navAdminProfile, tvNavAdminProfile, ivNavAdminProfile);
    }
    
    /**
     * Set navigation item as selected
     */
    private void setNavItemSelected(LinearLayout navItem, TextView textView, ImageView imageView) {
        navItem.setBackgroundResource(R.drawable.nav_item_selected_bg);
        textView.setTextColor(getResources().getColor(R.color.text_primary, null));
        // You can also change icon tint if needed
    }
    
    /**
     * Set navigation item as unselected
     */
    private void setNavItemUnselected(LinearLayout navItem, TextView textView, ImageView imageView) {
        navItem.setBackground(null);
        textView.setTextColor(getResources().getColor(R.color.text_secondary, null));
        // You can also change icon tint if needed
    }
}
