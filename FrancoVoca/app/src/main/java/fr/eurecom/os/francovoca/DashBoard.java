package fr.eurecom.os.francovoca;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewStub viewStub1;
    private ViewStub viewStub2;
    private ViewStub viewStub3;
    private List<Question> test;
    private Intent content_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        viewStub1 = (ViewStub) findViewById(R.id.contentView1);
        viewStub1.setLayoutResource(R.layout.content_dash_board);
        viewStub2 = (ViewStub) findViewById(R.id.contentView2);
        viewStub2.setLayoutResource(R.layout.content_competition);
        viewStub3 = (ViewStub) findViewById(R.id.contentView3);
        viewStub3.setLayoutResource(R.layout.content_unfinished);
        viewStub1.inflate();
        viewStub2.inflate();
        viewStub3.inflate();
        viewStub2.setVisibility(View.INVISIBLE);
        viewStub3.setVisibility(View.INVISIBLE);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setTitle("DashBoard");
                viewStub2.setVisibility(View.INVISIBLE);
                viewStub3.setVisibility(View.INVISIBLE);
                viewStub1.setVisibility(View.VISIBLE);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button startButton = (Button) findViewById(R.id.competition_button04);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(content_intent);
            }
        });
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
        getMenuInflater().inflate(R.menu.dash_board, menu);
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

        if (id == R.id.nav_vocabulary) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Vocabulary");
            viewStub3.setVisibility(View.VISIBLE);
            viewStub1.setVisibility(View.INVISIBLE);
            viewStub2.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_competition) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Competition");
            viewStub1.setVisibility(View.INVISIBLE);
            viewStub3.setVisibility(View.INVISIBLE);
            viewStub2.setVisibility(View.VISIBLE);
            test = loadSampleTest();
            content_intent = new Intent(getApplicationContext(), Questions.class);
            content_intent.putExtra("test", (Serializable) test);
        } else if (id == R.id.nav_culture) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Culture Everyday");
            viewStub3.setVisibility(View.VISIBLE);
            viewStub1.setVisibility(View.INVISIBLE);
            viewStub2.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_course) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Courses");
            viewStub3.setVisibility(View.VISIBLE);
            viewStub1.setVisibility(View.INVISIBLE);
            viewStub2.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_my_account) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("My Account");
            viewStub3.setVisibility(View.VISIBLE);
            viewStub1.setVisibility(View.INVISIBLE);
            viewStub2.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_shopping) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Online Shop");
            viewStub3.setVisibility(View.VISIBLE);
            viewStub1.setVisibility(View.INVISIBLE);
            viewStub2.setVisibility(View.INVISIBLE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<Question> loadSampleTest(){
        List<Question> test = new ArrayList<>(3);
        test.add(new Question("1. The meaning of Bonjour is:","A. Hello", "B. Nice to meet you", "C. Bye", "D. Good day", 4));
        test.add(new Question("2. The meaning of C'est is:","A. This is", "B. Good Morning", "C. Nice to meet you", "D. No", 1));
        test.add(new Question("3. The meaning of Qui is:","A. How", "B. When", "C. Who", "D. Whenever", 2));
        return test;
    }
}
