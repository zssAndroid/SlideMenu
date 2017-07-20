package com.sen.slidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class SlidingMenu extends ViewGroup {

	private int downX;
	private boolean isShowMenu;// 是否展示左侧菜单，true是展示左侧菜单
	private Scroller scroller;
	private int downY;

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		scroller = new Scroller(getContext());
	}
	
	// 测量 widthMeasureSpec Activity的宽 heightMeasureSpec Activity的高
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 测量自己的宽高
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 测量孩子的宽高
		// 获取第一个孩子
		View menu = getChildAt(0);
		menu.measure(menu.getLayoutParams().width, heightMeasureSpec);
		// 获取第二个孩子
		View content = getChildAt(1);
		content.measure(widthMeasureSpec, heightMeasureSpec);
	}
	// 布局 给孩子设置位置 ，int l, int t, int r, int b 自己的左上点和右下点坐标
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// 设置左边菜单的位置
		View menu = getChildAt(0);
		menu.layout(-menu.getMeasuredWidth(), 0, 0, b);
		// 设置正文的位置
		View content = getChildAt(1);
		content.layout(l, t, r, b);
	}
	// 拦截事件
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		return super.onInterceptTouchEvent(ev);// 默认return false，不拦截事件
		// 只拦截左右滑动事件
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			
			int diffX = moveX - downX;
			int diffY = moveY - downY;
			if(Math.abs(diffX)>Math.abs(diffY)){// 左右滑动，拦截掉事件
				return true;
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	// 处理事件 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			scrollBy(-200, 0);
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getX();
			
			// 手指移动的距离
			int diffX = downX - moveX;
//			System.out.println("diffX:"+diffX);
			// 控制移动的边界
			// 获取当前控件移动的距离，相当于 0，0 原点
			int scrollX = getScrollX()+diffX;
			System.out.println("diffX:" + diffX + ":scrollX:"+scrollX);
			if(scrollX<-getChildAt(0).getMeasuredWidth()){// 如果超出最左边，直接移动到最左边
				scrollTo(-getChildAt(0).getMeasuredWidth(), 0);
			}else if(scrollX>0){// 如果超出最右边，直接移动到0位置
				scrollTo(0, 0);
			}else{
				// 让整个控件移动手指移动的距离
				scrollBy(diffX, 0);
			}
			// 给downX重新赋值
			downX = moveX;
			break;
		case MotionEvent.ACTION_UP:
//			scrollBy(-200, 0);
			// 手指抬起时，根据当前控件移动的位置，与左侧菜单的一半位置进行比较
			// 如果当前控件移动的位置>左侧菜单的一半位置，左侧菜单展示的只有一小半，让控件展示正文布局
			// 如果当前控件移动的位置<左侧菜单的一半位置，左侧菜单展示的只有一大半，让控件展示菜单布局
			int scrollX2 = getScrollX();
			isShowMenu = scrollX2<-getChildAt(0).getMeasuredWidth()/2;
			showMenu();
			break;

		default:
			break;
		}
		return true;// 消费掉事件
	}

	private void showMenu() {
//		if(isShowMenu){// 展示左侧菜单
//			scrollTo(-getChildAt(0).getMeasuredWidth(), 0);
//		}else{// 展示正文
//			scrollTo(0, 0);
//		}
		// startX 起点x坐标
		// startY 起点y坐标
		// dx dy 起点到终点的差值
		// duration 从起点移动一个差值 用时
		int startX = getScrollX();
		int dx = 0;
		if(isShowMenu){// 展示左侧菜单
			dx = -getChildAt(0).getMeasuredWidth()-startX;
		}else{// 展示正文
			dx = 0 - startX;
		}
		int duration = Math.abs(dx) * 10; 
		if(duration>1000){
			duration = 1000;
		}
		scroller.startScroll(startX, 0, dx, 0, duration);// 初始化平滑移动的数据
		invalidate();
	}
	// invalidate 会触发computeScroll
	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {// 开始计算下一个目的地，返回值 true说明移动到终点，false已经移动到终点
			int currX = scroller.getCurrX();// 获取到计算的目的地x坐标
			scrollTo(currX, 0);
			invalidate();
		}
	}
	
	// 提供打开关闭菜单方法
	public void toggle(){
		isShowMenu=!isShowMenu;
		showMenu();
	}

}
