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

package com.fkeglevich.rawdumper.camera.shared;

import android.hardware.Camera;

import com.fkeglevich.rawdumper.camera.exception.CameraOpenException;
import com.fkeglevich.rawdumper.camera.extension.ICameraExtension;
import com.fkeglevich.rawdumper.camera.extension.IntelCameraExtensionLoader;
import com.fkeglevich.rawdumper.raw.info.CameraInfo;

/**
 * Created by Flávio Keglevich on 09/08/2017.
 * TODO: Add a class header comment!
 */

public class SharedCameraOpener
{
    private final int cameraId;
    private final SharedData sharedData;

    public SharedCameraOpener(int cameraId, SharedData sharedData)
    {
        this.cameraId   = cameraId;
        this.sharedData = sharedData;
    }

    public SharedCamera open() throws CameraOpenException
    {
        try
        {
            ICameraExtension cameraExtension    = getCameraExtension();
            Camera.CameraInfo cameraInfo        = getCameraInfo();
            CameraInfo rawCameraInfo            = getRawCameraInfo();
            return new SharedCamera(cameraExtension, cameraInfo, cameraId, rawCameraInfo);
        }
        catch (RuntimeException re)
        {
            throw new CameraOpenException();
        }
    }

    private ICameraExtension getCameraExtension()
    {
        return IntelCameraExtensionLoader.extendedOpenCamera(sharedData.getApplicationContext(), cameraId);
    }

    private Camera.CameraInfo getCameraInfo()
    {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        return cameraInfo;
    }

    private CameraInfo getRawCameraInfo()
    {
        return sharedData.getDeviceInfo().getCameras()[cameraId];
    }
}