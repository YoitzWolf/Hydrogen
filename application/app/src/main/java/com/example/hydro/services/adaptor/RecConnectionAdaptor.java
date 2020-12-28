package com.example.hydro.services.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydro.R;
import com.example.hydro.request.models.Connection;
import com.example.hydro.request.models.Hub;

import java.util.List;
import android.os.Handler;


public class RecConnectionAdaptor extends RecyclerView.Adapter<RecConnectionAdaptor.Holder> {

    private final LayoutInflater layoutInflater;
    private final List<Connection> connections;

    @NonNull
    @Override
    public RecConnectionAdaptor.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(RecHubAdaptor.Holder.resource, parent, false);
        return new RecConnectionAdaptor.Holder(view, leave, open);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Connection connection = this.connections.get(position);
        holder.load(connection);
    }

    public Handler leave = new Handler();
    public Handler open = new Handler();

    @Override
    public int getItemCount() {
        return this.connections.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public static final int resource = R.layout.recycle_hub_item;

        //-------------
        final TextView gamename;
        final TextView name;
        final LinearLayout mainLayout;
        final LinearLayout subinfo;
        final TextView players;
        final TextView hub_description;
        final TextView game_description;
        final EditText hub_password;
        final Button close;
        final Button connect;
        private final Button selectBtn;

        private Connection connection;

        //-------------

        public Holder(@NonNull View itemView, Handler leave, Handler open) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.gamename = itemView.findViewById(R.id.game);
            this.mainLayout = itemView.findViewById(R.id.mainLayout);
            this.subinfo = itemView.findViewById(R.id.subinfo);
            subinfo.setVisibility(View.GONE);
            this.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subinfo.setVisibility(View.VISIBLE);
                }
            });



            this.players = itemView.findViewById(R.id.players);
            this.hub_description = itemView.findViewById(R.id.hub_description);
            this.game_description = itemView.findViewById(R.id.game_description);
            this.hub_password = itemView.findViewById(R.id.hub_password);

            this.close = itemView.findViewById(R.id.close);
            this.connect = itemView.findViewById(R.id.connect);
            
            this.selectBtn = itemView.findViewById(R.id.select);

            selectBtn.setVisibility(View.VISIBLE);

            selectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open.sendEmptyMessage(connection.id);
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subinfo.setVisibility(View.GONE);
                }
            });

            connect.setText(R.string.leave_hub_words);
            connect.setBackgroundResource(R.drawable.typical_err_button_selector);
            connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leave.sendEmptyMessage(connection.id);
                }
            });

        }

        public static int getResourceId() {
            return resource;
        }

        public void load(Connection connection) {
            this.connection = connection;
            Hub hub = connection.hub;
            this.name.setText(hub.getName());
            this.gamename.setText(R.string.game_word);
            this.gamename.setText(this.gamename.getText() + " : " + hub.game.name);

            this.players.setText(Integer.toString(hub.players) + " / " + Integer.toString(hub.max_players));

            this.hub_description.setText(hub.description);
            this.game_description.setText(hub.game.description);
        }
    }

    public RecConnectionAdaptor(Context context, List<Connection> hubs, Handler leave, Handler open) {
        this.layoutInflater = LayoutInflater.from(context);
        this.connections = hubs;
        this.leave = leave;
        this.open = open;
    }
}
