package com.whysoserious.neeraj.multitasking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;

/**
 * Created by NEERAJ on 09-Jul-15.
 */
public class DatesettingsReciever extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(80);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        ///alarm
        String nextAlarm = Settings.System.getString(context.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED);
        if(nextAlarm!=null)
        {
            views.setTextViewText(R.id.tvalarm, nextAlarm);
        }
        else
            views.setTextViewText(R.id.tvalarm, null);


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

        ///brightness

        int curBrightnessValue = 0;

        try {
            curBrightnessValue = android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(curBrightnessValue <100)
        {
            views.setImageViewResource(R.id.ivbright, R.drawable.brightness_low);
        }
        else if(curBrightnessValue >=100 && curBrightnessValue <180)
        {
            views.setImageViewResource(R.id.ivbright, R.drawable.brightness_mid);
        }
        else if(curBrightnessValue >=180 && curBrightnessValue <255)
        {
            views.setImageViewResource(R.id.ivbright, R.drawable.brightnes_high);
        }
        else if(curBrightnessValue == 255)
        {
            views.setImageViewResource(R.id.ivbright, R.drawable.brightness);
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

        PendingIntent pendingIntentdate = PendingIntent.getActivity(context, 0, new Intent(Settings.ACTION_DATE_SETTINGS), 0);
        try {
            pendingIntentdate.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProvider.class), views);
    }
}
