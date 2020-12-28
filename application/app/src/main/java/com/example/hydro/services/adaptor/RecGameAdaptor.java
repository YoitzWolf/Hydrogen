package com.example.hydro.services.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydro.R;
import com.example.hydro.request.models.Game;
import com.example.hydro.request.models.Hub;

import java.util.List;


public class RecGameAdaptor extends RecyclerView.Adapter<RecGameAdaptor.Holder> {

    private final LayoutInflater layoutInflater;
    private final List<Game> games;

    @NonNull
    @Override
    public RecGameAdaptor.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(RecGameAdaptor.Holder.resource, parent, false);
        return new RecGameAdaptor.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Game game = this.games.get(position);
        holder.load(game);
        //holder.tvTitle.setText(hub.title);
        //holder.tvAuthor.setText(hub.author);
        //holder.tvPercent.setText(String.valueOf(hub.percent));
    }

    @Override
    public int getItemCount() {
        return this.games.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public static final int resource = R.layout.recycle_game_item;

        //-------------
        final TextView name;
        //-------------

        public Holder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
        }

        public static int getResourceId() {
            return resource;
        }

        public void load(Game game) {
            this.name.setText(game.getName());
        }
    }

    public RecGameAdaptor(Context context, List<Game> games) {
        this.layoutInflater = LayoutInflater.from(context);
        this.games = games;
    }
}
