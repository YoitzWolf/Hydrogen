package com.example.hydro.acts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hydro.R;
import com.example.hydro.explorer.Explorer;
import com.example.hydro.request.RequestRetrofitMaster;
import com.example.hydro.request.models.LOGINBODY;
import com.example.hydro.request.models.REGISTERBODY;

public class ActUserAuth extends AppCompatActivity {

    private final Handler REG_ERROR = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            closeAll();
            selectRegister();
        }

    };;

    public final Handler AUTH_OK = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            closeMe();
        }

    };

    private Intent intent;
    public final Handler AUTH_ERROR = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            closeAll();
            selectLogin();
        }
    };

    RequestRetrofitMaster master;
    Explorer explorer;

    private EditText login_login;
    private EditText login_password;

    private EditText register_login;
    private EditText register_email;
    private EditText register_password;
    private EditText register_password_rec;

    private Button loginTabBtn;
    private Button registerTabBtn;

    private Button sendLogin;
    private Button sendRegistration;

    private LinearLayout signInLayout;
    private LinearLayout signUpLayout;

    public void closeAll() {
        this.signInLayout.setVisibility(View.GONE);
        this.signUpLayout.setVisibility(View.GONE);

        this.loginTabBtn.setSelected(false);
        this.registerTabBtn.setSelected(false);
    }

    private void selectLogin() {
        this.loginTabBtn.setSelected(true);
        this.signInLayout.setVisibility(View.VISIBLE);
    }

    private void selectRegister() {
        this.registerTabBtn.setSelected(true);
        this.signUpLayout.setVisibility(View.VISIBLE);
    }

    private void initOnClicks() {
        this.loginTabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    // pass
                } else {
                    closeAll();
                    selectLogin();
                }
            }
        });

        this.registerTabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    // pass
                } else {
                    closeAll();
                    selectRegister();
                }
            }
        });

        this.sendLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLoginRequest();
            }
        });

        this.sendRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegisterRequest();
            }
        });
    }

    private void sendLoginRequest() {
        Log.i("REQ_LOG", "TRY");
        if (login_login.getTextSize() == 0 || login_password.getTextSize() == 0) {
            Log.i("REQ_LOG", login_login.getText().toString() + " : " + login_password.getText().toString());
            return;
        }
        Log.i("REQ_LOG", "SENDING");
        this.master.login(AUTH_OK, AUTH_ERROR, new LOGINBODY(login_login.getText().toString(), login_password.getText().toString()));

    }

    private void sendRegisterRequest() {
        Log.i("REQ_REG", "TRY");
        if (register_login.getTextSize() == 0 || register_password.getTextSize() == 0 || !register_password.getText().toString().equals(register_password_rec.getText().toString())) {
            Log.i("REQ_REG", register_login.getText().toString() + " : " + register_email.getText().toString() + " : " + register_password.getText().toString());
            return;
        }
        Log.i("REQ_REG", "SENDING");
        this.master.register(AUTH_OK, REG_ERROR, new REGISTERBODY(register_login.getText().toString(), register_email.getText().toString(), register_password.getText().toString()));

    }

    private void initIds() {
        // Buttons
        this.loginTabBtn = this.findViewById(R.id.bar_login);
        this.registerTabBtn = this.findViewById(R.id.bar_register);

        this.login_login = this.findViewById(R.id.sign_in_login);
        this.login_password = this.findViewById(R.id.sign_in_password);

        this.register_login = this.findViewById(R.id.sign_up_login);
        this.register_password = this.findViewById(R.id.sign_up_password);
        this.register_email = this.findViewById(R.id.sign_up_email);
        this.register_password_rec = this.findViewById(R.id.sign_up_password_rec);

        this.sendLogin = this.findViewById(R.id.login_next);
        this.sendRegistration = this.findViewById(R.id.register_next);

        // Views
        this.signInLayout = this.findViewById(R.id.sign_in); // layout login
        this.signUpLayout = this.findViewById(R.id.sign_up); // layout register

    }

    private void closeWhenAuthoriseable() {
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                while (Explorer.memory.getAuthToken().equals(null)) {
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
        Explorer.memory.notoken = true;
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Explorer.memory.clear();
        this.master = RequestRetrofitMaster.getInstance(this);
        /*
        try {
            master.getAuthToken(AUTH_OK, AUTH_ERROR);
            //auth is OK
        } catch (Exception e) {
            e.printStackTrace();
        }*/



        this.initIds();
        this.initOnClicks();
        this.closeAll();
        this.selectLogin();

        Log.i("act", "OPENED");

        //this.closeWhenAuthoriseable();
    }
}
