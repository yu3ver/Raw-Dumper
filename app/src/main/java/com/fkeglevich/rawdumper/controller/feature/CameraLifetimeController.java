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

package com.fkeglevich.rawdumper.controller.feature;

import android.content.DialogInterface;
import android.view.View;

import com.fkeglevich.rawdumper.R;
import com.fkeglevich.rawdumper.activity.ActivityReference;
import com.fkeglevich.rawdumper.camera.async.CameraManager;
import com.fkeglevich.rawdumper.camera.async.TurboCamera;
import com.fkeglevich.rawdumper.camera.exception.CameraPatchRequiredException;
import com.fkeglevich.rawdumper.camera.exception.RawIsUnavailableException;
import com.fkeglevich.rawdumper.ui.CameraPreviewTexture;
import com.fkeglevich.rawdumper.ui.activity.FullscreenManager;
import com.fkeglevich.rawdumper.ui.dialog.FatalErrorDialog;
import com.fkeglevich.rawdumper.ui.dialog.OkDialog;
import com.fkeglevich.rawdumper.util.Nothing;
import com.fkeglevich.rawdumper.util.event.EventListener;
import com.fkeglevich.rawdumper.util.exception.MessageException;

/**
 * TODO: Add class header
 * <p>
 * Created by Flávio Keglevich on 28/10/17.
 */

public class CameraLifetimeController
{
    private final EventListener<Nothing> pauseListener = new EventListener<Nothing>()
    {
        @Override
        public void onEvent(Nothing eventData)
        {
            cameraManager.closeCamera();
            textureView.setAlpha(0);
        }
    };

    private final ActivityReference reference;
    private final FullscreenManager fullscreenManager;
    private final FeatureControllerManager featureControllerManager;
    private final CameraPreviewTexture textureView;
    private final CameraManager cameraManager;
    private final SwitchButtonController switchButtonController;

    public CameraLifetimeController(ActivityReference reference)
    {
        this.reference = reference;
        setupActivityListeners();
        this.fullscreenManager          = new FullscreenManager(reference);
        this.featureControllerManager   = new FeatureControllerManager();
        featureControllerManager.createControllers(reference);
        this.textureView                = getTextureView(reference);
        this.cameraManager              = new CameraManager(reference, textureView);
        setupCameraManager();
        this.switchButtonController     = createSwitchButtonController();
    }

    private void setupActivityListeners()
    {
        reference.onResume.addListener(new EventListener<Nothing>()
        {
            @Override
            public void onEvent(Nothing eventData)
            {
                cameraManager.openCamera();
            }
        });
    }

    private void setupCameraManager()
    {
        cameraManager.onCameraOpened.addListener(new EventListener<TurboCamera>()
        {
            @Override
            public void onEvent(TurboCamera eventData)
            {
                onCameraOpened(eventData);
            }
        });
        cameraManager.onCameraException.addListener(new EventListener<MessageException>() {
            @Override
            public void onEvent(MessageException eventData)
            {
                if (eventData instanceof RawIsUnavailableException || eventData instanceof CameraPatchRequiredException)
                    OkDialog.show(reference, eventData,
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    cameraManager.selectNextCamera();
                                    cameraManager.openCamera();
                                }
                            });
                else
                    FatalErrorDialog.show(reference, eventData);
            }
        });
    }

    private void onCameraOpened(TurboCamera turboCamera)
    {
        fullscreenManager.goToFullscreenMode();

        reference.onPause.removeListener(pauseListener);
        reference.onPause.addListener(pauseListener);

        textureView.setupPreview(turboCamera);
        textureView.startOpenCameraAnimation();

        featureControllerManager.setupControllers(turboCamera, cameraManager.onCameraClosed);
        switchButtonController.setupFeature(turboCamera, cameraManager.onCameraClosed);
    }

    private CameraPreviewTexture getTextureView(ActivityReference reference)
    {
        return (CameraPreviewTexture) reference.weaklyGet().findViewById(R.id.textureView);
    }

    private SwitchButtonController createSwitchButtonController()
    {
        View switchButton = reference.weaklyGet().findViewById(R.id.camSwitchButton);
        CameraPreviewTexture textureView = (CameraPreviewTexture) reference.weaklyGet().findViewById(R.id.textureView);
        return new SwitchButtonController(switchButton, cameraManager, textureView);
    }
}
