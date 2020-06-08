package com.theeeceguy.eqresq;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class NewsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout news_one, news_two, news_three;

    Animation down;

    String smsBody = "";

    AudioManager audio;

    View v;

    String lowRangeText = "An Earthquake has occured!\n"+
            "Please don't panic.";
    String midRangeText = "An Earthquake has occured!\n" +
            "go to open space\n" +
            "if possible otherwise take cover \n" +
            "bulding won't breakdown so don't panic";
    String highRangeText = "An Earthquake has occured!\n" +
            "put on the safety band\n" +
            "take cover under something strong";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNews);
        setSupportActionBar(toolbar);

        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        try {
            smsBody = "" + getIntent().getExtras().get("smsbody").toString();
        } catch (Exception e) {
            //Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("Exception caught", e.getMessage());
        }

        news_one = (LinearLayout) findViewById(R.id.newsOne);
        news_two = (LinearLayout) findViewById(R.id.newsTwo);
        news_three = (LinearLayout) findViewById(R.id.newsThree);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        down = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_down);
        //fade_out.setAnimationListener(this);

        // Create loading to wait for few second before displaying ActivityHome
        //new Loading().execute();

        news_one.startAnimation(down);
        news_two.startAnimation(down);
        news_three.startAnimation(down);

        news_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(NewsActivity.this, NewsEventActivity.class);
                intent.putExtra("event", "1");
                startActivity(intent);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        news_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(NewsActivity.this, NewsEventActivity.class);
                intent.putExtra("event", "2");
                startActivity(intent);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        news_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(NewsActivity.this, NewsEventActivity.class);
                intent.putExtra("event", "3");
                startActivity(intent);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });


        sendNotification(smsBody);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finish();
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
            Intent i = new Intent(this, GenPrecautionsActivity.class);
            startActivity(i);
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


    public void sendNotification(String text) {
        if(text.contains("1")) {
            createNotification(v, lowRangeText);
        }
        else if(text.contains("2")) {
            createNotification(v, midRangeText);
        }
        else if(text.contains("3")) {
            createNotification(v, highRangeText);
        }
    }

    public void createNotification(View view, String msg) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, DeviceListActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        //int currentVolume = audio.getStreamVolume(AudioManager.STREAM_RING);
        int max = audio.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audio.setStreamVolume(AudioManager.STREAM_RING, max, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        r.play();


        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("Earthquake Alert!")
                .setContentText(msg)
                .setSmallIcon(R.drawable.notificationicon)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .addAction(R.drawable.helpicon, "SAVE ME!", pIntent);

    }



}
