package com.example.hydro.acts;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hydro.R;
import com.example.hydro.explorer.Explorer;
import com.example.hydro.request.RequestRetrofitMaster;
import com.example.hydro.request.models.Game;
import com.example.hydro.request.models.HUBBODY;
import com.example.hydro.request.models.LOGINBODY;
import com.example.hydro.request.models.REGISTERBODY;
import com.example.hydro.request.models.Token;
import com.example.hydro.services.adaptor.RecGameAdaptor;
import com.example.hydro.services.adaptor.RecHubAdaptor;

import java.util.List;

public class ActHubNew extends AppCompatActivity {

    public Toast toast;

    public final Handler GEN_OK = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            closeMe();
        }

    };

    public final Handler GEN_ERROR = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Log.i("HUB_NEW", msg.toString());
        }
    };

    RequestRetrofitMaster master;
    Explorer explorer;

    private EditText login_login;
    private EditText login_password;

    private EditText register_login;
    private EditText register_email;

    private Button sendNewHubRequest;
    private Button back;
    private Spinner spinner;
    private CheckBox is_public_box;
    private EditText hub_password;
    private ArrayAdapter adapter;
    private EditText hub_name;
    private EditText description;


    private void initOnClicks() {
        /*
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMe();
            }
        });
        */
        this.sendNewHubRequest.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLoginRequest();
            }
        });

        this.is_public_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // open pass tab
                    is_public_box.setText(R.string.is_private_checkbox);
                    hub_password.setVisibility(View.VISIBLE);
                }else {
                    // close pass tab
                    is_public_box.setText(R.string.is_public_checkbox);
                    hub_password.setVisibility(View.GONE);
                    hub_password.setText("");
                }
            }
        });

    }

    private void sendLoginRequest() {
        Token token = Explorer.memory.getAuthToken();
        Log.i("SPINNER", spinner.getSelectedItem().toString());
        HUBBODY body = new HUBBODY(
                hub_name.getText().toString(),
                token.getToken(),
                description.getText().toString(),
                ((Game)(spinner.getSelectedItem())).id,
                is_public_box.isChecked(),
                hub_password.getText().toString()

        );
        master.newHub(GEN_OK, GEN_ERROR, body);
    }

    private void initIds() {
        // Buttons

        this.sendNewHubRequest = this.findViewById(R.id.next);

        this.spinner = this.findViewById(R.id.game_list);

        this.is_public_box = this.findViewById(R.id.is_public_checkbox);
        this.hub_password = this.findViewById(R.id.hub_password);

        this.hub_name = this.findViewById(R.id.hub_name);
        this.description = this.findViewById(R.id.description);

    }

    private void closeWhenAuthoriseable() {
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                while (Explorer.memory.getAuthToken() == null) {
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                closeMe();
            }
        };
        t.start();
    }

    private void closeMe() {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_hub_reg);

        initIds();

        toast = new Toast(this);
        toast.makeText(this, "", Toast.LENGTH_LONG);

        master = RequestRetrofitMaster.getInstance(this);
        master.getAllGames(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                spinner.setAdapter( adapter );
                spinner.setSelection(0) ;
            }
        });

        adapter = new ArrayAdapter(this,
                R.layout.spin_item_raw,
                (List<String>) (List<?>) Explorer.memory.games_list
        );
        //
        //adapter = new RecGameAdaptor(this, Explorer.memory.);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter( adapter);
        spinner.setSelection(0) ;

        initOnClicks();
    }
}

