package com.example.travelapp.core.ui.gridLayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.travelapp.core.utils.dimen.ScreenTools;

import java.util.List;

import static android.content.ContentValues.TAG;

public class NineGridLayout extends ViewGroup {

    private int gap = 5;//每张的间隔
    private int columns;//列数
    private int rows;//排数
    private List listData;
    private Context mContext;
    private int totalWidth;//总宽度
    public NineGridLayout(Context context) {
        super(context);
        mContext = context;
    }
    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        ScreenTools screenTools=ScreenTools.instance(getContext());
        //getScreenWidth()获取屏幕宽
        //得到总宽度密度
        totalWidth=screenTools.getScreenWidth()-screenTools.dip2px(80);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
    //自定义一个layoutChildrenView()用来为子view设定相对于夫view的位置
    private void layoutChildrenView(){
        //子view数量
        int childrenCount = listData.size();
        //每张图片宽度
        int singleWidth = (totalWidth - gap * (3 - 1)) / 3;

        int singleHeight = singleWidth;

        /**根据子view数量确定高度
        * 这个LayoutParams.getLayoutParams是针对在父控件中的View参数获取
         * * 其实这个LayoutParams类是用于child view（子视图） 向 parent view（父视图）
         * 传达自己的意愿的一个东西（孩子想变成什么样向其父亲说明）其实子视图父视图可以简单理解成
         *  一个LinearLayout 和 这个LinearLayout里边一个 TextView 的关系 TextView
         * 就算LinearLayout的子视图 child view 。
         * 需要注意的是LayoutParams只是ViewGroup的一个内部类这里边这个也就是ViewGroup里边这个LayoutParams类是
         * base class 基类实际上每个不同的ViewGroup都有自己的LayoutParams子类
         * ————————————————
         *如果还不能理解下边在来一段直白的说明：
         * LayoutParams继承于Android.View.ViewGroup.LayoutParams.
         * LayoutParams相当于一个Layout的信息包，它封装了Layout的位置、高、宽等信息。假设在屏幕上一块区域是由一个Layout占领的，如果将一个View添加到一个Layout中，最好告诉Layout用户期望的布局方式，也就是将一个认可的layoutParams传递进去。
         * 可以这样去形容LayoutParams，在象棋的棋盘上，每个棋子都占据一个位置，也就是每个棋子都有一个位置的信息，如这个棋子在4行4列，这里的“4行4列”就是棋子的LayoutParams。
         * ————————————————
         */
        //设置夫布局高度
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = singleHeight * rows + gap * (rows - 1);
        setLayoutParams(params);
        for (int i = 0; i < childrenCount; i++) {
            //CustomImageView继承自imageView，改写了Touch方法
            //getChildAt(i)在集合中返回指定位置的视图。
            CustomImageView childrenView = (CustomImageView) getChildAt(i);
            childrenView.setImageUrl(((ClassifyImage) listData.get(i)).getUrl());
            int[] position = findPosition(i);//返回第i张图片的位置坐标
            int left = (singleWidth + gap) * position[1];
            int top = (singleHeight + gap) * position[0];
            int right = left + singleWidth;
            int bottom = top + singleHeight;

            childrenView.layout(left, top, right, bottom);
        }

    }
    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }
    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }
    public void setImagesData(List<ClassifyImage> lists) {
        if (lists == null || lists.isEmpty()) {
            return;
        }
        //初始化布局
        generateChildrenLayout(lists.size());
        //这里做一个重用view的处理
        if (listData == null) {
            int i = 0;
            while (i < lists.size()) {
                CustomImageView iv = generateImageView(i);
                addView(iv,generateDefaultLayoutParams());
                i++;
            }
        } else {
            int oldViewCount = listData.size();
            int newViewCount = lists.size();
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = oldViewCount; i < newViewCount; i++) {
                    CustomImageView iv = generateImageView(i);
                    addView(iv,generateDefaultLayoutParams());
                }
            }
        }
        listData = lists;
        layoutChildrenView();
    }
    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * num	row	column
     * 1	   1	1
     * 2	   1	2
     * 3	   1	3
     * 4	   2	2
     * 5	   2	3
     * 6	   2	3
     * 7	   3	3
     * 8	   3	3
     * 9	   3	3
     *
     * @param length
     */
    //在添加数据的时候，我们要根据图片的个数来确定具体的布局情况
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
            if (length == 4) {
                columns = 2;
            }
        } else {
            rows = 3;
            columns = 3;
        }
    }

    private CustomImageView generateImageView(final int i) {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("123456", String.valueOf(i));
                String imgUrl = ((ClassifyImage) listData.get(i)).getUrl();
                Toast.makeText(mContext,"被点击了 :" + i, Toast.LENGTH_SHORT).show();
//                DialogShow.showDialog(mContext,imgUrl);
            }
        });
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        return iv;
    }

}
