package com.example.hydro.acts;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hydro.R;
import com.example.hydro.explorer.Explorer;
import com.example.hydro.request.RequestRetrofitMaster;
import com.example.hydro.views.EditCode;

public class ActEdit extends AppCompatActivity {

    RequestRetrofitMaster master = null;
    Explorer explorer = null;

    private EditCode editCode;
    private Button codeBtn;
    private Button taskBtn;
    private Button toolBtn;
    private Button sendBtn;
    private HorizontalScrollView toolBar;

    public LinearLayout taskTab;
    public WebView taskView;

    public LinearLayout sendTab;

    public void closeAll() {
        codeBtn.setSelected(false);
        taskBtn.setSelected(false);
        sendBtn.setSelected(false);

        this.editCode.setVisibility(View.GONE);
        this.taskTab.setVisibility(View.GONE);
        this.sendTab.setVisibility(View.GONE);
    }

    public void openEditor() {
        closeAll();
        this.editCode.setVisibility(View.VISIBLE);
        this.codeBtn.setSelected(true);

    }

    public void openTask() {
        closeAll();
        closeBar();
        taskBtn.setSelected(true);
        this.taskTab.setVisibility(View.VISIBLE);
    }

    public void openSend() {
        closeAll();
        closeBar();
        this.sendBtn.setSelected(true);
        this.sendTab.setVisibility(View.VISIBLE);
    }

    public void closeBar(){
        this.toolBar.setVisibility(View.GONE);
        this.toolBtn.setSelected(false);
    }

    private void initOnClicks() {
        this.codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditor();
            }
        });

        this.taskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTask();
            }
        });

        this.toolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!codeBtn.isSelected()) return;
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

        this.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSend();
            }
        });
    }

    private void initIds() {
        // Buttons
        this.codeBtn = this.findViewById(R.id.bar_code_btn);
        this.taskBtn = this.findViewById(R.id.bar_task_btn);
        this.toolBtn = this.findViewById(R.id.bar_tools_btn);
        this.sendBtn = this.findViewById(R.id.bar_send_btn);

        // Views
        this.editCode = this.findViewById(R.id.code_editor);
        this.toolBar = this.findViewById(R.id.bar_tools_bar);

        this.taskTab = this.findViewById(R.id.task);
        this.taskView = this.findViewById(R.id.task_view);

        this.sendTab = this.findViewById(R.id.send);

    }

    public void loadTask(){
        this.master.getTask(new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                taskView.loadData(
                        Explorer.memory.currentTask.taskHtml,
                        "text/html; charset=utf-8", "UTF-8");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);

        master = RequestRetrofitMaster.getInstance(this);
        explorer = new Explorer(this);

        loadTask();

        this.initIds();
        this.initOnClicks();
        this.openTask();
    }
}
