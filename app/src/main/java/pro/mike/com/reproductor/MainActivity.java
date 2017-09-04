package pro.mike.com.reproductor;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.R.attr.duration;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,AdapterView.OnItemSelectedListener {
    Spinner spinner;
    boolean p = false;
    Handler handler;
    SeekBar volumen = null;
    private AudioManager audioManager = null;
    SeekBar progreso;
    Button button;
    int index = 0;
    int m;
    AssetFileDescriptor afd;
    List<Song> songs = new ArrayList<Song>();
    ImageView foto;
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        controlarVolumen();
        songs.add(new Song("Baile de las papas",R.raw.bailepapas,R.drawable.papas));
        songs.add(new Song("Tengo un berraco",R.raw.berraco,R.drawable.berraco));
        songs.add(new Song("Opa yo viaze un corra",R.raw.corra,R.drawable.elkoala));
        songs.add(new Song("Soy arbañi",R.raw.koalarbani,R.drawable.arbani));
        songs.add(new Song("Maplestory-Pantheon field",R.raw.maplestory,R.drawable.maplestory));
        mediaPlayer = MediaPlayer.create(this,songs.get(0).getIdSong());
        button = (Button) findViewById(R.id.play);
        foto = (ImageView) findViewById(R.id.imageView);
        mediaPlayer.setOnCompletionListener(this);
        progreso = (SeekBar)findViewById(R.id.progreso);
        progreso.setProgress(mediaPlayer.getCurrentPosition());
        spinner = (Spinner) findViewById(R.id.listaCanciones);
        ArrayAdapter<Song> adapter = new ArrayAdapter<Song>(this,
                android.R.layout.simple_spinner_item,songs );
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        handler = new Handler();
        final Runnable r = new Runnable(){
            public void run() {
                // Toast.makeText(getApplicationContext(),"Pro", Toast.LENGTH_SHORT).show();
                progreso.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,100);
            }
        };
        handler.postDelayed(r,1);
        progreso.setMax(0);
        progreso.setMax(mediaPlayer.getDuration());
        progreso.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(progreso.getProgress());
                mediaPlayer.start();
                handler.postDelayed(r,1);
                button.setBackgroundResource(android.R.drawable.ic_media_pause);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
                handler.removeCallbacks(r);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                //  Toast.makeText(getApplicationContext(),"Pro", Toast.LENGTH_SHORT).show();

            }
        });

    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        next();

    }

    private void next()
    {
        index = (index +1)% 5;
        AssetFileDescriptor afd = this.getResources().openRawResourceFd(songs.get(index).getIdSong());
        foto.setImageResource(songs.get(index).getIdPhoto());
        changeSong(afd);

    }


    public void play (View view) {
        p ^= true;
        if (p) {
            mediaPlayer.start();
            button.setBackgroundResource(android.R.drawable.ic_media_pause);
        } else {
            //mPlayerList.get(index).stop();
            // mPlayer.pause();
            mediaPlayer.pause();
            button.setBackgroundResource(android.R.drawable.ic_media_play);
        }
    }
    public void controlarVolumen(){
        try
        {
            volumen = (SeekBar)findViewById(R.id.volumen);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumen.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumen.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            volumen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void nextSong(View view){
        mediaPlayer.seekTo(mediaPlayer.getDuration()-1);
    }

    public void regresar(View view) {
        Button regresar = (Button) findViewById(R.id.back);
        if (mediaPlayer.getCurrentPosition() > 20000) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else {
            ////////////////////////////////
            index = (index +4)% 5;
            AssetFileDescriptor afd = this.getResources().openRawResourceFd(songs.get(index).getIdSong());
            foto.setImageResource(songs.get(index).getIdPhoto());
            changeSong(afd);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        index = position;
        AssetFileDescriptor afd = this.getResources().openRawResourceFd(songs.get(index).getIdSong());
        foto.setImageResource(songs.get(index).getIdPhoto());
        changeSong(afd);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void changeSong(AssetFileDescriptor afd){
        try
        {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            button.setBackgroundResource(android.R.drawable.ic_media_pause);
            afd.close();
            progreso.setMax(0);
            progreso.setMax(mediaPlayer.getDuration());
            spinner.setSelection(index);
        }
        catch (IllegalArgumentException e)
        {
            Log.e("pro", "Unable to play audio queue do to exception: " + e.getMessage(), e);
        }
        catch (IllegalStateException e)
        {
            Log.e("pro", "Unable to play audio queue do to exception: " + e.getMessage(), e);
        }
        catch (IOException e)
        {
            Log.e("pro", "Unable to play audio queue do to exception: " + e.getMessage(), e);
        }
    }
}
