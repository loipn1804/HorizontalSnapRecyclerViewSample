package phanloi.horizontalsnaprecyclerviewsample;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Copyright (c) 2017, VNG Corp. All rights reserved.
 *
 * @author Lio <loipn@vng.com.vn>
 * @version 1.0
 * @since July 30, 2017
 */

public class HorizontalSnapRecyclerView extends RecyclerView {

    private boolean mIsFlingArchived = false;
    private int mCurrentPosition;

    private boolean mIsSnap = true;
    private boolean mIsFling = true;

    public HorizontalSnapRecyclerView(Context context) {
        super(context);
    }

    public HorizontalSnapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HorizontalSnapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HorizontalSnapRecyclerView,
                0, 0);

        try {
            mIsSnap = a.getBoolean(R.styleable.HorizontalSnapRecyclerView_snap, true);
            mIsFling = a.getBoolean(R.styleable.HorizontalSnapRecyclerView_fling, true);
        } finally {
            a.recycle();
        }
    }

    public boolean isSnap() {
        return mIsSnap;
    }

    public void setSnap(boolean snap) {
        mIsSnap = snap;
    }

    public boolean isFling() {
        return mIsFling;
    }

    public void setFling(boolean fling) {
        mIsFling = fling;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        if (!mIsSnap || !mIsFling) return super.fling(velocityX, velocityY);

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

        int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastPosition = linearLayoutManager.findLastVisibleItemPosition();

        int destPosition;
        if (Math.abs(velocityX) > 1000) { // fling fast
            if (velocityX > 0) { // fling to right
                if (lastPosition - firstPosition <= 1) {
                    destPosition = lastPosition;
                } else {
                    if ((lastPosition - firstPosition) % 2 == 0) {
                        destPosition = (lastPosition + firstPosition) / 2;

                        if (destPosition > 0) {
                            View destView = linearLayoutManager.findViewByPosition(destPosition);
                            if (destView.getLeft() > 0) {
                                if ((destView.getLeft() + destView.getRight()) / 2 < (screenWidth / 2)) {
                                    destPosition = destPosition + 1;
                                }
                            }
                        }
                    } else {
                        destPosition = (lastPosition + firstPosition) / 2 + 1;
                    }
                }
            } else { // fling to left
                if (lastPosition - firstPosition <= 1) {
                    destPosition = firstPosition;
                } else {
                    if ((lastPosition - firstPosition) % 2 == 0) {
                        destPosition = (lastPosition + firstPosition) / 2;

                        if (destPosition < linearLayoutManager.getItemCount() - 1) {
                            View destView = linearLayoutManager.findViewByPosition(destPosition);
                            if (destView.getRight() < screenWidth) {
                                if ((destView.getLeft() + destView.getRight()) / 2 > screenWidth / 2) {
                                    destPosition = destPosition - 1;
                                }
                            }
                        }
                    } else {
                        destPosition = (lastPosition + firstPosition) / 2;
                    }
                }
            }
        } else { // fling slow
            if (lastPosition - firstPosition <= 1) {
                View firstView = linearLayoutManager.findViewByPosition(firstPosition);
                if (firstView.getRight() > screenWidth / 2) {
                    destPosition = firstPosition;
                } else {
                    destPosition = lastPosition;
                }
            } else {
                if ((lastPosition - firstPosition) % 2 == 0) {
                    destPosition = (lastPosition + firstPosition) / 2;
                } else {
                    View firstView = linearLayoutManager.findViewByPosition((lastPosition + firstPosition) / 2);
                    if (firstView.getRight() > screenWidth / 2) {
                        destPosition = (lastPosition + firstPosition) / 2;
                    } else {
                        destPosition = (lastPosition + firstPosition) / 2 + 1;
                    }
                }
            }
        }

        View destView = linearLayoutManager.findViewByPosition(destPosition);
        int distance = destView.getLeft() - (screenWidth - destView.getWidth()) / 2;
        smoothScrollBy(distance, 0);

        mCurrentPosition = destPosition;

        mIsFlingArchived = true;

        return true;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (!mIsSnap) return;

        // If you tap on the phone while the RecyclerView is scrolling it will stop in the middle.
        // This code fixes this. This code is not strictly necessary but it improves the behaviour.

        if (state == SCROLL_STATE_IDLE) {
            if (!mIsFlingArchived) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();

                int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

                int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int lastPosition = linearLayoutManager.findLastVisibleItemPosition();

                int destPosition;
                if (lastPosition - firstPosition <= 1) {
                    View firstView = linearLayoutManager.findViewByPosition(firstPosition);
                    if (firstView.getRight() > screenWidth / 2) {
                        destPosition = firstPosition;
                    } else {
                        destPosition = lastPosition;
                    }
                } else {
                    if ((lastPosition - firstPosition) % 2 == 0) {
                        destPosition = (lastPosition + firstPosition) / 2;
                    } else {
                        View firstView = linearLayoutManager.findViewByPosition((lastPosition + firstPosition) / 2);
                        if (firstView.getRight() > screenWidth / 2) {
                            destPosition = (lastPosition + firstPosition) / 2;
                        } else {
                            destPosition = (lastPosition + firstPosition) / 2 + 1;
                        }
                    }
                }

                View destView = linearLayoutManager.findViewByPosition(destPosition);
                int distance = destView.getLeft() - (screenWidth - destView.getWidth()) / 2;
                if (mCurrentPosition != destPosition || (mCurrentPosition != 0 && mCurrentPosition != linearLayoutManager.getItemCount() - 1)) {
                    smoothScrollBy(distance, 0);
                }

                mCurrentPosition = destPosition;
            } else {
                mIsFlingArchived = false;
            }
        }
    }
}
