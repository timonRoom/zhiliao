package com.learningword.myview;
import android.content.Context;  
import android.graphics.Matrix;  
import android.graphics.RectF;  
import android.graphics.drawable.Drawable;  
import android.util.AttributeSet;  
import android.view.MotionEvent;  
import android.view.ScaleGestureDetector;  
import android.view.View;  
import android.view.ViewTreeObserver;  
import android.widget.ImageView;  
 
/** 
* Created by 13798 on 2016/6/3. 
*/  
public class MyImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {  
   /** 
    * �ؼ���� 
    */  
   private int mWidth;  
   /** 
    * �ؼ��߶� 
    */  
   private int mHeight;  
   /** 
    * �õ�src��ͼƬ 
    */  
   private Drawable mDrawable;  
   /** 
    * ͼƬ��ȣ�ʹ��ǰ�ж�mDrawable�Ƿ�null�� 
    */  
   private int mDrawableWidth;  
   /** 
    * ͼƬ�߶ȣ�ʹ��ǰ�ж�mDrawable�Ƿ�null�� 
    */  
   private int mDrawableHeight;  
 
   /** 
    * ��ʼ������ֵ 
    */  
   private float mScale;  
 
   /** 
    * ˫��ͼƬ������ֵ 
    */  
   private float mDoubleClickScale;  
 
   /** 
    * ��������ֵ 
    */  
   private float mMaxScale;  
 
   /** 
    * ��С������ֵ 
    */  
   private float mMinScale;  
 
   private ScaleGestureDetector scaleGestureDetector;  
   /** 
    * ��ǰ��������ֵ��ƽ��ֵ�ľ��� 
    */  
   private Matrix matrix;  
 
   public MyImageView(Context context) {  
       this(context, null);  
   }  
 
   public MyImageView(Context context, AttributeSet attrs) {  
       this(context, attrs, 0);  
   }  
 
   public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {  
       super(context, attrs, defStyleAttr);  
       setOnTouchListener(this);  
       scaleGestureDetector = new ScaleGestureDetector(context, this);  
       initListener();  
   }  
 
 
   /** 
    * ��ʼ���¼����� 
    */  
   private void initListener() {  
       // ǿ������ģʽ  
       setScaleType(ScaleType.MATRIX);  
       // ��ӹ۲���  
       getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {  
           @Override  
           public void onGlobalLayout() {  
               // �Ƴ��۲���  
               getViewTreeObserver().removeGlobalOnLayoutListener(this);  
               // ��ȡ�ؼ���С  
               mWidth = getWidth();  
               mHeight = getHeight();  
 
               //ͨ��getDrawable���Src��ͼƬ  
               mDrawable = getDrawable();  
               if (mDrawable == null)  
                   return;  
               mDrawableWidth = mDrawable.getIntrinsicWidth();  
               mDrawableHeight = mDrawable.getIntrinsicHeight();  
               initImageViewSize();  
               moveToCenter();  
           }  
       });  
   }  
 
   /** 
    * ��ʼ����ԴͼƬ��� 
    */  
   private void initImageViewSize() {  
       if (mDrawable == null)  
           return;  
 
       // ����ֵ  
       float scale = 1.0f;  
       // ͼƬ��ȴ��ڿؼ���ȣ�ͼƬ�߶�С�ڿؼ��߶�  
       if (mDrawableWidth > mWidth && mDrawableHeight < mHeight)  
           scale = mWidth * 1.0f / mDrawableWidth;  
           // ͼƬ�߶ȶȴ��ڿؼ���ߣ�ͼƬ���С�ڿؼ����  
       else if (mDrawableHeight > mHeight && mDrawableWidth < mWidth)  
           scale = mHeight * 1.0f / mDrawableHeight;  
           // ͼƬ��ȴ��ڿؼ���ȣ�ͼƬ�߶ȴ��ڿؼ��߶�  
       else if (mDrawableHeight > mHeight && mDrawableWidth > mWidth)  
           scale = Math.min(mHeight * 1.0f / mDrawableHeight, mWidth * 1.0f / mDrawableWidth);  
           // ͼƬ���С�ڿؼ���ȣ�ͼƬ�߶�С�ڿؼ��߶�  
       else if (mDrawableHeight < mHeight && mDrawableWidth < mWidth)  
           scale = Math.min(mHeight * 1.0f / mDrawableHeight, mWidth * 1.0f / mDrawableWidth);  
       mScale = scale;  
       mMaxScale = mScale * 8.0f;  
       mMinScale = mScale * 0.5f;  
   }  
 
   /** 
    * �ƶ��ؼ��м�λ�� 
    */  
   private void moveToCenter() {  
       final float dx = mWidth / 2 - mDrawableWidth / 2;  
       final float dy = mHeight / 2 - mDrawableHeight / 2;  
       matrix = new Matrix();  
       // ƽ��������  
       matrix.postTranslate(dx, dy);  
       // �Կؼ�������Ϊ����  
       matrix.postScale(mScale, mScale, mWidth / 2, mHeight / 2);  
       setImageMatrix(matrix);  
   }  
 
   /** 
    * @return ��ǰ���ŵ�ֵ 
    */  
   private float getmScale() {  
       float[] floats = new float[9];  
       matrix.getValues(floats);  
       return floats[Matrix.MSCALE_X];  
   }  
 
   /** 
    * @param matrix ���� 
    * @return matrix�� l t b r ��width��height 
    */  
   private RectF getRectf(Matrix matrix) {  
       RectF f = new RectF();  
       if (mDrawable == null)  
           return null;  
       f.set(0, 0, mDrawableWidth, mDrawableHeight);  
       matrix.mapRect(f);  
       return f;  
   }  
 
   @Override  
   public boolean onScale(ScaleGestureDetector detector) {  
       if (mDrawable == null) {  
           return true;  
       }  
       // ϵͳ���������ֵ  
       float scaleFactor = detector.getScaleFactor();  
       // ��ȡ�Ѿ����ŵ�ֵ  
       float scale = getmScale();  
       float scaleResult = scale * scaleFactor;  
       if (scaleResult >= mMaxScale && scaleFactor > 1.0f)  
           scaleFactor = mMaxScale / scale;  
       if (scaleResult <= mMinScale && scaleFactor < 1.0f)  
           scaleFactor = mMinScale / scale;  
 
       matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());  
 
       RectF f = getRectf(matrix);  
       float dX = 0.0f;  
       float dY = 0.0f;  
       // ͼƬ�߶ȴ��ڿؼ��߶�  
       if (f.height() >= mHeight) {  
           // ͼƬ�������ֿհ�  
           if (f.top > 0) {  
               // �����ƶ�  
               dY = -f.top;  
           }  
           // ͼƬ�ײ����ֿհ�  
           if (f.bottom < mHeight) {  
               // �����ƶ�  
               dY = mHeight - f.bottom;  
           }  
       }  
       // ͼƬ��ȴ��ڿؼ����  
       if (f.width() >= mWidth) {  
           // ͼƬ��߳��ֿհ�  
           if (f.left > 0) {  
               // ������ƶ�  
               dX = -f.left;  
           }  
           // ͼƬ�ұ߳��ֿհ�  
           if (f.right < mWidth) {  
               // ���ұ��ƶ�  
               dX = mWidth - f.right;  
           }  
       }  
 
       if (f.width() < mWidth) {  
           dX = mWidth / 2 - f.right + f.width() / 2;  
       }  
 
       if (f.height() < mHeight) {  
           dY = mHeight / 2 - f.bottom + f.height() / 2;  
       }  
       matrix.postTranslate(dX, dY);  
       setImageMatrix(matrix);  
       return true;  
   }  
 
 
   @Override  
   public boolean onScaleBegin(ScaleGestureDetector detector) {  
       return true;  
   }  
 
   @Override  
   public void onScaleEnd(ScaleGestureDetector detector) {  
       float scale = getmScale();  
       if (scale < mScale) {  
           matrix.postScale(mScale / scale, mScale / scale, mWidth / 2, mHeight / 2);  
           setImageMatrix(matrix);  
       }  
   }  
 
 
   private float downX;  
   private float downY;  
   private float nowMovingX;  
   private float nowMovingY;  
   private float lastMovedX;  
   private float lastMovedY;  
   private boolean isFirstMoved = false;  
 
   @Override  
   public boolean onTouch(View v, MotionEvent event) {  
       switch (event.getAction() & MotionEvent.ACTION_MASK) {  
           case MotionEvent.ACTION_DOWN:  
               isFirstMoved = false;  
               downX = event.getX();  
               downY = event.getY();  
               break;  
           case MotionEvent.ACTION_POINTER_DOWN:  
               isFirstMoved = false;  
               break;  
           case MotionEvent.ACTION_MOVE:  
               nowMovingX = event.getX();  
               nowMovingY = event.getY();  
               if (!isFirstMoved) {  
                   isFirstMoved = true;  
                   lastMovedX = nowMovingX;  
                   lastMovedY = nowMovingY;  
               }  
               float dX = 0.0f;  
               float dY = 0.0f;  
               RectF rectf = getRectf(matrix);  
               // �жϻ�������  
               final float scrollX = nowMovingX - lastMovedX;  
               // �жϻ�������  
               final float scrollY = nowMovingY - lastMovedY;  
               // ͼƬ�߶ȴ��ڿؼ��߶�  
               if (rectf.height() > mHeight && canSmoothY()) {  
                   dY = nowMovingY - lastMovedY;  
               }  
 
               // ͼƬ��ȴ��ڿؼ����  
               if (rectf.width() > mWidth && canSmoothX()) {  
                   dX = nowMovingX - lastMovedX;  
               }  
               matrix.postTranslate(dX, dY);  
 
               remedyXAndY(dX,dY);  
 
               lastMovedX = nowMovingX;  
               lastMovedY = nowMovingY;  
               break;  
           case MotionEvent.ACTION_UP:  
               break;  
           case MotionEvent.ACTION_POINTER_UP:  
               isFirstMoved = false;  
               break;  
       }  
       return scaleGestureDetector.onTouchEvent(event);  
   }  
 
   /** 
    * �ж�x�������ܲ��ܻ��� 
    * @return ���Ի�������true 
    */  
   private boolean canSmoothX(){  
       RectF rectf = getRectf(matrix);  
       if (rectf.left >0 || rectf.right <getWidth())  
           return false;  
       return true;  
   }  
 
   /** 
    * �ж�y�����Ͽɲ����Ի��� 
    * @return ���Ի�������true 
    */  
   private boolean canSmoothY(){  
       RectF rectf = getRectf(matrix);  
       if (rectf.top>0 || rectf.bottom < getHeight())  
           return false;  
       return true;  
   }  
 
   /** 
    * ��������ĺ������ 
    * @param dx ����ƫ�Ƶĺ��� 
    * @param dy ���ֱ��˵����� 
    */  
   private void remedyXAndY(float dx,float dy){  
       if (!canSmoothX())  
           matrix.postTranslate(-dx,0);  
       if (!canSmoothY())  
           matrix.postTranslate(0,-dy);  
       setImageMatrix(matrix);  
   }  
}  