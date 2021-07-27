
/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package com.orzangleli.radarview.slice;

import com.orzangleli.radar.XRadarView;
import com.orzangleli.radarview.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * Demo ability slice is to show high imitation paln hero alliance aPP of Radar Map.
 */
public class DemoAbilitySlice extends AbilitySlice {
    private static final String TAG = DemoAbilitySlice.class.getSimpleName();
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD000F00, TAG);

    XRadarView xRadarView;
    final String[] titles = new String[]{"Kill", "Money", "Defense", "Magic", "Physics", "Assists", "Survival"};
    final double[] percents = new double[]{1.0, 0.46, 0.63, 0.75, 0.5, 0.9, 0.26};
    final int[] drawables = new int[]{
        ResourceTable.Media_icon,
        ResourceTable.Media_icon,
        ResourceTable.Media_icon,
        ResourceTable.Media_icon,
        ResourceTable.Media_icon,
        ResourceTable.Media_icon,
        ResourceTable.Media_icon};


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_demo);

        HiLog.info(LABEL_LOG, "%{public}s", "On start method call");
        xRadarView = (XRadarView) findComponentById(ResourceTable.Id_radarView1);
        xRadarView.setTitles(titles);
        xRadarView.setPercents(percents);
        xRadarView.setDrawables(drawables);
    }
}
