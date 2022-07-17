package com.efpstudios.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.efpstudios.R;
import com.efpstudios.constant.Applications;
import com.efpstudios.constant.Constant;
import com.efpstudios.constant.NativeComunicate;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.splash.SplashConfig;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MenuActivity extends AppCompatActivity {

    private static final int KODE_REQ_GALERI = 1;
    private static final int KODE_REQ_KAMERA = 2;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        final Applications applications = (Applications) getApplication();
        String status = applications.getStatusIklan();
        if(status.equals(Constant.gagalLoadIklan)) {
            StartAppSDK.init(this, new NativeComunicate(this).getStartAppId(),false);
            if (!isNetworkConnected()) {
                StartAppAd.disableSplash();
            }else{
                StartAppAd.showSplash(this, savedInstanceState,
                        new SplashConfig()
                                .setTheme(SplashConfig.Theme.USER_DEFINED)
                                .setCustomScreen(R.layout.activity_splash)
                );
            }
        }

        if(status.equals(Constant.berhasilLoadIklan)){
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.iklanMenu);
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(new NativeComunicate(this).getAdBanner());
            adView.loadAd(new AdRequest.Builder().build());
            adContainer.addView(adView);
        }else{
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.iklanMenu);
            Banner startAppBanner = new Banner(this);
            adContainer.addView(startAppBanner);
        }

        findViewById(R.id.btnChooseFromGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent views = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                views.setType("image/*");
                startActivityForResult(views, KODE_REQ_GALERI);
            }
        });

        findViewById(R.id.btnPickFromCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, KODE_REQ_KAMERA);
            }
        });

        findViewById(R.id.btnRateApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Rate This App at Store");
                alertDialog.setMessage("Do you like this app? If you like this app please give this app good rate and review so we will continue develop an update for make this app better");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Rate This App",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                                    startActivity(intent);
                                }catch (Exception e){
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id="+ getApplicationContext().getPackageName()));
                                    startActivity(i);
                                }
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        });

        findViewById(R.id.btnOtherApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("More Apps By Us at Store");
                alertDialog.setMessage("You are about leaving this app and open Play Store to browse All off our applications. Do you want to continue?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes, Continue",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("market://search?q=pub:"+ Constant.NAMA_DEV_ID));
                                    startActivity(intent);
                                }catch (Exception e){
                                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                                    i.setData(Uri.parse("https://play.google.com/store/apps/developer?id="+ Constant.NAMA_DEV_ID));
                                    startActivity(i);
                                }
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No, Stay at this app",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        findViewById(R.id.btnShareApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "Hi, I found this app \""+ getString(R.string.app_name) +"\" is very cool. I think you will like it, download for free from this link " + "https://play.google.com/store/apps/details?id="+ getApplicationContext().getPackageName();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share this app via ..."));
            }
        });
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            final Applications app = (Applications) getApplication();
            String status = app.getStatusIklan();
            if (status.equals(Constant.berhasilLoadIklan)) {
                if (!app.isBolehMenampilkanIklanHitung() || !app.isBolehMenampilkanIklanWaktu()
                        || !app.getInterstitial().isLoaded()) {
                    onResult(requestCode, data);
                }
                app.getInterstitial().setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        onResult(requestCode, data);

                        app.loadIntersTitial();
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        if (app.getHitungFailed() < 2) {
                            app.loadIntersTitial();
                            app.setHitungFailed();
                        }
                        super.onAdFailedToLoad(i);
                    }

                    @Override
                    public void onAdLoaded() {
                        app.setHitungFailed(0);
                        super.onAdLoaded();
                    }
                });
                app.tampilkanInterstitial();
            } else {
                if (app.getPenghitungStartApp() == 0) {
                    app.setPenghitungStartApp(1);
                    onResult(requestCode, data);
                    StartAppAd.showAd(context);
                } else {
                    app.setPenghitungStartApp(0);
                    onResult(requestCode, data);
                }
            }
        }else{
            Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show();
        }
    }


    private void onResult(int requestCode, Intent data){
            if (requestCode == KODE_REQ_GALERI) {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                yourSelectedImage = getResizedBitmap(yourSelectedImage, 1000);
                Constant.IMAGE_USER = yourSelectedImage;
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }else if(requestCode == KODE_REQ_KAMERA){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photo = getResizedBitmap(photo, 1000);
                Constant.IMAGE_USER = photo;
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }

    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Exit Confirmation");
        alertDialog.setMessage("You are about to close this app, are you sure you want to close this app?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
