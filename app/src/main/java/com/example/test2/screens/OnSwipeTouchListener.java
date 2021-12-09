package com.example.test2.screens;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * part of UI this class is responsible for tracking swipes on the app
 */
class OnSwipeTouchListener implements View.OnTouchListener {
    private final GestureDetector gestureDetector;
    OnSwipeTouchListener(Context c) {
        gestureDetector = new GestureDetector(c, new GestureListener());
    }
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    /**
     * we are declaring a class within a class, this is inevitable as it is the only way to have
     * the gesture listener be able to access the screen inputs of this view. We know that this
     * also goes against the single responsiblity principle but this is part of the core of android
     * and there is no other way to implement it.
     */
    private final class GestureListener extends
            GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            onClick();
            return super.onSingleTapUp(e);
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick();
            return super.onDoubleTap(e);
        }
        @Override
        public void onLongPress(MotionEvent e) {
            onLongClick();
            super.onLongPress(e);
        }

        /**
         * checks if the swipe has passed the thresh hold and if it has it calls swipe right or
         * swipe left on the home screen view class
         * @param e1 starting position of the card
         * @param e2 ending position of the card
         * @param velocityX velocity of the x movement
         * @param velocityY velocity of the y movement
         * @return returns false if the motion event does not count as a swipe
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }
    }
    public void onSwipeRight() {
    }
    public void onSwipeLeft() {
    }
    private void onClick() {
    }
    private void onDoubleClick() {
    }
    private void onLongClick() {
    }
}