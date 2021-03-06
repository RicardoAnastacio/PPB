package pt.ipleiria.ppb;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import pt.ipleiria.ppb.model.Game;
import pt.ipleiria.ppb.recyclerView.LineAdapter_game;


public class SearchActivity extends AppCompatActivity {

    public static final String ID_EDIT_TASK = "id_EditTask";
    public static final String ID_EDIT_TASK_GAME = "id_EditTaskGame";

    private RecyclerView recyclerView;
    private LineAdapter_game mAdapter;
    private LineAdapter_game mAdapterSearch;
    private Game game;
    private Paint p = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_icon);
        recyclerView = findViewById(R.id.recycler_view);
        setupRecycler();
        mAdapter.updateFullList();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.searchview, menu);

        final MenuItem item = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                ArrayList<Game> resultadoDaPesquisa = mAdapter.searchGame(query);

                if (resultadoDaPesquisa.isEmpty()) {
                    Toast.makeText(SearchActivity.this, getString(R.string.No_Games_Found), Toast.LENGTH_SHORT).show();
                }

                // Configurando o gerenciador de layout para ser uma lista.
                LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                recyclerView.setLayoutManager(layoutManager);

                // Adiciona o adapter que irá anexar os objetos à lista.
                // Está sendo criado com lista vazia, pois será preenchida posteriormente.
                mAdapterSearch = new LineAdapter_game(resultadoDaPesquisa);
                recyclerView.setAdapter(mAdapterSearch);

                // Configurando um dividr entre linhas, para uma melhor visualização.
                recyclerView.addItemDecoration(new DividerItemDecoration(SearchActivity.this, DividerItemDecoration.VERTICAL));
                mAdapterSearch.notifyDataSetChanged();

                return false;
            }
        });

        return true;
    }

    private void setupRecycler() {
        //RecyclerView  recyclerView1 = findViewById(R.id.recycler_view);
        // Configurando o gerenciador de layout para ser uma lista.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = new LineAdapter_game(new ArrayList<>(0));
        recyclerView.setAdapter(mAdapter);

        // Configurando um dividr entre linhas, para uma melhor visualização.
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void onClick_action_return(MenuItem item) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }




}

