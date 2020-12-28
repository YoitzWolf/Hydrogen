package com.example.hydro.explorer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import com.example.hydro.explorer.models.Preferenceable;
import com.example.hydro.request.models.Connection;
import com.example.hydro.request.models.Game;
import com.example.hydro.request.models.Hub;
import com.example.hydro.request.models.Token;
import com.example.hydro.request.models.TokenTypes;
import com.example.hydro.request.models.USER;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


public class Explorer {

    private SharedPreferences storage;
    private String name;
    private Context context;

    public static class Memory {

        private Token AuthToken;
        private USER User;

        public int selectedConnectionId;

        public List<Hub> hubs_list = new ArrayList<Hub>();
        public List<Game> games_list = new ArrayList<Game>();
        public List<Connection> conn_list = new ArrayList<Connection>();

        private Memory() {
            this.AuthToken = null;
            this.User = null;
            hubs_list = new ArrayList<Hub>();
            games_list = new ArrayList<Game>();
        }

        public void clear() {
            this.AuthToken = null;
            this.User = null;
        }

        private static Memory instance = new Memory();

        public static Memory getInstance() {
            return instance;
        }

        public void setAuthToken(Token authToken) {
            this.AuthToken = authToken;
        }

        public Token getAuthToken() {
            return this.AuthToken;
        }

        public USER getUser() {
            return User;
        }

        public void setUser(USER user) {
            if (user.status != null) {
                this.User = user;
            } else {
                this.User = null;
            }

        }
    }

    public static final Memory memory = Memory.getInstance();

    public Explorer(Context context) {
        this.context = context;
        this.name = "HYDROGEN_STORAGE";
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Context getContext() {
        return this.context;
    }

    public void removeTokens() {
        this.setStorage(Token.Storage());
        SharedPreferences.Editor editor = this.storage.edit();
        editor.remove(TokenTypes.Refr.name());
        editor.remove(TokenTypes.Auth.name());
        editor.remove(TokenTypes.Err.name());

        editor.commit();
    }

    public String getName() {
        return this.name;
    }

    public void setStorage(String name) {
        this.name = name;
        this.storage = this.context.getSharedPreferences(this.name, Context.MODE_PRIVATE);
    }

    public void savePreference(Preferenceable prefs) {
        this.setStorage(prefs.getStorage());
        SharedPreferences.Editor editor = this.storage.edit();

        Pair<String, String> pref = prefs.getPreference();
        editor.putString(pref.first, pref.second);

        editor.commit();
    }

    public Preferenceable loadPreference(Class<? extends Preferenceable> C, String key) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        this.setStorage(C.newInstance().getStorage());
        String value = this.storage.getString(key, "");
        Preferenceable pref = C.getConstructor(new Class[]{String.class, String.class}).newInstance(key, value);
        return pref;
    }
}
