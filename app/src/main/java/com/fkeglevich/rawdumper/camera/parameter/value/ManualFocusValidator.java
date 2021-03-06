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

package com.fkeglevich.rawdumper.camera.parameter.value;

import android.support.annotation.NonNull;

import com.fkeglevich.rawdumper.camera.data.ManualFocus;
import com.fkeglevich.rawdumper.camera.data.ManualFocusRange;
import com.fkeglevich.rawdumper.camera.extension.AsusParameters;
import com.fkeglevich.rawdumper.camera.parameter.ParameterCollection;

/**
 * TODO: Add class header
 * <p>
 * Created by Flávio Keglevich on 29/10/17.
 */

public class ManualFocusValidator implements ValueValidator<ManualFocus, ManualFocusRange>
{
    private final ManualFocusRange focusRange;

    @NonNull
    public static ManualFocusValidator create(ParameterCollection parameterCollection)
    {
        return new ManualFocusValidator(parameterCollection.get(AsusParameters.MANUAL_FOCUS_RANGE));
    }

    private ManualFocusValidator(ManualFocusRange focusRange)
    {
        this.focusRange = focusRange;
    }

    @Override
    public boolean isAvailable()
    {
        return focusRange != null;
    }

    @Override
    public boolean isValid(ManualFocus value)
    {
        return focusRange.contains(value);
    }

    @Override
    public ManualFocusRange getAvailableValues()
    {
        return focusRange;
    }
}
