package com.example.hydro.services.adaptor;

import android.content.Context;
import android.util.Log;
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
import com.example.hydro.request.models.Connection;
import com.example.hydro.request.models.Hub;

import java.util.List;
import android.os.Handler;


public class RecButtonAdaptor extends RecyclerView.Adapter<RecButtonAdaptor.Holder> {

    private final LayoutInflater layoutInflater;
    private final List<String> names;

    Handler press;

    @NonNull
    @Override
    public RecButtonAdaptor.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  layoutInflater.inflate(RecHubAdaptor.Holder.resource, parent, false);
        return new RecButtonAdaptor.Holder(view, press);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String btn = this.names.get(position);
        holder.load(btn, position);
    }

    @Override
    public int getItemCount() {
        return this.names.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public static final int resource = R.layout.toolbar_button_unit;

        public Button btn;
        public int myid;

        //-------------

        public Holder(@NonNull View itemView, Handler press) {
            super(itemView);

            this.btn = itemView.findViewById(R.id.btn);
            this.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    press.sendEmptyMessage(myid);
                }
            });
        }

        public static int getResourceId() {
            return resource;
        }

        public void load(String name, int pos) {
            Log.i("BTN", "LOAD");
            this.myid = pos;
            btn.setText(name);
        }
    }

    public RecButtonAdaptor(Context context, List<String> buttons, Handler press) {
        this.press = press;
        this.layoutInflater = LayoutInflater.from(context);
        this.names = buttons;
    }
}
