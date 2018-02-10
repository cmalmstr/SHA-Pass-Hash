package com.carlm.simplehasher;

import android.annotation.SuppressLint;
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
    private CountDownTimer countdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onPause(){
        super.onPause();
        if (countdown != null)
            countdown.cancel();
        clear();
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
    @SuppressWarnings("unused")
    public void hashIt(View view){
        EditText inputData = findViewById(R.id.inputField);
        String userInput = inputData.getText().toString();
        String hash = new String(Hex.encodeHex(DigestUtils.sha1(userInput)));
        inputData.setText(R.string.defInput);
        setContentView(R.layout.activity_show);
        TextView outputData = findViewById(R.id.outputField);
        inputData = findViewById(R.id.inputField);
        outputData.setText(hash);
        inputData.setText(userInput);
        clipIt(hash);
        showIt();
    }
    private void clipIt(String hash){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, hash);
        if (clipboard != null)
            clipboard.setPrimaryClip(clip);
    }
    private void showIt(){
        final int delay = 8000;
        final int steps = 200;
        final TextView clearData = findViewById(R.id.clearMessage);
        final ProgressBar showDelay = findViewById(R.id.delayLine);
        if (countdown != null)
            countdown.cancel();
        countdown = new CountDownTimer(delay, delay/steps) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                showDelay.incrementProgressBy(400/steps);
                clearData.setText(getString(R.string.clearMessage) + (1+millisUntilFinished/1000) + getString(R.string.s));
            }
            @Override
            public void onFinish() {
                clear();
            }
        }.start();
    }
    private void clear(){
        setContentView(R.layout.activity_main);
    }
}
