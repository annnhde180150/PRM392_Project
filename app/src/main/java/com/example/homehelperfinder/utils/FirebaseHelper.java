package com.example.homehelperfinder.utils;

import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FirebaseHelper {
    /**
     * Uploads a file to Firebase Storage and returns the download URL or an error message.
     *
     * @param fileUri        The URI of the file to upload.
     * @param folder         The folder in Firebase Storage where the file will be uploaded.
     * @param resultHandler  A BiConsumer that handles the result, with the first parameter as the download URL
     *                       and the second as an error message (null if successful).
     */

    public void uploadFileToFirebase(Uri fileUri, String folder, BiConsumer<String, String> resultHandler) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String fileName = folder + "/" + System.currentTimeMillis() + "_" + fileUri.getLastPathSegment();
        StorageReference fileRef = storageRef.child(fileName);

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> resultHandler.accept(uri.toString(), null)) // Success: pass URL
                        .addOnFailureListener(e -> resultHandler.accept(null, e.getMessage())))  // Failure: pass null
                .addOnFailureListener(e -> resultHandler.accept(null, e.getMessage()));         // Failure: pass null
    }


    public void uploadIdFile(Uri fileUri, BiConsumer<String, String> resultHandler) {
        uploadFileToFirebase(fileUri, "ids", resultHandler);
    }


    public void uploadCvFile(Uri fileUri, BiConsumer<String, String> resultHandler) {
        uploadFileToFirebase(fileUri, "cvs", resultHandler);
    }

    public void uploadProfilePicture(Uri fileUri, BiConsumer<String, String> resultHandler) {
        uploadFileToFirebase(fileUri, "profiles", resultHandler);
    }

    /**
     * Uploads multiple files and tracks completion.
     *
     * @param filesToUpload List of files to upload with their types and URIs
     * @param onAllComplete Callback when all uploads are complete
     */
    public void uploadMultipleFiles(java.util.List<FileUploadTask> filesToUpload, 
                                   Consumer<java.util.Map<String, String>> onAllComplete) {
        if (filesToUpload.isEmpty()) {
            onAllComplete.accept(new java.util.HashMap<>());
            return;
        }

        AtomicInteger pendingUploads = new AtomicInteger(filesToUpload.size());
        java.util.Map<String, String> results = new java.util.HashMap<>();

        for (FileUploadTask task : filesToUpload) {
            switch (task.getType()) {
                case ID:
                    uploadIdFile(task.getFileUri(), (url, error) -> {
                        if (url != null) {
                            results.put("id", url);
                        } else {
                            results.put("id_error", error);
                        }
                        checkAllUploadsComplete(pendingUploads, results, onAllComplete);
                    });
                    break;
                case CV:
                    uploadCvFile(task.getFileUri(), (url, error) -> {
                        if (url != null) {
                            results.put("cv", url);
                        } else {
                            results.put("cv_error", error);
                        }
                        checkAllUploadsComplete(pendingUploads, results, onAllComplete);
                    });
                    break;
                case PROFILE_PICTURE:
                    uploadProfilePicture(task.getFileUri(), url -> {
                        if (url != null) {
                            results.put("profile", url);
                        } else {
                            results.put("profile_error", "Failed to upload profile picture");
                        }
                        checkAllUploadsComplete(pendingUploads, results, onAllComplete);
                    });
                    break;
            }
        }
    }

    private void checkAllUploadsComplete(AtomicInteger pendingUploads, 
                                       java.util.Map<String, String> results,
                                       Consumer<java.util.Map<String, String>> onAllComplete) {
        if (pendingUploads.decrementAndGet() == 0) {
            onAllComplete.accept(results);
        }
    }

    /**
     * Uploads a CV file to Firebase Storage (legacy method for backward compatibility).
     */
    public void uploadCvFile(Uri fileUri, Consumer<String> onComplete) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String fileName = "cvs/" + System.currentTimeMillis() + "_" + fileUri.getLastPathSegment();
        StorageReference fileRef = storageRef.child(fileName);

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> onComplete.accept(uri.toString()))
                        .addOnFailureListener(e -> onComplete.accept(null)))
                .addOnFailureListener(e -> onComplete.accept(null));
    }

    /**
     * Uploads a profile picture to Firebase Storage (legacy method for backward compatibility).
     */
    public void uploadProfilePicture(Uri imageUri, Consumer<String> onComplete) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String fileName = "profile_pictures/" + System.currentTimeMillis() + "_" + imageUri.getLastPathSegment();
        StorageReference fileRef = storageRef.child(fileName);

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> onComplete.accept(uri.toString()))
                        .addOnFailureListener(e -> onComplete.accept(null)))
                .addOnFailureListener(e -> onComplete.accept(null));
    }

    /**
     * Enum for file upload types
     */
    public enum FileType {
        ID, CV, PROFILE_PICTURE
    }

    /**
     * Data class for file upload tasks
     */
    public static class FileUploadTask {
        private final Uri fileUri;
        private final FileType type;

        public FileUploadTask(Uri fileUri, FileType type) {
            this.fileUri = fileUri;
            this.type = type;
        }

        public Uri getFileUri() {
            return fileUri;
        }

        public FileType getType() {
            return type;
        }
    }
}
