package com.sauravtom.app;

/**
 * Created by saurav on 21/12/13.
 */

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;


public class DemoWallpaperService extends WallpaperService {

    public int count = 0;
    public int TIME = 200;
    public int y = 100;
    public int x = 100;
    public Bitmap cache;

    @Override
    public Engine onCreateEngine() {
        return new DemoWallpaperEngine();
    }
    private class DemoWallpaperEngine extends Engine {

        private boolean mVisible = false;
        public final Paint mPaint = new Paint();
        public int speed = 100;

        private final Handler mHandler = new Handler();
        private final Runnable mUpdateDisplay = new Runnable() {
            @Override
            public void run() {
                draw();
            }};

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                int color = Color.parseColor(prefs.getString("list_preference_color", "GREEN"));
                mPaint.setColor(color);
                speed = Integer.parseInt(prefs.getString("list_preference_speed","100"));

            }
        };

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    Paint p = new Paint();

                    if (cache != null){    c.drawBitmap(cache,0,0,p);   }

                    p.setTextSize(15);
                    p.setAntiAlias(true);
                    String text = "system time: "+Long.toString(System.currentTimeMillis());
                    float d = p.measureText(text, 0, text.length());
                    int offset = (int) d / 2;
                    int w = c.getWidth();
                    int h = c.getHeight();
                    p.setColor(Color.BLACK);
                    c.drawRect(0, 0, w, h, p);
                    p.setColor(Color.WHITE);
                    //c.drawText(text, w/2- offset, h/2, p);

                    String foo = "0101";
                    String [] array = foo.split("");
                    int fontsize = 40;
                    int columns = w/fontsize;

                    int[] colarr = new int[columns];


                    for(int i=0;i < w;i+=fontsize){

                        for(int j=0;j < h;j+=fontsize){
                            String letter = array[(int) Math.floor( Math.random() * array.length)];
                            c.drawText(letter,i, j, p);
                        }

                        p.setAlpha(155);

                        //String letter = array[(int) Math.floor( Math.random() * array.length)];
                        //c.drawText(letter,i, y, p);
                    }
                    y += fontsize;

                    if(++y > h) y = 0;
                    if (cache == null) {
                        cache = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                    }
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }
            mHandler.removeCallbacks(mUpdateDisplay);
            if (mVisible) {
                mHandler.postDelayed(mUpdateDisplay, TIME);
            }
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                draw();
            } else {
                mHandler.removeCallbacks(mUpdateDisplay);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            draw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mUpdateDisplay);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mVisible = false;
            mHandler.removeCallbacks(mUpdateDisplay);
        }
    }
}