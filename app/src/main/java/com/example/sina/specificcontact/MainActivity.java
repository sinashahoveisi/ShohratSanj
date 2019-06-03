package com.example.sina.specificcontact;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {


    static Context context;

    Typeface font_Medium;
    Typeface font_Bold;

    boolean isExit;

    public static Boolean currentUser;

    public static Dialog ActivityDialogInternetConnection;

    @Override
    public void onStart() {
        Log.d("looog", "onStart: ");
        try {
            if (ActivityDialogInternetConnection.isShowing())
                ActivityDialogInternetConnection.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onStart();
        isExit=false;
        Connection();
    }

    @Override
    protected void onResume() {
        isExit=false;
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setContentView(R.layout.activity_main);

        context=getApplicationContext();

        font_Medium=Typeface.createFromAsset(MainActivity.context.getAssets(),"fonts/IRANSans_Medium.ttf");
        font_Bold=Typeface.createFromAsset(MainActivity.context.getAssets(),"fonts/IRANSans_Bold.ttf");

    }

    public void Connection()
    {
        Log.d("loog", "Connection: ");

        OkHttpClient client = new OkHttpClient();

        String url = "http://217.218.215.67:6608/api/api-status";

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(url)
                .build();


        client=builder.build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            if (ActivityDialogInternetConnection.isShowing())
                                ActivityDialogInternetConnection.dismiss();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        if(!isFinishing())
                        {
                            ActivityDialogShowInternetConnection();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    final String myResponce=response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jObject = null;
                            try {
                                jObject = new JSONObject(myResponce);

                                String Status=jObject.getString("status");

                                if (Status.equals("ok"))
                                {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void run() {
                                            try {
                                                if (ActivityDialogInternetConnection.isShowing())
                                                    ActivityDialogInternetConnection.dismiss();
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                            if(!isFinishing() && !isExit)
                                            {
                                                currentUser = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("currentUser",false);
                                                if (currentUser) {
                                                    startActivity(new Intent(MainActivity.this, SearchPerson.class));
                                                    finish();
                                                }
                                                else {
                                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
                                                    {
                                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                                                        Intent intent = new Intent(MainActivity.this, Authentication.class);
                                                        startActivity(intent, options.toBundle());
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        startActivity(new Intent(MainActivity.this, Authentication.class));
                                                        finish();
                                                    }
                                                }
                                            }
                                        }
                                    }, 3000);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
    public void ActivityDialogShowInternetConnection()
    {
        ActivityDialogInternetConnection=new Dialog(MainActivity.this);
        ActivityDialogInternetConnection.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogInternetConnection.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogInternetConnection.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
        ActivityDialogInternetConnection.setContentView(R.layout.error_connection_dialog);

        TextView TitleErrorConnectionDialog=(TextView)ActivityDialogInternetConnection.findViewById(R.id.TitleErrorConnectionDialog);
        TextView TextErrorConnectionDialog=(TextView)ActivityDialogInternetConnection.findViewById(R.id.TextErrorConnectionDialog);

        TitleErrorConnectionDialog.setTypeface(font_Bold);
        TextErrorConnectionDialog.setTypeface(font_Medium);

        ActivityDialogInternetConnection.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                }
                Connection();
            }
        });

        ActivityDialogInternetConnection.show();
    }
    @Override
    protected void onStop() {
        isExit=true;
        try {
            if (ActivityDialogInternetConnection.isShowing())
                ActivityDialogInternetConnection.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onStop();
    }
}

