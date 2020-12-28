package com.example.hydro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydro.explorer.Explorer;
import com.example.hydro.request.RequestRetrofitMaster;
import com.example.hydro.services.adaptor.RecConnectionAdaptor;
import com.example.hydro.services.adaptor.RecGameAdaptor;
import com.example.hydro.services.adaptor.RecHubAdaptor;
import com.example.hydro.views.LogoProgressBar;

public class ActMain extends AppCompatActivity {

    public final Handler AUTH_OK = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            openUi();
            loadAccaunt();
            openAccountTab();
        }

    };

    private Intent intent;
    public final Handler AUTH_ERROR = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            startActivity(intent);
            openUi();
            loadAccaunt();
            openAccountTab();
        }
    };

    RequestRetrofitMaster master = null;
    Explorer explorer = null;

    public Intent editIntent;

    final Context context = this;
    private LinearLayout ui;
    private LinearLayout progressbar;
    private View accauntUi;
    private View hubsUi;
    private View myHubsUi;
    private View gamesUi;

    private TextView accauntUi_login;
    private TextView accauntUi_email;

    private Button accBtn;
    private Button hubsBtn;
    private Button myHubsBtn;
    private Button gamesBtn;
    private Button logOut;

    private Button newHubBtn;

    private RecHubAdaptor HubAdapter;
    private RecConnectionAdaptor myHubAdapter;
    private RecGameAdaptor GameAdapter;
    private RecyclerView recyclerHubView;
    private RecyclerView recyclerMyHubView;
    private RecyclerView recyclerGameView;

    public void toast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    private void initIds() {
        this.ui = this.findViewById(R.id.authorizedUI);
        this.progressbar = this.findViewById(R.id.loading);
        this.accauntUi = this.findViewById(R.id.AccauntUi);
        this.hubsUi = this.findViewById(R.id.HubsUi);
        this.myHubsUi = this.findViewById(R.id.MyHubsUi);
        this.gamesUi = this.findViewById(R.id.GamesUi);

        LogoProgressBar v = findViewById(R.id.logo);
        v.start(this);

        this.accBtn = this.findViewById(R.id.bar_acc_btn);
        this.hubsBtn = this.findViewById(R.id.bar_hubs_btn);
        this.myHubsBtn = this.findViewById(R.id.bar_my_hubs_btn);
        this.gamesBtn = this.findViewById(R.id.bar_games_btn);
        this.logOut = this.findViewById(R.id.logOut);


        this.newHubBtn = this.findViewById(R.id.new_hub);

        this.accauntUi_login = this.findViewById(R.id.AccauntUi_login);
        this.accauntUi_email = this.findViewById(R.id.AccauntUi_email);

    }


    private void openUi() {
        // set UI visible
        ui.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
    }

    private void closeUi() {
        // set UI GONE
        ui.setVisibility(View.GONE);
        progressbar.setVisibility(View.VISIBLE);
    }

    private void closeAll() {
        this.accauntUi.setVisibility(View.GONE);
        this.accBtn.setSelected(false);

        this.hubsUi.setVisibility(View.GONE);
        this.hubsBtn.setSelected(false);

        this.myHubsUi.setVisibility(View.GONE);
        this.myHubsBtn.setSelected(false);

        this.gamesUi.setVisibility(View.GONE);
        this.gamesBtn.setSelected(false);
    }

    private void loadAccaunt() throws NullPointerException {
        if (Explorer.memory.getUser() != null) {
            return;
        }

        if (Explorer.memory.getAuthToken() == null) {
            return;
        }

        this.master.getMe(AUTH_OK, AUTH_ERROR, Explorer.memory.getAuthToken().getToken());
    }

    private void openHubsTab() {
        closeUi();
        this.master.getAllHubs(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                openUi();
                closeAll();
                hubsBtn.setSelected(true);
                hubsUi.setVisibility(View.VISIBLE);

                HubAdapter.notifyDataSetChanged();
                recyclerHubView.setAdapter(HubAdapter);
            }
        });


    }

    private void setConnectionAdaptor(){
        this.myHubAdapter = new RecConnectionAdaptor(this,
                Explorer.memory.conn_list,
                new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        int id = msg.what;
                        Log.i("LEAVE", Integer.toString(id));
                    }
                },
                new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        int id = msg.what;
                        Log.i("OPEN_HUB", Integer.toString(id));
                        Explorer.memory.selectedConnectionId = id;
                        Explorer.memory.conn_list.clear();
                        Explorer.memory.games_list.clear();
                        Explorer.memory.hubs_list.clear();
                        startActivity(editIntent);
                    }
                }
        );
        recyclerMyHubView = findViewById(R.id.recycle_my_hub_view);
        this.myHubAdapter.notifyDataSetChanged();
        recyclerMyHubView.setAdapter(this.myHubAdapter);
    }

    private void openMyHubsTab() {
        closeUi();
        Handler conn_handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                openUi();
                closeAll();
                myHubsBtn.setSelected(true);
                myHubsUi.setVisibility(View.VISIBLE);
                setConnectionAdaptor();
            }
        };

        //this.master.getAllHubs();
        Handler auth_handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                master.getConnections(conn_handler);
            }
        };

        this.master.getAuthToken(auth_handler, this.AUTH_ERROR);

    }


    private void openAccountTab() {
        closeAll();
        this.accBtn.setSelected(true);
        this.accauntUi.setVisibility(View.VISIBLE);

        accauntUi_login.setText(Explorer.memory.getUser().login);
        accauntUi_email.setText(Explorer.memory.getUser().email);

    }

    private void after(){
        GameAdapter = new RecGameAdaptor(this, Explorer.memory.games_list);
        recyclerGameView = findViewById(R.id.recycle_game_view);
        GameAdapter.notifyDataSetChanged();
        recyclerGameView.setAdapter(GameAdapter);

    }

    private void openGamesTab() {
        closeAll();
        this.gamesBtn.setSelected(true);
        this.gamesUi.setVisibility(View.VISIBLE);

        Handler handler= new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                after();
            }
        };

        synchronized(this) {

           this.master.getAllGames(handler);

        }

    }

    public void logout() {
        Explorer explorer = new Explorer(this);
        explorer.removeTokens();
        Explorer.memory.clear();
        AUTH_ERROR.sendEmptyMessage(0);
    }

    private void initOnClicks() {
        this.accBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //open Acc Unit
                        openAccountTab();
                        Log.i("INFO", "to accaunt");
                    }
                }
        );

        this.logOut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout();
                    }
                }
        );

        this.hubsBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openHubsTab();
                    }
                }
        );

        this.myHubsBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // open My Hubs List
                        openMyHubsTab();
                    }
                }
        );

        this.gamesBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGamesTab();
                    }
                }
        );

        this.newHubBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newHub();
                    }
                }
        );
    }



    private void newHub() {
        startActivity(new Intent(this, com.example.hydro.acts.ActHubNew.class));
        openUi();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contest);

        intent = new Intent(this, com.example.hydro.acts.ActUserAuth.class);
        editIntent = new Intent(this, com.example.hydro.acts.ActEdit.class);

        master = RequestRetrofitMaster.getInstance(this);
        explorer = new Explorer(this);

        initIds();
        initOnClicks();


        //CONNECTIONTEST
        //this.master.testConnection();
        master.getAuthToken(AUTH_OK, AUTH_ERROR);

        //this.HubAdapter = new CustomAdapter<Hub, HubView>(this, this.hubs_list, R.layout.hub_layout);

        //this.HubAdapter.add(new Hub("1"));
        //this.HubAdapter.add(new Hub("1"));
        //this.HubAdapter.add(new Hub("1"));
        //this.HubAdapter.add(new Hub("1"));


        this.HubAdapter = new RecHubAdaptor(this, Explorer.memory.hubs_list, new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int id = msg.what;
                Log.i("SelectHub", Integer.toString(id));
            }
        });
        recyclerHubView = findViewById(R.id.recycle_hub_view);
        this.HubAdapter.notifyDataSetChanged();
        recyclerHubView.setAdapter(this.HubAdapter);


        setConnectionAdaptor();

        this.GameAdapter = new RecGameAdaptor(this, Explorer.memory.games_list);
        recyclerGameView = findViewById(R.id.recycle_game_view);
        this.GameAdapter.notifyDataSetChanged();
        recyclerGameView.setAdapter(this.GameAdapter);
    }
}
