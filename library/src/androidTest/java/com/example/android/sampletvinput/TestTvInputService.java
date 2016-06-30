/*
 * Copyright 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sampletvinput;

import android.media.tv.TvInputService;
import android.support.annotation.Nullable;

/**
 * Dummy TvInputService that may be called for testing purposes
 */
public class TestTvInputService extends TvInputService {
    @Nullable
    @Override
    public Session onCreateSession(String inputId) {
        return null;
    }
}