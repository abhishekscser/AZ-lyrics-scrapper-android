package com.cser.lyricsaz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText artist_txt;
    EditText song_txt;
    TextView lyrics_txtView;
    String url;
    String artist;
    String song;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ad code
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest); //ad code end


        lyrics_txtView = findViewById(R.id.lyrics_txtView);
        artist_txt = findViewById(R.id.artist_edtTxt);
        song_txt = findViewById(R.id.song_edtTxt);
    }

    public void onClickSearch(View view) {
        artist = artist_txt.getText().toString();
        artist = artist.replace(" ","");
        song = song_txt.getText().toString();
        song = song.replace(" ", "");
        url = "https://www.azlyrics.com/lyrics/"+artist+"/"+song+".html";
        url = url.toLowerCase();
        Log.i("test",url);
        new AsyncNetworking().execute(url);
    }

    public class AsyncNetworking extends AsyncTask<String, Void, Void> {
        String lyrics;

        @Override
        protected Void doInBackground(String... urls) {
            try {
                Document doc = Jsoup.connect(urls[0]).get();
                doc.outputSettings(new Document.OutputSettings().prettyPrint(true));
                doc.select("br").append("\n\n\n");
                lyrics = doc.text();
                lyrics = lyrics.substring(lyrics.indexOf("Lyrics",60),lyrics.lastIndexOf("Submit Corrections"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(lyrics!=null){
                lyrics_txtView.setText(lyrics);
                Log.i("test",lyrics);
            }

        }
    }

}