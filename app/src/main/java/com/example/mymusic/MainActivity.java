package com.example.mymusic;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public
class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView noMusicTextview,songTxt;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    ArrayList<AudioModel> SongList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: state to window");
        initVar();


        if (!checkPermission()) {
            requestPermissions();
            return;

        }


      String[] projection={
                MediaStore.Audio.Media.DATA,//Absolute filesystem path to the media item on disk
              MediaStore.Audio.Media.TITLE,
              MediaStore.Audio.Media.DURATION
      };

        String Selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";

        Cursor cursor =getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,Selection,null,null);
        while (cursor.moveToNext()){
            AudioModel songData = new AudioModel(cursor.getString(0),cursor.getString(2),cursor.getString(1));
            if (new File(songData.getPath()).exists()){
                SongList.add(songData);
            }
        }
        if (SongList.size()==0){
            songTxt.setVisibility(View.GONE);
            noMusicTextview.setVisibility(View.VISIBLE);

        }
     else {
            // recycleview
            Log.e(TAG, "onCreate: pass 1");
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            Log.e(TAG, "onCreate: pass 2");

            recyclerView.setAdapter(new MusicAdapter(SongList,getApplicationContext()));
        }
    }

    private
    void initVar() {
        recyclerView =findViewById(R.id.recycleView);
        noMusicTextview =findViewById(R.id.no_songtext);
        songTxt = findViewById(R.id.songtext);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.NavigationDrawer);
        // this is for custom toolbar
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate: state after the toolbar setup");

        // toggle action bar
        ActionBarDrawerToggle drawerToggle =new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        // click view swip kar ka toggle
        drawerLayout.addDrawerListener(drawerToggle);
        Log.d(TAG, "initVar: after drawer listner");
        drawerToggle.syncState();


    }

    private
    void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this, "READ PERMISSION IS REQUIRED, PLEASE ALLOW FROM SETTINGS", Toast.LENGTH_SHORT);
        }else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);

        }
    }


    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


}
