package pt.ipleiria.ppb;

import android.content.Context;
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
import android.support.design.widget.FloatingActionButton;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pt.ipleiria.ppb.model.Game;
import pt.ipleiria.ppb.model.SingletonPPB;
import pt.ipleiria.ppb.recyclerView.LineAdapter_task;

public class GameActivity extends AppCompatActivity {

    public static final String ID_EDIT_TASK = "id_EditTask";
    public static final String ID_EDIT_TASK_GAME = "id_EditTaskGame";
    public static final String ID_EDIT_GAME = "id_EditGame";
    public static final String ID_ADD_TASK = "id_addTask";
    public static final String ID_VIEW_GAME = "id_viewGame";

    private SingletonPPB PPB;
    private RecyclerView recyclerView;
    private LineAdapter_task mAdapter;
    private Paint p = new Paint();
    private boolean editing;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        PPB = SingletonPPB.getInstance();
        editing = false;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_icon);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_task);

        recyclerView = findViewById(R.id.recycler_view);
        setupRecycler();
        initSwipe();
        EditText etTitle = findViewById(R.id.game_Title);
        EditText etDescription = findViewById(R.id.game_Description);
        EditText etAuthor = findViewById(R.id.game_Author);
        EditText etDuration = findViewById(R.id.game_Duration);
        TextView etId = findViewById(R.id.game_Id);
        TextView etDate = findViewById(R.id.game_Update);

        View vid = findViewById(R.id.id);
        vid.setVisibility(View.INVISIBLE);
        View vgame_id = findViewById(R.id.game_Id);
        vgame_id.setVisibility(View.INVISIBLE);
        View vdate = findViewById(R.id.date);
        vdate.setVisibility(View.INVISIBLE);
        View vgame_Update = findViewById(R.id.game_Update);
        vgame_Update.setVisibility(View.INVISIBLE);

        View vfab = findViewById(R.id.add_task);
        View vbtAddgame = findViewById(R.id.btn_add_game);
        vfab.setVisibility(View.INVISIBLE);
        View include_gametask = findViewById(R.id.include_gametask);
        include_gametask.setVisibility(View.VISIBLE);

        Intent i = getIntent();

        if (i.getStringExtra(ID_VIEW_GAME) != null) {
            editing = false;
            vbtAddgame.setVisibility(View.INVISIBLE);
            vfab.setVisibility(View.VISIBLE);

            vid.setVisibility(View.VISIBLE);
            vgame_id.setVisibility(View.VISIBLE);
            vdate.setVisibility(View.VISIBLE);
            vgame_Update.setVisibility(View.VISIBLE);

            final String id = i.getStringExtra(ID_VIEW_GAME);
            game = PPB.containsID(id);

            mAdapter.updateFullList(game);

            etTitle.setHint(game.getTitle());
            etTitle.setEnabled(false);
            etDescription.setHint(game.getDescription());
            etDescription.setEnabled(false);
            etAuthor.setHint(game.getAuthor());
            etAuthor.setEnabled(false);
            etDuration.setHint("" + game.getDurationGame());
            etDuration.setEnabled(false);
            etId.setText("" + game.getId());
            etDate.setText(game.getLastUpdate());

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(GameActivity.this, TaskActivity.class);
                    intent.putExtra(ID_ADD_TASK, game.getId());
                    startActivity(intent);
                }
            });
        }

        if (i.getStringExtra(ID_EDIT_GAME) != null) {
            editing = true;
            include_gametask.setVisibility(View.INVISIBLE);
            vbtAddgame.setVisibility(View.VISIBLE);
            vfab.setVisibility(View.INVISIBLE);
            vid.setVisibility(View.VISIBLE);
            vgame_id.setVisibility(View.VISIBLE);
            vdate.setVisibility(View.VISIBLE);
            vgame_Update.setVisibility(View.VISIBLE);

            Button btnAddgame = findViewById(R.id.btn_add_game);
            final String id = i.getStringExtra(ID_EDIT_GAME);
            game = PPB.containsID(id);

            mAdapter.updateFullList(game);

            etTitle.setText(game.getTitle());
            etDescription.setText(game.getDescription());
            etAuthor.setText(game.getAuthor());
            etDuration.setText("" + game.getDurationGame());
            etId.setText("" + game.getId());
            etDate.setText(game.getLastUpdate());

            btnAddgame.setText(getString(R.string.Edit_Game_btn));

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            FileOutputStream fileOutputStream =
                    openFileOutput("game.bin", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(SingletonPPB.getInstance().getGames());
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(GameActivity.this, getString(R.string.Error_write_Game_to_internal), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (game != null) {
            mAdapter.updateFullList(game);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.upbar, menu);
        return true;
    }

    public void onClick_action_return(MenuItem item) {
        onBackPressed();
    }

    private void setupRecycler() {

        // Configurando o gerenciador de layout para ser uma lista.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = new LineAdapter_task(new ArrayList<>(0));
        recyclerView.setAdapter(mAdapter);

        // Configurando um dividr entre linhas, para uma melhor visualização.
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void onClick_btn_add_game(View view) {

        EditText etTitle = findViewById(R.id.game_Title);
        EditText etDescription = findViewById(R.id.game_Description);
        EditText etAuthor = findViewById(R.id.game_Author);
        EditText etDuration = findViewById(R.id.game_Duration);

        if (etTitle.getText().toString().isEmpty()) {
            etTitle.setError(getString(R.string.Invalid_Title));
        }
        if (etDescription.getText().toString().isEmpty()) {
            etDescription.setError(getString(R.string.Invalid_Description));
        }
        if (etAuthor.getText().toString().isEmpty()) {
            etAuthor.setError(getString(R.string.Invalid_Author_Name));
        }
        if (etDuration.getText().toString().isEmpty()) {
            etDuration.setError(getString(R.string.Invalid_Time_Duration));
        }
        if (!etTitle.getText().toString().isEmpty() && !etDescription.getText().toString().isEmpty() && !etAuthor.getText().toString().isEmpty() && !etDuration.getText().toString().isEmpty()) {
            // obter o texto do title
            String title = etTitle.getText().toString();
            // obter o texto do descricao
            String description = etDescription.getText().toString();
            // obter o texto do Author
            String Author = etAuthor.getText().toString();
            // obter o duracao minutos
            String durationText = etDuration.getText().toString();
            int duration = Integer.parseInt(durationText.trim());

            if (editing && game != null) {
                game.setTitle(title);
                game.setDescription(description);
                game.setAuthor(Author);
                game.setDurationGame(duration);
                game.setLastUpdate(getDateString());

                Toast.makeText(GameActivity.this, getString(R.string.Edit_Game_Complete), Toast.LENGTH_SHORT).show();
                editing = false;

            } else {
                // criar game
                PPB = SingletonPPB.getInstance();
                Game game = new Game(title, description, Author, duration);
                PPB.getGames().add(game);

                Toast.makeText(GameActivity.this, getString(R.string.Add_Game_Complete), Toast.LENGTH_SHORT).show();
            }
            // Check if no view has focus:  // use remove keyboard front view
            view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            finish();
        }
    }

    private void initSwipe() {

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            View recyclerview = recyclerView.findViewById(R.id.recycler_view);

            int dragFrom = -1;
            int dragTo = -1;

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                if (dragFrom == -1) {
                    dragFrom = fromPosition;
                }
                dragTo = toPosition;

                mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            private void reallyMoved(int from, int to) {
                // I guessed this was what you want...
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
                    reallyMoved(dragFrom, dragTo);
                }
                dragFrom = dragTo = -1;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(GameActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(GameActivity.this);
                    }
                    builder.setTitle(R.string.delete_task)
                            .setMessage(R.string.delete_task_mensage)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    mAdapter.removerItem(position);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mAdapter.notifyItemChanged(position);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show().setCanceledOnTouchOutside(false);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    Intent intent = new Intent(GameActivity.this, TaskActivity.class);
                    intent.putExtra(ID_EDIT_TASK, mAdapter.EditItem(position));
                    intent.putExtra(ID_EDIT_TASK_GAME, game.getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor(getString(R.string.Green_color)));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_edit);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor(getString(R.string.Red_color)));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    public String getDateString() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        return date;
    }

    public Date getDate() {
        Date date = Calendar.getInstance().getTime();
        return date;
    }
}
