package pt.ipleiria.ppb.recyclerView;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ipleiria.ppb.R;
import pt.ipleiria.ppb.model.Game;
import pt.ipleiria.ppb.model.SingletonPPB;

public class LineAdapter_game_Share extends RecyclerView.Adapter<LineHolder_game_Share> {

    private List<Game> mGames;

    public LineAdapter_game_Share(ArrayList games) {
        this.mGames = games;
    }

    @NonNull
    @Override
    public LineHolder_game_Share onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LineHolder_game_Share(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_game_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LineHolder_game_Share lineHolder, final int i) {
        final int position = i;
        lineHolder.gameTitle.setText(mGames.get(i).getTitle());
        lineHolder.gameDescription.setText(mGames.get(i).getDescription());
        lineHolder.view.setBackgroundColor(mGames.get(i).isSelected() ? Color.LTGRAY : Color.WHITE);

        lineHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGames.get(i).setSelected(!mGames.get(i).isSelected());
                lineHolder.view.setBackgroundColor(mGames.get(i).isSelected() ? Color.LTGRAY : Color.WHITE);

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

    public String EditItem(int position) {
        String id = mGames.get(position).getId();
        notifyItemChanged(position);
        return id;
    }

    // Método responsável por atualizar um usuário já existente na lista.
    private void updateItem(int position) {
        Game game = mGames.get(position);
        //game.setTitle("GAME_1");
        notifyItemChanged(position);
    }

    // Método responsável por remover um usuário da lista.
    public void removerItem(int position) {
        mGames.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mGames.size());
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mGames, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mGames, i, i - 1);

            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public ArrayList<Game> searchGame(String title) {
        ArrayList<Game> res = new ArrayList<>();

        for (Game g : mGames) {
            if (g.getTitle().contains(title)) {
                res.add(g);
            }
        }

        return res;
    }
}
