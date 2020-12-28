package com.example.hydro.request;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.hydro.R;
import com.example.hydro.acts.ActEdit;
import com.example.hydro.explorer.Explorer;
import com.example.hydro.request.callbacks.GenericCallback;
import com.example.hydro.request.models.Connection;
import com.example.hydro.request.models.Game;
import com.example.hydro.request.models.HUBBODY;
import com.example.hydro.request.models.Hub;
import com.example.hydro.request.models.LOGINBODY;
import com.example.hydro.request.models.REGISTERBODY;
import com.example.hydro.request.models.SIMPLEREQUEST;
import com.example.hydro.request.models.TOKENBODY;
import com.example.hydro.request.models.Task;
import com.example.hydro.request.models.Token;
import com.example.hydro.request.models.TokenTypes;
import com.example.hydro.request.models.USER;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RequestRetrofitMaster {

    private NETWORK service;
    private Retrofit retrofit;
    private Context context;


    public String baseURL = "";

    private static RequestRetrofitMaster instance = null;

    public static RequestRetrofitMaster getInstance(Context context) {
        if (instance == null) {
            instance = new RequestRetrofitMaster(context);
        }else{
            instance.context = context;
        }
        return instance;
    }

    private RequestRetrofitMaster(Context context) {
        this.context = context;
        this.baseURL = this.context.getString(R.string.hydrogen_services_url);

        this.retrofit = new Retrofit.Builder()
                .baseUrl(this.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.service = this.retrofit.create(NETWORK.class);
    }

    public void testConnection(Handler ok, Handler err) {
        Call<SIMPLEREQUEST<String>> call = service.connectionTest();

        call.enqueue(new GenericCallback<SIMPLEREQUEST<String>>() {
            @Override
            public void ifSuccess(SIMPLEREQUEST<String> result) {
                Log.i("request", "Ok:" + result);
                ok.sendEmptyMessage(1);
            }
        });
    }

    /*
    public void testToken(TOKEN token) {
        Call<SIMPLEREQUEST<String>> call = service.is_auntethicated(token);

        call.enqueue(new GenericCallback<SIMPLEREQUEST<String>>() {
            @Override
            public void ifSuccess(SIMPLEREQUEST<String> result) {
                Toast toast = Toast.makeText(context, result.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
    */
    public void register(Handler ok, Handler err, REGISTERBODY body) {
        Call<SIMPLEREQUEST<TOKENBODY>> call = service.register(body);
        call.enqueue(
                new GenericCallback<SIMPLEREQUEST<TOKENBODY>>() {
                    @Override
                    public void ifSuccess(SIMPLEREQUEST<TOKENBODY> result) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
                        // change CHANNEL authToken field
                        Toast toast;
                        toast = Toast.makeText(context, result.getBody().message, Toast.LENGTH_LONG);
                        Explorer explorer = new Explorer(context);
                        try {
                            explorer.savePreference(result.getBody().getToken());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            toast.setText("Error with preferences saving");
                        }
                        toast.show();
                        Log.i("REQ_REG", result.getBody().token);

                        getAuthToken(ok, err);
                    }

                    @Override
                    public void onFailure(Call<SIMPLEREQUEST<TOKENBODY>> call, Throwable t) {
                        super.onFailure(call, t);
                        Toast toast;
                        toast = Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                        err.sendEmptyMessage(0);
                    }
                }
        );

        Log.i("REQ_REG", "REQUEST SEND");
    }


    public void login(Handler ok, Handler err, LOGINBODY body) {
        Call<SIMPLEREQUEST<TOKENBODY>> call = service.login(body);
        call.enqueue(new GenericCallback<SIMPLEREQUEST<TOKENBODY>>() {
            @Override
            public void ifSuccess(SIMPLEREQUEST<TOKENBODY> result) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
                Toast toast;
                toast = Toast.makeText(context, result.getBody().getMessage(), Toast.LENGTH_LONG);
                Explorer explorer = new Explorer(context);
                try {
                    explorer.savePreference(result.getBody().getToken());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    toast.setText("Error with preferences saving");
                }
                toast.show();
                Log.i("REQ_LOG", result.getBody().token);

                getAuthToken(ok, err);
            }

            @Override
            public void onFailure(Call<SIMPLEREQUEST<TOKENBODY>> call, Throwable t) {
                super.onFailure(call, t);
                Toast toast;
                toast = Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG);
                toast.show();
                err.sendEmptyMessage(0);
            }
        });
    }


    public void getAuthToken(Handler ok, Handler err) {

        Explorer explorer = new Explorer(context);
        TOKENBODY body = null;
        try {
            body = new TOKENBODY(explorer.loadPreference(Token.class, TokenTypes.Refr.name()).getPreference());
        } catch (Exception e) {
            e.printStackTrace();
            err.sendEmptyMessage(0);
            return;
        }

        Log.d("prefs", body.toString());

        Call<SIMPLEREQUEST<TOKENBODY>> call = service.refresh(body, TokenTypes.Auth.name());
        call.enqueue(new GenericCallback<SIMPLEREQUEST<TOKENBODY>>() {
            /*@Override
            public void ifSuccess(SIMPLEREQUEST<TOKENBODY> result) {
                TOKENBODY authToken = result.getBody();
            }*/
            @Override
            public void ifSuccess(SIMPLEREQUEST<TOKENBODY> result) throws IllegalAccessException {
                Toast toast;
                toast = Toast.makeText(context, result.getBody().getMessage(), Toast.LENGTH_LONG);
                Explorer explorer = new Explorer(context);
                try {
                    //explorer.savePreference(result.getBody().getToken());
                    Explorer.memory.setAuthToken(result.getBody().getToken());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.i("REFRESH_TASK", "SAVING ERROR");
                    toast.setText("Error with preferences saving");
                }
                toast.show();
                Log.i("REQ_LOG", result.getBody().token);
                getMe(ok, err, result.getBody().getToken().getToken());
            }

            @Override
            public void onFailure(Call<SIMPLEREQUEST<TOKENBODY>> call, Throwable t) {
                super.onFailure(call, t);
                Explorer.memory.setAuthToken(null);
                Toast toast;
                toast = Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG);
                toast.show();
                err.sendEmptyMessage(0);
            }
        });
    }

    public void getMe(Handler ok, Handler err, String token) {

        if (token == null || token.isEmpty()){
            throw new NullPointerException("token is null");
        }

        Call<SIMPLEREQUEST<USER>> call = service.getme(token);
        call.enqueue(new GenericCallback<SIMPLEREQUEST<USER>>() {
            @Override
            public void ifSuccess(SIMPLEREQUEST<USER> result) {

                Explorer.memory.setUser(result.getBody());

                Log.i("REQ_LOG", result.getBody().status + " : " + result.getBody().login
                );
                ok.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(Call<SIMPLEREQUEST<USER>> call, Throwable t) {
                super.onFailure(call, t);
                Explorer.memory.setAuthToken(null);
                Toast toast;
                toast = Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG);
                toast.show();
                err.sendEmptyMessage(0);
            }
        });
    }

    public void getAllHubs(Handler handler) {
        Call<SIMPLEREQUEST<List<Hub>>> call = service.getAllHubs();
        call.enqueue(new GenericCallback<SIMPLEREQUEST<List<Hub>>>() {
            @Override
            public void ifSuccess(SIMPLEREQUEST<List<Hub>> result) {
                Log.i("REQ_HUB", result.getBody().toString());
                Explorer.memory.hubs_list.clear();
                for(Hub hub : result.getBody() ){
                    Explorer.memory.hubs_list.add(hub);
                };
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(Call<SIMPLEREQUEST<List<Hub>>> call, Throwable t) {
                super.onFailure(call, t);
                Log.i("REQ_HUB", "ERROR");
                Toast toast;
                toast = Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void getConnections(Handler handler) {
        Call<SIMPLEREQUEST<List<Connection>>> call = service.getConnections(Explorer.memory.getAuthToken().getToken());
        call.enqueue(new GenericCallback<SIMPLEREQUEST<List<Connection>>>() {
            @Override
            public void ifSuccess(SIMPLEREQUEST<List<Connection>> result) {
                Log.i("REQ_CON", result.getBody().toString());
                Explorer.memory.conn_list.clear();
                for(Connection connection : result.getBody() ){
                    Explorer.memory.conn_list.add(connection);
                };
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(Call<SIMPLEREQUEST<List<Connection>>> call, Throwable t) {
                super.onFailure(call, t);
                Log.i("REQ_CON", "ERROR");
                Toast toast;
                toast = Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void getTask(Handler handler) {
        Call<SIMPLEREQUEST<Task>> call = service.getTask(Explorer.memory.connection.hub.game.id);
        call.enqueue(new GenericCallback<SIMPLEREQUEST<Task>>() {
            @Override
            public void ifSuccess(SIMPLEREQUEST<Task> result) {
                Log.i("REQ_TASK", result.getBody().toString());
                Explorer.memory.currentTask = result.getBody();
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(Call<SIMPLEREQUEST<Task>> call, Throwable t) {
                super.onFailure(call, t);
                Log.i("REQ_TASK", "ERROR");
                Toast toast;
                toast = Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void getAllGames(Handler handler) {

        Call<SIMPLEREQUEST<List<Game>>> call = service.getAllGames();
        call.enqueue(new GenericCallback<SIMPLEREQUEST<List<Game>>>() {
            @Override
            public void ifSuccess(SIMPLEREQUEST<List<Game>> result) {
                Log.i("REQ_GAME", result.getBody().toString());

                Explorer.memory.games_list.clear();
                for(Game game : result.getBody() ){
                    Explorer.memory.games_list.add(game);
                };
                Log.i("REQ_GAME", "EXIT");

                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(Call<SIMPLEREQUEST<List<Game>>> calls, Throwable t) {
                super.onFailure(calls, t);
                Log.i("REQ_GAME", "ERROR");
                Toast toast;
                toast = Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG);
                toast.show();
                call.cancel();
                handler.sendEmptyMessage(0);
            }
        });
    }

    public void newHub(Handler ok, Handler err, HUBBODY body){
        if (body == null || body.token == null){
            throw new NullPointerException("token is null");
        }

        Call<SIMPLEREQUEST<TOKENBODY>> call = service.newHub(body);
        call.enqueue(new GenericCallback<SIMPLEREQUEST<TOKENBODY>>() {
            @Override
            public void ifSuccess(SIMPLEREQUEST<TOKENBODY> result) {

                Log.i("NEW_HUB", result.getBody().token);
                String tp = result.getBody().token_type;

                if(tp.equals( TokenTypes.Ok.name())){
                    ok.sendEmptyMessage(1);

                }else {
                //if(tp.equals( TokenTypes.Err.name()))
                    err.sendEmptyMessage(result.getBody().message);
                }

            }

            @Override
            public void onFailure(Call<SIMPLEREQUEST<TOKENBODY>> call, Throwable t) {
                super.onFailure(call, t);
                Explorer.memory.setAuthToken(null);
                Toast toast;
                toast = Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG);
                toast.show();
                err.sendEmptyMessage(0);
            }
        });
    }
}
