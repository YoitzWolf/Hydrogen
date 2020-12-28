package com.example.hydro.acts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hydro.R;
import com.example.hydro.views.EditCode;

public class ActEdit extends AppCompatActivity {

    private EditCode editCode;
    private Button codeBtn;
    private Button taskBtn;
    private Button toolBtn;
    private HorizontalScrollView toolBar;

    public void closeAll() {
        codeBtn.setSelected(false);
        taskBtn.setSelected(false);
        //toolBtn.setSelected(false);

        this.editCode.setVisibility(View.GONE);
    }

    public void openEditor() {
        this.editCode.setVisibility(View.VISIBLE);
        this.codeBtn.setSelected(true);
    }

    private void initOnClicks() {
        this.codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAll();
                v.setSelected(true);
                openEditor();
            }
        });

        this.taskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAll();
                v.setSelected(true);
            }
        });

        this.toolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    // CLOSE
                    toolBar.setVisibility(View.GONE);
                    v.setSelected(false);
                } else {
                    // OPEN
                    toolBar.setVisibility(View.VISIBLE);
                    v.setSelected(true);
                }
            }
        });
    }

    private void initIds() {
        // Buttons
        this.codeBtn = this.findViewById(R.id.bar_code_btn);
        this.taskBtn = this.findViewById(R.id.bar_task_btn);
        this.toolBtn = this.findViewById(R.id.bar_tools_btn);

        // Views
        this.editCode = this.findViewById(R.id.code_editor);
        this.toolBar = this.findViewById(R.id.bar_tools_bar);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);

        this.initIds();
        this.initOnClicks();
    }
}
