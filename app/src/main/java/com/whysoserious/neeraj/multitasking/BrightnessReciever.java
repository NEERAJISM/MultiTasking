package com.whysoserious.neeraj.multitasking;

import android.app.Activity;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by NEERAJ on 09-Jul-15.
 */
public class BrightnessReciever extends BroadcastReceiver {

    int curBrightnessValue;

    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(50);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);


        //audio
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL)
        {
            views.setImageViewResource(R.id.ivaudio,R.drawable.normal);
        }
        else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE)
        {
            views.setImageViewResource(R.id.ivaudio, R.drawable.vibration);
        }
        else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT)
        {
            views.setImageViewResource(R.id.ivaudio, R.drawable.silent);
        }

        ///bluetooth
        BluetoothAdapter ba = (BluetoothAdapter.getDefaultAdapter());
        if (ba.isEnabled()==true)
        {
            views.setImageViewResource(R.id.ivbluetooth, R.drawable.bluetoothon);
        }
        else if(ba.isEnabled()==false)
        {
            views.setImageViewResource(R.id.ivbluetooth, R.drawable.bluetoothoff);
        }


        //mdata
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);


        if (tm.getDataState()== TelephonyManager.DATA_CONNECTED) {
            views.setImageViewResource(R.id.ivmdata, R.drawable.mdataon);
        }
        else if(tm.getDataState()!= TelephonyManager.DATA_CONNECTED){
            views.setImageViewResource(R.id.ivmdata, R.drawable.mdataoff);
        }

        //rotation
        if(android.provider.Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1)
        {
            views.setImageViewResource(R.id.ivrotation, R.drawable.screen_rotationon);
        }
        else   if(android.provider.Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) != 1)
        {
            views.setImageViewResource(R.id.ivrotation, R.drawable.screen_rotation);
        }

        //wifi
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wm.isWifiEnabled()==false)
        {
            views.setImageViewResource(R.id.ivwifi, R.drawable.wifioff);
        }
        else if (wm.isWifiEnabled()==true)
        {
            views.setImageViewResource(R.id.ivwifi, R.drawable.wifion);
        }

        ///alarm
        String nextAlarm = Settings.System.getString(context.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED);
        if(nextAlarm!=null)
        {
            views.setTextViewText(R.id.tvalarm, nextAlarm);
        }
        else
            views.setTextViewText(R.id.tvalarm, null);

        try {
            curBrightnessValue = android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

        if(curBrightnessValue <100)
        {
            views.setImageViewResource(R.id.ivbright,R.drawable.brightness_low);
            saveBrightness(context, 100);
        }
        else if(curBrightnessValue >=100 && curBrightnessValue <180)
        {
            views.setImageViewResource(R.id.ivbright,R.drawable.brightness_mid);
            saveBrightness(context, 180);
        }
        else if(curBrightnessValue >=180 && curBrightnessValue <255)
        {
            views.setImageViewResource(R.id.ivbright,R.drawable.brightnes_high);
            saveBrightness(context, 255);
        }
        else if(curBrightnessValue == 255)
        {
            views.setImageViewResource(R.id.ivbright,R.drawable.brightness);
            saveBrightness(context, 55);
        }



        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProvider.class), views);
    }

    public static void saveBrightness(Context context, int brightness) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
        android.provider.Settings.System.putInt(resolver, "screen_brightness", brightness);
        resolver.notifyChange(uri, null);
    }
}
