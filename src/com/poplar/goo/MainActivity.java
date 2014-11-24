package com.poplar.goo;

import java.util.HashSet;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.poplar.goo.domain.Cheeses;
import com.poplar.goo.ui.GooViewListener;
import com.poplar.goo.util.Utils;

public class MainActivity extends ListActivity {

	private MediaPlayer mPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_touch_screen);
		setListAdapter(new MyAdapter(this, R.layout.layout_list_item,
				android.R.id.text1, Cheeses.sCheeseStrings));

		mPlayer = MediaPlayer.create(this, R.raw.burst);
		mPlayer.setVolume(0.4f, 0.4f);
	}

	class MyAdapter extends ArrayAdapter<String> {

		HashSet<Integer> mRemoved = new HashSet<Integer>();

		public MyAdapter(Context context, int resource, int textViewResourceId,
				String[] objects) {
			super(context, resource, textViewResourceId, objects);

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = super.getView(position, convertView, parent);

			TextView mUnreadView = (TextView) view.findViewById(R.id.point);
			boolean visiable = !mRemoved.contains(position);
			mUnreadView.setVisibility(visiable ? View.VISIBLE : View.GONE);

			if (visiable) {
				mUnreadView.setText(String.valueOf(position));
				mUnreadView.setTag(position);
				GooViewListener mGooListener = new GooViewListener(
						getContext(), mUnreadView) {
					@Override
					public void onDisappear(PointF mDragCenter) {
						super.onDisappear(mDragCenter);

						mPlayer.start();
						mRemoved.add(position);
						notifyDataSetChanged();
						Utils.showToast(getContext(),
								"Cheers! We have get rid of it!");
					}

					@Override
					public void onReset(boolean isOutOfRange) {
						super.onReset(isOutOfRange);

						notifyDataSetChanged();
						Utils.showToast(getContext(),
								isOutOfRange ? "Are you regret?" : "Try again!");
					}
				};
				mUnreadView.setOnTouchListener(mGooListener);
			}
			return view;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPlayer != null) {
			mPlayer.release();
		}
	}

}
