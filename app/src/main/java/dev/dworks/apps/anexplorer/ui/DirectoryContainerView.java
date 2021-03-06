/*
 * Copyright (C) 2014 Hari Krishna Dulipudi
 * Copyright (C) 2013 The Android Open Source Project
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

package dev.dworks.apps.anexplorer.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;

import dev.dworks.apps.anexplorer.misc.CrashReportingManager;

public class DirectoryContainerView extends FrameLayout {
	private boolean mDisappearingFirst = false;
    protected ArrayList<View> mDisappearingChildren;

    public DirectoryContainerView(Context context) {
        super(context);
        setClipChildren(false);
    }

    public DirectoryContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void dispatchDraw(Canvas canvas) {
    	try {
    	    Field field = ViewGroup.class.getDeclaredField("mDisappearingChildren");
    	    field.setAccessible(true);
    	    mDisappearingChildren = (ArrayList<View>) field.get(this);
    	} catch (Exception e) {
            CrashReportingManager.logException(e);
    	}
        final ArrayList<View> disappearing = mDisappearingChildren;
        if (mDisappearingFirst && disappearing != null) {
            for (int i = 0; i < disappearing.size(); i++) {
                super.drawChild(canvas, disappearing.get(i), getDrawingTime());
            }
        }
        super.dispatchDraw(canvas);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (mDisappearingFirst && mDisappearingChildren != null
                && mDisappearingChildren.contains(child)) {
            return false;
        }
        return super.drawChild(canvas, child, drawingTime);
    }

    public void setDrawDisappearingFirst(boolean disappearingFirst) {
        mDisappearingFirst = disappearingFirst;
    }
}