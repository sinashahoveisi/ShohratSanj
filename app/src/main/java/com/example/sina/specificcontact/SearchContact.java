package com.example.sina.specificcontact;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuhart.stepview.StepView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.sina.specificcontact.SearchPerson.FromContactActivity;

public class SearchContact extends AppCompatActivity {

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

    BottomNavigationView bottomNavigationView;

    public static Dialog ActivityDialogInternetConnectionSearchContact;
    public static Dialog ActivityDialogInformationSearchContact;
    public static Dialog ActivityDialogErrorSearchContact;
    public static Dialog ActivityDialogExitSearchContact;

    @Override
    protected void onStart() {
        super.onStart();

        getResultSearchContact();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contact);

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

        bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottom_navigation_view);

        font_Medium= Typeface.createFromAsset(SearchPerson.context.getAssets(),"fonts/IRANSans_Medium.ttf");
        font_Bold=Typeface.createFromAsset(SearchPerson.context.getAssets(),"fonts/IRANSans_Bold.ttf");

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

        TitleContact.setTypeface(font_Bold);

        stepView=(StepView)findViewById(R.id.step_view);

        stepView = findViewById(R.id.step_view);
        stepView.setStepsNumber(3);
        stepView.go(2, true);
        stepView.done(true);

        bottomNavigationView.setSelectedItemId(R.id.navigation_contacts);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.navigation_person)
                {
                    FromContactActivity=true;
                    startActivity(new Intent(getBaseContext(), SearchPerson.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        ActivityDialogShowExitSearchContact();
    }

    public void ActivityDialogShowInternetConnectionSearchContact()
    {
        ActivityDialogInternetConnectionSearchContact=new Dialog(SearchContact.this);
        ActivityDialogInternetConnectionSearchContact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogInternetConnectionSearchContact.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogInternetConnectionSearchContact.setContentView(R.layout.error_connection_dialog);

        TextView TitleErrorConnectionDialog=(TextView)ActivityDialogInternetConnectionSearchContact.findViewById(R.id.TitleErrorConnectionDialog);
        TextView TextErrorConnectionDialog=(TextView)ActivityDialogInternetConnectionSearchContact.findViewById(R.id.TextErrorConnectionDialog);

        TitleErrorConnectionDialog.setTypeface(font_Bold);
        TextErrorConnectionDialog.setTypeface(font_Medium);

        ActivityDialogInternetConnectionSearchContact.show();
    }
    public void ActivityDialogShowinformation(String Title,String Text)
    {
        ActivityDialogInformationSearchContact=new Dialog(SearchContact.this);
        ActivityDialogInformationSearchContact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogInformationSearchContact.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
        ActivityDialogErrorSearchContact=new Dialog(SearchContact.this);
        ActivityDialogErrorSearchContact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogErrorSearchContact.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
        ActivityDialogExitSearchContact=new Dialog(SearchContact.this);
        ActivityDialogExitSearchContact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogExitSearchContact.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                Toast.makeText(SearchContact.this, "خروج", Toast.LENGTH_SHORT).show();
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
        loading= new Dialog(SearchContact.this);
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
                                        ActivityDialogShowinformation("قــبلا ارســـال شـــده","درخواست شما با موفقیت قبلا ارسال شده است. برای مشاهده نتیجه لطفا صبور باشید. ");
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
            }
        });
    }
}
