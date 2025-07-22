package com.example.homehelperfinder.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.request.SearchChatRequest;
import com.example.homehelperfinder.data.model.response.HelperSearchResult;
import com.example.homehelperfinder.data.model.response.SearchChatResponse;
import com.example.homehelperfinder.data.model.response.UserSearchResult;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.chat.ChatApiService;
import com.example.homehelperfinder.ui.base.BaseActivity;
import com.example.homehelperfinder.ui.chat.adapter.SearchResultsAdapter;
import com.example.homehelperfinder.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Activity for searching users and helpers to start chat conversations
 */
public class SearchChatActivity extends BaseActivity implements SearchResultsAdapter.OnSearchResultClickListener {
    
    private static final int SEARCH_DELAY_MS = 500; // Debounce delay
    private static final int DEFAULT_PAGE_SIZE = 20;
    
    // UI Components
    private Toolbar toolbar;
    private TextInputEditText etSearch;
    private AutoCompleteTextView spinnerSearchType;
    private MaterialButton btnFilter;
    private TextView tvResultsCount;
    private TextView tvPaginationInfo;
    private RecyclerView rvSearchResults;
    private LinearLayout layoutEmptyState;
    private LinearLayout layoutLoading;
    private LinearLayout layoutPagination;
    private MaterialButton btnPrevious;
    private MaterialButton btnNext;
    private TextView tvPageInfo;
    private TextView tvEmptyMessage;
    
    // Data
    private ChatApiService chatApiService;
    private SearchResultsAdapter adapter;
    private SearchChatRequest currentRequest;
    private SearchChatResponse currentResponse;
    private Handler searchHandler;
    private Runnable searchRunnable;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_chat);
        
        initializeComponents();
        setupUI();
        setupSearchFunctionality();
    }
    
    private void initializeComponents() {
        // Initialize API service
        chatApiService = new ChatApiService(this);
        searchHandler = new Handler(Looper.getMainLooper());
        
        // Find views
        toolbar = findViewById(R.id.toolbar);
        etSearch = findViewById(R.id.etSearch);
        spinnerSearchType = findViewById(R.id.spinnerSearchType);
        btnFilter = findViewById(R.id.btnFilter);
        tvResultsCount = findViewById(R.id.tvResultsCount);
        tvPaginationInfo = findViewById(R.id.tvPaginationInfo);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        layoutLoading = findViewById(R.id.layoutLoading);
        layoutPagination = findViewById(R.id.layoutPagination);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        tvPageInfo = findViewById(R.id.tvPageInfo);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
    }
    
    private void setupUI() {
        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        
        // Setup search type spinner
        String[] searchTypes = {"All", "Users", "Helpers"};
        ArrayAdapter<String> searchTypeAdapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_dropdown_item_1line, searchTypes);
        spinnerSearchType.setAdapter(searchTypeAdapter);
        spinnerSearchType.setText("All", false);
        
        // Setup RecyclerView
        adapter = new SearchResultsAdapter(this);
        adapter.setOnSearchResultClickListener(this);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(adapter);
        
        // Setup pagination buttons
        btnPrevious.setOnClickListener(v -> loadPreviousPage());
        btnNext.setOnClickListener(v -> loadNextPage());
        
        // Setup filter button (placeholder for now)
        btnFilter.setOnClickListener(v -> showFilterDialog());
        
        // Initial state
        showEmptyState("Search for users and helpers to start chatting");
    }
    
    private void setupSearchFunctionality() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancel previous search
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                
                // Schedule new search with debounce
                searchRunnable = () -> performSearch(s.toString().trim());
                searchHandler.postDelayed(searchRunnable, SEARCH_DELAY_MS);
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        spinnerSearchType.setOnItemClickListener((parent, view, position, id) -> {
            String currentSearchTerm = etSearch.getText().toString().trim();
            if (!currentSearchTerm.isEmpty()) {
                performSearch(currentSearchTerm);
            }
        });
    }
    
    private void performSearch(String searchTerm) {
        if (searchTerm.isEmpty()) {
            showEmptyState("Search for users and helpers to start chatting");
            return;
        }
        
        showLoadingState();
        
        // Create search request
        currentRequest = new SearchChatRequest();
        currentRequest.setSearchTerm(searchTerm);
        currentRequest.setPageNumber(1);
        currentRequest.setPageSize(DEFAULT_PAGE_SIZE);
        currentRequest.setIsActive(true);
        
        // Set search type
        String selectedType = spinnerSearchType.getText().toString();
        switch (selectedType) {
            case "Users":
                currentRequest.setSearchType("users");
                break;
            case "Helpers":
                currentRequest.setSearchType("helpers");
                break;
            default:
                currentRequest.setSearchType("all");
                break;
        }
        
        // Perform search
        chatApiService.searchUsersForChat(this, currentRequest, new BaseApiService.ApiCallback<SearchChatResponse>() {
            @Override
            public void onSuccess(SearchChatResponse response) {
                runOnUiThread(() -> handleSearchResponse(response));
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> handleSearchError(errorMessage));
            }
        });
    }
    
    private void handleSearchResponse(SearchChatResponse response) {
        currentResponse = response;
        
        if (response.hasResults()) {
            showResults(response);
        } else {
            showEmptyState("No results found for your search");
        }
    }
    
    private void handleSearchError(String error) {
        showEmptyState("Error occurred while searching");
        Toast.makeText(this, "Search failed: " + error, Toast.LENGTH_SHORT).show();
    }
    
    private void showResults(SearchChatResponse response) {
        // Hide loading and empty states
        layoutLoading.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.GONE);
        
        // Show results
        rvSearchResults.setVisibility(View.VISIBLE);
        adapter.updateResults(response.getUsers(), response.getHelpers());
        
        // Update results count
        tvResultsCount.setText(response.getResultsSummary());
        
        // Update pagination
        if (response.hasPagination()) {
            layoutPagination.setVisibility(View.VISIBLE);
            tvPageInfo.setText(response.getPaginationInfo());
            btnPrevious.setEnabled(response.canGoPrevious());
            btnNext.setEnabled(response.canGoNext());
            
            tvPaginationInfo.setVisibility(View.VISIBLE);
            tvPaginationInfo.setText(response.getPaginationInfo());
        } else {
            layoutPagination.setVisibility(View.GONE);
            tvPaginationInfo.setVisibility(View.GONE);
        }
    }
    
    private void showLoadingState() {
        rvSearchResults.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.GONE);
        layoutPagination.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.VISIBLE);
        tvResultsCount.setText("Searching...");
    }
    
    private void showEmptyState(String message) {
        rvSearchResults.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.GONE);
        layoutPagination.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
        tvEmptyMessage.setText(message);
        tvResultsCount.setText("");
    }
    
    private void loadPreviousPage() {
        if (currentRequest != null && currentResponse != null && currentResponse.canGoPrevious()) {
            currentRequest.setPageNumber(currentRequest.getPageNumber() - 1);
            performSearchWithCurrentRequest();
        }
    }
    
    private void loadNextPage() {
        if (currentRequest != null && currentResponse != null && currentResponse.canGoNext()) {
            currentRequest.setPageNumber(currentRequest.getPageNumber() + 1);
            performSearchWithCurrentRequest();
        }
    }
    
    private void performSearchWithCurrentRequest() {
        if (currentRequest == null) return;
        
        showLoadingState();
        chatApiService.searchUsersForChat(this, currentRequest, new BaseApiService.ApiCallback<SearchChatResponse>() {
            @Override
            public void onSuccess(SearchChatResponse response) {
                runOnUiThread(() -> handleSearchResponse(response));
            }

            @Override
            public void onError(String errorMessage, Throwable throwable) {
                runOnUiThread(() -> handleSearchError(errorMessage));
            }
        });
    }
    
    private void showFilterDialog() {
        // TODO: Implement filter dialog for advanced search options
        Toast.makeText(this, "Filter options coming soon", Toast.LENGTH_SHORT).show();
    }
    
    // SearchResultsAdapter.OnSearchResultClickListener implementation
    @Override
    public void onUserClick(UserSearchResult user) {
        // Handle user profile view (optional)
    }
    
    @Override
    public void onHelperClick(HelperSearchResult helper) {
        // Handle helper profile view (optional)
    }
    
    @Override
    public void onStartChatClick(Object result) {
        startChatWithResult(result);
    }
    
    private void startChatWithResult(Object result) {
        Intent intent = new Intent(this, ChatActivity.class);

        if (result instanceof UserSearchResult) {
            UserSearchResult user = (UserSearchResult) result;
            intent.putExtra(Constants.INTENT_OTHER_USER_ID, user.getUserId());
            intent.putExtra("participant_name", user.getDisplayName());
        } else if (result instanceof HelperSearchResult) {
            HelperSearchResult helper = (HelperSearchResult) result;
            intent.putExtra(Constants.INTENT_OTHER_HELPER_ID, helper.getHelperId());
            intent.putExtra("participant_name", helper.getDisplayName());
        }

        startActivity(intent);
        finish(); // Close search activity
    }
}
