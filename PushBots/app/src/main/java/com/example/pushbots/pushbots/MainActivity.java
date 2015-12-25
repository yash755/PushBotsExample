package com.example.pushbots.pushbots;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.pushbots.push.Pushbots;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button notify,tags;
    DatabaseHelper db;
    int  count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(isConnected()) {
            Pushbots.sharedInstance().init(this);
            notify = (Button) findViewById(R.id.notify);
            tags = (Button) findViewById(R.id.tags);
        }
        else
            Toast.makeText(getApplicationContext(), "You are Not Connected", Toast.LENGTH_SHORT).show();


        notify.setOnClickListener(this);
        tags.setOnClickListener(this);


        //Dummy DataBase
        db = new DatabaseHelper(this);
        count = db.getcount();

        if (db.getcount() == 0) {

            db.insertdata("Dance");
            db.insertdata("Music");
            db.insertdata("Art");
            db.insertdata("Sports");
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.notify:
                startActivity(new Intent(this, SendNotification.class));
                break;

            case R.id.tags:
                startActivity(new Intent(this, TagUntag.class));
                break;


        }

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
