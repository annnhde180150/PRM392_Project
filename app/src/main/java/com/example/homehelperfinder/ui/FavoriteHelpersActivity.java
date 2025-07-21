package com.example.homehelperfinder.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.FavoriteHelperDetailsDto;
import com.example.homehelperfinder.data.model.response.FavoriteHelperDto;
import com.example.homehelperfinder.data.remote.BaseApiService;
import com.example.homehelperfinder.data.remote.favorite.FavoriteHelperApiService;
import com.example.homehelperfinder.utils.UserManager;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class FavoriteHelpersActivity extends AppCompatActivity {
    private FavoriteHelpersAdapter adapter;
    private FavoriteHelperApiService favoriteHelperApiService;
    private RecyclerView recyclerView;
    private List<FavoriteHelperDetailsDto> currentFavorites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_helpers);

        recyclerView = findViewById(R.id.recyclerViewFavoriteHelpers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoriteHelpersAdapter(new ArrayList<>(), favorite -> {
            removeFavorite(favorite);
        });
        recyclerView.setAdapter(adapter);

        favoriteHelperApiService = new FavoriteHelperApiService();
        int userId = UserManager.getInstance(this).getCurrentUserId();
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        loadFavoriteHelpers(userId);
    }

    private void loadFavoriteHelpers(int userId) {
        favoriteHelperApiService.getFavoriteHelpersByUserId(this, userId, new BaseApiService.ApiCallback<List<FavoriteHelperDto>>() {
            @Override
            public void onSuccess(List<FavoriteHelperDto> favoriteDtos) {
                List<FavoriteHelperDetailsDto> detailsList = new ArrayList<>();
                if (favoriteDtos != null) {
                    for (FavoriteHelperDto dto : favoriteDtos) {
                        if (dto.getHelperInfo() != null) {
                            detailsList.add(dto.getHelperInfo());
                        }
                    }
                }
                currentFavorites = detailsList;
                adapter.updateFavorites(detailsList);
                if (detailsList.isEmpty()) {
                    Toast.makeText(FavoriteHelpersActivity.this, "No favorite helpers found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String error, Throwable throwable) {
                Toast.makeText(FavoriteHelpersActivity.this, "Failed to load favorites: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeFavorite(FavoriteHelperDetailsDto favorite) {
        try {
            if (favorite == null) {
                Toast.makeText(this, "Favorite is null", Toast.LENGTH_SHORT).show();
                return;
            }
            int helperId = favorite.getHelperId();
            int userId = UserManager.getInstance(this).getCurrentUserId();
            if (helperId == 0 || userId == -1) {
                Toast.makeText(this, "Invalid helperId or userId", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject json = new JSONObject();
            json.put("helperId", helperId);
            json.put("userId", userId);
            RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));

            favoriteHelperApiService.deleteFavorite(this, body, new BaseApiService.ApiCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    Toast.makeText(FavoriteHelpersActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    currentFavorites.remove(favorite);
                    adapter.updateFavorites(new ArrayList<>(currentFavorites));
                }
                @Override
                public void onError(String error, Throwable throwable) {
                    Toast.makeText(FavoriteHelpersActivity.this, "Failed to remove: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error preparing request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}   