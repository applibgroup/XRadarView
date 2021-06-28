package com.orzangleli.radarview;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.orzangleli.radar.XRadarView;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    XRadarView radarView;
    TextView countTextView, layerCountTextView;
    SeekBar seekBar, seekBar2, seekBar3, seekBar4;
    ToggleButton drawBorder, drawPoint, drawPolygon, drawShade, drawMultiColor, regionSupportShader, regionCircle, drawRadius, drawText, drawIcon, drawRichText;
    Button loadAnimation;

    // icon
    private int drawables[] = new int[]{com.orzangleli.radar.R.drawable.ic_launcher, com.orzangleli.radar.R.drawable.ic_launcher,
            com.orzangleli.radar.R.drawable.ic_launcher, com.orzangleli.radar.R.drawable.ic_launcher, com.orzangleli.radar.R.drawable.ic_launcher,
            com.orzangleli.radar.R.drawable.ic_launcher, com.orzangleli.radar.R.drawable.ic_launcher};

    // title
    CharSequence titles[] = new CharSequence[]{"Kill", "Money", "Survival", "Defense", "Rubik's Cube", "Physics", "Assists", "wisdom"};
    // Value for each property (0 to 1.0)
    double percents[] = new double[]{0.8, 0.8, 0.9, 1, 0.6, 0.5, 0.77, 0.9};
    // Numeric text below each title
    CharSequence values[] = new CharSequence[]{"80", "80%", "0.9", "100%", "3/5", "0.5", "0.77个", "1~12"};
    // An array of colors when distinguishing adjacent areas with different colors (optional)
    int colors[] = new int[]{Color.parseColor("#A0ffcc00"), Color.parseColor("#A000ff00"),
            Color.parseColor("#A00000ff"), Color.parseColor("#A0FF00FF"), Color.parseColor("#A000FFFF"),
            Color.parseColor("#A0FFFF00"), Color.parseColor("#A000FF00"), Color.parseColor("#A0FF00FF")};


    TextView jump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countTextView = (TextView) this.findViewById(R.id.countTextView);
        seekBar = (SeekBar) this.findViewById(R.id.seekBar);

        layerCountTextView = (TextView) this.findViewById(R.id.layerCountTextView);
        seekBar2 = (SeekBar) this.findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar) this.findViewById(R.id.seekBar3);
        seekBar4 = (SeekBar) this.findViewById(R.id.seekBar4);

        drawBorder = (ToggleButton) this.findViewById(R.id.drawBorder);
        loadAnimation = (Button) this.findViewById(R.id.loadAnimation);
        drawPoint = (ToggleButton) this.findViewById(R.id.drawPoint);
        drawPolygon = (ToggleButton) this.findViewById(R.id.drawPolygon);
        drawShade = (ToggleButton) this.findViewById(R.id.drawShade);
        drawMultiColor = (ToggleButton) this.findViewById(R.id.drawMultiColor);
        regionSupportShader = (ToggleButton) this.findViewById(R.id.regionSupportShader);
        regionCircle = (ToggleButton) this.findViewById(R.id.regionCircle);
        drawRadius = (ToggleButton) this.findViewById(R.id.drawRadius);
        drawText = (ToggleButton) this.findViewById(R.id.drawText);
        drawIcon = (ToggleButton) this.findViewById(R.id.drawIcon);
        drawRichText = (ToggleButton) this.findViewById(R.id.drawRichText);

        drawBorder.setOnCheckedChangeListener(this);
        drawPoint.setOnCheckedChangeListener(this);
        drawPolygon.setOnCheckedChangeListener(this);
        drawShade.setOnCheckedChangeListener(this);
        drawMultiColor.setOnCheckedChangeListener(this);
        regionSupportShader.setOnCheckedChangeListener(this);
        regionCircle.setOnCheckedChangeListener(this);
        drawRadius.setOnCheckedChangeListener(this);
        drawText.setOnCheckedChangeListener(this);
        drawIcon.setOnCheckedChangeListener(this);
        drawRichText.setOnCheckedChangeListener(this);


        loadAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radarView.loadAnimation(true);
            }
        });

        jump = (TextView) this.findViewById(R.id.jump);


        radarView = (XRadarView) this.findViewById(R.id.radarView);
        // The scale of values in the radar chart
        radarView.setPercents(percents);
        // Sets an array of colors for each area
        // If colors are set, each area will display a different color, otherwise all areas will display the same color Action 1
        radarView.setColors(null);
        // If you want to set all regions the same color, you can set dataColor action 2 (operation 1 and action 2 mutually exclusive)
//        radarView.setDataColor(Color.parseColor("#999900"));
        // Set each title
        radarView.setTitles(titles);
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
        radarView.setSingleColor(Color.parseColor("#800000ff"));
        // Configure the area gradient, and the second parameter needs to be fine-tuned to get good results
        radarView.setRegionShaderConfig(new int[]{Color.YELLOW, Color.RED}, new float[]{0.2f, 0.6f});

        // Radar image title and icon click event
        radarView.setOnTitleClickListener(new XRadarView.OnTitleClickListener() {
            @Override
            public void onTitleClick(XRadarView view, int position, int x, int y, Rect rect) {
                Toast.makeText(MainActivity.this, "position = " + position, Toast.LENGTH_SHORT).show();
                Log.d("lxc", "position ----> " + position);
                Log.d("lxc", "x ----> " + x);
                Log.d("lxc", "y ----> " + y);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String layoutText = "number of layers";
                layerCountTextView.setText(layoutText + progress);
                radarView.setLayerCount(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radarView.setDrawableSize(progress + 20);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radarView.setTitleSize(progress + 30);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DemoActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.drawBorder:
                radarView.setEnabledBorder(isChecked);
                break;
            case R.id.drawPoint:
                radarView.setEnabledShowPoint(isChecked);
                break;
            case R.id.drawPolygon:
                radarView.setEnabledPolygon(isChecked);
                break;
            case R.id.drawShade:
                radarView.setEnabledShade(isChecked);
                break;
            case R.id.drawMultiColor:
                if (isChecked) {
                    radarView.setColors(colors);
                } else {
                    radarView.setColors(null);
                }
                break;
            case R.id.regionSupportShader:
                if (isChecked && radarView.getColors() != null) {
                    Toast.makeText(this, "turn on the area color gradient, you need to turn off the multi-color distinguished area first", Toast.LENGTH_LONG).show();
                    return;
                }
                radarView.setEnabledRegionShader(isChecked);
                break;
            case R.id.regionCircle:
                radarView.setCircle(isChecked);
                break;
            case R.id.drawRadius:
                radarView.setEnabledRadius(isChecked);
                break;
            case R.id.drawText:
                radarView.setEnabledText(isChecked);
                break;
            case R.id.drawIcon:
                if (isChecked) {
                    radarView.setDrawables(drawables);
                } else {
                    radarView.setDrawables(null);
                }
                break;
            case R.id.drawRichText:
                if (isChecked) {
                    // Note that if you use rich text, only the tittles field is displayed, so you can manually stitch values behind the tips
                    SpannableString ss = new SpannableString("Plain text\nvalue is 0.1");
                    ss.setSpan(new AbsoluteSizeSpan(30), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(Color.GREEN), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new BackgroundColorSpan(Color.YELLOW), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(Color.BLUE), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new AbsoluteSizeSpan(10), 4, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    titles[0] = ss;
                    radarView.setTitles(titles);
                } else {
                    titles[0] = "kill";
                    radarView.setTitles(titles);
                }
                break;
        }
    }
}