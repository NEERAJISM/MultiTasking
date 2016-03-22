package com.whysoserious.neeraj.multitasking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++)
        {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDisabled(Context context) {

    }



    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        /////flash
        Intent flashreceiver = new Intent(context, FlashReciever.class);
        flashreceiver.setAction("COM_FLASHLIGHT");
        flashreceiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);

        ////wifi
        Intent wifireceiver = new Intent(context, WifiReciever.class);
        wifireceiver.setAction("COM_WIFI");
        wifireceiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);

        ///mobile data
        Intent mdatareceiver = new Intent(context, MdataReciever.class);
        wifireceiver.setAction("COM_MDATA");
        wifireceiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);

        ///BLUETOOTH
        Intent bluetoothreceiver = new Intent(context, BluetoothReciever.class);
        bluetoothreceiver.setAction("COM_BLUETOOTH");
        bluetoothreceiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);

        ///audio
        Intent audioreceiver = new Intent(context, AudioReciever.class);
        audioreceiver.setAction("COM_AUDIO");
        audioreceiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);


        ///SETTINGS
        Intent settingsreciever = new Intent(context, SettingsReciever.class);
        audioreceiver.setAction("COM_SETTINGS");
        audioreceiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);

        ///DSETTINGS
        Intent datesettingsreciever = new Intent(context, DatesettingsReciever.class);
        datesettingsreciever.setAction("COM_DSETTINGS");
        datesettingsreciever.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);


        ///BRIGHTNESS
        Intent brightnessreceiver = new Intent(context, BrightnessReciever.class);
        brightnessreceiver.setAction("COM_BRIGHTNESS");
        brightnessreceiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);


        ///Alarm App
        Intent alarmsettingsreceiver = new Intent(context, AlarmSettingsReciever.class);
        alarmsettingsreceiver.setAction("COM_ALARMSETTINGS");
        alarmsettingsreceiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);

        ///rotATOION
        Intent rotationreceiver = new Intent(context,RotationReciever.class);
        rotationreceiver.setAction("COM_ROTATION");
        rotationreceiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);

        ///calender Activity
        ///rotATOION
        Intent calenderreceiver = new Intent(context,Calendersettings.class);
        calenderreceiver.setAction("COM_CALENDER");
        calenderreceiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);





        ///broadcast
        PendingIntent pendingIntentflash = PendingIntent.getBroadcast(context, 0, flashreceiver, 0);
        PendingIntent pendingIntentwifi = PendingIntent.getBroadcast(context, 0, wifireceiver, 0);
        PendingIntent pendingIntentmdata = PendingIntent.getBroadcast(context, 0, mdatareceiver, 0);
        PendingIntent pendingIntentbluetooth = PendingIntent.getBroadcast(context, 0, bluetoothreceiver, 0);
        PendingIntent pendingIntentaudio = PendingIntent.getBroadcast(context, 0, audioreceiver, 0);
        PendingIntent pendingIntentsettings = PendingIntent.getBroadcast(context, 0,settingsreciever, 0);
        PendingIntent pendingIntentclocksettings = PendingIntent.getBroadcast(context, 0, datesettingsreciever, 0);
        PendingIntent pendingIntentbright = PendingIntent.getBroadcast(context, 0, brightnessreceiver, 0);
        PendingIntent pendingIntentasettings = PendingIntent.getBroadcast(context, 0, alarmsettingsreceiver, 0);
        PendingIntent pendingIntentarotation = PendingIntent.getBroadcast(context, 0, rotationreceiver, 0);
        PendingIntent pendingIntentcalender = PendingIntent.getBroadcast(context, 0, calenderreceiver, 0);



        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);


        ///Battery
        Intent received = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        String receivedAction = received.getAction();
        if (receivedAction.equals(Intent.ACTION_BATTERY_CHANGED))
        {
            String nextAlarm = Settings.System.getString(context.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED);
            if(nextAlarm!=null)
            {
                views.setViewVisibility(R.id.linearalarm, View.VISIBLE);
                views.setTextViewText(R.id.tvalarm, nextAlarm);
            }
            ComponentName appComponent = new ComponentName(context,WidgetProvider.class);
            AppWidgetManager.getInstance(context).updateAppWidget(appComponent,views);
        }


        ///alarm
        String nextAlarm = Settings.System.getString(context.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED);
        if(nextAlarm!=null)
        {
            views.setViewVisibility(R.id.linearalarm, View.VISIBLE);
            views.setTextViewText(R.id.tvalarm, nextAlarm);
        }

        //audio
        AudioManager  audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
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
        if (ba.isEnabled())
        {
            views.setImageViewResource(R.id.ivbluetooth, R.drawable.bluetoothon);
        }
        else {
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
        else {
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

        if (!wm.isWifiEnabled())
        {
            views.setImageViewResource(R.id.ivwifi, R.drawable.wifioff);
        }
        else
            views.setImageViewResource(R.id.ivwifi, R.drawable.wifion);


        views.setOnClickPendingIntent(R.id.ivflash, pendingIntentflash);
        views.setOnClickPendingIntent(R.id.ivwifi, pendingIntentwifi);
        views.setOnClickPendingIntent(R.id.ivmdata, pendingIntentmdata);
        views.setOnClickPendingIntent(R.id.ivbluetooth, pendingIntentbluetooth);
        views.setOnClickPendingIntent(R.id.ivaudio, pendingIntentaudio);
        views.setOnClickPendingIntent(R.id.ivbright, pendingIntentbright);
        views.setOnClickPendingIntent(R.id.ivsettings, pendingIntentsettings);
        views.setOnClickPendingIntent(R.id.linearclock,pendingIntentclocksettings);
        views.setOnClickPendingIntent(R.id.linearalarm,pendingIntentasettings);
        views.setOnClickPendingIntent(R.id.ivrotation,pendingIntentarotation);
        views.setOnClickPendingIntent(R.id.linearcalender,pendingIntentcalender);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}