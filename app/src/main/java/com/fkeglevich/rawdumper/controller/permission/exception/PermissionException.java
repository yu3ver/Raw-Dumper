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

package com.fkeglevich.rawdumper.controller.permission.exception;

import android.content.Context;

import com.fkeglevich.rawdumper.R;
import com.fkeglevich.rawdumper.util.exception.MessageException;

/**
 * Created by Flávio Keglevich on 03/09/2017.
 * TODO: Add a class header comment!
 */

public class PermissionException extends MessageException
{
    @Override
    public String getMessageResource(Context context)
    {
        return context.getResources().getString(R.string.permission_error);
    }
}
