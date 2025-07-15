package com.example.homehelperfinder.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.homehelperfinder.ui.editProfile.HelperEditProfileActivity;

public class CircularImageUtils {
    public static void loadCircularImage(Context context, String profilePictureUrl, ImageView ivProfilePicture, int icGuestIcon) {
        Glide.with(context).load(profilePictureUrl).circleCrop().placeholder(icGuestIcon).into(ivProfilePicture);
    }

    public static void loadCircularImage(HelperEditProfileActivity helperEditProfileActivity, String profilePictureUrl, ImageView ivProfilePicture) {
        Glide.with(helperEditProfileActivity).load(profilePictureUrl).circleCrop().into(ivProfilePicture);
    }
}
