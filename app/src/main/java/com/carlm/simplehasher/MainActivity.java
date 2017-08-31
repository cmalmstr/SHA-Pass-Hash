package com.carlm.simplehasher;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class MainActivity extends AppCompatActivity {
private int hashes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hashes = 0;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Uri uriUrl = Uri.parse("http://cmalmstrom.com/donate.html");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
        return true;
    }
    @SuppressWarnings("UnusedParameters")
    public void hashIt(View view){
        int delay = 8000;
        EditText inputData = (EditText)findViewById(R.id.inputField);
        String userInput = inputData.getText().toString();
        String hash = new String(Hex.encodeHex(DigestUtils.sha1(userInput)));
        setContentView(R.layout.activity_show);
        TextView outputData = (TextView)findViewById(R.id.outputField);
        inputData = (EditText)findViewById(R.id.inputField);
        outputData.setText(hash);
        inputData.setText(userInput);
        hashes++;
        clipIt(hash);
        showIt(delay);
    }
    private void clipIt(String hash){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, hash);
        clipboard.setPrimaryClip(clip);
    }
    @SuppressWarnings("UnusedAssignment")
    private void showIt(final int delay){
        final int steps = 200;
        final TextView clearData = (TextView)findViewById(R.id.clearMessage);
        final ProgressBar showDelay = (ProgressBar)findViewById(R.id.delayLine);
        final String clearMessage = "Hash will clear in ";
        final String s = " seconds";
        CountDownTimer countdown = new CountDownTimer(delay, delay/steps) {
            @Override
            public void onTick(long millisUntilFinished) {
                showDelay.incrementProgressBy(400/steps);
                clearData.setText(clearMessage + (1+millisUntilFinished/1000) + s);
            }
            @Override
            public void onFinish() {
                clear();
            }
        }.start();
    }
    private void clear(){
        hashes--;
        if(hashes == 0)
        setContentView(R.layout.activity_main);
    }
}
