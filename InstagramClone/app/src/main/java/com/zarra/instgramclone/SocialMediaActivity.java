package com.zarra.instgramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class SocialMediaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("Social Media App!");

        toolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        tabAdapter=new TabAdapter(getSupportFragmentManager(),0);
        viewPager=findViewById(R.id.viewPager);
        viewPager.setAdapter(tabAdapter);

        tabLayout=findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager,true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.postImageItem:
                if(Build.VERSION.SDK_INT>=23 &&
                        ActivityCompat.checkSelfPermission(SocialMediaActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},3000);
                else{
                    captureImage();
                }
                break;
            case R.id.logoutUser:
                ParseUser.logOut();
                finish();
                startActivity(new Intent(SocialMediaActivity.this,SignUp.class));
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==3000){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                captureImage();
            }
        }
    }
    private void captureImage() {
        //FancyToast.makeText(getContext(),"N",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
        Intent intent=new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,4000);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==4000){
            if(resultCode== Activity.RESULT_OK){
                try {
                    Uri selectedImage=data.getData();
                    Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] bytes=byteArrayOutputStream.toByteArray();

                    ParseFile parseFile=new ParseFile("img.png",bytes);
                    ParseObject parseObject=new ParseObject("Photo");
                    parseObject.put("picture",parseFile);
                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                    final ProgressDialog dialog=new ProgressDialog(SocialMediaActivity.this);
                    dialog.setMessage("Loading...");
                    dialog.show();
                    parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                                    if(e==null){
                                        FancyToast.makeText(SocialMediaActivity.this,"Successfully uploaded!",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                    } else {
                                        FancyToast.makeText(SocialMediaActivity.this,"Something went wrong( "+e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                                    }
                                    dialog.dismiss();
                                }
                            });
                        }

                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}