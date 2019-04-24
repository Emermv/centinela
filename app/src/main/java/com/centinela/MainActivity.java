package com.centinela;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.centinela.fragments.Camera2BasicFragment;
import com.centinela.fragments.Home;
import com.centinela.fragments.Report;
import com.centinela.fragments.Settings;
import com.centinela.fragments.Trackme;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Home.OnFragmentInteractionListener, Report.OnFragmentInteractionListener,
        Trackme.OnFragmentInteractionListener,Settings.OnFragmentInteractionListener {
      SimpleDraweeView avatar;
    TextView user_name,user_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
      */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fragmentManager=getSupportFragmentManager();

           fragmentManager.beginTransaction().replace(R.id.scope, Camera2BasicFragment.newInstance(null)).commit();

        View header = navigationView.getHeaderView(0);
        avatar=header.findViewById(R.id.user_avatar);
        user_name=header.findViewById(R.id.user_name);
        user_user=header.findViewById(R.id.user_user);
        setUserSettings();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager=getSupportFragmentManager();
        if (id == R.id.nav_camera) {
            fragmentManager.beginTransaction().replace(R.id.scope, Camera2BasicFragment.newInstance("XD XD XD")).commit();

        } else if (id == R.id.nav_help) {
            fragmentManager.beginTransaction().replace(R.id.scope,new Report()).commit();
        } else if (id == R.id.nav_trackme) {
            fragmentManager.beginTransaction().replace(R.id.scope,new Home()).commit();
           // fragmentManager.beginTransaction().replace(R.id.scope,new Trackme()).commit();
        } else if (id == R.id.nav_manage) {
            fragmentManager.beginTransaction().replace(R.id.scope,new Settings()).commit();
        } else if (id == R.id.nav_share) {
            Intent intentInvite = new Intent(Intent.ACTION_SEND);
            intentInvite.setType("text/plain");
            String body = "Utilza centinela para reportar crímenes en tiempo real! XD https://advisorsystem.net/centinela.apk ";
            String subject = "Utilza centinela para reportar crímenes en tiempo real! XD";
            intentInvite.putExtra(Intent.EXTRA_SUBJECT, subject);
            intentInvite.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(intentInvite, "Compartir utilizando"));
        }else if(id==R.id.nav_exit){
            deleteSharedPreferences( "settings" );
           /* SharedPreferences settings = getSharedPreferences("settings", Context.MODE_MULTI_PROCESS);
            settings.edit().clear();*/
            Intent intent=new Intent( this,LoginActivity.class );
            startActivity( intent );
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    private void setUserSettings(){
        SharedPreferences prefs =
                getSharedPreferences("settings", Context.MODE_MULTI_PROCESS);

         if(prefs!=null){
             Uri uri = Uri.parse(prefs.getString( "avatar" ,""));
             int overlayColor = getResources().getColor(R.color.colorPrimaryDark);
             RoundingParams roundingParams = RoundingParams.fromCornersRadius(7f);
             roundingParams.setRoundAsCircle( true );
             avatar.setHierarchy(new GenericDraweeHierarchyBuilder(getResources())
                     .setRoundingParams(roundingParams)
                     .build());
             ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                     .setResizeOptions(new ResizeOptions(50, 50))
                     .build();
             avatar.setController(
                     Fresco.newDraweeControllerBuilder()
                             .setOldController(avatar.getController())
                             .setImageRequest(request)
                             .build());

             //avatar.setImageURI(uri);
             user_name.setText(prefs.getString("name","Centinela"));
             user_user.setText(prefs.getString("user","android"));
         }else{
             Log.d("SETTINGS","preferences is null");
             Log.d("SETTINGS",prefs.toString());
         }
    }
}
