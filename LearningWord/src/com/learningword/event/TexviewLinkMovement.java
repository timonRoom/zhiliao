package com.learningword.event;

import com.learningword.activity.ZihuDetailactivity;
import com.learningword.model.MessageSpan;

import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

public class TexviewLinkMovement extends LinkMovementMethod {
	private static TexviewLinkMovement sInstance;
	private Handler handler = null;
	private Class spanClass = null;
	private int hander_what=-1;
	public static TexviewLinkMovement getInstance(Handler _handler,int _hander_what,Class _spanClass) {
		if (sInstance == null) {
			sInstance = new TexviewLinkMovement();
			((TexviewLinkMovement) sInstance).handler = _handler;
			((TexviewLinkMovement) sInstance).spanClass = _spanClass;
			((TexviewLinkMovement) sInstance).hander_what = _hander_what;
		} 
		return sInstance; 
	}
	public static void disInstance() {
		sInstance = null;
	}
	int x1;
	int x2;
	int y1;
	int y2;
	@Override
	public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			x1 = (int) event.getX();
			y1 = (int) event.getY(); 
			} 
		if (event.getAction() == MotionEvent.ACTION_UP) {
			x2 = (int) event.getX();
			y2 = (int) event.getY();
			if (Math.abs(x1 - x2) < 10 && Math.abs(y1 - y2) < 10) {
				x2 -= widget.getTotalPaddingLeft();
				y2 -= widget.getTotalPaddingTop();
				x2 += widget.getScrollX(); 
				y2 += widget.getScrollY(); 
				Layout layout = widget.getLayout(); 
				int line = layout.getLineForVertical(y2); 
				int off = layout.getOffsetForHorizontal(line, x2);
				/**             * get you interest span             */
				Object[] spans = buffer.getSpans(off, off, spanClass);
				if (spans.length != 0) {
					Selection.setSelection(buffer, buffer.getSpanStart(spans[0]), buffer.getSpanEnd(spans[0]));
					MessageSpan obj = new MessageSpan();
					obj.setObj(spans); obj.setView(widget);
					Message message = handler.obtainMessage();
					message.obj = obj;
					message.what = hander_what;
					message.sendToTarget();
					return true; }
				}
			}
		return super.onTouchEvent(widget, buffer, event);
	}

}
