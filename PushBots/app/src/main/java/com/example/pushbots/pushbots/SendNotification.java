package com.example.pushbots.pushbots;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pushbots.push.Pushbots;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendNotification extends AppCompatActivity implements View.OnClickListener{

    DatabaseHelper db;
    Button tag,all;
    EditText msg;
    JSONArray arr = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        all = (Button) findViewById(R.id.all);
        tag = (Button) findViewById(R.id.tag);
        msg = (EditText)findViewById(R.id.msg);

        all.setOnClickListener(this);
        tag.setOnClickListener(this);

        db = new DatabaseHelper(this);


        Cursor cr = db.getTagList();

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



        ListAdapter adpt = new Custom1(this, notifylist, value);
        final ListView li = (ListView) findViewById(R.id.lv1);
        li.setAdapter(adpt);

        li.setOnTouchListener(new SwipeDetector());

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                if (SwipeDetector.swipeDetected()) {

                    if (SwipeDetector.getAction() == SwipeDetector.Action.LR) {
                        arg1.setBackgroundColor(Color.GREEN);
                        arr.put(notifylist.get(position));

                    }
                    else if (SwipeDetector.getAction() == SwipeDetector.Action.RL) {
                        arg1.setBackgroundColor(Color.WHITE);
                        arr.remove(position);


                    }
                }
                else
                    Toast.makeText(SendNotification.this, "Wrong Press", Toast.LENGTH_SHORT).show();

            }
        };

        li.setOnItemClickListener(listener);



    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.all:
                sendall();
                break;

            case R.id.tag:
                 tagall(arr);
                break;


        }

    }

    void sendall()
    {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        String url = "https://api.pushbots.com/push/all";

    try {

        JSONObject pay = new JSONObject();
        pay.put("largeIcon","https://lh3.googleusercontent.com/-2bMJQbKLNRQ/AAAAAAAAAAI/AAAAAAAA_nc/SYnugOyXboE/s120-c/photo.jpga");

            JSONObject jsonobj = new JSONObject();
            jsonobj.put("platform", "1");
            jsonobj.put("msg",msg.getText().toString());
            jsonobj.put("payload",pay);


            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            // JSONArray jsonArray = null;
                            try {


                                //  jsonArray = response.getJSONArray("success");
                                //   JSONObject jsonobj1 = new JSONObject(response);
                                response = response.getJSONObject("");
                                //  String site = response.getString("fname");
                                //         network = response.getString("network");
                                System.out.println("I am response" + response.toString());
                                pDialog.hide();
                            } catch (JSONException e) {
                                System.out.println("I am catch" + response.toString());
                                pDialog.hide();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(), "Time Out Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(), "Authentication Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NetworkError) {
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(), "Sent Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            })

            {


                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("x-pushbots-appid","567d7d0f177959a5518b456a");
                    headers.put("x-pushbots-secret","4f6d1eaa3ce697bb64a40021864b2ea9");
                    return headers;

                }


            };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(postRequest);

        }catch (Exception e){
            e.printStackTrace();
        }



    }


    void tagall(JSONArray arr)
    {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        String url = "https://api.pushbots.com/push/all";

        try {
            JSONObject pay = new JSONObject();
            pay.put("largeIcon","https://lh3.googleusercontent.com/-2bMJQbKLNRQ/AAAAAAAAAAI/AAAAAAAA_nc/SYnugOyXboE/s120-c/photo.jpga");

            JSONObject jsonobj = new JSONObject();
            jsonobj.put("platform", "1");
            jsonobj.put("tags",arr);
            jsonobj.put("msg", msg.getText().toString());
            jsonobj.put("payload",pay);


            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            // JSONArray jsonArray = null;
                            try {


                                //  jsonArray = response.getJSONArray("success");
                                //   JSONObject jsonobj1 = new JSONObject(response);
                                response = response.getJSONObject("");
                                //  String site = response.getString("fname");
                                //         network = response.getString("network");
                                System.out.println("I am response" + response.toString());
                                pDialog.hide();
                            } catch (JSONException e) {
                                System.out.println("I am catch" + response.toString());
                                pDialog.hide();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(), "Time Out Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(), "Authentication Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NetworkError) {
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(), "Sent Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            })

            {


                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("x-pushbots-appid","567d7d0f177959a5518b456a");
                    headers.put("x-pushbots-secret","4f6d1eaa3ce697bb64a40021864b2ea9");
                    return headers;

                }


            };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(postRequest);

        }catch (Exception e){
            e.printStackTrace();
        }



    }

}


