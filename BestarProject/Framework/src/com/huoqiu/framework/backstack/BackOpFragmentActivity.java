package com.huoqiu.framework.backstack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;

import com.huoqiu.framework.exception.BaseUncaughtExceptionHandler;
import com.huoqiu.framework.util.StringUtil;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

public abstract class BackOpFragmentActivity extends FragmentActivity implements BackOpController {
    public final static String IW_PACKAGE_NAME = "com.manyi.lovehouse";
    public static String PACKAGE_NAME;
    private String currentStackKey = "default";

    private HashMap<String, Stack<Op>> stacks = new HashMap<String, Stack<Op>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentStackKey = this.getClass().getSimpleName();
        System.out.println("currentStackKey : " + currentStackKey);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        BaseUncaughtExceptionHandler baseUncaughtExceptionHandler = BaseUncaughtExceptionHandler.getInstance();
        baseUncaughtExceptionHandler.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//		BaseUncaughtExceptionHandler baseUncaughtExceptionHandler = BaseUncaughtExceptionHandler.getInstance();
//      baseUncaughtExceptionHandler.init(this);
    }

    @Override
    public void onBackPressed() {
        Op op = pop();
        if (op != null) {
            op.perform(this);
        } else {
            super.onBackPressed();
        }
    }

    public String getCurrentStackKey() {
        return currentStackKey;
    }

    public void setCurrentStackKey(String currentStackKey) {
        this.currentStackKey = currentStackKey;
    }

    public HashMap<String, Stack<Op>> getStacks() {
        return stacks;
    }

    public void setStacks(HashMap<String, Stack<Op>> stacks) {
        this.stacks = stacks;
    }

    @Override
    public void push(Op op) {
        if (!getStacks().containsKey(getCurrentStackKey())) {
            getStacks().put(currentStackKey, new Stack<Op>());
        }
        getStacks().get(getCurrentStackKey()).push(op);
    }

    @Override
    public Op pop() {
        try {
            Stack<Op> ops = getStacks().get(getCurrentStackKey());
            if (ops == null || ops.size() == 0)
                return null;
            else {
                return ops.pop();
            }
        } catch (EmptyStackException e) {
            Log.i(this.getClass().getName(), e.getMessage(), e);
            return null;
        }
    }

    public Op pop(String tag) {
        try {
            Stack<Op> ops = getStacks().get(getCurrentStackKey());
            if (ops == null)
                return null;
            else {
                Op op = ops.pop();
                if (op.getTag().equals(tag)) {
                    return op;
                } else {
                    return pop(tag);
                }
            }
        } catch (EmptyStackException e) {
            Log.i(this.getClass().getName(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 指定队列,调用退出方法直到找到指定的那个tag,用于先切换tab,再后台进行fragment回退操作.//徐斌.2014.7.7
     *
     * @param tag      要退出的tag
     * @param stackKey 队列的tag
     * @return
     */
    public Op pop(String tag, String stackKey) {
        try {
            Stack<Op> ops = getStacks().get(stackKey);
            if (ops == null)
                return null;
            else {
                Op op = ops.pop();
                if (op.getTag().equals(tag))
                    return op;
                else {
                    return pop(tag);
                }
            }
        } catch (EmptyStackException e) {
            Log.i(this.getClass().getName(), e.getMessage(), e);
            return null;
        }

    }

    /**
     * 指定队列,调用退出方法直到找到指定的那个tag,用于先切换tab,再后台进行fragment回退操作.
     *
     * @param stackKey 队列的tag
     * @return
     */
    public Op rePop(String stackKey) {
        try {
            Stack<Op> ops = getStacks().get(stackKey);
            if (ops == null || ops.size() == 0)
                return null;
            else {
                Op op = ops.pop();
                if (ops.size() == 0) {
                    return op;
                } else {
                    return rePop(stackKey);
                }
            }
        } catch (EmptyStackException e) {
            Log.i(this.getClass().getName(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 弹出当前栈里所有的fragment by xiongwei
     */
    public void popAll() {
        try {
            Stack<Op> ops = getStacks().get(getCurrentStackKey());
            if (ops == null || ops.size() == 0)
                return;
            else {
                Op op = ops.pop();
                if (null != op) {
                    op.perform(this);
                }
                popAll();
            }
        } catch (EmptyStackException e) {
            Log.i(this.getClass().getName(), e.getMessage(), e);
        }
    }

    @Override
    public Op peek() {
        try {
            Stack<Op> ops = getStacks().get(getCurrentStackKey());
            if (ops == null) {
                return null;
            } else {
                return ops.peek();
            }
        } catch (EmptyStackException e) {
            Log.i(this.getClass().getName(), e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() > 0) {
            return false;
        }
        /** 进入返回按钮的逻辑 */
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /** 拿到fragmenmanager */
            FragmentManager fm = getSupportFragmentManager();

            /** 先判断一下当前Activity对应栈中有几个op */
            Stack<Op> ops = getStacks().get(getCurrentStackKey());
            /** 有多个op */
            if (null != ops && ops.size() > 0) {
                /** 首先拿到Activity中对应的当前栈的顶部的Fragment句柄，也就是op */
                Op op = peek();
                /** 首先判空 */
                if (null != op) {
                    String tag = op.getTag();
                    if (!StringUtil.isEmptyOrNull(tag) && null != fm.findFragmentByTag(tag)) {
                        Fragment topFragment = fm.findFragmentByTag(tag);
                        if (topFragment instanceof BackOpFragment) {
                            if (((BackOpFragment) topFragment).canFragmentGoback(0)) {
                                onBackPressed();
                                return true;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回上一页的调用分两个入口，系统的返回按钮和应用顶部的返回按钮
     * <p/>
     * 这个接口提供给应用中顶部Title中的返回按钮调用，其他的地方不要调用。
     */
    public void clickBackTitleButton() {
        /** 拿到fragmenmanager */
        FragmentManager fm = getSupportFragmentManager();

        /** 先判断一下当前Activity对应栈中有几个op */
        Stack<Op> ops = getStacks().get(getCurrentStackKey());
        /** 有多个op */
        if (null != ops && ops.size() > 0) {
            /** 首先拿到Activity中对应的当前栈的顶部的Fragment句柄，也就是op */
            Op op = peek();
            /** 首先判空 */
            if (null != op) {
                String tag = op.getTag();
                if (!StringUtil.isEmptyOrNull(tag) && null != fm.findFragmentByTag(tag)) {
                    Fragment topFragment = fm.findFragmentByTag(tag);
                    if (topFragment instanceof BackOpFragment) {
                        if (((BackOpFragment) topFragment).canFragmentGoback(1)) {
                            onBackPressed();
                            return;
                        } else {
                            return;
                        }
                    }
                }
            }
        }
        onBackPressed();
    }

    // /***
    // * 在Activity接收到返回事件时，如果栈里只有一个fragment，如果fragment支持返回，就要检查Activity有没有拦截事件
    // *
    // * 如果子类希望在栈里只有一个fragment时，拦截返回键事件，处理相关事情，必须重写本方法
    // *
    // * @param from
    // * 返回事件的来源 0表示系统的返回按钮 1代表应用内顶部的返回上一页按钮
    // * @return true代表当前页面可以被销毁 false代表当前页面存在未完成的事情，不能返回
    // *
    // */
    // public boolean canActivityGoback(int from) {
    // return true;
    // }

}