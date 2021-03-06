/*
 * Copyright 2017, Flávio Keglevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fkeglevich.rawdumper.util;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * TODO: Add class header
 * <p>
 * Created by Flávio Keglevich on 10/10/17.
 */

public class TimeLogger
{
    public static final String TAG = "TimeLogger";
    public static final int CALL_STACK_INDEX = 4;

    private final long nanoTime;

    public TimeLogger()
    {
        nanoTime = System.nanoTime();
    }

    public void log(String methodName)
    {
        Log.i(TAG, methodName + " time: " + ((System.nanoTime() - nanoTime)/1000000d) + " ms");
    }

    public void log(Method enclosingMethod)
    {
        log(enclosingMethod.getName());
    }

    public void log()
    {
        log(getCallingMethodName());
    }

    private String getCallingMethodName()
    {
        return Thread.currentThread().getStackTrace()[CALL_STACK_INDEX].getMethodName();
    }
}
