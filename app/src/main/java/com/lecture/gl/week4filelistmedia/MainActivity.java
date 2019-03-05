package com.lecture.gl.week4filelistmedia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private String file="";
    private TextToSpeech tts=null;

    private final int REQ_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, null);
        readFileContent();

        }

    public void radio_click(View v){

        int id = v.getId();
        MediaPlayer mp=null;
        switch (id){
            case R.id.rb_sos:
                mp = MediaPlayer.create(this, R.raw.sos);
                break;

            case R.id.rb_oldCar:
                mp = MediaPlayer.create(this, R.raw.oldcar);
                break;
            case R.id.rb_muscle:
                mp = MediaPlayer.create(this, R.raw.musclecar);
                break;
            default:
                Toast.makeText(this,"Wrong Click", Toast.LENGTH_SHORT).show();
        }
        if(mp!=null){
            mp.start();
        }
    }

    private void readFileContent(){
        //read file
        Scanner scanFile = new Scanner(getResources().openRawResource(R.raw.example));
        String fileContent="";
        while(scanFile.hasNext()){
            fileContent += scanFile.nextLine();
        }

        scanFile.close();


        //now set the file content to textview
        TextView txt = findViewById(R.id.txt_file);
        txt.setTextSize(30);
        txt.setText(fileContent);

    }

    public void writeFile(View view) {
        try {
            PrintStream out = new PrintStream(
                    openFileOutput("out.txt", MODE_PRIVATE)
            );

            EditText edt =findViewById(R.id.edt_fileWrite);
            String theContent = edt.getText().toString();
            out.print(theContent);

            out.close();

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public void readTheFile(View v){

        /*Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.d("Week4", "Buton is clicked");

                    //return html
                    URL uri = new URL("https://github.com/fabio001/MediaPlayerCamera/blob/master/app/src/main/res/raw/mydict.txt");
                    Scanner scanner = new Scanner(uri.openStream());
                    Log.d("Week4", "File Read");

                    while(scanner.hasNext()){
                        file += scanner.nextLine();
                    }
                    scanner.close();

                }catch (Exception e){
                    Log.e("Week4", e.getLocalizedMessage());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Week4", file);
                        Toast.makeText(MainActivity.this, file, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        thread.start();
*/

        try {
            file="";
            Scanner scanner = new Scanner(
                    openFileInput("out.txt"));

            while (scanner.hasNext()) {
                file += scanner.nextLine();
            }

            scanner.close();

            Toast.makeText(this, file, Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        if(!tts.isSpeaking())
            tts.speak(file, TextToSpeech.QUEUE_FLUSH, null);

    }

    public void takePics(View v){

        Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            ImageView imgView = findViewById(R.id.imageView);
            imgView.setImageBitmap(bitmap);
        }
    }
}
