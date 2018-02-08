package com.iesvirgendelcarmen.dam.video01;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Video01 extends AppCompatActivity implements SurfaceHolder.Callback {
    Button reproducir, pausar;
    Spinner spinner;
    TextView mediaUri;
    SimpleCursorAdapter adaptador;
    SurfaceView surfaceView;
    Uri pelicula;
    SurfaceHolder surfaceHolder;
    MediaPlayer reproductor;
    boolean pausa=false;
    Context micontexto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video01);

        reproducir=(Button)findViewById(R.id.reproducir);
        pausar=(Button)findViewById(R.id.pausar);
        spinner=(Spinner)findViewById(R.id.spinner);
        mediaUri=(TextView)findViewById(R.id.fichero);
        surfaceView=(SurfaceView)findViewById(R.id.video);

        final Uri mediaSrc= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        final Cursor cursor=managedQuery(mediaSrc,null,null,null,MediaStore.Audio.Media.TITLE);

        String[] nombre={MediaStore.MediaColumns.TITLE};
        int[]ids={android.R.id.text1};
        adaptador=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,nombre,ids);
        spinner.setAdapter(adaptador);


        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

            public void Renderizado(Context contexto){
                micontexto=contexto;
            }

            public void onItemSelected(AdapterView<?> padre, View vista, int posicion, long id) {
               Cursor cursor=adaptador.getCursor();
               cursor.moveToPosition(posicion);
               String indice=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
               pelicula=Uri.withAppendedPath(mediaSrc,indice);
               //cuadrado1 = new Cuadrado(color1);
               mediaUri.setText(pelicula.toString());
               Toast.makeText(getApplicationContext(), (CharSequence) mediaUri,Toast.LENGTH_SHORT).show();
            }


            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.setFixedSize(176,144);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        reproductor=new MediaPlayer();
        reproducir.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                pausa=false;
                if(reproductor.isPlaying()){
                    reproductor.reset();
                }
                reproductor.setAudioStreamType(AudioManager.STREAM_MUSIC);
                reproductor.setDisplay(surfaceHolder);
                try {
                    reproductor.setDataSource(getApplicationContext(), pelicula);
                    reproductor.prepare();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reproductor.start();
            }
        });
        pausar.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
               if(pausa==true){
                   pausa=false;
                   reproductor.start();
               }else{
                   reproductor.pause();
               }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reproductor.release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
