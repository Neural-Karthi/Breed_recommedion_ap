package com.example.project_dog;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText ed_mail,ed_pass;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_mail=(EditText) findViewById(R.id.useremail);
        ed_pass=(EditText) findViewById(R.id.userpassword);

        btn=findViewById(R.id.button2);
        boolean shouldHideActionBar = true;
        if (shouldHideActionBar) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_mail.getText().toString().equals("Admin") && ed_pass.getText().toString().equals("Admin")){
                    Intent new_screen=new Intent(MainActivity.this,puppy_home.class);
                    startActivity(new_screen);
                }
                else{
                    Toast.makeText(getBaseContext(),"Invalid credential",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}