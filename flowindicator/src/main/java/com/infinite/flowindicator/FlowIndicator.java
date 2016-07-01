package com.infinite.flowindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by inf on 2016/6/30.
 */
public class FlowIndicator extends View {

    private static final int DEFAULT_LINE_COLOR = Color.WHITE;
    /**
     * 画线的宽度
     */
    private static final int DEFAULT_LINE_WIDTH = 6;
    /**
     * 每个步骤之间的间隔长度
     */
    private static final int FLOW_SPACING=60;
    private static final int RADIUS=20;
    /**
     * 布局最小高度
     */
    private static final int MIN_HEIGHT=2*RADIUS;
    private int mLineColor;
    private Paint mLinePaint;
    private int mFlowSize;
    private int mHeight;
    private int mWidth;


    public FlowIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowIndicator);
        mLineColor = array.getColor(R.styleable.FlowIndicator_lineColor, DEFAULT_LINE_COLOR);
        array.recycle();
        init();
    }

    public FlowIndicator(Context context) {
        super(context);
        init();
    }

    private void init(){
        mLinePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
    }

    public void setFlow(String[] flow){
        mFlowSize= flow.length;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode==MeasureSpec.AT_MOST){
            //宽度，元素数量*(圆直径+直线长度)-一条直线的长度
            width=mFlowSize*(2*RADIUS+FLOW_SPACING)-FLOW_SPACING;
        }

        if (heightMode==MeasureSpec.AT_MOST){
            height=MIN_HEIGHT;
        }else if (heightMode==MeasureSpec.EXACTLY){
            if (height<MIN_HEIGHT){
                height=MIN_HEIGHT;
            }
        }
        width+=getPaddingLeft()+getPaddingRight();
        height+=getPaddingTop()+getPaddingBottom();
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //初始水平方向的偏移量
        int xShift=RADIUS+getPaddingLeft();

        for(int i=0;i<mFlowSize;i++){
            canvas.drawCircle(xShift,getHeight()/2,RADIUS,mLinePaint);
            //画圆后，偏移量增加一个半径的长度，准备画直线
            xShift+=RADIUS;
            //如果是最后一个圆则跳出循环，不再画直线
            if (i==mFlowSize-1)
                return;
            canvas.drawLine(xShift,getHeight()/2,xShift+FLOW_SPACING,getHeight()/2,mLinePaint);
            // 画直线后，偏移量增加直线的长度+一个半径，准备画下一个圆
            xShift+=FLOW_SPACING+RADIUS;
        }


    }
}
