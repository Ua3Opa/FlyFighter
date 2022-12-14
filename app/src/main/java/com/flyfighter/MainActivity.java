package com.flyfighter;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flyfighter.config.Config;
import com.flyfighter.enums.RunState;
import com.flyfighter.holder.MainDataHolder;
import com.flyfighter.view.MainWindow;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private MainWindow mainWindow;
    private RelativeLayout adsContainer;
    private AdView mAdView;
    public RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainWindow = findViewById(R.id.mainWindow);
        adsContainer = findViewById(R.id.adsContainer);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(0);
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        MobileAds.initialize(this, initializationStatus -> {
        });
        initObserver();
        initBottomAds();
        initRewordAds();
    }

    private void initObserver() {
        MainDataHolder.mainAdState.observe(this, show -> {
            adsContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        });
        MainDataHolder.mainRewordAdState.observe(this, show -> {
            if (show == null) {
                return;
            }
            if (show) {
                showRewardedVideo();
            }
        });
        MainDataHolder.runState.observe(this, state -> {
            mainWindow.handleRunState(state);
        });
    }


    private void initBottomAds() {
        mAdView = new AdView(this);
        mAdView.setAdUnitId(Config.getBannerUnitId());
        adsContainer.addView(mAdView);
        loadBanner();
    }


    @Override
    public void onBackPressed() {
        if (MainDataHolder.runState.getValue() == RunState.Running) {
            MainDataHolder.runState.setValue(RunState.Pause);
            return;
        }
        if (MainDataHolder.runState.getValue() == RunState.Config) {
            MainDataHolder.runState.setValue(RunState.Close_Config);
            return;
        }
        if (MainDataHolder.runState.getValue() == RunState.Ranking) {
            MainDataHolder.runState.setValue(RunState.Close_Ranking);
            return;
        }
        if (MainDataHolder.runState.getValue() == RunState.Help) {
            MainDataHolder.runState.setValue(RunState.Close_Help);
            return;
        }

        super.onBackPressed();
    }

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.setAdSize(getAdSize());
        mAdView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }


    private void initRewordAds() {
        if (rewardedAd == null) {
            AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
            RewardedAd.load(this, Config.getRewordUnitId(), adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    // Handle the error.
                    Log.d(TAG, loadAdError.getMessage());
                    rewardedAd = null;
                    //Toast.makeText(MainActivity.this, "广告加载失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdLoaded(RewardedAd rewardedAd) {
                    MainActivity.this.rewardedAd = rewardedAd;
                    Log.d(TAG, "onAdLoaded");
                }
            });
        }
    }

    private void showRewardedVideo() {
        if (rewardedAd == null) {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
            return;
        }
        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.d(TAG, "onAdShowedFullScreenContent");
//                        Toast.makeText(MainActivity.this, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
//                                .show();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Log.d(TAG, "onAdFailedToShowFullScreenContent");
                        rewardedAd = null;
                        Toast.makeText(MainActivity.this, "广告展示失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(TAG, "onAdDismissedFullScreenContent");
                        rewardedAd = null;
                        MainActivity.this.initRewordAds();
                        if (MainDataHolder.getReward) {
                            MainDataHolder.runState.setValue(RunState.ContinuePlay);
                        }
                        MainDataHolder.mainRewordAdState.setValue(false);
                    }
                });
        MainDataHolder.getReward = false;
        rewardedAd.show(this, rewardItem -> {
            // Handle the reward.
            Log.d("TAG", "The user earned the reward.");
//            int rewardAmount = rewardItem.getAmount();
//            String rewardType = rewardItem.getType();
            MainDataHolder.getReward = true;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!MainDataHolder.mainRewordAdState.getValue()) {
            MainDataHolder.runState.postValue(RunState.Pause);
        }

    }
}
