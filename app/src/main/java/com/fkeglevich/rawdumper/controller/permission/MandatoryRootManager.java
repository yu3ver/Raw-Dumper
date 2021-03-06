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

package com.fkeglevich.rawdumper.controller.permission;

import com.fkeglevich.rawdumper.controller.permission.exception.RootAccessException;
import com.fkeglevich.rawdumper.su.ShellManager;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by Flávio Keglevich on 09/08/2017.
 * TODO: Add a class header comment!
 */

public class MandatoryRootManager extends MandatoryPermissionManager
{
    @Override
    void dispatchPermissionsGranted()
    {
        if (!ShellManager.getInstance().isRunning())
        {
            ShellManager.getInstance().open(new Shell.OnCommandResultListener()
            {
                @Override
                public void onCommandResult(int commandCode, int exitCode, List<String> output)
                {
                    if (exitCode == Shell.OnCommandResultListener.SHELL_RUNNING)
                        MandatoryRootManager.super.dispatchPermissionsGranted();
                    else
                        dispatchMissingPermissions(new RootAccessException());
                }
            });
        }
        else
            super.dispatchPermissionsGranted();
    }
}
