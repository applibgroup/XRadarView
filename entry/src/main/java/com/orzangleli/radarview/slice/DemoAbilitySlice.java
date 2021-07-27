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
