package com.infinite.flowindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private int mCurrentStep;

    private Bitmap mCompleteBmp,mInProcessingBmp,mTodoBmp;

    private int mCompleteRes,mInProcessingRes,mTodoRes;


    public FlowIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowIndicator);
        mLineColor = array.getColor(R.styleable.FlowIndicator_lineColor, DEFAULT_LINE_COLOR);
        mCompleteRes=array.getResourceId(R.styleable.FlowIndicator_complete_icon,0);
        mInProcessingRes=array.getResourceId(R.styleable.FlowIndicator_in_process_icon,0);
        mTodoRes=array.getResourceId(R.styleable.FlowIndicator_todo_icon,0);
        array.recycle();
        init();
    }

    public FlowIndicator(Context context) {
        super(context);
        init();
    }

    private void init(){
        mCompleteBmp= BitmapFactory.decodeResource(getResources(),mCompleteRes);
        mInProcessingBmp= BitmapFactory.decodeResource(getResources(),mInProcessingRes);
        mTodoBmp= BitmapFactory.decodeResource(getResources(),mTodoRes);
        mLinePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(DEFAULT_LINE_WIDTH);
    }

    public void setFlow(String[] flow){
        mFlowSize= flow.length;
        invalidate();
    }

    public void setFlow(String[] flow,int inProcessing){
        mFlowSize= flow.length;
        mCurrentStep=inProcessing;
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
            width=mFlowSize*(mCompleteBmp.getWidth()+FLOW_SPACING)-FLOW_SPACING;
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
        int xShift;
        xShift=getPaddingLeft();
        for(int i=0;i<mFlowSize;i++){

            if (i<mCurrentStep){
                canvas.drawBitmap(mCompleteBmp,xShift,(getHeight()-mCompleteBmp.getHeight())/2,null);
                xShift+=mCompleteBmp.getWidth();
            }else if(i==mCurrentStep){
                canvas.drawBitmap(mInProcessingBmp,xShift,(getHeight()-mCompleteBmp.getHeight())/2,null);
                xShift+=mInProcessingBmp.getWidth();
            }else {
                canvas.drawBitmap(mTodoBmp,xShift,(getHeight()-mCompleteBmp.getHeight())/2,null);
                xShift+=mTodoBmp.getWidth();
            }

            //画圆后，偏移量增加一个半径的长度，准备画直线

            //如果是最后一个圆则跳出循环，不再画直线
            if (i==mFlowSize-1)
                return;
            canvas.drawLine(xShift,(getHeight())/2,xShift+FLOW_SPACING,(getHeight())/2,mLinePaint);
            // 画直线后，偏移量增加直线的长度+一个半径，准备画下一个圆
            xShift+=FLOW_SPACING;
        }


    }
}
