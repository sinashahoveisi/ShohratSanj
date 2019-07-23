package com.example.sina.specificcontact;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.BubbleToggleView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.shuhart.stepview.StepView;

import java.util.Objects;

import static com.example.sina.specificcontact.RankActivity.ranking;
import static com.example.sina.specificcontact.RankActivity.rankingInt;

public class FameActivity extends AppCompatActivity {

    static Context context;

    Typeface font_Medium;
    Typeface font_Bold;

    TextView TitleFame;
    TextView RankUser;
    TextView RankTitleUser;

    public static ProgressBar RankProgressBar;

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
        setContentView(R.layout.activity_fame);

        context=getApplicationContext();

        TitleFame=(TextView)findViewById(R.id.titleFame);
        RankUser=(TextView)findViewById(R.id.RankUser);
        RankTitleUser=(TextView)findViewById(R.id.RankTitleUser);
        RankProgressBar=(ProgressBar)findViewById(R.id.RankProgressBar);
        bottom_navigation_view_linear=(BubbleNavigationLinearView) findViewById(R.id.bottom_navigation_view_linear);

        font_Medium= Typeface.createFromAsset(FameActivity.context.getAssets(),"fonts/IRANSans_Medium.ttf");
        font_Bold=Typeface.createFromAsset(FameActivity.context.getAssets(),"fonts/IRANSans_Bold.ttf");

        TitleFame.setTypeface(font_Bold);
        RankTitleUser.setTypeface(font_Bold);
        RankUser.setTypeface(font_Medium);
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
        if (ranking==null)
        {
            RankUser.setText("0 %");
            RankProgressBar.setProgress(0);
        }
        else
        {
            RankUser.setText(ranking);
            RankProgressBar.setProgress(rankingInt);
        }
    }

    @Override
    public void onBackPressed() {
        ActivityDialogShowExit();
    }
    public void ActivityDialogShowExit()
    {
        ActivityDialogExit=new Dialog(FameActivity.this);
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
                Toast.makeText(FameActivity.this, "خروج", Toast.LENGTH_SHORT).show();
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
        TapTargetView.showFor(FameActivity.this,
                TapTarget.forView(findViewById(R.id.navigation_fame), "میزان معروفیت", "آیا می\u200Cخواهید از میزان معروفیت خود آگاه بشوید ؟؟؟"+"\n"+
                        "در این قسمت می\u200Cتوانید معروفیت خود را مشاهده کرده و با دوستان خود مقایسه کنید")
                        .outerCircleColor(R.color.fameCircle)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.fametargetCircle)
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
                        BubbleToggleView bubbleToggleView = (BubbleToggleView)findViewById(R.id.navigation_likeness);
                        bottom_navigation_view_linear.onClick(bubbleToggleView);
                    }
                });
    }
}
