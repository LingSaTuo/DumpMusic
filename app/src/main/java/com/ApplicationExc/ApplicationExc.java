package com.ApplicationExc;

import android.content.Context;
import android.content.Intent;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Administrator on 2016/12/15.
 */

public class ApplicationExc implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultHandler;
    public static ApplicationExc exceptionHandler;
    private  boolean inited = false ;
    private Context context;
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        StringWriter sw= new StringWriter();
        PrintWriter pw=new PrintWriter(sw);
        e.printStackTrace(pw);
       String errlog= sw.toString();
        Intent i = new Intent(context,BugActivity.class);
        i.putExtra("BUG",errlog);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    public ApplicationExc(){}
    public static ApplicationExc getInstance(){
        if(exceptionHandler==null){
            exceptionHandler=new ApplicationExc();
        }
        return  exceptionHandler;
    }

    public void init(Context context){
        if(inited)return;
        this.context=context;
        defaultHandler=Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
}
