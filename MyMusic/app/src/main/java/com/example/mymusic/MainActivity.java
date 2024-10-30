package com.example.mymusic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    private static final int LEVEL_PAUSE = 0;
    private static final int LEVEL_PLAY = 1;
    private static final MediaPlayer player = new MediaPlayer();
    private static final int STATE_IDE = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSED = 3;
    private final ArrayList<SongEntity> listSong = new ArrayList<>();
    private TextView tvName, tvAlbum, tvTime;
    private SeekBar seekBar;
    private ImageView ivPlay;
    private int index;
    private SongEntity songEntity;
    private Thread thread;
    private int state = STATE_IDE;
    private String totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        ivPlay = findViewById(R.id.iv_play);
        ivPlay.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_next).setOnClickListener(this);
        tvName = findViewById(R.id.tv_name);
        tvAlbum = findViewById(R.id.tv_album);
        tvTime = findViewById(R.id.tv_time);
        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);

        // Kiểm tra quyền đọc bộ nhớ
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            return;
        }

        loadingListSongOffline();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadingListSongOffline();
        } else {
            Toast.makeText(this, R.string.txt_alert, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadingListSongOffline() {
        Cursor c = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (c != null) {
            listSong.clear();
            if (c.moveToFirst()) {
                do {
                    String name;
                    String path;
                    String album = "N/A";
                    try {
                        name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                        path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                            album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST));
                        } else {
                            album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        continue; // Bỏ qua bài hát nếu có lỗi
                    }

                    listSong.add(new SongEntity(name, path, album));
                } while (c.moveToNext());
            }

            // Kiểm tra nếu không có bài hát nào
            if (listSong.isEmpty()) {
                Toast.makeText(this, "No audio files found in your library", Toast.LENGTH_SHORT).show();
            } else {
                RecyclerView rv = findViewById(R.id.rv_song);
                rv.setLayoutManager(new LinearLayoutManager(this));
                rv.setAdapter(new MusicAdapter(listSong, this));
                play();
                playPause();
            }

            c.close();
        } else {
            Toast.makeText(this, "Error querying media store", Toast.LENGTH_SHORT).show();
        }
    }



    public void playSong(SongEntity songEntity) {
        index = listSong.indexOf(songEntity);
        this.songEntity = songEntity;
        play();
    }

    @Override
    public void onClick(View v) {
        if (listSong.isEmpty()) {
            Toast.makeText(this, "No songs available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (v.getId() == R.id.iv_play) {
            playPause();
        } else if (v.getId() == R.id.iv_next) {
            next();
        } else if (v.getId() == R.id.iv_back) {
            back();
        }
    }

    private void back() {
        if (index == 0) {
            index = listSong.size() - 1;
        } else {
            index--;
        }
        play();
    }

    private void next() {
        if (index >= listSong.size() - 1) {
            index = 0;
        } else {
            index++;
        }
        play();
    }

    private void playPause() {
        if (state == STATE_PLAYING && player.isPlaying()) {
            player.pause();
            ivPlay.setImageLevel(LEVEL_PAUSE);
            state = STATE_PAUSED;
        } else if (state == STATE_PAUSED) {
            player.start();
            state = STATE_PLAYING;
            ivPlay.setImageLevel(LEVEL_PLAY);
        } else {
            play();
        }
    }

    private void play() {
        songEntity = listSong.get(index);
        tvName.setText(songEntity.getName());
        tvAlbum.setText(songEntity.getAlbum());
        player.reset();
        try {
            player.setDataSource(songEntity.getPath());
            player.prepare();
            player.start();
            ivPlay.setImageLevel(LEVEL_PLAY);
            state = STATE_PLAYING;
            totalTime = getTime(player.getDuration());
            seekBar.setMax(player.getDuration());
            if (thread == null) {
                startLooping();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLooping() {
        thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        return;
                    }
                    runOnUiThread(() -> updateTime());
                }
            }
        };
        thread.start();
    }

    private void updateTime() {
        if (state == STATE_PLAYING || state == STATE_PAUSED) {
            int time = player.getCurrentPosition();
            tvTime.setText(String.format("%s/%s", getTime(time), totalTime));
            seekBar.setProgress(time);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getTime(int time) {
        return new SimpleDateFormat("mm:ss").format(new Date(time));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread != null) { // Kiểm tra xem thread có khác null hay không
            thread.interrupt();
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (state == STATE_PLAYING || state == STATE_PAUSED) {
            player.seekTo(seekBar.getProgress());
        }
    }
}
