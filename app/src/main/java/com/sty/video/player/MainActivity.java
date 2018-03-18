package com.sty.video.player;

import android.Manifest;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private VideoView vvVitamio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        playVideoWithPermission();
    }

    private void playVideoWithPermission(){
        if(PermissionsUtil.hasPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})){
            playVideo();
        }else{
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(String[] permission) {
                    playVideo();
                }

                @Override
                public void permissionDenied(String[] permission) {
                    Toast.makeText(MainActivity.this, "您拒绝了外置存储的访问权限", Toast.LENGTH_LONG).show();
                }
            }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
        }
    }

    private void playVideo(){
        if(!LibsChecker.checkVitamioLibs(this)){
            return;
        }

        vvVitamio = findViewById(R.id.vv_vitamio);
        String videoPath = Environment.getExternalStorageDirectory().getPath() + "/sty/ad.mp4";
        vvVitamio.setVideoPath(videoPath);

        vvVitamio.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                vvVitamio.start();
            }
        });

        vvVitamio.setMediaController(new MediaController(this));
    }
}
