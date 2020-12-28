package com.example.hydro.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.lang.Math;
import androidx.annotation.Nullable;

import com.example.hydro.R;

public class Drawler extends View {

    private Resources resources;
    private Paint mPaint;
    private Bitmap bitmap;

    public Drawler(Context context) {
        super(context);
        init(context);
    }

    public Drawler(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Drawler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Drawler(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        resources = context.getResources();
        byte[] data = {127, 0, 127, 12, 12, 12};
        bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        for(int x=0; x<bitmap.getWidth();x++){
            for(int y=0;y<bitmap.getHeight() ;y++){
                bitmap.setPixel(x, y, Color.rgb(255, (int) (Math.random()*256),  (int) (Math.random()*256) ));
            }
        }



        /////////Log.i("bitmapa", bitmap.toString());
    }
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, this.getWidth(), this.getWidth(), false), 0, 0, mPaint);
    }
}
