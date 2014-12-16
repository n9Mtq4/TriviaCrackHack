package com.n9mtq4.xposed.preguntados;

import android.content.Context;
import android.widget.Toast;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.getIntField;

/**
 * Created by Will on 12/15/14.
 */
public class PreguntadosHook implements IXposedHookLoadPackage {
	
	private Context context;
	
	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpp) throws Throwable {
		
//		makes sure the code below only runs if you have triviacrack open
		if ((!lpp.packageName.equals("com.etermax.preguntados.lite"))
				&& (!lpp.packageName.equals("com.etermax.preguntados.pro"))) return;
		
//		hook into their application
		findAndHookMethod("com.etermax.gamescommon.EtermaxGamesApplication", lpp.classLoader, "onCreate", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//				Steal their context (for later)
				context = (Context) param.thisObject;
//				notify the user that the hack is running
				Toast.makeText(context, "TriviaCrack Hack\nGives you answers!", Toast.LENGTH_LONG).show();
			}
		});
		
//		hook into the question
		findAndHookMethod("com.etermax.preguntados.datasource.dto.QuestionDTO", lpp.classLoader, "getText", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//				get answer
				int index = getIntField(param.thisObject, "correct_answer");
//				turn it into A B C or D
				String choice = (new String[] {"A", "B", "C", "D"})[index];
//				tell the user what the answer is
				Toast.makeText(context, choice, Toast.LENGTH_LONG).show();
			}
		});
		
	}
	
}
