package com.poplar.goo.util;

import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

public class Utils {
	
	public static String getActionName(MotionEvent event){
		String action = "unknow";
		switch (MotionEventCompat.getActionMasked(event)) {
			case MotionEvent.ACTION_DOWN:
				action = "ACTION_DOWN";
				break;
			case MotionEvent.ACTION_MOVE:
				action = "ACTION_MOVE";
				break;
			case MotionEvent.ACTION_UP:
				action = "ACTION_UP";
				break;
			case MotionEvent.ACTION_CANCEL:
				action = "ACTION_CANCEL";
				break;
			case MotionEvent.ACTION_SCROLL:
				action = "ACTION_SCROLL";
				break;
			case MotionEvent.ACTION_OUTSIDE:
				action = "ACTION_SCROLL";
				break;
			default:
				break;
		}
		return action;
	}
}
