package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public
class MusicPlayerActivity extends AppCompatActivity {
    ArrayList<AudioModel> songlist;
    AudioModel currentSong;
    TextView musicName,endTime,startTime;
    ImageView play,repeat;
    ImageButton listBtn,previous,next;
    SeekBar seekBar;
    boolean repeated=false,playMusic =false;
    MediaPlayer mediaPlayer= My.getInstance();// carefully

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        initVar();

        songlist = (ArrayList<AudioModel>) getIntent().getSerializableExtra("List");
        setResourceswithMusic();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                if (playMusic==false){
                    playSong();
                    playMusic=true;
                }
                else{
                    pauseSong();
                    playMusic=false;
                }

            }
        });

        listBtn.setOnClickListener(v -> listView());
        previous.setOnClickListener(v -> previousSong());
        next.setOnClickListener(v -> nextSoung());
        repeat.setOnClickListener(v -> repeatSong());
        musicName.setSelected(true);

seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public
    void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(mediaPlayer!=null & fromUser){
            // it bring music to the seeked position by user
            mediaPlayer.seekTo(progress);
        }

    }

    @Override
    public
    void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public
    void onStopTrackingTouch(SeekBar seekBar) {

    }
});

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public
            void run() {
                if (mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    startTime.setText(convertTOMS(mediaPlayer.getCurrentPosition()+""));
                }
                if (mediaPlayer.getCurrentPosition()==mediaPlayer.getDuration() && repeated==false ){
                    nextSoung();
                }


                new Handler().postDelayed(this,100);
            }
        });

    }

    private
    void initVar() {
        play = findViewById(R.id.play);

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        listBtn =findViewById(R.id.listView);
        previous = findViewById(R.id.previousMusic);
        next= findViewById(R.id.nextMusic);
        repeat=findViewById(R.id.repeat);
        seekBar = findViewById(R.id.seekbar);
        musicName = findViewById(R.id.musicName);

    }


    private
    void repeatSong() {
        if (repeated ==false) {
            repeat.setImageResource(R.drawable.ic_baseline_repeat_one_24);
            mediaPlayer.setLooping(true);
            repeated=true;
            return;
        }
        repeat.setImageResource(R.drawable.ic_baseline_repeat_24);
        repeated=false;
        mediaPlayer.setLooping(false);

        return;



    }

    private
    void nextSoung() {
        playMusic=false;
        if (My.currentindex==songlist.size()-1){
            return;
        }
        My.currentindex+=1;
        mediaPlayer.reset();
        setResourceswithMusic();
        playSong();



    }

    private
    void previousSong() {
        playMusic=false;
        if (My.currentindex==0){
            return;
        }
        My.currentindex-=1;
        mediaPlayer.reset();
        setResourceswithMusic();
        playSong();

    }

    private
    void listView() {
    }

    private
    void pauseSong() {
        mediaPlayer.pause();
        play.setImageResource(R.drawable.ic_baseline_play_arrow_24);

    }
    private
    void playSong() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
            play.setImageResource(R.drawable.ic_baseline_pause_24);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private
    void setResourceswithMusic() {

        currentSong = songlist.get(My.currentindex);
        musicName.setText(currentSong.getTitle());
        endTime.setText(convertTOMS(currentSong.getDuration()));


    }

    private
    static String convertTOMS(String duration) {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis)%TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis)%TimeUnit.MINUTES.toSeconds(1));

    }
}