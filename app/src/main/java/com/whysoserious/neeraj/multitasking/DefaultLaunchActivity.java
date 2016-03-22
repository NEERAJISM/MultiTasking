package com.whysoserious.neeraj.multitasking;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

/**
 * Created by NEERAJ on 15-Jul-15.
 */
public class DefaultLaunchActivity extends Activity {

    ViewFlipper vf;
    ImageView ivwidgetsettings,ivnexus5,ivtablet;
    Button launch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_xml);

        vf = (ViewFlipper) findViewById(R.id.viewflipper);
        ivwidgetsettings = (ImageView)findViewById(R.id.widgetsettings);
        ivnexus5 = (ImageView)findViewById(R.id.nexus5);
        ivtablet = (ImageView) findViewById(R.id.tablet);

        launch = (Button) findViewById(R.id.blaunchwidget);

        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
                startActivity(pickIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
