package com.example.myreelloader;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText,caption;
    ClipboardManager clipboardManager;
    Button button,paste,save,copy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.link);
        button = findViewById(R.id.button);
        paste=findViewById(R.id.paste);
        save=findViewById(R.id.savecaption);
        copy=findViewById(R.id.copy_cap);
        caption=findViewById(R.id.caption);
        clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        SharedPreferences sharedPreferences=getSharedPreferences("Caption",MODE_PRIVATE);
        String val=sharedPreferences.getString("Cap","Nothing");
        caption.setText(val);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Instagram.download(MainActivity.this,editText.getText().toString());
            }
        });

        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData a=clipboardManager.getPrimaryClip();
                ClipData.Item item=a.getItemAt(0);
                String text=item.getText().toString();
                editText.setText(text);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=caption.getText().toString();
                SharedPreferences sharedPreferences=getSharedPreferences("Caption",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("Cap",msg);
                editor.apply();
                caption.setText(msg);
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clipdata=ClipData.newPlainText("Captions",caption.getText().toString());
                clipboardManager.setPrimaryClip(clipdata);
                Toast.makeText(MainActivity.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

}