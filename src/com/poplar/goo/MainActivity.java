package com.poplar.goo;

import java.util.HashSet;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.poplar.goo.R;
import com.poplar.goo.MyShapeDrawable.OnDisappearListener;
import com.poplar.goo.domain.Cheeses;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_touch_screen);
		setListAdapter(new MyAdapter(this, R.layout.layout_list_item,
				android.R.id.text1, Cheeses.sCheeseStrings));

	}

	class MyAdapter extends ArrayAdapter<String> {

		private WindowManager mWm;
		private WindowManager.LayoutParams mParams;
		MyShapeDrawable mGooView;
		HashSet<Integer> mRemoved = new HashSet<Integer>();

		public MyAdapter(Context context, int resource, int textViewResourceId,
				String[] objects) {
			super(context, resource, textViewResourceId, objects);

			mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			mParams = new WindowManager.LayoutParams();
			mParams.format = PixelFormat.TRANSLUCENT;
			mGooView = new MyShapeDrawable(MainActivity.this);

		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);

			TextView point = (TextView) view.findViewById(R.id.point);
			boolean visiable = !mRemoved.contains(position);
			point.setVisibility(visiable ? View.VISIBLE : View.GONE);
			if (visiable) {
				point.setText(String.valueOf(position));
				point.setOnTouchListener(new MyTouchListener(point, position));
			}

			return view;
		}

		class MyTouchListener implements OnTouchListener {

			private View point;
			private int number;

			public MyTouchListener(View point, int number) {
				this.point = point;
				this.number = number;
			}

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int action = MotionEventCompat.getActionMasked(event);
				if (action == MotionEvent.ACTION_DOWN) {

					ViewParent parent = v.getParent();
					parent.requestDisallowInterceptTouchEvent(true);

					point.setVisibility(View.INVISIBLE);

					Log.d("TAG", "rawX: " + event.getRawX() + " rawY: " + event.getRawY());
					
					mGooView.setStatusBarHeight(getStatusBarHeight(v));
					mGooView.setNumber(number);
					
					mGooView.initCenter(event.getRawX(), event.getRawY());
					mGooView.setOnDisappearListener(mListener);

					mWm.addView(mGooView, mParams);
				}
				mGooView.onTouchEvent(event);
				return true;
			}

			OnDisappearListener mListener = new OnDisappearListener() {

				@Override
				public void onDisappear() {
					if (mWm != null&& mGooView.getParent() != null) {
						mWm.removeView(mGooView);
					}
					mRemoved.add(number);
					notifyDataSetChanged();

					showToast(getContext(), "Cheers! We have get rid of it!");
				}

				@Override
				public void onReset(boolean isOutOfRange) {
					if (mWm != null && mGooView.getParent() != null) {
						mWm.removeView(mGooView);
					}
					notifyDataSetChanged();
					
					showToast(getContext(), isOutOfRange ? "Are you regret?" : "Try again!");
				}
			};
		};

	}
	public int getStatusBarHeight(View v){
		Rect frame = new Rect();
		v.getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}
	
	public Toast mToast;
	public void showToast(Context mContext, String msg){
		if (mToast == null){
			mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		}
		mToast.setText(msg);
		mToast.show();
	}

}
