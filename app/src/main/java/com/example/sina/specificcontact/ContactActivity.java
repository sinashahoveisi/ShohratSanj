package com.example.sina.specificcontact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.shuhart.stepview.StepView;

import org.json.JSONArray;
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

public class ContactActivity extends AppCompatActivity {

     TextView NameTitleOne;
     TextView NameOne;
     TextView PhoneTitleOne;
     TextView PhoneOne;

     TextView NameTitleTwo;
     TextView NameTwo;
     TextView PhoneTitleTwo;
     TextView PhoneTwo;

     TextView NameTitleThree;
     TextView NameThree;
     TextView PhoneTitleThree;
     TextView PhoneThree;

     TextView NameTitleFour;
     TextView NameFour;
     TextView PhoneTitleFour;
     TextView PhoneFour;

     TextView NameTitleFive;
     TextView NameFive;
     TextView PhoneTitleFive;
     TextView PhoneFive;

     TextView TitleContact;


    StepView stepView;

    Typeface font_Medium;
    Typeface font_Bold;

    Dialog loading;

    static Context context;

    BubbleNavigationLinearView bottom_navigation_view_linear;

    public static Dialog ActivityDialogInternetConnectionSearchContact;
    public static Dialog ActivityDialogInformationSearchContact;
    public static Dialog ActivityDialogErrorSearchContact;
    public static Dialog ActivityDialogExitSearchContact;

    @Override
    protected void onStart() {
        super.onStart();

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
                        {
                            showContacts();
                            getResultSearchContact();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }, 500);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        context=getApplicationContext();

        NameTitleOne=(TextView)findViewById(R.id.NameTitleOne);
        NameOne=(TextView)findViewById(R.id.NameOne);
        PhoneTitleOne=(TextView)findViewById(R.id.PhoneTitleOne);
        PhoneOne=(TextView)findViewById(R.id.PhoneOne);

        NameTitleTwo=(TextView)findViewById(R.id.NameTitleTwo);
        NameTwo=(TextView)findViewById(R.id.NameTwo);
        PhoneTitleTwo=(TextView)findViewById(R.id.PhoneTitleTwo);
        PhoneTwo=(TextView)findViewById(R.id.PhoneTwo);

        NameTitleThree=(TextView)findViewById(R.id.NameTitleThree);
        NameThree=(TextView)findViewById(R.id.NameThree);
        PhoneTitleThree=(TextView)findViewById(R.id.PhoneTitleThree);
        PhoneThree=(TextView)findViewById(R.id.PhoneThree);

        NameTitleFour=(TextView)findViewById(R.id.NameTitleFour);
        NameFour=(TextView)findViewById(R.id.NameFour);
        PhoneTitleFour=(TextView)findViewById(R.id.PhoneTitleFour);
        PhoneFour=(TextView)findViewById(R.id.PhoneFour);

        NameTitleFive=(TextView)findViewById(R.id.NameTitleFive);
        NameFive=(TextView)findViewById(R.id.NameFive);
        PhoneTitleFive=(TextView)findViewById(R.id.PhoneTitleFive);
        PhoneFive=(TextView)findViewById(R.id.PhoneFive);

        TitleContact=(TextView)findViewById(R.id.titleContact);

        bottom_navigation_view_linear=(BubbleNavigationLinearView) findViewById(R.id.bottom_navigation_view_linear);

        font_Medium= Typeface.createFromAsset(ContactActivity.context.getAssets(),"fonts/IRANSans_Medium.ttf");
        font_Bold=Typeface.createFromAsset(ContactActivity.context.getAssets(),"fonts/IRANSans_Bold.ttf");

        NameTitleOne.setTypeface(font_Bold);
        NameOne.setTypeface(font_Medium);
        PhoneTitleOne.setTypeface(font_Bold);
        PhoneOne.setTypeface(font_Medium);

        NameTitleTwo.setTypeface(font_Bold);
        NameTwo.setTypeface(font_Medium);
        PhoneTitleTwo.setTypeface(font_Bold);
        PhoneTwo.setTypeface(font_Medium);

        NameTitleThree.setTypeface(font_Bold);
        NameThree.setTypeface(font_Medium);
        PhoneTitleThree.setTypeface(font_Bold);
        PhoneThree.setTypeface(font_Medium);

        NameTitleFour.setTypeface(font_Bold);
        NameFour.setTypeface(font_Medium);
        PhoneTitleFour.setTypeface(font_Bold);
        PhoneFour.setTypeface(font_Medium);

        NameTitleFive.setTypeface(font_Bold);
        NameFive.setTypeface(font_Medium);
        PhoneTitleFive.setTypeface(font_Bold);
        PhoneFive.setTypeface(font_Medium);
        bottom_navigation_view_linear.setTypeface(font_Medium);
        TitleContact.setTypeface(font_Bold);

        stepView=(StepView)findViewById(R.id.step_view);

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
                if (view.getId()==R.id.navigation_rank)
                {
                    startActivity(new Intent(getBaseContext(), RankActivity.class));
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
            }
        });
    }

    @Override
    public void onBackPressed() {
        ActivityDialogShowExitSearchContact();
    }

    public void ActivityDialogShowInternetConnectionSearchContact()
    {
        ActivityDialogInternetConnectionSearchContact=new Dialog(ContactActivity.this);
        ActivityDialogInternetConnectionSearchContact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogInternetConnectionSearchContact.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogInternetConnectionSearchContact.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
        ActivityDialogInternetConnectionSearchContact.setContentView(R.layout.error_connection_dialog);

        TextView TitleErrorConnectionDialog=(TextView)ActivityDialogInternetConnectionSearchContact.findViewById(R.id.TitleErrorConnectionDialog);
        TextView TextErrorConnectionDialog=(TextView)ActivityDialogInternetConnectionSearchContact.findViewById(R.id.TextErrorConnectionDialog);

        TitleErrorConnectionDialog.setTypeface(font_Bold);
        TextErrorConnectionDialog.setTypeface(font_Medium);

        ActivityDialogInternetConnectionSearchContact.show();
    }
    public void ActivityDialogShowInformation(String Title,String Text)
    {
        ActivityDialogInformationSearchContact=new Dialog(ContactActivity.this);
        ActivityDialogInformationSearchContact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogInformationSearchContact.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogInformationSearchContact.getWindow().getAttributes().windowAnimations=R.style.DialogSlideLeftRight;
        ActivityDialogInformationSearchContact.setContentView(R.layout.information_dialog);

        TextView TitleInfoDialog=(TextView)ActivityDialogInformationSearchContact.findViewById(R.id.TitleInfoDialog);
        TextView TextInfoDialog=(TextView)ActivityDialogInformationSearchContact.findViewById(R.id.TextInfoDialog);

        TitleInfoDialog.setText(Title);
        TextInfoDialog.setText(Text);

        TitleInfoDialog.setTypeface(font_Bold);
        TextInfoDialog.setTypeface(font_Medium);

        ActivityDialogInformationSearchContact.show();
    }
    public void ActivityDialogShowError(String TextError)
    {
        ActivityDialogErrorSearchContact=new Dialog(ContactActivity.this);
        ActivityDialogErrorSearchContact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogErrorSearchContact.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogErrorSearchContact.getWindow().getAttributes().windowAnimations=R.style.DialogScale;
        ActivityDialogErrorSearchContact.setContentView(R.layout.error_dialog);

        TextView TextErrorDialog = (TextView)ActivityDialogErrorSearchContact.findViewById(R.id.TextErrorDialog);
        TextView TitleErrorDialog = (TextView)ActivityDialogErrorSearchContact.findViewById(R.id.TitleErrorDialog);

        TextErrorDialog.setText(TextError);

        TitleErrorDialog.setTypeface(font_Bold);
        TextErrorDialog.setTypeface(font_Medium);

        ActivityDialogErrorSearchContact.show();
    }
    public void ActivityDialogShowExitSearchContact()
    {
        ActivityDialogExitSearchContact=new Dialog(ContactActivity.this);
        ActivityDialogExitSearchContact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogExitSearchContact.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogExitSearchContact.getWindow().getAttributes().windowAnimations=R.style.DialogSlideLeftRight;
        ActivityDialogExitSearchContact.setContentView(R.layout.exit_dialog);

        ImageView ExitYes = (ImageView)ActivityDialogExitSearchContact.findViewById(R.id.ExitYes);
        ImageView ExitNo = (ImageView)ActivityDialogExitSearchContact.findViewById(R.id.ExitNo);
        TextView TitleExit=(TextView)ActivityDialogExitSearchContact.findViewById(R.id.TitleExit);
        TextView TextExit=(TextView)ActivityDialogExitSearchContact.findViewById(R.id.TextExit);

        ActivityDialogExitSearchContact.setCancelable(false);

        TitleExit.setTypeface(font_Bold);
        TextExit.setTypeface(font_Medium);

        ExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ContactActivity.this, "خروج", Toast.LENGTH_SHORT).show();
                ActivityDialogExitSearchContact.dismiss();
                finishAffinity();
            }
        });
        ExitNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDialogExitSearchContact.dismiss();
            }
        });

        ActivityDialogExitSearchContact.show();
    }
    public String getContactName(final String phoneNumber, Context context)
    {
        try {
            Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

            String contactName="";
            Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

            if (cursor != null) {
                if(cursor.moveToFirst()) {
                    contactName=cursor.getString(0);
                }
                cursor.close();
            }

            return contactName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    void getResultSearchContact()
    {
        try
        {
            if (loading.isShowing())
                loading.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        loading= new Dialog(ContactActivity.this);
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.loading_dialog);
        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();

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
                            getResultSearchContact();
                            try {
                                if (ActivityDialogInternetConnectionSearchContact.isShowing())
                                    ActivityDialogInternetConnectionSearchContact.dismiss();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            ActivityDialogShowInternetConnectionSearchContact();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponce=response.body().string();

                if (response.isSuccessful()){

                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            if (!isFinishing())
                            {
                                try {

                                    Log.d("looog", myResponce);

                                    JSONObject jObject = new JSONObject(myResponce);

                                    int ReqStatus = jObject.getInt("reqStatus");

                                    if (ReqStatus == 1)
                                    {
                                        try {
                                            if (loading.isShowing())
                                                loading.dismiss();
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        try {
                                            JSONArray importantContacts = jObject.getJSONArray("importantContacts");

                                            //Contact 1
                                            JSONObject contact = new JSONObject();
                                            contact=importantContacts.getJSONObject(0);

                                            String phone =String.valueOf(contact.getLong("phone"));

                                            String NameofContact="";

                                            NameofContact=getContactName("0"+phone,getApplicationContext());

                                            if (NameofContact.equals(""))
                                            {
                                                NameofContact=getContactName("+98"+phone,getApplicationContext());
                                            }
                                            if (NameofContact.equals(""))
                                            {
                                                NameofContact="نا معلوم";
                                            }

                                            NameOne.setText(NameofContact);
                                            PhoneOne.setText("0"+phone);

                                            //Contact 2
                                            contact=importantContacts.getJSONObject(1);

                                            phone =String.valueOf(contact.getLong("phone"));
                                            NameofContact="";

                                            NameofContact=getContactName("0"+phone,getApplicationContext());

                                            if (NameofContact.equals(""))
                                            {
                                                NameofContact=getContactName("+98"+phone,getApplicationContext());
                                            }
                                            if (NameofContact.equals(""))
                                            {
                                                NameofContact="نا معلوم";
                                            }

                                            NameTwo.setText(NameofContact);
                                            PhoneTwo.setText("0"+phone);

                                            //Contact 3
                                            contact=importantContacts.getJSONObject(2);

                                            phone =String.valueOf(contact.getLong("phone"));
                                            NameofContact="";

                                            NameofContact=getContactName("0"+phone,getApplicationContext());

                                            if (NameofContact.equals(""))
                                            {
                                                NameofContact=getContactName("+98"+phone,getApplicationContext());
                                            }
                                            if (NameofContact.equals(""))
                                            {
                                                NameofContact="نا معلوم";
                                            }

                                            NameThree.setText(NameofContact);
                                            PhoneThree.setText("0"+phone);

                                            //Contact 4
                                            contact=importantContacts.getJSONObject(3);

                                            phone =String.valueOf(contact.getLong("phone"));
                                            NameofContact="";

                                            NameofContact=getContactName("0"+phone,getApplicationContext());

                                            if (NameofContact.equals(""))
                                            {
                                                NameofContact=getContactName("+98"+phone,getApplicationContext());
                                            }
                                            if (NameofContact.equals(""))
                                            {
                                                NameofContact="نا معلوم";
                                            }

                                            NameFour.setText(NameofContact);
                                            PhoneFour.setText("0"+phone);

                                            //Contact 5
                                            contact=importantContacts.getJSONObject(4);

                                            phone =String.valueOf(contact.getLong("phone"));
                                            NameofContact="";

                                            NameofContact=getContactName("0"+phone,getApplicationContext());

                                            if (NameofContact.equals(""))
                                            {
                                                NameofContact=getContactName("+98"+phone,getApplicationContext());
                                            }
                                            if (NameofContact.equals(""))
                                            {
                                                NameofContact="نا معلوم";
                                            }

                                            NameFive.setText(NameofContact);
                                            PhoneFive.setText("0"+phone);

                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                    else if (ReqStatus == 2)
                                    {
                                        ActivityDialogShowInformation("قــبلا ارســـال شـــده","درخواست شما با موفقیت قبلا ارسال شده است. برای مشاهده نتیجه لطفا صبور باشید. ");
                                    }
                                    else if (ReqStatus == 0)
                                    {
                                        try {
                                            if (loading.isShowing())
                                                loading.dismiss();
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        ActivityDialogShowError("درخواست شما ثبت نشده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ");
                                    }
                                    else if (ReqStatus == -1)
                                    {
                                        try {
                                            if (loading.isShowing())
                                                loading.dismiss();
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        ActivityDialogShowError("درخواست شما ارسال نشد. مشکلی در ثبت درخواست شما در سرور به وجود آمده است. برای مشاهده مهمترین مخاطب لطفا روی گزینه اسکن مخاطبین کلیک کنید. ");
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                }
                else {
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
    }
    private void showContacts() throws JSONException {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("ispermission",true).apply();

            } else {
                Toast.makeText(this, "برای نمایش مهم ترین مخاطبین لازم است دسترسی به مخاطبین داده شود", Toast.LENGTH_SHORT).show();
                getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("ispermission",false).apply();
            }
        }
    }
    public void Guide()
    {
        TapTargetView.showFor(ContactActivity.this,
                TapTarget.forView(findViewById(R.id.navigation_contacts), "رتبه بندی مخاطبین",  "تا به حال به این فکر افتاده\u200Cاید که کدام یک از مخاطبین شما از همه معروفــتر و مهمــتر است؟؟؟"+"\n"+
                        "در این قسمت می\u200Cتوانید رتبه بندی مخاطبین خود را بر اساس میزان معروفیت مشاهده کنید")
                        .outerCircleColor(R.color.contactsCircle)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.contactstargetCircle)
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
                        getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("guidedone",true).apply();
                    }
                });
    }
}
