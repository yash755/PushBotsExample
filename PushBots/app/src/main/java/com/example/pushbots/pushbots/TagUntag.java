package com.example.pushbots.pushbots;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pushbots.push.Pushbots;

import java.util.ArrayList;

public class TagUntag extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_untag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        db = new DatabaseHelper(this);


        Cursor cr = db.getList();

        cr.moveToFirst();
        final ArrayList<String> notifylist = new ArrayList<>();
        while (!cr.isAfterLast()) {
            notifylist.add(cr.getString(cr.getColumnIndex("tag")));
            cr.moveToNext();

        }




        cr.moveToFirst();
        final ArrayList<String> value = new ArrayList<>();
        while (!cr.isAfterLast()) {
            value.add(cr.getString(cr.getColumnIndex("value")));
            cr.moveToNext();

        }



        cr.close();



        ListAdapter adpt = new Custom(this, notifylist, value);
        final ListView li = (ListView) findViewById(R.id.lv);
        li.setAdapter(adpt);

        li.setOnTouchListener(new SwipeDetector());

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                if (SwipeDetector.swipeDetected()) {

                    if (SwipeDetector.getAction() == SwipeDetector.Action.LR) {
                        arg1.setBackgroundColor(Color.GREEN);
                        db.onValueUpdate(notifylist.get(position), "yes");
                        Pushbots.sharedInstance().tag(notifylist.get(position));
                    }
                    else if (SwipeDetector.getAction() == SwipeDetector.Action.RL) {
                        arg1.setBackgroundColor(Color.WHITE);
                        db.onValueUpdate(notifylist.get(position), "no");
                        Pushbots.sharedInstance().untag(notifylist.get(position));

                    }
                }
                else
                    Toast.makeText(TagUntag.this, "Wrong Press", Toast.LENGTH_SHORT).show();

            }
        };

        li.setOnItemClickListener(listener);



    }

}
