package org.eslack.fixomate;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;
import java.lang.reflect.Method;
import java.lang.Class;


public class FixOmate implements IXposedHookLoadPackage {
/*

 void printSamples(MotionEvent ev) {
     final int historySize = ev.getHistorySize();
     final int pointerCount = ev.getPointerCount();
     for (int h = 0; h < historySize; h++) {
         XposedBridge.log("PAU: At time " + ev.getHistoricalEventTime(h));
         for (int p = 0; p < pointerCount; p++) {
             XposedBridge.log("PAU: ---->  pointer " + ev.getPointerId(p) + ": (" + ev.getHistoricalX(p, h) +"," + ev.getHistoricalY(p, h) + ")");
         }
     }
     XposedBridge.log("PAU: At time " + ev.getEventTime());
     for (int p = 0; p < pointerCount; p++) {
         XposedBridge.log("PAU: ---->  pointer " + ev.getPointerId(p) + ": (" + ev.getX(p) + "," + ev.getY(p) + ")");
     }
 }
*/
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {


       final XC_MethodHook gpcHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
		//XposedBridge.log("PAU: getPointerCount() Hook in place");
		param.setResult(1);
            }
        };

       final XC_MethodHook fixAction = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    MotionEvent event = (MotionEvent)param.args[0];
                    if(event.getDevice().getName().equals("mtk-tpd")) { 
		    	//XposedBridge.log("PAU: " + event.toString() + " getAction=" + event.getAction());
			if (event.getAction()==261 || event.getAction()==517) {
		    		//XposedBridge.log("PAU: ^^^ changed to ACTION_MOVE");
				event.setAction(MotionEvent.ACTION_MOVE);
				param.setResult(true);
			}
		    }
            }
        };


        findAndHookMethod("android.view.MotionEvent", lpparam.classLoader, "getPointerCount", gpcHook);

        findAndHookMethod("android.view.View", lpparam.classLoader, "dispatchTouchEvent", MotionEvent.class, fixAction);
        findAndHookMethod("android.app.Activity", lpparam.classLoader, "dispatchTouchEvent", MotionEvent.class, fixAction);

        findAndHookMethod("android.view.View", lpparam.classLoader, "onTouchEvent", MotionEvent.class, fixAction);
        findAndHookMethod("android.app.Activity", lpparam.classLoader, "onTouchEvent", MotionEvent.class, fixAction);

        findAndHookMethod("android.view.View", lpparam.classLoader, "dispatchGenericMotionEvent", MotionEvent.class, fixAction);
        findAndHookMethod("android.app.Activity", lpparam.classLoader, "dispatchGenericMotionEvent", MotionEvent.class, fixAction);

/*
        findAndHookMethod("android.view.MotionEvent", lpparam.classLoader, "getPointerCount", new XC_MethodReplacement() {
            @Override
		protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
			//XposedBridge.log("PAU: getPointerCount() Hook in place");
			param.setResult(1);
			return 1;
		}
        });
*/


    }
}
