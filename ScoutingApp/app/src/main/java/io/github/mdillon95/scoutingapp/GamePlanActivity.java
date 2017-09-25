package io.github.mdillon95.scoutingapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.MenuPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class GamePlanActivity extends AppCompatActivity {

    private final String TAG = "GamePlanActivity";

    private DrawingView dv;
    private boolean drawBg = true;

    private ImageView iv;

    private CoordinatorLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_plan);
        dv = new DrawingView(this);
        iv = (ImageView) findViewById(R.id.field_iv);
        rl = (CoordinatorLayout) findViewById(R.id.game_plan);
        this.addContentView(dv, findViewById(R.id.field_iv).getLayoutParams());
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_gp);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        rl.removeView(fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(GamePlanActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.draw_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Clear Drawing")) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        } else if (item.getTitle().equals("Toggle Field Background")) {
                            if (drawBg) {
                                drawBg = false;
                                iv.setVisibility(View.INVISIBLE);
                            } else {
                                drawBg = true;
                                iv.setVisibility(View.VISIBLE);
                            }
                        } else if (item.getTitle().equals("Toggle Ink Color")) {
                            if (dv.c == Color.RED) {
                                dv.c = Color.BLUE;
                            } else {
                                dv.c = Color.RED;
                            }
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
        this.addContentView(fab, lp);
    }

}

class DrawingView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path  mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;
    int c = Color.RED;

    public DrawingView(Context c) {
        super(c);
        context=c;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8f);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(2f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath,  mPaint);
        canvas.drawPath(circlePath,  circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPaint.setColor(c);
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.lineTo(mX, mY);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 15, Path.Direction.CW);
        }
    }

    private void touch_up() {
        mPath.setLastPoint(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath,  mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
}
