package com.feedback.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.feedback.Fragments.FragmentHome;
import com.feedback.Model.UserResponse;
import com.feedback.Presenter.HomePresenter;
import com.feedback.R;
import com.feedback.Service.FeedbackService;
import com.feedback.Session.SessionLogin;
import com.feedback.Utils.Constants_app;
import com.feedback.Utils.Utils;

import java.io.File;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomePresenter.ProfilePicChangeListener {
    private Activity mActivity;
    private int imageSourceOption = 0;
    private ImageView ivProfile;
    private String image;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        try {
            mActivity = ActivityMain.this;
            Nammu.init(this);
            EasyImage.configuration(this)
                    .setImagesFolderName(Constants_app.folderName)
                    .saveInRootPicturesDirectory().saveInAppExternalFilesDir();
            utils = new Utils(mActivity);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newPost();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            setHomeFragment();

            UserResponse userResponse = SessionLogin.getUser();
            View headerLayout = navigationView.getHeaderView(0);
            TextView tvName = (TextView) headerLayout.findViewById(R.id.tv_name);
            TextView tvEmail = (TextView) headerLayout.findViewById(R.id.tv_mail);
            ivProfile = (ImageView) headerLayout.findViewById(R.id.iv_profile);
            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickImageOption();
                }
            });

            tvEmail.setText(userResponse.getEmail());
            tvName.setText(userResponse.getFirstname() + " " + userResponse.getLasttname());
            String image = FeedbackService.BASE_URL_IMAGE + userResponse.getProfile_picture();
            Glide.with(mActivity).load(image).placeholder(R.drawable.ic_place_holder).into(ivProfile);

            if (userResponse.getUser_type().equals("1")) {
                fab.setVisibility(View.VISIBLE);
                navigationView.getMenu().findItem(R.id.nav_new_post).setVisible(true);
            } else {
                fab.setVisibility(View.GONE);
                navigationView.getMenu().findItem(R.id.nav_new_post).setVisible(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void pickImageOption() {
        final String[] list = {"Gallery", "Camera"};

        new MaterialDialog.Builder(this)
                .title("Image from")
                .items(list)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        imageSourceOption = which;
                        Log.e("imageSourceOption", "" + imageSourceOption);
                        if (imageSourceOption == 0)
                            selectImageFromDeviceGallery();
                        else
                            pickImageFromCamera();

                        return true;
                    }
                })
                .positiveText("Ok")
                .show();
    }


    private void pickImageFromCamera() {
        try {
            EasyImage.openCamera(this, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void selectImageFromDeviceGallery() {
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                EasyImage.openGallery(mActivity, 0);
            } else {
                getWritePermissionForGallery();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWritePermissionForGallery() {
        Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                EasyImage.openGallery(mActivity, 0);
            }

            @Override
            public void permissionRefused() {
            }
        });
    }


    private void setImage(File imageFile) {
        Glide.with(mActivity).load(imageFile).placeholder(R.drawable.ic_place_holder).into(ivProfile);
        File file = new File(Utils.compressImage(imageFile));
        image = Utils.fileToBase64(file);
        utils.showProgress();
        HomePresenter homePresenter = new HomePresenter(this, mActivity);
        UserResponse userResponse = new UserResponse();
        userResponse.setMobile(SessionLogin.getmobile());
        userResponse.setProfile_picture(image);
        homePresenter.change(userResponse);
    }

    @Override
    protected void onDestroy() {
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }


    private void newPost() {
        Intent intent = new Intent(mActivity, ActivityNewPost.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("requestCode", "-->" + requestCode);
        Log.e("resultCode", "-->" + resultCode);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            FragmentHome fragmentHome = (FragmentHome) getSupportFragmentManager().findFragmentByTag("FragmentHome");
            fragmentHome.refresh();
        }

        if (resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    if (data.hasExtra("from")) {
                        FragmentHome fragmentHome = (FragmentHome) getSupportFragmentManager().findFragmentByTag("FragmentHome");
                        fragmentHome.refresh();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                setImage(imageFile);
            }


            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(mActivity);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.nav_home) {
        } else if (id == R.id.nav_change_password) {
            Intent intent = new Intent(mActivity, ActivityChangePssword.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(mActivity, ActivityLogin.class);
            finish();
            SessionLogin.clearLoginSession();
            startActivity(intent);
        } else if (id == R.id.nav_new_post) {
            newPost();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setHomeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_activity_main, new FragmentHome(), "FragmentHome")
                .commit();
    }

    @Override
    public void profilePicChanged() {
        utils.hideProgress();
        UserResponse userResponse = SessionLogin.getUser();
        String image = FeedbackService.BASE_URL_IMAGE + userResponse.getProfile_picture();
        Glide.with(mActivity).load(image).placeholder(R.drawable.ic_place_holder).into(ivProfile);
    }

    @Override
    public void failure(String msg) {
        utils.hideProgress();
        Utils.alertBox(mActivity, msg);
    }
}
