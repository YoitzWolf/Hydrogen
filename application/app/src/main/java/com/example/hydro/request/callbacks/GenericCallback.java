package com.example.hydro.request.callbacks;

import android.util.Log;

import com.example.hydro.request.models.SIMPLEREQUEST;

import java.lang.reflect.InvocationTargetException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class GenericCallback<T extends SIMPLEREQUEST> implements Callback<T> {

    public abstract void ifSuccess(T result) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            T resp = response.body();
            Log.d("REQUEST", resp.getBody().toString());
            try {
                this.ifSuccess(resp);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("REQUEST", "RESPONSE IS NOT SUCCESS" + response.message());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d("REQ_REG", t.getMessage());
    }
}
