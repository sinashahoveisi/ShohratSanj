package com.example.sina.specificcontact;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.chaos.view.PinView;
import com.github.florent37.materialtextfield.MaterialTextField;
import com.shuhart.stepview.StepView;

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


public class Authentication extends AppCompatActivity {

    private int currentStep = 0;
    LinearLayout layout1,layout2;
    StepView stepView;

    private Button sendCodeButton;
    private Button verifyCodeButton;

    private EditText phoneNum;
    private PinView verifyCodeET;
    private TextView phonenumberText;
    private TextView ReSend;
    private MaterialTextField PhoneNumberClick;
    private TextView EnterPhoneNumberMessage;
    private TextView EnterPhoneNumberMessageEndPage;
    private TextView SendCodeMessage;
    private TextView SendCodeMessageEndPage;

    Dialog loading;

    static Context context;

    Typeface font_Medium;
    Typeface font_Light;
    Typeface font_Bold;

    public static String phoneNumber;

    private CountDownTimer countDownTimer;
    private long  timeLeftInMiliseconds;

    public boolean isResend;
    public boolean isVibration;
    boolean isTimerOn;

    IconRoundCornerProgressBar TimerProgressBar;

    public static Dialog ActivityDialogChangePhone;
    public static Dialog ActivityDialogInternetConnectionAuthentication;
    public static Dialog ActivityDialogExit;
    public static Dialog ActivityDialogErrorCode;
    public static Dialog ActivityDialogErrorTime;
    public static Dialog ActivityDialogErrorSeconds;
    public static Dialog ActivityDialogErrorHours;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            Slide enterTransaction = new Slide();
            enterTransaction.setSlideEdge(Gravity.BOTTOM);
            enterTransaction.setDuration(1000);
            enterTransaction.setInterpolator(new AnticipateInterpolator());
            getWindow().setEnterTransition(enterTransaction);
        }

        context=getApplicationContext();

        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        sendCodeButton = (Button) findViewById(R.id.submit1);
        verifyCodeButton = (Button) findViewById(R.id.submit2);
        phoneNum = (EditText) findViewById(R.id.phonenumber);
        verifyCodeET = (PinView) findViewById(R.id.pinView);
        phonenumberText = (TextView) findViewById(R.id.phonenumberText);
        ReSend=(TextView) findViewById(R.id.resend);
        PhoneNumberClick=(MaterialTextField)findViewById(R.id.phonenumberClick);
        EnterPhoneNumberMessage=(TextView) findViewById(R.id.EnterPhoneNumberMessage);
        EnterPhoneNumberMessageEndPage=(TextView) findViewById(R.id.EnterPhoneNumberMessageEndPage);
        SendCodeMessage=(TextView) findViewById(R.id.SendCodeMessage);
        SendCodeMessageEndPage=(TextView) findViewById(R.id.SendCodeMessageEndPage);
        TimerProgressBar=(IconRoundCornerProgressBar) findViewById(R.id.TimerProgressBar);

        ReSend.setVisibility(View.INVISIBLE);
        TimerProgressBar.setMax(60);

        font_Medium=Typeface.createFromAsset(Authentication.context.getAssets(),"fonts/IRANSans_Medium.ttf");
        font_Light=Typeface.createFromAsset(Authentication.context.getAssets(),"fonts/IRANSans_Light.ttf");
        font_Bold=Typeface.createFromAsset(Authentication.context.getAssets(),"fonts/IRANSans_Bold.ttf");
        //EnterNumber
        EnterPhoneNumberMessage.setTypeface(font_Bold);
        phonenumberText.setTypeface(font_Medium);
        EnterPhoneNumberMessageEndPage.setTypeface(font_Light);
        sendCodeButton.setTypeface(font_Bold);
        phoneNum.setTypeface(font_Medium);
        //EnterCode
        SendCodeMessage.setTypeface(font_Bold);
        phonenumberText.setTypeface(font_Medium);
        verifyCodeButton.setTypeface(font_Bold);
        ReSend.setTypeface(font_Medium);
        SendCodeMessageEndPage.setTypeface(font_Light);


        stepView = findViewById(R.id.step_view);
        stepView.setStepsNumber(3);
        stepView.go(0, true);
        layout1.setVisibility(View.VISIBLE);

        isResend=false;
        isVibration=false;
        isTimerOn=false;

        TimerProgressBar.setOnIconClickListener(new IconRoundCornerProgressBar.OnIconClickListener() {
            @Override
            public void onIconClick() {
                if (isVibration) {
                    Toast.makeText(Authentication.this, "حالت ویبره در زمان خاموش", Toast.LENGTH_SHORT).show();
                    isVibration=false;
                }
                else {
                    Toast.makeText(Authentication.this, "حالت ویبره در زمان روشن", Toast.LENGTH_SHORT).show();
                    isVibration=true;
                }
            }
        });

        phonenumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDialogShowChangePhone();
            }
        });

        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phoneNumber = phoneNum.getText().toString();
                phonenumberText.setText(phoneNumber);

                if (TextUtils.isEmpty(phoneNumber)) {
                    phoneNum.setError("لطفا شماره تلفن خود را وارد کنید");
                    phoneNum.requestFocus();
                } else if (phoneNumber.length() < 11 ) {
                    phoneNum.setError("شماره تلفن باید ۱۱ عدد باشد");
                    phoneNum.requestFocus();
                }else if (!phoneNumber.startsWith("09")) {
                    phoneNum.setError("شماره تلفن باید با 09 شروع شود");
                    phoneNum.requestFocus();
                }
                else {

                    loading= new Dialog(Authentication.this);
                    loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    loading.setContentView(R.layout.loading_dialog);
                    Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));;
                    loading.setCancelable(false);
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("phone", phoneNumber);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String jsonString = jsonObject.toString();

                            OkHttpClient client = new OkHttpClient();

                            OkHttpClient.Builder builder = new OkHttpClient.Builder();
                            builder.connectTimeout(15, TimeUnit.SECONDS);
                            builder.readTimeout(15, TimeUnit.SECONDS);
                            builder.writeTimeout(15, TimeUnit.SECONDS);

                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType, jsonString);
                            Request request = new Request.Builder()
                                    .url("http://217.218.215.67:6608/api/user/login")
                                    .post(body)
                                    .addHeader("Content-Type", "application/json")
                                    .build();

                            client = builder.build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("looooog", e.toString());
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                if(!isFinishing())
                                                    ActivityDialogShowInternetConnectionAuthentication();
                                            }
                                        });

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String myResponce = response.body().string();
                                    if (response.isSuccessful()) {
                                        Log.d("looog", "Successful");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(!isFinishing())
                                                {
                                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                    imm.hideSoftInputFromWindow(sendCodeButton.getWindowToken(), 0);
                                                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("PhoneNumber",phoneNumber).apply();
                                                    PhoneNumberClick.callOnClick();

                                                    final Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (!isResend)
                                                            {
                                                                if (currentStep < stepView.getStepCount() - 1) {
                                                                    currentStep++;
                                                                    stepView.go(currentStep, true);
                                                                } else {
                                                                    stepView.done(true);
                                                                }
                                                            }
                                                            if (loading.isShowing())
                                                                loading.dismiss();
                                                            layout1.setVisibility(View.GONE);
                                                            layout2.setVisibility(View.VISIBLE);
                                                            ReSend.setVisibility(View.INVISIBLE);
                                                            if (isTimerOn)
                                                                countDownTimer.cancel();
                                                            StartTimer();
                                                        }
                                                    }, 1500);
                                                }
                                            }
                                        });
                                    }
                                    else
                                    {
                                        if (!isFinishing())
                                        {
                                            try {
                                                Log.d("loooog", myResponce);
                                                JSONObject jObject = new JSONObject(myResponce);

                                                int ErrorCode = jObject.getInt("code");

                                                if (loading.isShowing())
                                                    loading.dismiss();

                                                runOnUiThread(new Runnable() {
                                                    public void run() {

                                                        if (ErrorCode==4020)
                                                        {
                                                            ActivityDialogShowErrorSeconds();
                                                        }
                                                        else if (ErrorCode==4021)
                                                        {
                                                            ActivityDialogShowErrorHours();
                                                        }
                                                        else if (ErrorCode==4008)
                                                        {
                                                            phoneNum.setError("لطفا شماره معتبر وارد کنید");
                                                            phoneNum.requestFocus();
                                                        }
                                                    }
                                                });
                                            }catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        ReSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isResend=true;
                sendCodeButton.callOnClick();

                Toast.makeText(Authentication.this, "دوباره ارسال شد", Toast.LENGTH_SHORT).show();
            }
        });


        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();

                String verificationCode = verifyCodeET.getText().toString();
                if(verificationCode.length()<4){
                    Toast.makeText(Authentication.this,"لطفا کد ۴ رقمی که برای شما ارسال شده را وارد کنید",Toast.LENGTH_SHORT).show();
                }
                else {

                    loading= new Dialog(Authentication.this);
                    loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    loading.setContentView(R.layout.loading_dialog);
                    Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));;
                    loading.setCancelable(false);
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("phone", phoneNumber);
                                jsonObject.put("code", verificationCode);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String jsonString = jsonObject.toString();

                            OkHttpClient client = new OkHttpClient();

                            OkHttpClient.Builder builder = new OkHttpClient.Builder();
                            builder.connectTimeout(15, TimeUnit.SECONDS);
                            builder.readTimeout(15, TimeUnit.SECONDS);
                            builder.writeTimeout(15, TimeUnit.SECONDS);

                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType, jsonString);
                            Request request = new Request.Builder()
                                    .url("http://217.218.215.67:6608/api/user/validationCode")
                                    .post(body)
                                    .addHeader("Content-Type", "application/json")
                                    .build();

                            client = builder.build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("looooog", e.toString());
                                    runOnUiThread(new Runnable() {
                                        public void run() {

                                            if (loading.isShowing())
                                                loading.dismiss();
                                            if (!isFinishing())
                                                ActivityDialogShowInternetConnectionAuthentication();
                                        }
                                    });

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String myResponce = response.body().string();

                                    if (response.isSuccessful()) {
                                        Log.d("looog", myResponce);
                                        Authentication.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!isFinishing())
                                                {
                                                    try {
                                                        if (loading.isShowing())
                                                            loading.dismiss();
                                                        countDownTimer.cancel();
                                                        JSONObject jObject = new JSONObject(myResponce);

                                                        String token = jObject.getString("token");

                                                        getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putString("token",token).apply();

                                                        if (currentStep < stepView.getStepCount() - 1) {
                                                            currentStep++;
                                                            stepView.go(currentStep, true);
                                                        } else {
                                                            stepView.done(true);
                                                        }
                                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                        imm.hideSoftInputFromWindow(sendCodeButton.getWindowToken(), 0);
                                                        loading= new Dialog(Authentication.this);
                                                        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                        loading.setContentView(R.layout.tik_dialog);
                                                        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));;
                                                        loading.setCancelable(false);
                                                        loading.setCanceledOnTouchOutside(false);
                                                        loading.show();
                                                        Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                loading.dismiss();
                                                                startActivity(new Intent(Authentication.this, SearchPerson.class));
                                                                finish();
                                                            }
                                                        },4000);


                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        if (!isFinishing())
                                        {
                                            try {
                                                if (loading.isShowing())
                                                    loading.dismiss();

                                                Log.d("looog", myResponce);

                                                JSONObject jObject = new JSONObject(myResponce);

                                                int ErrorCode = jObject.getInt("code");

                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        if (ErrorCode==4022 && ReSend.getVisibility() == View.VISIBLE) {
                                                            ActivityDialogShowErrorTime();
                                                        } else if (ErrorCode==4022) {
                                                            ActivityDialogShowErrorCode();
                                                        }
                                                    }
                                                });
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PhoneNumberClick.callOnClick();
            }
        }, 1500);

    }

    public void StartTimer ()
    {
        timeLeftInMiliseconds=60000;
        isTimerOn=true;
        countDownTimer = new CountDownTimer(timeLeftInMiliseconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMiliseconds=millisUntilFinished;

                TimerProgressBar.setProgress((int)(timeLeftInMiliseconds/1000));

                if (isVibration)
                {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(200);
                    }
                }
            }

            @Override
            public void onFinish() {
                isTimerOn=false;
                TimerProgressBar.setProgress(0);
                ReSend.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void closeKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void ActivityDialogShowChangePhone()
    {
        ActivityDialogChangePhone=new Dialog(Authentication.this);
        ActivityDialogChangePhone.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActivityDialogChangePhone.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogChangePhone.setContentView(R.layout.change_phone_dialog);

        ImageView Yes = (ImageView)ActivityDialogChangePhone.findViewById(R.id.ChangePhoneYes);
        ImageView No = (ImageView)ActivityDialogChangePhone.findViewById(R.id.ChangePhoneNo);
        TextView TitleChangePhone=(TextView)ActivityDialogChangePhone.findViewById(R.id.TitleChangePhone);
        TextView TextChangePhone=(TextView)ActivityDialogChangePhone.findViewById(R.id.TextChangePhone);

        ActivityDialogChangePhone.setCancelable(false);

        TitleChangePhone.setTypeface(font_Bold);
        TextChangePhone.setTypeface(font_Medium);

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStep--;
                stepView.go(currentStep, true);

                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                ReSend.setVisibility(View.INVISIBLE);
                PhoneNumberClick.callOnClick();
                if (ActivityDialogChangePhone.isShowing())
                    ActivityDialogChangePhone.dismiss();
            }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDialogChangePhone.dismiss();
            }
        });

        ActivityDialogChangePhone.show();
    }

    public void ActivityDialogShowInternetConnectionAuthentication()
    {
        ActivityDialogInternetConnectionAuthentication=new Dialog(Authentication.this);
        ActivityDialogInternetConnectionAuthentication.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogInternetConnectionAuthentication.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogInternetConnectionAuthentication.setContentView(R.layout.error_connection_dialog);

        TextView TitleErrorConnectionDialog=(TextView)ActivityDialogInternetConnectionAuthentication.findViewById(R.id.TitleErrorConnectionDialog);
        TextView TextErrorConnectionDialog=(TextView)ActivityDialogInternetConnectionAuthentication.findViewById(R.id.TextErrorConnectionDialog);

        TitleErrorConnectionDialog.setTypeface(font_Bold);
        TextErrorConnectionDialog.setTypeface(font_Medium);

        ActivityDialogInternetConnectionAuthentication.show();
    }
    public void ActivityDialogShowErrorCode()
    {
        ActivityDialogErrorCode=new Dialog(Authentication.this);
        ActivityDialogErrorCode.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogErrorCode.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogErrorCode.setContentView(R.layout.error_code_dialog);

        TextView TitleErrorCode=(TextView)ActivityDialogErrorCode.findViewById(R.id.TitleErrorCode);
        TextView TextErrorCode=(TextView)ActivityDialogErrorCode.findViewById(R.id.TextErrorCode);

        TitleErrorCode.setTypeface(font_Bold);
        TextErrorCode.setTypeface(font_Medium);

        ActivityDialogErrorCode.show();
    }
    public void ActivityDialogShowErrorTime()
    {
        ActivityDialogErrorTime=new Dialog(Authentication.this);
        ActivityDialogErrorTime.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogErrorTime.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogErrorTime.setContentView(R.layout.error_time_dialog);

        TextView TitleErrorTime=(TextView)ActivityDialogErrorTime.findViewById(R.id.TitleErrorTime);
        TextView TextErrorTime=(TextView)ActivityDialogErrorTime.findViewById(R.id.TextErrorTime);

        TitleErrorTime.setTypeface(font_Bold);
        TextErrorTime.setTypeface(font_Medium);

        ActivityDialogErrorTime.show();
    }
    public void ActivityDialogShowErrorSeconds()
    {
        ActivityDialogErrorSeconds=new Dialog(Authentication.this);
        ActivityDialogErrorSeconds.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogErrorSeconds.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogErrorSeconds.setContentView(R.layout.error_seconds_dialog);

        TextView TitleErrorSeconds=(TextView)ActivityDialogErrorSeconds.findViewById(R.id.TitleErrorSeconds);
        TextView TextErrorSeconds=(TextView)ActivityDialogErrorSeconds.findViewById(R.id.TextErrorSeconds);

        TitleErrorSeconds.setTypeface(font_Bold);
        TextErrorSeconds.setTypeface(font_Medium);

        ActivityDialogErrorSeconds.show();
    }
    public void ActivityDialogShowErrorHours()
    {
        ActivityDialogErrorHours=new Dialog(Authentication.this);
        ActivityDialogErrorHours.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogErrorHours.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ActivityDialogErrorHours.setContentView(R.layout.error_hours_dialog);

        TextView TitleErrorHours=(TextView)ActivityDialogErrorHours.findViewById(R.id.TitleErrorHours);
        TextView TextErrorHours=(TextView)ActivityDialogErrorHours.findViewById(R.id.TextErrorhours);

        TitleErrorHours.setTypeface(font_Bold);
        TextErrorHours.setTypeface(font_Medium);

        ActivityDialogErrorHours.show();
    }
    public void ActivityDialogShowExit()
    {
        ActivityDialogExit=new Dialog(Authentication.this);
        ActivityDialogExit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(ActivityDialogExit.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                Toast.makeText(Authentication.this, "خروج", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        ActivityDialogShowExit();
    }
}
