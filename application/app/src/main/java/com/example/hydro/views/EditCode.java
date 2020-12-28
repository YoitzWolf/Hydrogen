package com.example.hydro.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Patterns;

import com.example.hydro.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.min;

public class EditCode extends androidx.appcompat.widget.AppCompatEditText {


    private Pattern KEYWORDS = Pattern.compile("\\b(def|self|elif|if|else|break|continue|try|catch|while|for|return|print|class|=)\\b");


    //---------------------
    final float scale = getContext().getResources().getDisplayMetrics().density;
    private int gWidth = 0;
    private int gDigitCount = 0;
    private double gMarginDP = 0f;
    private int gMargin = 0;
    private int gLineWidth = 5;

    private double gFontSizePT = 20;
    private int gFontSize;

    private int gPaddingLeft = 10;
    private int gPaddingRight = 10;
    private int paddingLeft = 10;
    private int paddingRight = 0;
    private int paddingTop = 0;

    private Paint gTextPaint; // number lines
    private Paint gDividerPaint; // divider line
    private Paint gBlockPaint; // block rect paint
    private TextWatcher watcher;

    //--------------------

    private void getPxFromDp(double dp) {
        return;
    }

    private void updatePaints() {
        this.gTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.gTextPaint.setTextSize((float) (this.getTextSize() * 0.9));
        this.gTextPaint.setColor(getContext().getResources().getColor(R.color.actionGreen));

        this.gDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.gDividerPaint.setStrokeWidth(gLineWidth);
        this.gDividerPaint.setColor(getContext().getResources().getColor(R.color.actionGreen));

        this.gBlockPaint = new Paint();
        this.gBlockPaint.setColor(getContext().getResources().getColor(R.color.darkGray));
    }

    public void processText(String newText){
        removeTextChangedListener(watcher);
        /* code here */
        // TODO
        this.setText(newText);
        addTextChangedListener(watcher);
    }

    /* HIGHTLIGHTING*/

    private void syntaxHighlight(){
        Editable text = this.getText();

        Matcher matcher = KEYWORDS.matcher(text);
        matcher.region(0, this.getText().length());
        while (matcher.find()){
            text.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#7F0055")),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        Log.i("HL", "SHIT");
    }

    private void  cancelSyntaxHighlighting() {
        //styler.endTASk()
    }

    private void  updateSyntaxHighlighting() {
        syntaxHighlight();
    }

    public void complex() {

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
        addTextChangedListener(watcher);
        this.gMargin = (int) (gMarginDP * scale + 0.5f);
        this.setHorizontallyScrolling(true);
        updatePaints();

    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        this.gTextPaint.setTextSize(size);
    }

    public EditCode(Context context) {
        super(context);
        complex();
    }

    public EditCode(Context context, AttributeSet attrs) {
        super(context, attrs);
        complex();
    }

    public EditCode(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        complex();
    }

    private void updateGutter() {
        int minCount = 3;
        int linesCount = getLineCount();
        updatePaints();

        gDigitCount = Integer.toString(linesCount).length();
        if (gDigitCount >= minCount) {
            minCount = gDigitCount;
        }
        String wid = "";
        for (int i = 0; i < minCount; i++) {
            wid += "0";
        }

        Rect bounds = new Rect();
        Paint textPaint = this.getPaint();
        textPaint.getTextBounds(wid, 0, wid.length(), bounds);

        gWidth = (int) bounds.width();
        gWidth += gMargin;
        if (paddingLeft != gWidth + gMargin) {
            this.setPadding(gWidth + gMargin + gLineWidth + paddingLeft + gPaddingRight + gPaddingLeft, gMargin, paddingRight, 0);
        }
    }

    private int getTopLine() {
        return this.getScrollY() / this.getLineHeight();
    }

    private int getBottomLine() {
        return this.getTopLine() + this.getHeight() / this.getLineHeight() + 1;
    }




    @Override
    protected void onDraw(Canvas canvas) {
        this.updateGutter();
        this.updateSyntaxHighlighting();
        super.onDraw(canvas);

        int firstVisibleLine = getTopLine();
        int lastVisibleLine = min(this.getBottomLine(), this.getLineCount() - 1);
        int textRight = (gWidth - gMargin / 2) + this.getScrollX();
        Log.i("item", String.valueOf((this.getLineHeight() - this.getTextSize())));

        Rect rect = new Rect(0, this.getScrollY(), (gWidth + gPaddingLeft + gPaddingRight + this.getScrollX()), this.getHeight() + this.getScrollY());
        canvas.drawRect(
                rect,
                gBlockPaint
        );

        for (int visibleLine = firstVisibleLine, i = 1; visibleLine <= lastVisibleLine; visibleLine++, i++) {
            canvas.drawText(
                    Integer.toString(visibleLine + 1),
                    this.getScrollX() + gPaddingLeft + 0f,
                    0f + this.getScrollY() + (i * this.getLineHeight()) - (this.getLineHeight() - this.getTextSize()) / 2,
                    gTextPaint
            );
        }

        canvas.drawLine(
                (gWidth + this.getScrollX() + gPaddingLeft + gPaddingRight) + 0f,
                this.getScrollY() + 0f,
                (gWidth + this.getScrollX() + gPaddingLeft + gPaddingRight) + 0f,
                (this.getScrollY() + this.getHeight()) + 0f,
                gDividerPaint
        );
    }


}
