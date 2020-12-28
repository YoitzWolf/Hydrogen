package com.example.hydro.services.editor;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class TextProcessor {

    private TextWatcher watcher;

    private void syntaxHighlight(){

    }

    private void manageText(String text){

    }

    public TextProcessor(
            Context context,
            AttributeSet attrs,
            int defStyleAttr
    ){
        watcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                syntaxHighlight();
            }
        };
    }
}
