
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

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import com.orzangleli.radar.Node;
import com.orzangleli.radar.XRadarView;
import com.orzangleli.radarview.ResourceTable;
import java.util.ArrayList;

/**
 * Demo ability slice is to show high imitation paln hero alliance aPP of Radar Map.
 */
public class DemoAbilitySlice extends AbilitySlice {
    private static final String TAG = DemoAbilitySlice.class.getSimpleName();
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD000F00, TAG);

    XRadarView xRadarView;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_demo);

        HiLog.info(LABEL_LOG, "%{public}s", "On start method call");
        xRadarView = (XRadarView) findComponentById(ResourceTable.Id_radarView1);
        xRadarView.setNodeList(getNodeList());
    }

    private ArrayList<Node> getNodeList() {
        ArrayList<Node> nodeList = new ArrayList<>();

        Node node1 = new Node();
        node1.setTitle("Node 1");
        node1.setPercent(1.0);
        node1.setIconRef(ResourceTable.Media_icon);
        nodeList.add(node1);

        Node node2 = new Node();
        node2.setTitle("Node 2");
        node2.setPercent(0.72);
        node2.setIconRef(ResourceTable.Media_icon);
        nodeList.add(node2);

        Node node3 = new Node();
        node3.setTitle("Node 3");
        node3.setPercent(0.63);
        node3.setIconRef(ResourceTable.Media_icon);
        nodeList.add(node3);

        Node node4 = new Node();
        node4.setTitle("Node 4");
        node4.setPercent(0.75);
        node4.setIconRef(ResourceTable.Media_icon);
        nodeList.add(node4);

        Node node5 = new Node();
        node5.setTitle("Node 5");
        node5.setPercent(0.2);
        node5.setIconRef(ResourceTable.Media_icon);
        nodeList.add(node5);

        Node node6 = new Node();
        node6.setTitle("Node 6");
        node6.setPercent(0.9);
        node6.setIconRef(ResourceTable.Media_icon);
        nodeList.add(node6);

        Node node7 = new Node();
        node7.setTitle("Node 7");
        node7.setPercent(0.26);
        node7.setIconRef(ResourceTable.Media_icon);
        nodeList.add(node7);

        return nodeList;
    }
}
