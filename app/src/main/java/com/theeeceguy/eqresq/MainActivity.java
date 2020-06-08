package com.theeeceguy.eqresq;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText receiverCountry;
    EditText myCountry;
    EditText amount;

    String rc = "";
    String spinnerText = "";
    String mc = "";
    String am = "";

    String givenDate = "Mon, 4 Jan 2016, 05:05";

    String smsBody = "";

    //Button show_info;

    String date;

    View viewNew;

    int marker = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            smsBody = getIntent().getExtras().get("smsbody").toString();
            //Toast.makeText(getBaseContext(), smsBody, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("Exception caught", e.getMessage());
        }

        receiverCountry = (EditText) findViewById(R.id.rCountry);
        myCountry = (EditText) findViewById(R.id.myCountry);
        amount = (EditText) findViewById(R.id.amount);

        newtimer.start();

        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
        String[] items = new String[]{"04/01/2016 Dhaka, Bangladesh", "20/04/2017 Hokkaido, Japan", "18/04/2017 Mastuj, Pakistan"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        spinnerText = dropdown.getSelectedItem().toString();

        FloatingActionButton fabBtn = (FloatingActionButton) findViewById(R.id.fab);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rc= receiverCountry.getText().toString();
                    mc = myCountry.getText().toString();
                    am = amount.getText().toString();
                    Toast.makeText(getBaseContext(), "Message Sent!"+"\nReceiver Country: "+rc+"\nEvent: "+spinnerText+"\nMy Country: "+mc+"\nAmount: "+am, Toast.LENGTH_LONG).show();
                    //SmsManager sms = SmsManager.getDefault();
                    //sms.sendTextMessage(mobileNumber, null, textMessage, null, null);
                    //Snackbar.make(view, "Message Sent", Snackbar.LENGTH_LONG)
                           // .setAction("Action", null).show();
                    //backdoor
                    if(am.equals("100")) {
                        createNotification(v);
                    }
                } catch (Exception e) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    AlertDialog dialog = alertDialogBuilder.create();
                    dialog.setMessage(e.getMessage());
                    dialog.show();
                }
                   // createNotification(v);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        if (id == R.id.nav_us) {
            // Handle the camera action
            //mwebView.loadUrl("http://www.newsweek.com/us");
        } else if (id == R.id.nav_world) {
            //mwebView.loadUrl("http://www.newsweek.com/world");
        } else if (id == R.id.nav_tech) {
            //mwebView.loadUrl("http://www.newsweek.com/tech-science");
        } else if (id == R.id.nav_sports) {
            //mwebView.loadUrl("http://www.newsweek.com/sports");
        } else if (id == R.id.nav_share) {
            //mwebView.loadUrl("http://www.newsweek.com/about-newsweek");
        } else if (id == R.id.nav_send) {
            //mwebView.loadUrl("http://www.newsweek.com/contact");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void createNotification(View view) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, DeviceListActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("Earthquake Alert!")
                .setContentText("A 6.7 magnitude earthquake struck at 5:05am Bangladesh Standard Time.\n" +
                        "\n" +
                        "Its epicentre lay 29 kilometres west of Imphal, India and 352 kilometres east-northeast of Dhaka. Please be alert and press Save Me! TO LET US KNOW YOUR CURRENT LOCATION.")
                .setSmallIcon(R.drawable.notificationicon)
                .addAction(R.drawable.helpicon, "SAVE ME!", pIntent);

        //Toast.makeText(getBaseContext(), "Location and Heart Pulse Sent!", Toast.LENGTH_LONG).show();

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        builder.setVibrate(pattern);
        //builder.setStyle(new NotificationCompat.InboxStyle());
// Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

        // Build notification
        // Actions are just fake
//        Notification noti = new Notification.Builder(this)
//                .setContentTitle("New mail from " + "test@gmail.com")
//                .setContentText("Subject").setSmallIcon(R.drawable.notificationicon)
//                .setContentIntent(pIntent)
//                .addAction(R.drawable.helpicon, "SAVE ME!", pIntent).build();
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        // hide the notification after its selected
//        noti.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        notificationManager.notify(0, noti);

    }

    CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {

        public void onTick(long millisUntilFinished) {
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            date = df.format(Calendar.getInstance().getTime());
            //Toast.makeText(getBaseContext(), date, Toast.LENGTH_SHORT).show();
            if(date.equals(givenDate) && marker == 1) {
                createNotification(viewNew);
                marker = 0;
            }
        }
        public void onFinish() {

        }
    };


}




