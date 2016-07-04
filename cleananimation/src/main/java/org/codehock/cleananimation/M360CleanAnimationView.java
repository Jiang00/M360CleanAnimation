package org.codehock.cleananimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by 刚 on 2016/7/2.
 */
public class M360CleanAnimationView extends View {

    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mCleanPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextButtonShapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mCircleDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mProgressTextSize = 0;
    private float mTextButtonTextSize = 0;

    private float mCircleRadius = 0;
    private float mCircleRealRadius = 0;
    private float mCx, mCy;

    private float mDotY;

    private float mDotRadius;
    private float mDotRealRadius;

    private int mCleanTip;
    private String mCleanTipString = "";

    private boolean isStartRender = false;

    public M360CleanAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCleanTip = 0;

        mDotRealRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources().getDisplayMetrics());
        mProgressTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, context.getResources().getDisplayMetrics());
        mTextButtonTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, context.getResources().getDisplayMetrics());

        mDotY = -mDotRadius;

        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mTextButtonPaint.setColor(Color.WHITE);
        mTextButtonPaint.setTextSize(mTextButtonTextSize);

        mCircleDotPaint.setColor(Color.parseColor("#ffffff"));
        mCircleDotPaint.setStyle(Paint.Style.FILL);

        mCleanPointPaint.setColor(Color.parseColor("#33FFFFFF"));
        mCleanPointPaint.setStyle(Paint.Style.FILL);

        mProgressTextPaint.setColor(Color.WHITE);
        mProgressTextPaint.setTextSize(mProgressTextSize);

        mTextButtonShapePaint.setColor(Color.parseColor("#9aA9A9A9"));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCircleRealRadius = getHeight() / 2 - getHeight() / 4;
        mCx = getWidth() / 2;
        mCy = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.parseColor("#1C86EE"));

        /*绘制下落的小球*/
        drawDownDot(canvas);

        /*-绘制圆环-*/
        drawCircle(canvas);

        if (isStartRender) {
            /*-绘制数字进度-*/
            drawNumberProgress(canvas);

        /*-绘制文字按钮-*/
            drawTextButton(canvas);

        /*-绘制散落的垃圾碎片-*/
            drawMinCircles(canvas);
        }
    }

    /**
     * 设置清理状态数字
     *
     * @param number
     */
    public void setCleanNumber(int number) {
        mCleanTip = number;
        invalidate();
    }

    /**
     * 开始动画
     */
    public void startAnimation() {
        isStartRender = false;
        mCircleRadius = 0;

        invalidate();

        mDotRadius = mDotRealRadius;
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator animator = ValueAnimator.ofFloat(0, mCy);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mDotY = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mDotRadius = 0;
                isStartRender = true;
                invalidate();
            }
        });

        ValueAnimator animator2 = ValueAnimator.ofFloat(0, mCircleRealRadius);
        animator2.setDuration(300);
        animator2.setInterpolator(new AccelerateInterpolator());
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCircleRadius = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                invalidate();
            }
        });

        ValueAnimator animator3 = ValueAnimator.ofInt(0, mCleanTip);
        animator3.setDuration(300);
        animator3.setInterpolator(new AccelerateInterpolator());
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCleanTipString = String.valueOf(valueAnimator.getAnimatedValue());
                Log.e("info", "mCleanTipString : " + mCleanTipString);
                invalidate();
            }
        });
        animator3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                invalidate();
            }
        });

        animatorSet.play(animator).before(animator2);
        animatorSet.play(animator3).before(animator2);

        animatorSet.start();
    }

    /**
     * 绘制下落的小球
     *
     * @param canvas
     */
    private void drawDownDot(Canvas canvas) {
        canvas.drawCircle(mCx, mDotY, mDotRadius, mCircleDotPaint);
    }

    /**
     * 绘制清理的小碎片, 360手机助手的清理点位置是固定的
     *
     * @param canvas
     */
    private void drawMinCircles(Canvas canvas) {

        //确定一组点，这组点的位置在圆环的外围
        float left = mCx - mCircleRadius;
        float top = mCy - mCircleRadius;
        float right = mCx + mCircleRadius;
        float bottom = mCy + mCircleRadius;

        CleanPoint point1 = new CleanPoint(right - 20, top + 8, 5);
        canvas.drawCircle(point1.x, point1.y, point1.radius, mCleanPointPaint);

        CleanPoint point2 = new CleanPoint(left - 20, top + 8, 10);
        canvas.drawCircle(point2.x, point2.y, point2.radius, mCleanPointPaint);

        CleanPoint point3 = new CleanPoint(bottom + 40, left + 45, 10);
        canvas.drawCircle(point3.x, point3.y, point3.radius, mCleanPointPaint);

        CleanPoint point4 = new CleanPoint(left - 60, mCy, 5);
        canvas.drawCircle(point4.x, point4.y, point4.radius, mCleanPointPaint);

        CleanPoint point5 = new CleanPoint(right + 60, bottom, 6);
        canvas.drawCircle(point5.x, point5.y, point5.radius, mCleanPointPaint);

        CleanPoint point6 = new CleanPoint(left - 60, bottom, 10);
        canvas.drawCircle(point6.x, point6.y, point6.radius, mCleanPointPaint);

        CleanPoint point7 = new CleanPoint(right + 60, mCy, 8);
        canvas.drawCircle(point7.x, point7.y, point7.radius, mCleanPointPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    /**
     * 绘制文字按钮
     *
     * @param canvas
     */
    private void drawTextButton(Canvas canvas) {
        String text = "一键清理";
        Rect rect = new Rect();
        mTextButtonPaint.getTextBounds(text, 0, text.length(), rect);

        int strwid = rect.width();
        int strhei = rect.height();

        //绘制文字背景叠层
        RectF rectF = new RectF(mCx - strwid / 2 - 8, mCy + getHeight() / 8 - strhei - 4, mCx - strwid / 2 + strwid + 8, mCy + getHeight() / 8 + strhei / 2);
        canvas.drawRoundRect(rectF, 18, 18, mTextButtonShapePaint);
        canvas.drawText(text, mCx - strwid / 2, mCy + getHeight() / 8, mTextButtonPaint);
    }

    /**
     * 绘制数字进度条
     *
     * @param canvas
     */
    private void drawNumberProgress(Canvas canvas) {
        Rect rect = new Rect();
        mProgressTextPaint.getTextBounds(mCleanTipString, 0, mCleanTipString.length(), rect);

        int strwid = rect.width();
        int strhei = rect.height();
        canvas.drawText(mCleanTipString, mCx - strwid / 2, mCy, mProgressTextPaint);
    }

    /**
     * 绘制圆环
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(mCx, mCy, mCircleRadius, mCirclePaint);
    }

    static class CleanPoint {

        private float x, y, radius;

        public CleanPoint(float x, float y, float radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

    }
}
