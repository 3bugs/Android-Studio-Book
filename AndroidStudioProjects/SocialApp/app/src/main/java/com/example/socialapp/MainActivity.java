package com.example.socialapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private static final String PERMISSION = "publish_actions";

    CallbackManager callbackManager;
    ProfileTracker profileTracker;

    private TextView userName;
    private ProfilePictureView profilePicture;
    private Button postLinkButton;
    private Button postPictureButton;

    private enum PendingAction {
        NONE,
        POST_LINK,
        POST_PICTURE
    }
    private PendingAction pendingAction = PendingAction.NONE;

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {
            String title = "เกิดข้อผิดพลาดในการโพสต์";
            String msg = error.getMessage();
            showResult(title, msg);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            if (result.getPostId() != null) {
                String title = "โพสต์ลง Facebook สำเร็จ";
                String id = result.getPostId();
                String msg = String.format("Post ID: %s", id);
                showResult(title, msg);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton("OK", null)
                    .show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handlePendingAction();
                        updateUI();
                    }

                    @Override
                    public void onCancel() {
                        updateUI();
                    }

                    @Override
                    public void onError(FacebookException e) {
                        updateUI();
                    }
                });

        setContentView(R.layout.activity_main);

        userName = (TextView) findViewById(R.id.user_name);
        profilePicture = (ProfilePictureView) findViewById(R.id.profile_picture);
        postLinkButton = (Button) findViewById(R.id.post_link_button);
        postPictureButton = (Button) findViewById(R.id.post_picture_button);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                updateUI();
            }
        };

        postLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performPublish(PendingAction.POST_LINK);
            }
        });

        postPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickPictureDialog();
            }
        });
    }

    private void updateUI() {
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();

        if (loggedIn && (profile != null)) {
            profilePicture.setProfileId(profile.getId());
            userName.setText(profile.getName());
            postLinkButton.setEnabled(true);
            postPictureButton.setEnabled(true);
        } else {
            profilePicture.setProfileId(null);
            userName.setText(null);
            postLinkButton.setEnabled(false);
            postPictureButton.setEnabled(false);
        }
    }

    private void performPublish(PendingAction action) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null) {
            pendingAction = action;
            handlePendingAction();
        }
    }

    private void handlePendingAction() {
        PendingAction oldPendingAction = pendingAction;
        pendingAction = PendingAction.NONE;

        switch (oldPendingAction) {
            case NONE:
                break;
            case POST_LINK:
                postLink();
                break;
            case POST_PICTURE:
                postPicture();
                break;
        }
    }

    private void postLink() {
        Profile profile = Profile.getCurrentProfile();
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("3bugs.com")
                .setContentDescription("บทเรียน บทความ และอบรมการเขียนแอพ Android")
                .setContentUrl(Uri.parse("http://www.3bugs.com/"))
                .setImageUrl(Uri.parse("http://www.3bugs.com/logo.png"))
                .build();

        if (profile != null && hasPublishPermission()) {
            ShareApi.share(content, shareCallback);
        } else {
            pendingAction = PendingAction.POST_LINK;
            LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList(PERMISSION));
        }
    }

    private void postPicture() {
        Profile profile = Profile.getCurrentProfile();

        Bitmap picture = BitmapFactory.decodeFile(imageFilePath);
        SharePhoto pictureToShare = new SharePhoto.Builder()
                .setBitmap(picture)
                .build();

        ArrayList<SharePhoto> pictureList = new ArrayList<>();
        pictureList.add(pictureToShare);

        SharePhotoContent content = new SharePhotoContent.Builder()
                .setPhotos(pictureList)
                .build();

        if (profile != null && hasPublishPermission()) {
            ShareApi.share(content, shareCallback);
        } else {
            pendingAction = PendingAction.POST_PICTURE;
            LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList(PERMISSION));
        }
    }

    private boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }

    private final static int PICK_IMAGE = 1;
    private String imageFilePath;  // ชื่อพาธของไฟล์รูปภาพ

    private void showPickPictureDialog() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "เลือกรูปภาพ"), PICK_IMAGE);
    }

    private void showConfirmPostPictureDialog() {
        Bitmap picture = BitmapFactory.decodeFile(imageFilePath);
        if (picture == null) {
            Log.d(TAG, "Bitmap is NULL!!!");
        }

        final ImageView imageview = new ImageView(this);
        imageview.setImageBitmap(picture);

        new AlertDialog.Builder(this)
                .setTitle("โพสต์รูปภาพนี้ลง Facebook ?")
                .setView(imageview)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        performPublish(PendingAction.POST_PICTURE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            Uri uri = data.getData();

            if (uri != null) {
                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor;

                if (Build.VERSION.SDK_INT > 19) {
                    // Will return "image:x*"
                    String wholeID = DocumentsContract.getDocumentId(uri);
                    // Split at colon, use second item in the array
                    String id = wholeID.split(":")[1];
                    // where id is equal to
                    String sel = MediaStore.Images.Media._ID + "=?";

                    cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            projection, sel, new String[] { id }, null);
                }
                else {
                    cursor = getContentResolver().query(uri, projection, null, null, null);
                }

                cursor.moveToFirst();

                imageFilePath = cursor.getString(0);
                Log.d(TAG, "File path: " + imageFilePath);
                showConfirmPostPictureDialog();

                cursor.close();
            }
        }
    }
}
