package org.eslack.fixomate;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class FixOmate implements IXposedHookLoadPackage {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        findAndHookMethod("android.view.MotionEvent", lpparam.classLoader, "getPointerCount", new XC_MethodReplacement() {
            @Override
		protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
			//XposedBridge.log("PAU: getPointerCount() Hook in place");
			return 1;
		}
        });

    }
}
