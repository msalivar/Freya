package com.example.cil.freya;

import android.app.Application;
import android.content.Context;

/**
 * Created by cil on 3/31/16.
 */
public class StaticContextHandler extends Application
{
        private static Context context;

    @Override
        public void onCreate() {
            super.onCreate();
            StaticContextHandler.context = getApplicationContext();
        }

        public static Context getAppContext() {
            return StaticContextHandler.context;
        }
}
