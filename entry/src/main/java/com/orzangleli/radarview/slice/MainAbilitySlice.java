package com.orzangleli.radarview.slice;

import com.orzangleli.radar.XRadarView;
import com.orzangleli.radarview.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.AbsButton;
import ohos.agp.components.Button;
import ohos.agp.components.Slider;
import ohos.agp.components.Text;
import ohos.agp.components.ToggleButton;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

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
    ToggleButton drawRichText;
    Button loadAnimation;

    //icon array
    final int[] drawables = new int[]{
        ResourceTable.Media_icon,
        ResourceTable.Media_icon,
        ResourceTable.Media_icon,
        ResourceTable.Media_icon,
        ResourceTable.Media_icon,
        ResourceTable.Media_icon,
        ResourceTable.Media_icon};

    // title
    final CharSequence[] titles = new CharSequence[]{
        "Kill",
        "Money",
        "Survival",
        "Defense",
        "Rubik's Cube",
        "Physics",
        "Assists",
        "wisdom"};

    // Value for each property (0 to 1.0)
    final double[] percents = new double[]{0.8, 0.8, 0.9, 1, 0.6, 0.5, 0.77, 0.9};

    // Numeric text below each title
    final CharSequence[] values = new CharSequence[]{"80", "80%", "0.9", "100%", "3/5", "0.5", "0.77个", "1~12"};

    // An array of colors when distinguishing adjacent areas with different colors (optional)
    final int[] colors = new int[]{RgbPalette.parse("#A0ffcc00"), RgbPalette.parse("#A000ff00"),
            RgbPalette.parse("#A00000ff"), RgbPalette.parse("#A0FF00FF"), RgbPalette.parse("#A000FFFF"),
            RgbPalette.parse("#A0FFFF00"), RgbPalette.parse("#A000FF00"), RgbPalette.parse("#A0FF00FF")};

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
        drawRichText.setCheckedStateChangedListener(this);

        loadAnimation.setClickedListener(component -> radarView.loadAnimation(true));

        drawBorder.setChecked(true);
        drawPoint.setChecked(true);
        drawIcon.setChecked(true);
        drawText.setChecked(true);
        drawRadius.setChecked(true);
        drawPolygon.setChecked(true);
        drawShade.setChecked(true);
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
        drawRichText = (ToggleButton) this.findComponentById(ResourceTable.Id_drawRichText);
        jump = (Text) this.findComponentById(ResourceTable.Id_jump);
        radarView = (XRadarView) this.findComponentById(ResourceTable.Id_radarView);
    }

    private void configRadarView() {
        // The scale of values in the radar chart
        radarView.setPercents(percents);
        // Sets an array of colors for each area
        // If colors are set, each area will display a different color,
        // otherwise all areas will display the same color Action 1
        radarView.setColors(null);
        // Set each title
        radarView.setTitles(titles);
        // Set title color
        radarView.setTitleColor(Color.RED);
        // Set the text of the values displayed by each item
        radarView.setValues(values);
        // Set the icon for each item
        radarView.setDrawables(drawables);
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
        radarView.setSingleColor(new Color(RgbPalette.parse("#800000ff")));
        // Configure the area gradient, and the second parameter needs to be fine-tuned to get good results
        radarView.setRegionShaderConfig(new Color[]{Color.YELLOW, Color.RED}, new float[]{0.2f, 0.6f});

        // Radar image title and icon click event
        radarView.setOnTitleClickListener((view, position, x, y, rect) -> {
            new ToastDialog(getContext())
                    .setText("position = " + position)
                    .setDuration(2000)
                    .show();
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
                if (isChecked) {
                    radarView.setColors(colors);
                } else {
                    radarView.setColors(null);
                }
                break;
            case ResourceTable.Id_regionSupportShader:
                if (isChecked && radarView.getColors() != null) {
                    new ToastDialog(this)
                            .setText("turn on the area color gradient, you need to turn off the multi-color distinguished area first")
                            .show();
                    return;
                }
                radarView.setEnabledRegionShader(isChecked);
                break;
            case ResourceTable.Id_regionCircle:
                radarView.setCircle(isChecked);
                break;
            case ResourceTable.Id_drawRadius:
                radarView.setEnabledRadius(isChecked);
                break;
            case ResourceTable.Id_drawText:
                radarView.setEnabledText(isChecked);
                break;
            case ResourceTable.Id_drawIcon:
                if (isChecked) {
                    radarView.setDrawables(drawables);
                } else {
                    radarView.setDrawables(null);
                }
                break;
            case ResourceTable.Id_drawRichText:
                titles[0] = "kill";
                radarView.setTitles(titles);
                break;
            default:{
                //Do nothing
            }
        }
    }
}
