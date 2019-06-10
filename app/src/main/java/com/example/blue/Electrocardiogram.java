package com.example.blue;

/**
 * Created by 陈泳吉 on 2019-05-07.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 陈泳吉 on 2019-05-07.
 */

public class Electrocardiogram extends View {

    //private float gap_x;    //两点间横坐标间距
    //private int dataNum_per_grid = 18;   //每小格内的数据个数
    private int xori;//原点x坐标

    public  static float num2 =0;
    public  int last_x=20;

    public static  int initial_data=0; //存放初始位
    public static float draw_data=0;   //存放画图位
    public float last_data=405;

    private final static String TAG = Electrocardiogram.class.getSimpleName();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
    };

    private boolean mIsDrawGird = true;                    // 是否画网格
    private Paint paint;
    private Paint electrocarPaint;                         // 画心电图曲线的画笔
    private Path electrocarPath;
    // 心电图曲线的轨迹 path

    private Paint getPaint_circle_1;
    private Paint Paint_circle_1;
    private Path Path_circle_1;

    private Paint getPaint_circle_2;
    private Paint Paint_circle_2;
    private Path Path_circle_2;


    private int width;
    private int height;
    private static int baseLine;                                  // 基准线

    private int horizontalBigGirdNum;    //= 6;                       // 横向的线，即纵向大格子的数量，每个大格子里面包含5个小格子
    private int verticalBigGirdNum;        //= 8;                       // 纵向的线，即横向大格子的数量。
    private int widthOfSmallGird;                          // 小格子的宽度

    private static List<Float> datas = new ArrayList<Float>();
    private static List<Float> electrocardDatas = new ArrayList<Float>();

    private int index = 0;



    public Electrocardiogram(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Electrocardiogram(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();                            // 画网格的 Paint
        paint.setStyle(Paint.Style.STROKE);
        electrocarPaint = new Paint();                    // 画心电图曲线的 Paint
        electrocarPaint.setColor(Color.BLACK);
        electrocarPaint.setStyle(Paint.Style.STROKE);
        electrocarPaint.setAntiAlias(true);             //抗锯齿
        electrocarPaint.setStrokeWidth(7);
        electrocarPath = new Path();                    // 心电图的轨迹 Path


        getPaint_circle_1 = new Paint();                            // 画圆的 Paint1
        getPaint_circle_1.setStyle(Paint.Style.STROKE);
        Paint_circle_1= new Paint();
        Paint_circle_1.setColor(Color.RED);
        Paint_circle_1.setStyle(Paint.Style.STROKE);
        Paint_circle_1.setAntiAlias(true);             //抗锯齿
        Paint_circle_1.setStrokeWidth(3);
        Path_circle_1= new Path();                    // 心电图的轨迹 Path

        getPaint_circle_2 = new Paint();                            // 画圆的 Paint1
        getPaint_circle_2.setStyle(Paint.Style.STROKE);
        Paint_circle_2= new Paint();
        Paint_circle_2.setColor(Color.GREEN);
        Paint_circle_2.setStyle(Paint.Style.STROKE);
        Paint_circle_2.setAntiAlias(true);             //抗锯齿
        Paint_circle_2.setStrokeWidth(3);
        Path_circle_2= new Path();                    // 心电图的轨迹 Path


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged");
        xori = 0;
        widthOfSmallGird = 18;
        width = w;     //竖屏1080  横屏1920
        height = h;  //=810
        horizontalBigGirdNum = height / widthOfSmallGird;    //横线条数
        verticalBigGirdNum = width / widthOfSmallGird;      //纵线条数   =60
       // gap_x = widthOfSmallGird / dataNum_per_grid;   //两点间横坐标间距
        //widthOfSmallGird = width / (verticalBigGirdNum * 10); // 小网格的宽度，每个大网格有 5 个小网格
        baseLine = height / 2;                // 基准线位于中间
        //maxLevel = height / 3;				// 心电图曲线最大的高度
        setData();                            // 设置数据
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw");
        Log.e(TAG,width+ "**************");
        /*
            scrollBy(1, 0);
            postInvalidateDelayed(10);
      */
       // setTranslationX(10);


        drawElectrocardiogram(canvas);        // 画心电图

    }


    /**
     * 画心电图曲线
     */
    private void drawElectrocardiogram(Canvas canvas) {

  if(ClentSendActivity.i ==0)
        {

            electrocarPath.moveTo(last_x, 405);
            electrocarPath.lineTo(last_x, last_data);

        }
        else if(ClentSendActivity.i ==1){
   /*         if(draw_data == 1)
            {
                Path_circle_1.addCircle(last_x, last_data, 10, Path.Direction.CCW);
                Path_circle_1.addRect(last_x-20,last_data-50,last_x+20,last_data+50,Path.Direction.CCW);
            }
            if(draw_data == 2)
            {
                Path_circle_2.addCircle(last_x, last_data, 10, Path.Direction.CCW);
                Path_circle_2.addRect(last_x-20,last_data-50,last_x+20,last_data+50,Path.Direction.CCW);
            }*/
            Log.e(TAG, last_x + "**********************");
            electrocarPath.moveTo(last_x,last_data);
            electrocarPath.lineTo(last_x+10, electrocardDatas.get(ClentSendActivity.i-1));
            last_x=last_x+10;
           if(draw_data == 1)
            {
                Path_circle_1.addCircle(last_x, electrocardDatas.get(ClentSendActivity.i-1), 10, Path.Direction.CCW);
                Path_circle_1.addRect(last_x-40,electrocardDatas.get(ClentSendActivity.i-1)-100,last_x+40,electrocardDatas.get(ClentSendActivity.i-1)+100,Path.Direction.CCW);
            }
            if(draw_data == 2)
            {
                Path_circle_2.addCircle(last_x, electrocardDatas.get(ClentSendActivity.i-1), 10, Path.Direction.CCW);
                Path_circle_2.addRect(last_x-40,electrocardDatas.get(ClentSendActivity.i-1)-100,last_x+40,electrocardDatas.get(ClentSendActivity.i-1)+100,Path.Direction.CCW);
            }
        }
        else {
            electrocarPath.moveTo(last_x,electrocardDatas.get(ClentSendActivity.i-2));
            electrocarPath.lineTo(last_x+10, electrocardDatas.get(ClentSendActivity.i-1));
            last_x=last_x+10;

     // scrollTo(10, 0);
      Log.e(TAG,  "88888888888888888888888888888888888888888888888888888888");

           if(draw_data == 1)
            {
                Path_circle_1.addCircle(last_x, electrocardDatas.get(ClentSendActivity.i-1), 10, Path.Direction.CCW);
                Path_circle_1.addRect(last_x-40,electrocardDatas.get(ClentSendActivity.i-1)-100,last_x+40,electrocardDatas.get(ClentSendActivity.i-1)+100,Path.Direction.CCW);
            }
            if(draw_data == 2)
            {
                Path_circle_2.addCircle(last_x, electrocardDatas.get(ClentSendActivity.i-1), 10, Path.Direction.CCW);
                Path_circle_2.addRect(last_x-40,electrocardDatas.get(ClentSendActivity.i-1)-100,last_x+40,electrocardDatas.get(ClentSendActivity.i-1)+100,Path.Direction.CCW);
            }
//1080
     // if ((last_x) % 540 == 0){
      if (electrocardDatas.size()>100){
          last_data=electrocardDatas.get(ClentSendActivity.i-1);
          electrocardDatas.clear();
          //electrocarPath.reset();
          // Path_circle_1.reset();
          // Path_circle_2.reset();
          datas.clear();
          ClentSendActivity.i=0;
            }



        }
      /*  for (int i = 0; i < electrocardDatas.size(); i++) {
            electrocarPath.moveTo(ClentSendActivity.i * gap_x *5, electrocardDatas.get(i));
            break;
        }
      if(num2 !=0) {
          electrocarPath.lineTo(ClentSendActivity.i * gap_x *2, electrocardDatas.get(ClentSendActivity.i-1));
    }
*/




        canvas.drawPath(electrocarPath, electrocarPaint);
        canvas.drawPath(Path_circle_1, Paint_circle_1);
        canvas.drawPath(Path_circle_2, Paint_circle_2);
    }


    /**
     * 增加数据，使心电图呈现由左到右显示出波形的效果
     */
    public void addData() {
        if(datas.size() >= 0) {
            // datas 是收集到的数据， electrocardDatas 是显示在屏幕的数据，两者都是 ArrayList<Float>
            generateElectrocar();
            electrocardDatas.add(datas.get(ClentSendActivity.i));
            Log.e(TAG, electrocardDatas + "5555555555");


  /*          else if (index >= datas.size() - 1){
                index = 0;
                electrocardDatas.clear();
                electrocarPath.reset();
                datas.clear();
                generateElectrocar();
            }
            else {
                index++;
            }*/
//
            if(last_x>1000) {   // heng  1800
               if(last_x==2000) {
                    scrollBy(-990,0);//    heng  8190  cha 1810
                    last_x=20;

                  electrocarPath.reset();
                    Path_circle_1.reset();
                    Path_circle_2.reset();

                }
                else {
                    scrollBy(10, 0);
              }
            }

            invalidate();
        }

    }


    Runnable runnable;

    public void startDraw() {

        addData();
    }


    public void setData() {
        generateElectrocar();
        //  System.out.println();
        Log.e(TAG, datas + "");
    }

    public  void getnum(float num)
    {
        num2= num;
        Log.e(TAG, last_x + "@@@@@@@@@@@@@111111111@@@@@@@@@@@111111111111111111");
        startDraw();
    }


    //获取数据并转换
    public void generateElectrocar() {

        if(num2 !=0){
            draw_data=num2%10;
            num2=(int)num2/10;
            num2 = (num2 - 400) * (-1);
            num2 = num2 * 3 / 4 + baseLine;

            datas.add(num2);



        }
    }

}