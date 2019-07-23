package com.example.sina.specificcontact;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.BubbleToggleView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.shuhart.stepview.StepView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RankActivity extends AppCompatActivity {


    static Context context;

    TextView TitleRank;
    TextView RankBetweenContact;
    TextView RankContactTitleUser;

    public  Dialog ActivityDialogSuccess;
    public  Dialog ActivityDialogError;
    public  Dialog ActivityDialogInformation;
    public  Dialog ActivityDialogInternetConnectionSearchPerson;
    public Dialog loading;

    public static String ranking;
    public static String rankingContact;
    public static String phoneCommonContact;
    public static String numberCommonContact;
    public static int rankingInt;

    Typeface font_Medium;
    Typeface font_Bold;
    Typeface rank_font;

    StepView stepView;

    BubbleNavigationLinearView bottom_navigation_view_linear;

    public Dialog ActivityDialogExit;

    @Override
    protected void onStart() {
        super.onStart();
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
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        boolean GuideDone = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("guidedone",false);
                        if (!GuideDone)
                            Guide();
                        else
                            OnStartSearchPerson();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }, 500);
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
        setContentView(R.layout.activity_rank);

        context=getApplicationContext();

        TitleRank=(TextView)findViewById(R.id.titleRank);
        RankBetweenContact=(TextView)findViewById(R.id.RankBetweenContact);
        RankContactTitleUser=(TextView)findViewById(R.id.RankContactTitleUser);
        bottom_navigation_view_linear=(BubbleNavigationLinearView) findViewById(R.id.bottom_navigation_view_linear);

        font_Medium= Typeface.createFromAsset(RankActivity.context.getAssets(),"fonts/IRANSans_Medium.ttf");
        font_Bold=Typeface.createFromAsset(RankActivity.context.getAssets(),"fonts/IRANSans_Bold.ttf");
        rank_font=Typeface.createFromAsset(RankActivity.context.getAssets(),"fonts/SITKAB.TTC");

        TitleRank.setTypeface(font_Bold);
        RankContactTitleUser.setTypeface(font_Bold);
        RankBetweenContact.setTypeface(rank_font);
        bottom_navigation_view_linear.setTypeface(font_Medium);

        stepView = findViewById(R.id.step_view);
        stepView.setStepsNumber(3);
        stepView.go(2, true);
        stepView.done(true);

        bottom_navigation_view_linear.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                if (view.getId()==R.id.navigation_scan)
                {
                    startActivity(new Intent(getBaseContext(), ScanActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (view.getId()==R.id.navigation_fame)
                {
                    startActivity(new Intent(getBaseContext(), FameActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (view.getId()==R.id.navigation_likeness)
                {
                    startActivity(new Intent(getBaseContext(), LikenessActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (view.getId()==R.id.navigation_contacts)
                {
                    startActivity(new Intent(getBaseContext(), ContactActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        ActivityDialogShowExit();
    }
    public void ActivityDialogShowExit()
    {
        ActivityDialogExit=new Dialog(RankActivity.this);
        ActivityDialogExit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogExit.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogExit.getWindow().getAttributes().windowAnimations=R.style.DialogSlideLeftRight;
        ActivityDialogExit.setContentView(R.layout.exit_dialog);

        ImageView ExitYes = (ImageView)ActivityDialogExit.findViewById(R.id.ExitYes);
        ImageView ExitNo = (ImageView)ActivityDialogExit.findViewById(R.id.ExitNo);
        TextView TitleExit=(TextView)ActivityDialogExit.findViewById(R.id.TitleExit);
        TextView TextExit=(TextView)ActivityDialogExit.findViewById(R.id.TextExit);

        ActivityDialogExit.setCancelable(false);

        TitleExit.setTypeface(font_Bold);
        TextExit.setTypeface(font_Medium);

        ExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RankActivity.this, "خروج", Toast.LENGTH_SHORT).show();
                ActivityDialogExit.dismiss();
                finishAffinity();
            }
        });
        ExitNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDialogExit.dismiss();
            }
        });

        ActivityDialogExit.show();
    }
    public void Guide()
    {
        TapTargetView.showFor(RankActivity.this,
                TapTarget.forView(findViewById(R.id.navigation_rank), "رتبه بین مخاطبین", "تا به حال به فکر رتبه خود بین مخاطبین افتاده\u200Cاید؟؟؟"+"\n"+
                        "در این قسمت می\u200Cتوانید رتبه خود را مشاهده کنید")
                        .outerCircleColor(R.color.rankCircle)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.ranktargetCircle)
                        .titleTextSize(20)
                        .titleTextColor(R.color.cardViewBackground)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.cardViewBackground)
                        .textColor(R.color.cardViewBackground)
                        .textTypeface(font_Medium)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(false)
                        .targetRadius(60),
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        BubbleToggleView bubbleToggleView = (BubbleToggleView)findViewById(R.id.navigation_scan);
                        bottom_navigation_view_linear.onClick(bubbleToggleView);
                    }
                });
    }
    public void ActivityDialogShowInternetConnectionSearchPerson()
    {
        ActivityDialogInternetConnectionSearchPerson=new Dialog(RankActivity.this);
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
    public void ActivityDialogShowInformation(String Title,String Text)
    {
        ActivityDialogInformation=new Dialog(RankActivity.this);
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
    public void ActivityDialogShowError(String TextError)
    {
        ActivityDialogError=new Dialog(RankActivity.this);
        ActivityDialogError.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogError.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogError.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
        ActivityDialogError.setContentView(R.layout.error_dialog);

        TextView TextErrorDialog = (TextView)ActivityDialogError.findViewById(R.id.TextErrorDialog);
        TextView TitleErrorDialog = (TextView)ActivityDialogError.findViewById(R.id.TitleErrorDialog);

        TextErrorDialog.setText(TextError);

        TitleErrorDialog.setTypeface(font_Bold);
        TextErrorDialog.setTypeface(font_Medium);

        ActivityDialogError.show();
    }
    void OnStartSearchPerson() {
        try {

            loading = new Dialog(RankActivity.this);
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
                                                ranking = String.valueOf(jObject.getDouble("rank"));
                                                if (ranking.length() > 6)
                                                    ranking = ranking.substring(0, 6) + " %";
                                                else
                                                    ranking = ranking + " %";


                                                rankingInt = (int) jObject.getDouble("rank");

                                                rankingContact = String.valueOf(jObject.getInt("rankBetweenContact"));

                                                RankBetweenContact.setText(rankingContact);

                                                stepView.done(true);

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {

                                                            OkHttpClient client = new OkHttpClient();

                                                            String url = "http://217.218.215.67:6608/api/contacts/similar";

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
                                                                                        try {
                                                                                            Log.d("looog", myResponce);
                                                                                            JSONObject jObject = new JSONObject(myResponce);

                                                                                            phoneCommonContact = jObject.getString("phone");

                                                                                            numberCommonContact = String.valueOf(jObject.getDouble("numberCommonContact"));
                                                                                            if (numberCommonContact.length() > 5)
                                                                                                numberCommonContact = numberCommonContact.substring(0, 5) + " %";
                                                                                            else
                                                                                                numberCommonContact = numberCommonContact + " %";

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
                                        } else if (ReqStatus == 2) {
                                            try {
                                                try {
                                                    if (loading.isShowing())
                                                        loading.dismiss();
                                                }catch (Exception el){
                                                    el.printStackTrace();
                                                }
                                                ActivityDialogShowInformation("قــبلا ارســـال شـــده", "درخواست شما با موفقیت قبلا ارسال شده است. برای مشاهده نتیجه لطفا صبور باشید. ");
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
                                            ActivityDialogShowError("درخواست شما ثبت نشده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ");
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
                                            ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در ثبت درخواست شما در سرور به وجود آمده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ");
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
                                            ActivityDialogShowError("درخواست شما ارسال نشد. برای مشاهده مهمترین مخاطب لطفا تعداد مخاطبین خود را افزایش داده و سپس روی گزینه اسکن مخاطبین کلیک کنید. ");
                                        } else if (ErrorCode == 4001) {
                                            ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در ثبت شماره شما در سرور به وجود آمده است و برای رفع آن باید شماره ی خود را دوباره تأیید کنید. ");
                                        } else if (ErrorCode == 4002) {
                                            ActivityDialogShowError("درخواست شما ارسال نشد. مدت زمان زیادی درخواستی از جانب شما ارسال نشده است و برای رفع آن باید شماره ی خود را دوباره تأیید کنید. ");
                                        } else if (ErrorCode == 4008) {
                                            ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در شماره برخی از مخاطبین شما وجود دارد که خواندن آن با مشکل مواجه شده است. ");
                                        } else if (ErrorCode == 4041) {
                                            ActivityDialogShowInformation("درخـــواستــی ارســـال نــشده", "درخواستی از جانب شما ارسال نشده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ");
                                        } else if (ErrorCode == 4040) {
                                            ActivityDialogShowInformation("درخـواست قـبـلا ارسـال شـده", "درخواست از جانب شما قبلا ارسال شده است. برای ارسال درخواست جدید باید تغییری در مخاطبین شما به وجود آید. ");
                                        } else {
                                            ActivityDialogShowError("درخواست شما ارسال نشد. علت این ناموفقیت نامشخص است. ");
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
