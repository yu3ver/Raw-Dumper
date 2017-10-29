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

package com.fkeglevich.rawdumper.camera.async.impl;

import com.fkeglevich.rawdumper.camera.async.Closeable;
import com.fkeglevich.rawdumper.camera.async.TurboCamera;
import com.fkeglevich.rawdumper.camera.async.direct.LowLevelCamera;
import com.fkeglevich.rawdumper.camera.data.CaptureSize;
import com.fkeglevich.rawdumper.camera.data.Ev;
import com.fkeglevich.rawdumper.camera.data.Flash;
import com.fkeglevich.rawdumper.camera.data.Iso;
import com.fkeglevich.rawdumper.camera.data.ShutterSpeed;
import com.fkeglevich.rawdumper.camera.feature.Feature;
import com.fkeglevich.rawdumper.camera.feature.FeatureRecyclerFactory;
import com.fkeglevich.rawdumper.camera.feature.FocusFeature;
import com.fkeglevich.rawdumper.camera.feature.ManualFocusFeature;
import com.fkeglevich.rawdumper.camera.feature.WritableFeature;
import com.fkeglevich.rawdumper.camera.feature.restriction.ExposureRestriction;
import com.fkeglevich.rawdumper.camera.feature.restriction.FocusRestriction;
import com.fkeglevich.rawdumper.util.Nullable;

import java.util.List;

/**
 * TODO: Add class header
 * <p>
 * Created by Flávio Keglevich on 03/10/17.
 */

public class TurboCameraImpl implements TurboCamera, Closeable
{
    private final LowLevelCamera lowLevelCamera;
    private final FeatureRecyclerFactory recyclerFactory;

    private WritableFeature<Iso, List<Iso>>                     isoFeature;
    private Feature<Nullable<Iso>>                              isoMeteringFeature;
    private WritableFeature<ShutterSpeed, List<ShutterSpeed>>   shutterSpeedFeature;
    private Feature<Nullable<ShutterSpeed>>                     ssMeteringFeature;
    private WritableFeature<Ev, List<Ev>>                       evFeature;
    private WritableFeature<Flash, List<Flash>>                 flashFeature;
    private Feature<CaptureSize>                                previewFeature;
    private FocusFeature                                        focusFeature;
    private ManualFocusFeature                                  manualFocusFeature;

    private ExposureRestriction                                 exposureRestriction;
    private FocusRestriction                                    focusRestriction;

    public TurboCameraImpl(LowLevelCamera lowLevelCamera)
    {
        this.lowLevelCamera = lowLevelCamera;
        this.recyclerFactory = new FeatureRecyclerFactory(  lowLevelCamera.getCameraContext(),
                                                            lowLevelCamera.getParameterCollection(),
                                                            lowLevelCamera.getCameraActions());
        createFeatures();
        createRestrictions();
    }

    private void createFeatures()
    {
        isoFeature          = recyclerFactory.createIsoFeature();
        isoMeteringFeature  = recyclerFactory.createIsoMeteringFeature();
        shutterSpeedFeature = recyclerFactory.createShutterSpeedFeature();
        ssMeteringFeature   = recyclerFactory.createSSMeteringFeature();
        evFeature           = recyclerFactory.createEVFeature();
        flashFeature        = recyclerFactory.createFlashFeature();
        previewFeature      = recyclerFactory.createPreviewFeature();
        focusFeature        = recyclerFactory.createFocusFeature();
        manualFocusFeature  = recyclerFactory.createManualFocusFeature();
    }

    private void createRestrictions()
    {
        exposureRestriction = new ExposureRestriction(isoFeature, shutterSpeedFeature, evFeature);
        focusRestriction    = new FocusRestriction(focusFeature, manualFocusFeature);
    }

    @Override
    public WritableFeature<Iso, List<Iso>> getIsoFeature()
    {
        return isoFeature;
    }

    @Override
    public Feature<Nullable<Iso>> getIsoMeteringFeature()
    {
        return isoMeteringFeature;
    }

    @Override
    public WritableFeature<ShutterSpeed, List<ShutterSpeed>> getShutterSpeedFeature()
    {
        return shutterSpeedFeature;
    }

    @Override
    public Feature<Nullable<ShutterSpeed>> getSSMeteringFeature()
    {
        return ssMeteringFeature;
    }

    @Override
    public WritableFeature<Ev, List<Ev>> getEVFeature()
    {
        return evFeature;
    }

    @Override
    public WritableFeature<Flash, List<Flash>> getFlashFeature()
    {
        return flashFeature;
    }

    @Override
    public FocusFeature getFocusFeature()
    {
        return focusFeature;
    }

    @Override
    public ManualFocusFeature getManualFocusFeature()
    {
        return manualFocusFeature;
    }

    @Override
    public Feature<CaptureSize> getPreviewFeature()
    {
        return previewFeature;
    }

    @Override
    public void close()
    {
        recyclerFactory.cleanUpAllFeatures();
        lowLevelCamera.close();
    }
}
