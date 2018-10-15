package pt.ipleiria.ppb.recyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ipleiria.ppb.R;
import pt.ipleiria.ppb.model.Game;
import pt.ipleiria.ppb.model.SingletonPPB;

/// pode ser public '???
public class LineAdapter_game extends RecyclerView.Adapter<LineHolder_game> {

    private List<Game> mGames;

    public LineAdapter_game(ArrayList games) {
        this.mGames = games;
    }

    @NonNull
    @Override
    public LineHolder_game onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LineHolder_game(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_game_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineHolder_game lineHolder, int i) {
        final int position = i;
        lineHolder.gameTitle.setText(mGames.get(i).getTitle());
        lineHolder.gameDescription.setText(mGames.get(i).getDescription());

        lineHolder.imageBtnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Delete pos: " + position,
                        Toast.LENGTH_SHORT).show();
                removerItem(position);
            }
        });

        lineHolder.imageBtnEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Edit pos: " + position,
                        Toast.LENGTH_SHORT).show();
                removerItem(position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mGames != null ? mGames.size() : 0;
    }


    public void updateList(Game game) {
        insertItem(game);
    }

    public void updateFullList() {
        mGames = SingletonPPB.getInstance().getGames();
        notifyDataSetChanged();
    }

    private void insertItem(Game game) {
        mGames.add(game);
        notifyItemInserted(getItemCount());
    }

    // Método responsável por atualizar um usuário já existente na lista.
    private void updateItem(int position) {
        Game game = mGames.get(position);
        //game.setTitle("GAME_1");
        notifyItemChanged(position);
    }

    // Método responsável por remover um usuário da lista.
    private void removerItem(int position) {
        mGames.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mGames.size());
    }


}