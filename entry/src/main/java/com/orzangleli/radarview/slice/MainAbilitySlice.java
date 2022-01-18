
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

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.AbsButton;
import ohos.agp.components.Button;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Slider;
import ohos.agp.components.Text;
import ohos.agp.components.ToggleButton;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import com.orzangleli.radar.Node;
import com.orzangleli.radar.XRadarView;
import com.orzangleli.radarview.ResourceTable;
import java.util.ArrayList;

/**
 * Main ability slice to test all the functionality of XRadarView library.
 */
public class MainAbilitySlice extends AbilitySlice implements AbsButton.CheckedStateChangedListener {

    //Contants
    private static final String TAG = MainAbilitySlice.class.getSimpleName();
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD000F00, TAG);

    //Components
    XRadarView radarView;
    Text countTextView;
    Text layerCountTextView;
    Text jump;
    Slider seekBar;
    Slider seekBar2;
    Slider seekBar3;
    Slider seekBar4;
    ToggleButton drawBorder;
    ToggleButton drawPoint;
    ToggleButton drawPolygon;
    ToggleButton drawShade;
    ToggleButton drawMultiColor;
    ToggleButton regionSupportShader;
    ToggleButton regionCircle;
    ToggleButton drawRadius;
    ToggleButton drawText;
    ToggleButton drawIcon;
    Button loadAnimation;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        initComponents();
        configRadarView();

        drawBorder.setCheckedStateChangedListener(this);
        drawPoint.setCheckedStateChangedListener(this);
        drawPolygon.setCheckedStateChangedListener(this);
        drawShade.setCheckedStateChangedListener(this);
        drawMultiColor.setCheckedStateChangedListener(this);
        regionSupportShader.setCheckedStateChangedListener(this);
        regionCircle.setCheckedStateChangedListener(this);
        drawRadius.setCheckedStateChangedListener(this);
        drawText.setCheckedStateChangedListener(this);
        drawIcon.setCheckedStateChangedListener(this);

        loadAnimation.setClickedListener(component -> {
            if (radarView.isEnabledAnimation()) {
                radarView.loadAnimation(true);
            } else {
                new ToastDialog(getContext())
                        .setText("Please enable animation first!")
                        .setDuration(2000)
                        .show();
            }
        });

        drawBorder.setChecked(radarView.isEnabledBorder());
        drawPoint.setChecked(radarView.isEnabledShowPoint());
        drawIcon.setChecked(radarView.isEnableIcons());
        drawText.setChecked(radarView.isEnabledText());
        drawRadius.setChecked(radarView.isEnabledRadius());
        drawPolygon.setChecked(radarView.isEnabledPolygon());
        drawShade.setChecked(radarView.isEnabledShade());
    }

    private void initComponents() {
        countTextView = (Text) this.findComponentById(ResourceTable.Id_countTextView);
        seekBar = (Slider) this.findComponentById(ResourceTable.Id_seekBar);

        layerCountTextView = (Text) this.findComponentById(ResourceTable.Id_layerCountTextView);
        seekBar2 = (Slider) this.findComponentById(ResourceTable.Id_seekBar2);
        seekBar3 = (Slider) this.findComponentById(ResourceTable.Id_seekBar3);
        seekBar4 = (Slider) this.findComponentById(ResourceTable.Id_seekBar4);

        drawBorder = (ToggleButton) this.findComponentById(ResourceTable.Id_drawBorder);
        loadAnimation = (Button) this.findComponentById(ResourceTable.Id_loadAnimation);
        drawPoint = (ToggleButton) this.findComponentById(ResourceTable.Id_drawPoint);
        drawPolygon = (ToggleButton) this.findComponentById(ResourceTable.Id_drawPolygon);
        drawShade = (ToggleButton) this.findComponentById(ResourceTable.Id_drawShade);
        drawMultiColor = (ToggleButton) this.findComponentById(ResourceTable.Id_drawMultiColor);
        regionSupportShader = (ToggleButton) this.findComponentById(ResourceTable.Id_regionSupportShader);
        regionCircle = (ToggleButton) this.findComponentById(ResourceTable.Id_regionCircle);
        drawRadius = (ToggleButton) this.findComponentById(ResourceTable.Id_drawRadius);
        drawText = (ToggleButton) this.findComponentById(ResourceTable.Id_drawText);
        drawIcon = (ToggleButton) this.findComponentById(ResourceTable.Id_drawIcon);
        jump = (Text) this.findComponentById(ResourceTable.Id_jump);
        radarView = (XRadarView) this.findComponentById(ResourceTable.Id_radarView);
    }

    private void configRadarView() {

        // Set node list with all the node details.
        radarView.setNodeList(getNodeList());

        // Set the drawable padding for each item
        radarView.setDrawablePadding(20);
        // The setting allows connections between points
        radarView.setEnabledBorder(true);
        // Show dots
        radarView.setEnabledShowPoint(true);
        // Set dots radius
        radarView.setPointRadius(8);
        // Set dots color
        radarView.setPointColor(Color.RED);
        // Draw positive n deformation
        radarView.setEnabledPolygon(true);
        // Draws a gradient ring
        radarView.setEnabledShade(true);
        // Draw the radius
        radarView.setEnabledRadius(true);
        // Draw titles, values, icons, etc
        radarView.setEnabledText(true);
        // Turn on the animation
        radarView.setEnabledAnimation(true);
        // Set the number of layers
        radarView.setLayerCount(5);
        // A single color without gradient colors
        radarView.setSingleColor(new Color(getColor(ResourceTable.Color_single)));
        // Configure the area gradient, and the second parameter needs to be fine-tuned to get good results
        radarView.setRegionShaderConfig(new Color[]{Color.YELLOW, Color.RED}, new float[]{0.2f, 0.6f});

        // Radar image title and icon click event
        radarView.setOnTitleClickListener((view, position, x, y, rect) -> {

            showToastDialog("position = " + position);

            HiLog.debug(LABEL_LOG, "lxc", "position ----> " + position);
            HiLog.debug(LABEL_LOG, "lxc", "x ----> " + x);
            HiLog.debug(LABEL_LOG, "lxc", "y ----> " + y);
        });

        seekBar.setValueChangedListener(new Slider.ValueChangedListener() {
            @Override
            public void onProgressUpdated(Slider slider, int progress, boolean fromUser) {
                String jibianxing = "";
                if (progress == 0) {
                    jibianxing = "positive triangle";
                } else if (progress == 1) {
                    jibianxing = "positive quad";
                } else if (progress == 2) {
                    jibianxing = "positive pentagon";
                } else if (progress == 3) {
                    jibianxing = "positive hexagon";
                } else if (progress == 4) {
                    jibianxing = "Regular heptagon";
                }
                countTextView.setText(jibianxing);
                radarView.setCount(progress + 3);
            }

            @Override
            public void onTouchStart(Slider slider) {
                // Do nothing
            }

            @Override
            public void onTouchEnd(Slider slider) {
                // Do nothing
            }
        });

        seekBar2.setValueChangedListener(new Slider.ValueChangedListener() {
            @Override
            public void onProgressUpdated(Slider slider, int progress, boolean b) {
                String layoutText = "number of layers";
                layerCountTextView.setText(layoutText + progress);
                radarView.setLayerCount(progress);
            }

            @Override
            public void onTouchStart(Slider slider) {
                // Do nothing
            }

            @Override
            public void onTouchEnd(Slider slider) {
                // Do nothing
            }
        });

        seekBar3.setValueChangedListener(new Slider.ValueChangedListener() {
            @Override
            public void onProgressUpdated(Slider slider, int progress, boolean b) {
                radarView.setDrawableSize(progress + 20);
            }

            @Override
            public void onTouchStart(Slider slider) {
                // Do nothing
            }

            @Override
            public void onTouchEnd(Slider slider) {
                // Do nothing
            }
        });

        seekBar4.setValueChangedListener(new Slider.ValueChangedListener() {
            @Override
            public void onProgressUpdated(Slider slider, int progress, boolean b) {
                radarView.setTitleSize(progress + 30);
            }

            @Override
            public void onTouchStart(Slider slider) {
                // Do nothing
            }

            @Override
            public void onTouchEnd(Slider slider) {
                // Do nothing
            }
        });

        jump.setClickedListener(component -> present(new DemoAbilitySlice(), new Intent()));
    }

    @Override
    public void onCheckedChanged(AbsButton absButton, boolean isChecked) {
        switch (absButton.getId()) {
            case ResourceTable.Id_drawBorder:
                radarView.setEnabledBorder(isChecked);
                break;
            case ResourceTable.Id_drawPoint:
                radarView.setEnabledShowPoint(isChecked);
                break;
            case ResourceTable.Id_drawPolygon:
                radarView.setEnabledPolygon(isChecked);
                break;
            case ResourceTable.Id_drawShade:
                radarView.setEnabledShade(isChecked);
                break;
            case ResourceTable.Id_drawMultiColor:
                radarView.setEnabledMultiColorRegion(isChecked);
                break;
            case ResourceTable.Id_regionSupportShader:
                if (isChecked && radarView.isEnabledMultiColorRegion()) {
                    showToastDialog(getString(ResourceTable.String_msg_turn_off_multi_color));
                    regionSupportShader.setChecked(false);
                    return;
                }
                radarView.setEnabledRegionShader(isChecked);
                break;
            case ResourceTable.Id_regionCircle:
                radarView.setEnabledCircularOutline(isChecked);
                break;
            case ResourceTable.Id_drawRadius:
                radarView.setEnabledRadius(isChecked);
                break;
            case ResourceTable.Id_drawText:
                radarView.setEnabledText(isChecked);
                break;
            case ResourceTable.Id_drawIcon:
                radarView.setEnableIcons(isChecked);
                break;
            default: {
                //Do nothing
            }
        }
    }

    /**
     * Create dummy node list.
     * @return Array list of node.
     */
    private ArrayList<Node> getNodeList() {
        ArrayList<Node> nodeList = new ArrayList<>();

        nodeList.add(new Node("Kill",
                "80",
                0.8,
                ResourceTable.Media_icon,
                getColor(ResourceTable.Color_kill)));
        nodeList.add(new Node("Money",
                "80%",
                0.8,
                ResourceTable.Media_icon,
                getColor(ResourceTable.Color_money)));
        nodeList.add(new Node("Survival",
                "0.9",
                0.9,
                ResourceTable.Media_icon,
                getColor(ResourceTable.Color_survival)));
        nodeList.add(new Node("Defense",
                "100%",
                1,
                ResourceTable.Media_icon,
                getColor(ResourceTable.Color_defense)));
        nodeList.add(new Node("Rubik's Cube",
                "3/5",
                0.6,
                ResourceTable.Media_icon,
                getColor(ResourceTable.Color_rubik_cube)));
        nodeList.add(new Node("Physics",
                "50%",
                0.5,
                ResourceTable.Media_icon,
                getColor(ResourceTable.Color_physics)));
        nodeList.add(new Node("Assists",
                "0.77个",
                0.77,
                ResourceTable.Media_icon,
                getColor(ResourceTable.Color_assists)));

        return nodeList;
    }

    private void showToastDialog(String msg) {
        DirectionalLayout toastLayout = (DirectionalLayout) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_layout_toast, null, false);

        Text txtMessage = (Text) toastLayout.findComponentById(ResourceTable.Id_msg_toast);
        txtMessage.setText(msg);

        new ToastDialog(MainAbilitySlice.this)
                .setContentCustomComponent(toastLayout)
                .setAlignment(LayoutAlignment.CENTER)
                .setSize(MATCH_CONTENT, MATCH_CONTENT)
                .show();
    }
}
