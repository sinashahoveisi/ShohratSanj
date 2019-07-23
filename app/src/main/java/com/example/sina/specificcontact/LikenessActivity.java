package com.example.sina.specificcontact;

import android.Manifest;
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

import org.json.JSONException;

import java.util.Objects;

import static com.example.sina.specificcontact.RankActivity.numberCommonContact;
import static com.example.sina.specificcontact.RankActivity.phoneCommonContact;

public class LikenessActivity extends AppCompatActivity {

    static Context context;

    Typeface font_Medium;
    Typeface font_Bold;

    TextView TitleLikeness;
    TextView NumberCommon;
    TextView PhoneCommon;
    TextView CommonTitleUser;
    TextView NameCommonTitle;
    TextView NameCommon;
    TextView NumberCommonTitle;
    TextView PhoneCommonTitle;

    StepView stepView;

    BubbleNavigationLinearView bottom_navigation_view_linear;

    public Dialog ActivityDialogExit;

    @Override
    protected void onStart() {
        super.onStart();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean GuideDone = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("guidedone",false);
                    if (!GuideDone)
                        Guide();
                    else
                        showContacts();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likeness);

        context=getApplicationContext();

        NumberCommon=(TextView)findViewById(R.id.NumberCommon);
        TitleLikeness=(TextView)findViewById(R.id.titleLikeness);
        PhoneCommon=(TextView)findViewById(R.id.PhoneCommon);
        CommonTitleUser=(TextView)findViewById(R.id.CommonTitleUser);
        NumberCommonTitle=(TextView)findViewById(R.id.NumberCommonTitle);
        PhoneCommonTitle=(TextView)findViewById(R.id.PhoneCommonTitle);
        NameCommonTitle=(TextView)findViewById(R.id.NameCommonTitle);
        NameCommon=(TextView)findViewById(R.id.NameCommon);
        bottom_navigation_view_linear=(BubbleNavigationLinearView) findViewById(R.id.bottom_navigation_view_linear);

        font_Medium= Typeface.createFromAsset(LikenessActivity.context.getAssets(),"fonts/IRANSans_Medium.ttf");
        font_Bold=Typeface.createFromAsset(LikenessActivity.context.getAssets(),"fonts/IRANSans_Bold.ttf");


        TitleLikeness.setTypeface(font_Bold);
        NumberCommon.setTypeface(font_Medium);
        PhoneCommon.setTypeface(font_Medium);
        CommonTitleUser.setTypeface(font_Bold);
        NumberCommonTitle.setTypeface(font_Medium);
        NameCommonTitle.setTypeface(font_Medium);
        PhoneCommonTitle.setTypeface(font_Medium);
        NameCommon.setTypeface(font_Medium);
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
                if (view.getId()==R.id.navigation_contacts)
                {
                    startActivity(new Intent(getBaseContext(), ContactActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        });

        String name ="";

        name=getContactName("0"+phoneCommonContact,getApplicationContext());

        if (name.equals(""))
        {
            name=getContactName("+98"+phoneCommonContact,getApplicationContext());
        }
        if (name.equals(""))
        {
            name="نا معلوم";
        }
        String phone = "نا معلوم";
        if (phoneCommonContact!=null)
            phone = "0"+phoneCommonContact;

        NameCommon.setText(name);
        NumberCommon.setText(numberCommonContact);
        PhoneCommon.setText(phone);
    }
    @Override
    public void onBackPressed() {
        ActivityDialogShowExit();
    }
    public void ActivityDialogShowExit()
    {
        ActivityDialogExit=new Dialog(LikenessActivity.this);
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
                Toast.makeText(LikenessActivity.this, "خروج", Toast.LENGTH_SHORT).show();
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
        TapTargetView.showFor(LikenessActivity.this,
                TapTarget.forView(findViewById(R.id.navigation_likeness), "میزان شباهت", "تا به حال به این فکر افتاده\u200Cاید که مخاطبین شما با مخاطبین چه کسی شباهت دارد؟؟؟"+"\n"+
                        "در این قسمت می توانید به این سوال پاسخ دهید")
                        .outerCircleColor(R.color.likenessCircle)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.likenesstargetCircle)
                        .titleTextSize(20)
                        .titleTextColor(R.color.textColor)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.textColor)
                        .textColor(R.color.textColor)
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
                        BubbleToggleView bubbleToggleView = (BubbleToggleView)findViewById(R.id.navigation_contacts);
                        bottom_navigation_view_linear.onClick(bubbleToggleView);
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
}
