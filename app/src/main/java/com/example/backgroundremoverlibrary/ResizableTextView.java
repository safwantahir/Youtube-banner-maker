package com.example.backgroundremoverlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.appcompat.widget.AppCompatTextView;

public class ResizableTextView extends AppCompatTextView {

    private float lastTouchX;
    private float lastTouchY;

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

    public ResizableTextView(Context context) {
        super(context);
        init(context);
    }

    public ResizableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);

        float x = event.getRawX();
        float y = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = x;
                lastTouchY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaX = x - lastTouchX;
                float deltaY = y - lastTouchY;

                setX(getX() + deltaX);
                setY(getY() + deltaY);

                lastTouchX = x;
                lastTouchY = y;
                break;

            default:
                return super.onTouchEvent(event);
        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            // Apply scaling to the TextView
            setScaleX(scaleFactor);
            setScaleY(scaleFactor);

            return true;
        }
    }
}

