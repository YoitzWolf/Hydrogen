package com.example.hydro.services.adaptor.holders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydro.R;

public class HubHolder extends RecyclerView.ViewHolder implements ResourceIdHolder {

    private static final int resource = R.layout.recycle_hub_item;

    public HubHolder(@NonNull View itemView) {
        super(itemView);
    }

    public static int getResourceId() {
        return HubHolder.resource;
    }
}
