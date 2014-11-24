package com.poplar.goo.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.poplar.goo.R;
import com.poplar.goo.ui.MyShapeDrawable.OnDisappearListener;
import com.poplar.goo.util.Utils;

public class GooViewListener implements OnTouchListener, OnDisappearListener {

	private WindowManager mWm;
	private WindowManager.LayoutParams mParams;
	private MyShapeDrawable mGooView;
	private View point;
	private int number;
	private final Context mContext;

	private Handler mHandler;

	public GooViewListener(Context mContext, View point) {
		this.mContext = mContext;
		this.point = point;
		this.number = (Integer) point.getTag();

		mGooView = new MyShapeDrawable(mContext);

		mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mParams = new WindowManager.LayoutParams();
		mParams.format = PixelFormat.TRANSLUCENT;
		
		mHandler= new Handler(mContext.getMainLooper());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		int action = MotionEventCompat.getActionMasked(event);
		if (action == MotionEvent.ACTION_DOWN) {

			ViewParent parent = v.getParent();
			parent.requestDisallowInterceptTouchEvent(true);

			point.setVisibility(View.INVISIBLE);

			Log.d("TAG",
					"rawX: " + event.getRawX() + " rawY: " + event.getRawY());

			mGooView.setStatusBarHeight(Utils.getStatusBarHeight(v));
			mGooView.setNumber(number);
			mGooView.initCenter(event.getRawX(), event.getRawY());
			mGooView.setOnDisappearListener(this);

			mWm.addView(mGooView, mParams);
		}
		mGooView.onTouchEvent(event);
		return true;
	}

	@Override
	public void onDisappear(PointF mDragCenter) {
		if (mWm != null && mGooView.getParent() != null) {
			mWm.removeView(mGooView);
			ImageView imageView = new ImageView(mContext);
			imageView.setImageResource(R.drawable.anim_bubble_pop);
			AnimationDrawable mAnimDrawable = (AnimationDrawable) imageView
					.getDrawable();

			final BubbleLayout bubbleLayout = new BubbleLayout(mContext);
			bubbleLayout.setCenter((int) mDragCenter.x, (int) mDragCenter.y
					- Utils.getStatusBarHeight(mGooView));
			bubbleLayout.addView(imageView, new FrameLayout.LayoutParams(
					android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
					android.widget.FrameLayout.LayoutParams.WRAP_CONTENT));

			mWm.addView(bubbleLayout, mParams);
			mAnimDrawable.start();

			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mWm.removeView(bubbleLayout);
				}
			}, 501);
		}

	}

	@Override
	public void onReset(boolean isOutOfRange) {
		if (mWm != null && mGooView.getParent() != null) {
			mWm.removeView(mGooView);
		}
	}
};