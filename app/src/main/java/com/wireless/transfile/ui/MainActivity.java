package com.wireless.transfile.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wireless.transfile.R;
import com.wireless.transfile.app.AppSettings;
import com.wireless.transfile.constants.Constants;
import com.wireless.transfile.service.HTTPService;
import com.wireless.transfile.utility.Utility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button startStopButton;
    TextView ipAddress;
    Boolean buttonFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Check whether service is started or not.
        boolean isRunning = AppSettings.isServiceStarted(this);
        //Getting references of views....
        startStopButton = (Button) findViewById(R.id.startStopButton);
        ipAddress = (TextView) findViewById(R.id.ipAddressText);
        startStopButton.setOnClickListener(btnClick);
        startStopButton.performClick();
        setButtonText(isRunning);
        setInfoText(isRunning);

    }

    private View.OnClickListener btnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startStopButton: {
                    Intent intent = new Intent(MainActivity.this, HTTPService.class);
                    if (AppSettings.isServiceStarted(MainActivity.this)) {
                        stopService(intent);
                        AppSettings.setServiceStarted(MainActivity.this, false);
                        setButtonText(false);
                        setInfoText(false);
                    } else {
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        int serverPort = Integer.parseInt(pref.getString(Constants.PREF_SERVER_PORT, "" + Constants.DEFAULT_SERVER_PORT));
                        if (Utility.available(serverPort)) {
                            startService(intent);
                            AppSettings.setServiceStarted(MainActivity.this, true);
                            setButtonText(true);
                            setInfoText(true);
                        } else {
                            Toast.makeText(getApplicationContext(), "Port " + serverPort + " already in use!", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                }
            }
        }
    };

    private void setButtonText(boolean isServiceRunning) {
        ((Button) findViewById(R.id.startStopButton)).setText(getString(isServiceRunning ? R.string.stop_caption : R.string.start_caption));
    }

    private void setInfoText(boolean isServiceRunning) {
        TextView textViewLog = (TextView) findViewById(R.id.ipAddressText);
        String text = getString(R.string.log_notrunning);

        if (isServiceRunning) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            text = getString(R.string.log_running) + "\n" + getString(R.string.log_msg1) + "\n'http://" + Utility.getLocalIpAddress(1) + ":" + pref.getString(Constants.PREF_SERVER_PORT, "" + Constants.DEFAULT_SERVER_PORT) + "' " + getString(R.string.log_msg2);
        }

        textViewLog.setText(text);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}