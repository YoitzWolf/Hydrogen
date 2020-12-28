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
import com.example.hydro.explorer.Explorer;
import com.example.hydro.request.models.Hub;

import java.util.List;
import android.os.Handler;


public class RecHubAdaptor extends RecyclerView.Adapter<RecHubAdaptor.Holder> {

    private final LayoutInflater layoutInflater;
    private final List<Hub> hubs;

    @NonNull
    @Override
    public RecHubAdaptor.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(RecHubAdaptor.Holder.resource, parent, false);
        return new RecHubAdaptor.Holder(view, onclick);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Hub hub = this.hubs.get(position);
        holder.load(hub);
    }

    public Handler onclick = new Handler();

    @Override
    public int getItemCount() {
        return this.hubs.size();
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

        private Hub hub;

        //-------------

        public Holder(@NonNull View itemView, Handler onclick) {
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

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subinfo.setVisibility(View.GONE);
                }
            });

            connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(hub.is_public || hub_password.getTextSize() != 0) {
                        Explorer.memory.buffer.put("hub_password", hub_password.getText().toString());
                        onclick.sendEmptyMessage(hub.id);
                    }
                }
            });

        }

        public static int getResourceId() {
            return resource;
        }

        public void clear(){
            if(! hub.is_public){
                mainLayout.setBackgroundResource(R.drawable.hub_private_bg);
            } else{
                mainLayout.setBackgroundResource(R.drawable.hub_public_bg);
            }
        }

        public void load(Hub hub) {
            this.hub = hub;
            this.name.setText(hub.getName());
            this.gamename.setText(R.string.game_word);
            this.gamename.setText(this.gamename.getText() + " : " + hub.game.name);

            this.players.setText(Integer.toString(hub.players) + " / " + Integer.toString(hub.max_players));

            if(! hub.is_public){
                this.hub_password.setVisibility(View.VISIBLE);
                this.mainLayout.setBackgroundResource(R.drawable.hub_private_bg);
            } else{
                this.hub_password.setVisibility(View.GONE);
                this.mainLayout.setBackgroundResource(R.drawable.hub_public_bg);
            }
            this.hub_description.setText(hub.description);
            this.game_description.setText(hub.game.description);
        }
    }

    public RecHubAdaptor(Context context, List<Hub> hubs, Handler onclick) {
        this.layoutInflater = LayoutInflater.from(context);
        this.hubs = hubs;
        this.onclick = onclick;
    }
}
