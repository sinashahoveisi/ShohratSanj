package com.example.sina.specificcontact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuhart.stepview.StepView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchPerson extends AppCompatActivity {

    TextView PhoneTitleUser;
    TextView PhoneUser;
    TextView RankTitleUser;
    TextView RankUser;
    TextView TitlePerson;
    TextView RankBetweenContact;
    TextView RankContactTitleUser;

    public static ProgressBar RankProgressBar;

    CountDownTimer ResultTimer;
    long  timeLeftInMiliseconds;
    public boolean TimerIsON ;

    public static boolean FromContactActivity=false;

    public static Dialog ActivityDialogSuccess;
    public static Dialog ActivityDialogError;
    public static Dialog ActivityDialogInformation;
    public static Dialog ActivityDialogInternetConnectionSearchPerson;
    public static Dialog ActivityDialogExitSearch;

    Typeface font_Medium;
    Typeface font_Bold;
    Typeface rank_font;

    RelativeLayout RelativeProgress;
    LinearLayout RelativeImage;


    static Context context;

    Button Scan;
    public static JSONArray Contacts;
    String PhoneNumber;

    public Dialog loading;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    StepView stepView;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onStart() {
        super.onStart();
        FromContactActivity=false;
        try {
            if (ActivityDialogSuccess.isShowing())
                ActivityDialogSuccess.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            if (ActivityDialogError.isShowing())
                ActivityDialogError.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            if (ActivityDialogInformation.isShowing())
                ActivityDialogInformation.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            if (ActivityDialogInternetConnectionSearchPerson.isShowing())
                ActivityDialogInternetConnectionSearchPerson.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            if (loading.isShowing())
                loading.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            OnStartSearchPerson();
        }catch (Exception e)
        {
            try {
                if (loading.isShowing())
                    loading.dismiss();
            }catch (Exception el){
                el.printStackTrace();
            }
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_person);

        context=getApplicationContext();

        Scan = (Button) findViewById(R.id.Scan);
        PhoneTitleUser=(TextView)findViewById(R.id.PhoneTitleUser);
        PhoneUser=(TextView)findViewById(R.id.PhoneUser);
        RankTitleUser=(TextView)findViewById(R.id.RankTitleUser);
        RankUser=(TextView)findViewById(R.id.RankUser);
        TitlePerson=(TextView)findViewById(R.id.titlePerson);
        RankBetweenContact=(TextView)findViewById(R.id.RankBetweenContact);
        RankContactTitleUser=(TextView)findViewById(R.id.RankContactTitleUser);
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        RankProgressBar=(ProgressBar)findViewById(R.id.RankProgressBar);
        RelativeProgress=(RelativeLayout)findViewById(R.id.RelativeProgress);
        RelativeImage=(LinearLayout) findViewById(R.id.RelativeImage);

        font_Medium= Typeface.createFromAsset(SearchPerson.context.getAssets(),"fonts/IRANSans_Medium.ttf");
        font_Bold=Typeface.createFromAsset(SearchPerson.context.getAssets(),"fonts/IRANSans_Bold.ttf");
        rank_font=Typeface.createFromAsset(SearchPerson.context.getAssets(),"fonts/SITKAB.TTC");

        Scan.setTypeface(font_Bold);
        PhoneTitleUser.setTypeface(font_Bold);
        RankTitleUser.setTypeface(font_Bold);
        TitlePerson.setTypeface(font_Bold);
        RankContactTitleUser.setTypeface(font_Bold);

        PhoneUser.setTypeface(font_Medium);
        RankUser.setTypeface(font_Medium);
        RankBetweenContact.setTypeface(rank_font);


        stepView = findViewById(R.id.step_view);
        stepView.setStepsNumber(3);
        stepView.go(2, true);

        bottomNavigationView.setSelectedItemId(R.id.navigation_person);
        try {
            if (!FromContactActivity)
                bottomNavigationView.setVisibility(View.GONE);
        }
        catch (Exception e)
        {
            bottomNavigationView.setVisibility(View.GONE);
        }

        try {
            showContacts();
        } catch (JSONException e) {
            try {
                if (loading.isShowing())
                    loading.dismiss();
            }catch (Exception el){
                el.printStackTrace();
            }
            e.printStackTrace();
        }

        PhoneNumber=getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("PhoneNumber","0");
        PhoneUser.setText(PhoneNumber);

        getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("currentUser",true).apply();

        TimerIsON = false;

        Log.d("LOOOOg", PhoneNumber);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.navigation_contacts)
                {
                    startActivity(new Intent(getBaseContext(), SearchContact.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
                return false;
            }
        });

        RelativeProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number= (String) RankUser.getText();
                number=number.replace(" %","");
                if (number.equals("0"))
                    ActivityDialogShowInformation("مــیـزان مـعروفـیـت","میزان معروفیت شما نا معلوم میباشد. برای محاسبه لطفا روی اسکن مخاطبین کلیک کنید.");
                else
                    ActivityDialogShowInformation("مــیـزان مـعروفـیـت","میزان معروفیت شما "+number+" درصد میباشد.");
            }
        });

        RelativeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number= (String) RankBetweenContact.getText();
                if (number.equals("0"))
                    ActivityDialogShowInformation("رتــبـه بـیـن مخــاطبین","رتبه شما بین مخاطبین خود نا معلوم میباشد. برای محاسبه لطفا روی اسکن مخاطبین کلیک کنید.");
                else
                    ActivityDialogShowInformation("رتــبـه بـیـن مخــاطبین","شما رتبه "+number+" را در بین مخاطبین خود دارید.");
            }
        });


        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if (loading.isShowing())
                        loading.dismiss();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                loading= new Dialog(SearchPerson.this);
                loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                loading.setContentView(R.layout.loading_dialog);
                Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        loading.show();
                        ActivityDialogShowInformation("لطفا صبــــر کنید","درخواست شما در حال بررسی است.");
                    }
                });
                loading.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            showContacts();
                        } catch (JSONException e) {
                            try {
                                if (loading.isShowing())
                                    loading.dismiss();
                            }catch (Exception el){
                                el.printStackTrace();
                            }
                            e.printStackTrace();
                        }

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("contacts", Contacts);
                        } catch (JSONException e) {
                            try {
                                if (loading.isShowing())
                                    loading.dismiss();
                            }catch (Exception el){
                                el.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                        try {
                            String jsonString = jsonObject.toString();
                            Log.d("looog", jsonString);

                            String Authorization = "Bearer " + getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("token", "0");

                            Log.d("looog", Authorization);
                            OkHttpClient client = new OkHttpClient();

                            OkHttpClient.Builder builder = new OkHttpClient.Builder();
                            builder.connectTimeout(15, TimeUnit.SECONDS);
                            builder.readTimeout(15, TimeUnit.SECONDS);
                            builder.writeTimeout(15, TimeUnit.SECONDS);

                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType, jsonString);
                            Request request = new Request.Builder()
                                    .url("http://217.218.215.67:6608/api/contacts/request")
                                    .post(body)
                                    .addHeader("Content-Type", "application/json")
                                    .addHeader("Authorization", Authorization)
                                    .build();

                            client = builder.build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("looooog", e.toString());
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            if (!isFinishing()) {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                    if (ActivityDialogInternetConnectionSearchPerson.isShowing())
                                                        ActivityDialogInternetConnectionSearchPerson.dismiss();
                                                } catch (Exception e) {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    }catch (Exception el){
                                                        el.printStackTrace();
                                                    }
                                                    e.printStackTrace();
                                                }
                                                ActivityDialogShowInternetConnectionSearchPerson();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String myResponce = response.body().string();

                                    if (response.isSuccessful()) {
                                        if (!isFinishing()) {
                                            try {
                                                Log.d("looog", myResponce);

                                                JSONObject jObject = new JSONObject(myResponce);

                                                int ReqStatus = jObject.getInt("reqStatus");
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (ReqStatus == 2) {
                                                            ActivityDialogShowSuccess("درخواست شما با موفقیت ارسال شد. برای مشاهده نتیجه لطفا صبور باشید. ");
                                                            StartTimer();
                                                        } else if (ReqStatus == 1) {
                                                            try {

                                                                OkHttpClient client = new OkHttpClient();

                                                                String url = "http://217.218.215.67:6608/api/contacts/request";

                                                                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                                                builder.connectTimeout(15, TimeUnit.SECONDS);
                                                                builder.readTimeout(15, TimeUnit.SECONDS);
                                                                builder.writeTimeout(15, TimeUnit.SECONDS);

                                                                Request request = new Request.Builder()
                                                                        .url(url)
                                                                        .addHeader("Authorization", Authorization)
                                                                        .addHeader("Content-Type", "application/json")
                                                                        .build();

                                                                client = builder.build();
                                                                client.newCall(request).enqueue(new Callback() {

                                                                    @Override
                                                                    public void onFailure(Call call, IOException e) {
                                                                        runOnUiThread(new Runnable() {
                                                                            public void run() {
                                                                                if (!isFinishing()) {
                                                                                    try {
                                                                                        if (loading.isShowing())
                                                                                            loading.dismiss();
                                                                                        if (ActivityDialogInternetConnectionSearchPerson.isShowing())
                                                                                            ActivityDialogInternetConnectionSearchPerson.dismiss();
                                                                                    } catch (Exception e) {
                                                                                        try {
                                                                                            if (loading.isShowing())
                                                                                                loading.dismiss();
                                                                                        }catch (Exception el){
                                                                                            el.printStackTrace();
                                                                                        }
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    ActivityDialogShowInternetConnectionSearchPerson();
                                                                                }
                                                                            }
                                                                        });
                                                                    }

                                                                    @Override
                                                                    public void onResponse(Call call, Response response) throws IOException {
                                                                        if (response.isSuccessful()) {
                                                                            final String myResponce = response.body().string();
                                                                            runOnUiThread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    if (!isFinishing()) {
                                                                                        try {
                                                                                            try {
                                                                                                if (loading.isShowing())
                                                                                                    loading.dismiss();
                                                                                            }catch (Exception e){
                                                                                                try {
                                                                                                    if (loading.isShowing())
                                                                                                        loading.dismiss();
                                                                                                }catch (Exception el){
                                                                                                    el.printStackTrace();
                                                                                                }
                                                                                                e.printStackTrace();
                                                                                            }
                                                                                            try {
                                                                                                Log.d("looog", myResponce);
                                                                                                JSONObject jObject = new JSONObject(myResponce);
                                                                                                if (TimerIsON)
                                                                                                    ResultTimer.cancel();

                                                                                                String ranking = String.valueOf(jObject.getDouble("rank"));
                                                                                                if (ranking.length() > 6)
                                                                                                    ranking = ranking.substring(0, 6) + " %";
                                                                                                else
                                                                                                    ranking = ranking + " %";

                                                                                                RankUser.setText(ranking);

                                                                                                int rankingInt = (int) jObject.getDouble("rank");
                                                                                                RankProgressBar.setProgress(rankingInt);

                                                                                                String rankingContact = String.valueOf(jObject.getInt("rankBetweenContact"));
                                                                                                RankBetweenContact.setText(rankingContact);

                                                                                                ActivityDialogShowSuccess("درخواست شما با موفقیت ثبت شد. شما می توانید نتیجه را مشاهده کنید. ");
                                                                                                stepView.done(true);

                                                                                                Scan.setText("اسکن مجدد مخاطبین");

                                                                                                bottomNavigationView.setVisibility(View.VISIBLE);
                                                                                            }catch (Exception e){
                                                                                                try {
                                                                                                if (loading.isShowing())
                                                                                                    loading.dismiss();
                                                                                            }catch (Exception el){
                                                                                                el.printStackTrace();
                                                                                            }
                                                                                            e.printStackTrace();
                                                                                            }
                                                                                        } catch (Exception e) {
                                                                                            try {
                                                                                                if (loading.isShowing())
                                                                                                    loading.dismiss();
                                                                                            }catch (Exception el){
                                                                                                el.printStackTrace();
                                                                                            }
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                        } else {
                                                                            if (!isFinishing()) {
                                                                                try {

                                                                                    Log.d("looog", myResponce);

                                                                                    JSONObject jObject = new JSONObject(myResponce);

                                                                                    int ErrorCode = jObject.getInt("code");
                                                                                    runOnUiThread(new Runnable() {
                                                                                        @Override
                                                                                        public void run() {

                                                                                            if (loading.isShowing())
                                                                                                loading.dismiss();

                                                                                            if (ErrorCode == 4030) {
                                                                                                ActivityDialogShowError("درخواست شما ارسال نشد. برای مشاهده مهمترین مخاطب لطفا تعداد مخاطبین خود را افزایش داده و سپس روی گزینه اسکن مخاطبین کلیک کنید. ", false);
                                                                                            } else if (ErrorCode == 4001) {
                                                                                                ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در ثبت شماره شما در سرور به وجود آمده است و برای رفع آن باید شماره ی خود را دوباره تأیید کنید. ", true);
                                                                                            }else if (ErrorCode == 4002) {
                                                                                                ActivityDialogShowError("درخواست شما ارسال نشد. مدت زمان زیادی درخواستی از جانب شما ارسال نشده است و برای رفع آن باید شماره ی خود را دوباره تأیید کنید. ", true);
                                                                                            }else if (ErrorCode == 4008) {
                                                                                                ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در شماره برخی از مخاطبین شما وجود دارد که خواندن آن با مشکل مواجه شده است. ", false);
                                                                                            } else if (ErrorCode == 4041) {
                                                                                                ActivityDialogShowInformation("درخـــواستــی ارســـال نــشده", "درخواستی از جانب شما ارسال نشده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ");
                                                                                            } else if (ErrorCode == 4040) {
                                                                                                ActivityDialogShowInformation("درخـواست قـبـلا ارسـال شـده", "درخواست از جانب شما قبلا ارسال شده است. برای ارسال درخواست جدید باید تغییری در مخاطبین شما به وجود آید. ");
                                                                                            } else {
                                                                                                ActivityDialogShowError("درخواست شما ارسال نشد. علت این ناموفقیت نامشخص است. ", false);
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                } catch (Exception e) {
                                                                                    try {
                                                                                        if (loading.isShowing())
                                                                                            loading.dismiss();
                                                                                    }catch (Exception el){
                                                                                        el.printStackTrace();
                                                                                    }
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                            }catch (Exception e)
                                                            {
                                                                try {
                                                                    if (loading.isShowing())
                                                                        loading.dismiss();
                                                                }catch (Exception el){
                                                                    el.printStackTrace();
                                                                }
                                                                e.printStackTrace();
                                                            }
                                                        } else if (ReqStatus == 0) {
                                                            if (loading.isShowing())
                                                                loading.dismiss();

                                                            ActivityDialogShowError("درخواست شما ثبت نشده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ", false);
                                                        } else if (ReqStatus == -1) {
                                                            if (loading.isShowing())
                                                                loading.dismiss();

                                                            ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در ثبت درخواست شما در سرور به وجود آمده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ", false);
                                                        }
                                                    }
                                                });
                                            } catch (Exception e) {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception el){
                                                    el.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        if (!isFinishing()) {
                                            try {

                                                Log.d("looog", myResponce);

                                                JSONObject jObject = new JSONObject(myResponce);

                                                int ErrorCode = jObject.getInt("code");
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        if (loading.isShowing())
                                                            loading.dismiss();

                                                        boolean IsPermission = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("ispermission", false);
                                                        if (IsPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                                            if (ErrorCode == 4030) {
                                                                ActivityDialogShowError("درخواست شما ارسال نشد. برای مشاهده مهمترین مخاطب لطفا تعداد مخاطبین خود را افزایش داده و سپس روی گزینه اسکن مخاطبین کلیک کنید. ", false);
                                                            } else if (ErrorCode == 4001) {
                                                                ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در ثبت شماره شما در سرور به وجود آمده است و برای رفع آن باید شماره ی خود را دوباره تأیید کنید. ", true);
                                                            } else if (ErrorCode == 4002) {
                                                                ActivityDialogShowError("درخواست شما ارسال نشد. مدت زمان زیادی درخواستی از جانب شما ارسال نشده است و برای رفع آن باید شماره ی خود را دوباره تأیید کنید. ", true);
                                                            } else if (ErrorCode == 4008) {
                                                                ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در شماره برخی از مخاطبین شما وجود دارد که خواندن آن با مشکل مواجه شده است. ", false);
                                                            } else if (ErrorCode == 4041) {
                                                                ActivityDialogShowInformation("درخـــواستــی ارســـال نــشده", "درخواستی از جانب شما ارسال نشده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ");
                                                            } else if (ErrorCode == 4040) {
                                                                ActivityDialogShowInformation("درخـواست قـبـلا ارسـال شـده", "درخواست از جانب شما قبلا ارسال شده است. برای ارسال درخواست جدید باید تغییری در مخاطبین شما به وجود آید. ");
                                                            } else {
                                                                ActivityDialogShowError("درخواست شما ارسال نشد. علت این ناموفقیت نامشخص است. ", false);
                                                            }
                                                        } else {
                                                            ActivityDialogShowError("درخواست شما ارسال نشد. برای مشاهده مهمترین مخاطب باید دسترسی مخاطبین داده شود. ", false);
                                                        }

                                                    }
                                                });
                                            } catch (Exception e) {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception el){
                                                    el.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            });
                        }catch (Exception e)
                        {
                            try {
                if (loading.isShowing())
                    loading.dismiss();
            }catch (Exception el){
                el.printStackTrace();
            }
            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });


    }

    private void showContacts() throws JSONException {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        else {
            getContactList();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Toast.makeText(this, "برای نمایش مهم ترین مخاطب لطفا روی اسکن مخاطبین کلیک کنید", Toast.LENGTH_LONG).show();
                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("ispermission",true).apply();

            } else {
                Toast.makeText(this, "برای نمایش مهم ترین مخاطب لازم است دسترسی به مخاطبین داده شود", Toast.LENGTH_SHORT).show();
                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("ispermission",false).apply();
            }
        }
    }

    private void getContactList() throws JSONException {
        try {
            Contacts = new JSONArray();

            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {

                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                        JSONArray jsonArrayPhone = new JSONArray();

                        assert pCur != null;
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            try {
                                for (int i = 0; i < phoneNo.length(); i++) {
                                    try {
                                        char Char = phoneNo.charAt(i);
                                        if (Char != '0' && Char != '1' && Char != '2' && Char != '3' && Char != '4' && Char != '5' && Char != '6' && Char != '7' &&
                                                Char != '8' && Char != '9' && Char != '+') {
                                            phoneNo = charRemoveAt(phoneNo, i);
                                        }
                                        if (Char == '+' && i != 0) {
                                            phoneNo = charRemoveAt(phoneNo, i);
                                        }
                                    } catch (Exception e) {
                                        try {
                                            if (loading.isShowing())
                                                loading.dismiss();
                                        }catch (Exception el){
                                            el.printStackTrace();
                                        }
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                try {
                                    if (loading.isShowing())
                                        loading.dismiss();
                                }catch (Exception el){
                                    el.printStackTrace();
                                }
                                e.printStackTrace();
                            }

                            try {
                                if (phoneNo.startsWith("+") && phoneNo.length() > 13) {
                                    phoneNo = phoneNo.substring(0, 13);
                                } else if (phoneNo.startsWith("0") && phoneNo.length() > 11) {
                                    phoneNo = phoneNo.substring(0, 11);
                                }
                            } catch (Exception e) {
                                try {
                                    if (loading.isShowing())
                                        loading.dismiss();
                                }catch (Exception el){
                                    el.printStackTrace();
                                }
                                e.printStackTrace();
                            }
                            try {
                                if ((phoneNo.startsWith("+") && phoneNo.length() == 13) || (phoneNo.startsWith("0") && phoneNo.length() == 11))
                                    jsonArrayPhone.put(phoneNo);
                            } catch (Exception e) {
                                try {
                                    if (loading.isShowing())
                                        loading.dismiss();
                                }catch (Exception el){
                                    el.printStackTrace();
                                }
                                e.printStackTrace();
                            }
                        }

                        try {
                            JSONObject jsonObject = new JSONObject();
                            if (name.equals(""))
                                name="un known";
                            jsonObject.put("fullname", name);
                            jsonObject.put("phone", jsonArrayPhone);
                            Contacts.put(jsonObject);
                        }catch (Exception e)
                        {
                            try {
                                if (loading.isShowing())
                                    loading.dismiss();
                            }catch (Exception el){
                                el.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                        pCur.close();
                    }
                }
            }
            if (cur != null) {
                cur.close();
            }
        }
        catch (Exception e)
        {
            try {
                if (loading.isShowing())
                    loading.dismiss();
            }catch (Exception el){
                el.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    public static String charRemoveAt(String str, int p) {
        return str.substring(0, p) + str.substring(p + 1);
    }
    @Override
    public void onBackPressed() {
        ActivityDialogShowExitSearch();
    }
    public void ActivityDialogShowSuccess(String Text)
    {
        ActivityDialogSuccess=new Dialog(SearchPerson.this);
        ActivityDialogSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogSuccess.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogSuccess.getWindow().getAttributes().windowAnimations=R.style.DialogSlideUpDown;
        ActivityDialogSuccess.setContentView(R.layout.success_dialog);

        TextView TextSuccessDialog=(TextView)ActivityDialogSuccess.findViewById(R.id.TextSuccessDialog);
        TextView TitleSuccessDialog=(TextView)ActivityDialogSuccess.findViewById(R.id.TitleSuccessDialog);

        TextSuccessDialog.setText(Text);

        TitleSuccessDialog.setTypeface(font_Bold);
        TextSuccessDialog.setTypeface(font_Medium);

        ActivityDialogSuccess.show();
    }
    public void ActivityDialogShowInformation(String Title, String Text)
    {
        ActivityDialogInformation=new Dialog(SearchPerson.this);
        ActivityDialogInformation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogInformation.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogInformation.getWindow().getAttributes().windowAnimations=R.style.DialogSlideLeftRight;
        ActivityDialogInformation.setContentView(R.layout.information_dialog);

        TextView TitleInfoDialog=(TextView)ActivityDialogInformation.findViewById(R.id.TitleInfoDialog);
        TextView TextInfoDialog=(TextView)ActivityDialogInformation.findViewById(R.id.TextInfoDialog);

        TitleInfoDialog.setText(Title);
        TextInfoDialog.setText(Text);

        TitleInfoDialog.setTypeface(font_Bold);
        TextInfoDialog.setTypeface(font_Medium);


        ActivityDialogInformation.show();
    }
    public void ActivityDialogShowError(String TextError,boolean GoToFirst)
    {
        ActivityDialogError=new Dialog(SearchPerson.this);
        ActivityDialogError.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogError.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogError.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
        ActivityDialogError.setContentView(R.layout.error_dialog);

        TextView TextErrorDialog = (TextView)ActivityDialogError.findViewById(R.id.TextErrorDialog);
        TextView TitleErrorDialog = (TextView)ActivityDialogError.findViewById(R.id.TitleErrorDialog);

        TextErrorDialog.setText(TextError);

        TitleErrorDialog.setTypeface(font_Bold);
        TextErrorDialog.setTypeface(font_Medium);

        ActivityDialogError.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCancel(DialogInterface dialog) {
                if (GoToFirst)
                {
                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("currentUser",false).apply();
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchPerson.this);
                    Intent intent = new Intent(SearchPerson.this, Authentication.class);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            }
        });

        ActivityDialogError.show();
    }
    public void ActivityDialogShowInternetConnectionSearchPerson()
    {
        ActivityDialogInternetConnectionSearchPerson=new Dialog(SearchPerson.this);
        ActivityDialogInternetConnectionSearchPerson.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogInternetConnectionSearchPerson.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogInternetConnectionSearchPerson.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
        ActivityDialogInternetConnectionSearchPerson.setContentView(R.layout.error_connection_dialog);

        TextView TitleErrorConnectionDialog=(TextView)ActivityDialogInternetConnectionSearchPerson.findViewById(R.id.TitleErrorConnectionDialog);
        TextView TextErrorConnectionDialog=(TextView)ActivityDialogInternetConnectionSearchPerson.findViewById(R.id.TextErrorConnectionDialog);

        TitleErrorConnectionDialog.setTypeface(font_Bold);
        TextErrorConnectionDialog.setTypeface(font_Medium);

        ActivityDialogInternetConnectionSearchPerson.show();
    }
    public void ActivityDialogShowExitSearch()
    {
        ActivityDialogExitSearch=new Dialog(SearchPerson.this);
        ActivityDialogExitSearch.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogExitSearch.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogExitSearch.getWindow().getAttributes().windowAnimations=R.style.DialogSlideLeftRight;
        ActivityDialogExitSearch.setContentView(R.layout.exit_dialog);

        ImageView ExitYes = (ImageView)ActivityDialogExitSearch.findViewById(R.id.ExitYes);
        ImageView ExitNo = (ImageView)ActivityDialogExitSearch.findViewById(R.id.ExitNo);
        TextView TitleExit=(TextView)ActivityDialogExitSearch.findViewById(R.id.TitleExit);
        TextView TextExit=(TextView)ActivityDialogExitSearch.findViewById(R.id.TextExit);

        ActivityDialogExitSearch.setCancelable(false);

        TitleExit.setTypeface(font_Bold);
        TextExit.setTypeface(font_Medium);

        ExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchPerson.this, "خروج", Toast.LENGTH_SHORT).show();
                ActivityDialogExitSearch.dismiss();
                finishAffinity();
            }
        });
        ExitNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDialogExitSearch.dismiss();
            }
        });

        ActivityDialogExitSearch.show();
    }
    public void StartTimer ()
    {
        timeLeftInMiliseconds=10000;
        TimerIsON=true;
        ResultTimer = new CountDownTimer(timeLeftInMiliseconds,1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                TimerIsON=false;
                String Authorization="Bearer "+getSharedPreferences("PREFERENCE",MODE_PRIVATE).getString("token","0");

                OkHttpClient client = new OkHttpClient();

                String url = "http://217.218.215.67:6608/api/contacts/request";

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(10, TimeUnit.SECONDS);
                builder.readTimeout(10, TimeUnit.SECONDS);
                builder.writeTimeout(10, TimeUnit.SECONDS);

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization",Authorization)
                        .addHeader("Content-Type", "application/json")
                        .build();

                client=builder.build();
                client.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!isFinishing())
                                {
                                    try {
                                        if (ActivityDialogInternetConnectionSearchPerson.isShowing())
                                            ActivityDialogInternetConnectionSearchPerson.dismiss();
                                    }catch (Exception e)
                                    {
                                        try {
                                            if (loading.isShowing())
                                                loading.dismiss();
                                        }catch (Exception el){
                                            el.printStackTrace();
                                        }
                                        e.printStackTrace();
                                    }
                                    StartTimer();
                                    ActivityDialogShowInternetConnectionSearchPerson();
                                }
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String myResponce=response.body().string();

                        if (response.isSuccessful()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (!isFinishing())
                                    {
                                        try {
                                            Log.d("looog", myResponce);

                                            JSONObject jObject = new JSONObject(myResponce);

                                            int ReqStatus = jObject.getInt("reqStatus");

                                            if (ReqStatus==1)
                                            {
                                                try {
                                                    if (TimerIsON)
                                                        ResultTimer.cancel();

                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    String ranking = String.valueOf(jObject.getDouble("rank"));
                                                    if (ranking.length() > 6)
                                                        ranking = ranking.substring(0, 6) + " %";
                                                    else
                                                        ranking = ranking + " %";

                                                    RankUser.setText(ranking);

                                                    int rankingInt = (int) jObject.getDouble("rank");
                                                    RankProgressBar.setProgress(rankingInt);

                                                    String rankingContact = String.valueOf(jObject.getInt("rankBetweenContact"));
                                                    RankBetweenContact.setText(rankingContact);

                                                    ActivityDialogShowSuccess("درخواست شما با موفقیت ثبت شد. شما می توانید نتیجه را مشاهده کنید. ");
                                                    stepView.done(true);

                                                    Scan.setText("اسکن مجدد مخاطبین");

                                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                            else if (ReqStatus == 2)
                                            {
                                                StartTimer();
                                            }
                                            else if (ReqStatus == 0)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                                ActivityDialogShowError("درخواست شما ثبت نشده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ",false);
                                            }
                                            else if (ReqStatus == -1)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                                ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در ثبت درخواست شما در سرور به وجود آمده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ",false);
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            try {
                                                if (loading.isShowing())
                                                    loading.dismiss();
                                            }catch (Exception el){
                                                el.printStackTrace();
                                            }
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                        else
                        {
                            if (!isFinishing())
                            {
                                try {

                                    Log.d("looog", myResponce);

                                    JSONObject jObject = new JSONObject(myResponce);

                                    int ErrorCode = jObject.getInt("code");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (loading.isShowing())
                                                loading.dismiss();

                                            if (ErrorCode==4030)
                                            {
                                                ActivityDialogShowError("درخواست شما ارسال نشد. برای مشاهده مهمترین مخاطب لطفا تعداد مخاطبین خود را افزایش داده و سپس روی گزینه اسکن مخاطبین کلیک کنید. ",false);
                                            }
                                            else if (ErrorCode==4001)
                                            {
                                                ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در ثبت شماره شما در سرور به وجود آمده است و برای رفع آن باید شماره ی خود را دوباره تأیید کنید. ",true);
                                            }
                                            else if (ErrorCode == 4002)
                                            {
                                                ActivityDialogShowError("درخواست شما ارسال نشد. مدت زمان زیادی درخواستی از جانب شما ارسال نشده است و برای رفع آن باید شماره ی خود را دوباره تأیید کنید. ", true);
                                            }
                                            else if (ErrorCode==4008)
                                            {
                                                ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در شماره برخی از مخاطبین شما وجود دارد که خواندن آن با مشکل مواجه شده است. ",false);
                                            }
                                            else if (ErrorCode == 4041)
                                            {
                                                ActivityDialogShowInformation("درخـــواستــی ارســـال نــشده", "درخواستی از جانب شما ارسال نشده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ");
                                            }
                                            else if (ErrorCode == 4040)
                                            {
                                                ActivityDialogShowInformation("درخـواست قـبـلا ارسـال شـده", "درخواست از جانب شما قبلا ارسال شده است. برای ارسال درخواست جدید باید تغییری در مخاطبین شما به وجود آید. ");
                                            }
                                            else
                                            {
                                                ActivityDialogShowError("درخواست شما ارسال نشد. علت این ناموفقیت نامشخص است. ",false);
                                            }

                                        }
                                    });
                                }
                                catch (Exception e)
                                {
                                    try {
                                        if (loading.isShowing())
                                            loading.dismiss();
                                    }catch (Exception el){
                                        el.printStackTrace();
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        }.start();
    }
    void OnStartSearchPerson() {
        try {

            loading = new Dialog(SearchPerson.this);
            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loading.setContentView(R.layout.loading_dialog);
            Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            String Authorization = "Bearer " + getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("token", "0");

            OkHttpClient client = new OkHttpClient();

            String url = "http://217.218.215.67:6608/api/contacts/request";

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(10, TimeUnit.SECONDS);
            builder.writeTimeout(10, TimeUnit.SECONDS);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", Authorization)
                    .addHeader("Content-Type", "application/json")
                    .build();

            client = builder.build();
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!isFinishing()) {
                                try {
                                    if (loading.isShowing())
                                        loading.dismiss();
                                } catch (Exception e) {
                                    try {
                                        if (loading.isShowing())
                                            loading.dismiss();
                                    }catch (Exception el){
                                        el.printStackTrace();
                                    }
                                    e.printStackTrace();
                                }
                                try {
                                    if (ActivityDialogInternetConnectionSearchPerson.isShowing())
                                        ActivityDialogInternetConnectionSearchPerson.dismiss();
                                } catch (Exception e) {
                                    try {
                                        if (loading.isShowing())
                                            loading.dismiss();
                                    }catch (Exception el){
                                        el.printStackTrace();
                                    }
                                    e.printStackTrace();
                                }
                                OnStartSearchPerson();
                                ActivityDialogShowInternetConnectionSearchPerson();
                            }
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String myResponce = response.body().string();

                    if (response.isSuccessful()) {

                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                if (!isFinishing()) {
                                    try {

                                        Log.d("looog", myResponce);

                                        JSONObject jObject = new JSONObject(myResponce);

                                        int ReqStatus = jObject.getInt("reqStatus");

                                        if (ReqStatus == 1) {
                                            try
                                            {
                                                if (loading.isShowing())
                                                    loading.dismiss();
                                                if (TimerIsON)
                                                    ResultTimer.cancel();
                                            }catch (Exception e)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception el){
                                                    el.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }
                                            try
                                            {
                                                String ranking = String.valueOf(jObject.getDouble("rank"));
                                                if (ranking.length() > 6)
                                                    ranking = ranking.substring(0, 6) + " %";
                                                else
                                                    ranking = ranking + " %";

                                                RankUser.setText(ranking);

                                                int rankingInt = (int) jObject.getDouble("rank");
                                                RankProgressBar.setProgress(rankingInt);

                                                String rankingContact = String.valueOf(jObject.getInt("rankBetweenContact"));
                                                RankBetweenContact.setText(rankingContact);

                                                stepView.done(true);

                                                Scan.setText("اسکن مجدد مخاطبین");

                                                bottomNavigationView.setVisibility(View.VISIBLE);
                                            }
                                            catch (Exception e)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception el){
                                                    el.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }
                                        } else if (ReqStatus == 2) {
                                            try {
                                                ActivityDialogShowInformation("قــبلا ارســـال شـــده", "درخواست شما با موفقیت قبلا ارسال شده است. برای مشاهده نتیجه لطفا صبور باشید. ");
                                                StartTimer();
                                            }catch (Exception e)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception el){
                                                    el.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }
                                        } else if (ReqStatus == 0) {
                                            try
                                            {
                                                if (loading.isShowing())
                                                    loading.dismiss();
                                            }catch (Exception e)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception el){
                                                    el.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }
                                            ActivityDialogShowError("درخواست شما ثبت نشده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ", false);
                                        } else if (ReqStatus == -1) {
                                            try
                                            {
                                                if (loading.isShowing())
                                                    loading.dismiss();
                                            }catch (Exception e)
                                            {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception el){
                                                    el.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }
                                            ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در ثبت درخواست شما در سرور به وجود آمده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ", false);
                                        }
                                    } catch (Exception e) {
                                        try {
                                            if (loading.isShowing())
                                                loading.dismiss();
                                        }catch (Exception el){
                                            el.printStackTrace();
                                        }
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else {
                        if (!isFinishing()) {
                            try {

                                Log.d("looog", myResponce);

                                JSONObject jObject = new JSONObject(myResponce);

                                int ErrorCode = jObject.getInt("code");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                       try {
                                           if (loading.isShowing())
                                               loading.dismiss();
                                       }catch (Exception e){
                                           try {
                                                if (loading.isShowing())
                                                    loading.dismiss();
                                            }catch (Exception el){
                                                el.printStackTrace();
                                            }
                                            e.printStackTrace();
                                       }
                                        if (ErrorCode == 4030) {
                                            ActivityDialogShowError("درخواست شما ارسال نشد. برای مشاهده مهمترین مخاطب لطفا تعداد مخاطبین خود را افزایش داده و سپس روی گزینه اسکن مخاطبین کلیک کنید. ", false);
                                        } else if (ErrorCode == 4001) {
                                            ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در ثبت شماره شما در سرور به وجود آمده است و برای رفع آن باید شماره ی خود را دوباره تأیید کنید. ", true);
                                        } else if (ErrorCode == 4002) {
                                            ActivityDialogShowError("درخواست شما ارسال نشد. مدت زمان زیادی درخواستی از جانب شما ارسال نشده است و برای رفع آن باید شماره ی خود را دوباره تأیید کنید. ", true);
                                        } else if (ErrorCode == 4008) {
                                            ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در شماره برخی از مخاطبین شما وجود دارد که خواندن آن با مشکل مواجه شده است. ", false);
                                        } else if (ErrorCode == 4041) {
                                            ActivityDialogShowInformation("درخـــواستــی ارســـال نــشده", "درخواستی از جانب شما ارسال نشده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ");
                                        } else if (ErrorCode == 4040) {
                                            ActivityDialogShowInformation("درخـواست قـبـلا ارسـال شـده", "درخواست از جانب شما قبلا ارسال شده است. برای ارسال درخواست جدید باید تغییری در مخاطبین شما به وجود آید. ");
                                        } else {
                                            ActivityDialogShowError("درخواست شما ارسال نشد. علت این ناموفقیت نامشخص است. ", false);
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                try {
                                    if (loading.isShowing())
                                        loading.dismiss();
                                }catch (Exception el){
                                    el.printStackTrace();
                                }
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }catch (Exception e)
        {
            try {
                if (loading.isShowing())
                    loading.dismiss();
            }catch (Exception el){
                el.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}