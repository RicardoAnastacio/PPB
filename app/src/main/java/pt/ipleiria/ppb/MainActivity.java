package pt.ipleiria.ppb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import pt.ipleiria.ppb.model.Game;
import pt.ipleiria.ppb.model.SingletonPPB;
import pt.ipleiria.ppb.recyclerView.LineAdapter_game;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SingletonPPB PPB;
    private RecyclerView recyclerView;
    private LineAdapter_game mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_game);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Game game = new Game("ds","dsds", "er",12);
                SingletonPPB.getInstance().getGames().add(game);
                mAdapter.updateFullList();

                Snackbar.make(view, "Add Game", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        setupRecycler();
        PPB = SingletonPPB.getInstance();

        Game game1 = new Game("ds","dsds", "er",12);
        Game game2 = new Game("easd","sdfgg", "sdfsdf23",15);
        Game game3 = new Game("cc","dsgfdsds", "234",16);
        PPB.getGames().add(game1);
        PPB.getGames().add(game2);
        PPB.getGames().add(game3);


        mAdapter.updateList(game1);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.updateFullList();
    }

    private void setupRecycler() {

        // Configurando o gerenciador de layout para ser uma lista.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = new LineAdapter_game(new ArrayList<>(0));
        recyclerView.setAdapter(mAdapter);


        // Configurando um dividr entre linhas, para uma melhor visualização.
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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

        if (id == R.id.nav_camera) {
            goGame();
        } else if (id == R.id.nav_gallery) {
            goTask();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void goGame(){
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }
    private void goTask(){
        Intent intent = new Intent(MainActivity.this, TaskActivity.class);
        startActivity(intent);
    }

}

