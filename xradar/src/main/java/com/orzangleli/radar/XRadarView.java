package com.orzangleli.radar;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.*;
import ohos.agp.text.SimpleTextLayout;
import ohos.agp.utils.*;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.TouchEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class XRadarView extends Component implements Component.DrawTask, Component.TouchEventListener {

    private static final String TAG = XRadarView.class.getSimpleName();

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD000F00, TAG);

    //attributes
    private static final String ATTRIBUTE_COUNT = "count";
    private static final String ATTRIBUTE_LAYER_COUNT = "layerCount";
    private static final String ATTRIBUTE_DRAWABLE_SIZE = "mDrawableSize";
    private static final String ATTRIBUTE_DRAWABLE_PADDING = "mDrawablePadding";
    private static final String ATTRIBUTE_DESC_PADDING = "descPadding";
    private static final String ATTRIBUTE_TITLE_SIZE = "titleSize";
    private static final String ATTRIBUTE_DATA_SIZE = "dataSize";
    private static final String ATTRIBUTE_RADAR_PERCENT = "radarPercent";
    private static final String ATTRIBUTE_START_COLOR = "startColor";
    private static final String ATTRIBUTE_END_COLOR = "endColor";
    private static final String ATTRIBUTE_COBWEB_COLOR = "cobwebColor";
    private static final String ATTRIBUTE_DATA_COLOR = "dataColor";
    private static final String ATTRIBUTE_SINGLE_COLOR = "singleColor";
    private static final String ATTRIBUTE_TITLE_COLOR = "titleColor";
    private static final String ATTRIBUTE_POINT_COLOR = "pointColor";
    private static final String ATTRIBUTE_BORDER_COLOR = "borderColor";
    private static final String ATTRIBUTE_RADIUS_COLOR = "radiusColor";
    private static final String ATTRIBUTE_BOUNDARY_WIDTH = "boundaryWidth";
    private static final String ATTRIBUTE_POINT_RADIUS = "pointRadius";
    private static final String ATTRIBUTE_ENABLED_BORDER = "enabledBorder";
    private static final String ATTRIBUTE_ENABLED_ANIMATION = "enabledAnimation";
    private static final String ATTRIBUTE_ENABLED_SHOW_POINT = "enabledShowPoint";
    private static final String ATTRIBUTE_ENABLED_POLYGON = "enabledPolygon";
    private static final String ATTRIBUTE_ENABLED_SHADE = "enabledShade";
    private static final String ATTRIBUTE_ENABLED_RADIUS = "enabledRadius";
    private static final String ATTRIBUTE_ENABLED_TEXT = "enabledText";
    private static final String ATTRIBUTE_ANIMATION_DURATION = "animDuration";

    // Several-sided radar
    private int count = 5;
    private int layerCount = 6;  // Number of layer
    private int drawableSize = 40;
    private int drawablePadding = 10;
    private int descPadding = 5;
    private int titleSize = 40;
    private int dataSize = 30;

    private float radarPercent = 0.7f;

    private Color startColor = new Color(RgbPalette.parse("#80FF0000"));
    private Color endColor = new Color(RgbPalette.parse("#8000FF00"));
    // The color of the cobweb line
    private Color cobwebColor;
    // The text color of the data value
    private Color dataColor;
    // If it is not a multicolored area, it is a single color
    private Color singleColor;

    // The color of the title text
    private Color titleColor;
    // Dot color
    private Color pointColor;
    // The size of the dot radius
    private int pointRadius;
    // The color of the boundary line
    private Color borderColor;
    // The width of the boundary line
    private int boundaryWidth;
    // The color of the radius line
    private Color radiusColor;
    // Radar chart gradient color array
    private Color[] shaderColors;
    // The location of various color distributions of radar map gradient colors
    private float[] shaderPositions;


    // Whether to draw a boundary line
    private boolean enabledBorder = false;
    // Whether to turn on the animation
    private boolean enabledAnimation = true;
    // The duration of the animation
    private int animDuration = 1000;
    // Whether the dot is displayed
    private boolean enabledShowPoint = true;
    // Whether to draw the grid
    private boolean enabledPolygon = true;
    // Whether to draw a gradient ring
    private boolean enabledShade = true;
    // Whether to draw the radius
    private boolean enabledRadius = true;
    // Whether to draw the text
    private boolean enabledText = true;
    // Whether to paint the radar area as a gradient color
    private boolean enabledRegionShader = false;


    private int MAX_TEXT_WIDTH;  // Text maximum allowable width

    // The center of the circle for each edge
    private float angle;
    // Round x
    private int centerX;
    // Round y
    private int centerY;
    // radius
    private float radius;

    // Whether the outer outline is circular
    private boolean isCircle = false;

    // Area gradient shader
    private Shader regionShader;

    private Paint cobwebPaint;
    private Paint dataPaint;
    private Paint singlePaint;
    private TextPaint titlePaint;
    private Paint layerPaint;
    private Paint pointPaint;
    private Paint radiusPaint;
    private Paint borderPaint;


    // The current scale ratio
    private float currentScale;

    private List<Rect> titleRects;

    // icon
    private int[] drawables;
    // title
    CharSequence[] titles;
    // Value for each property (0 to 1.0)
    double[] percents;
    // Numeric text below each title
    CharSequence[] values;
    // The area color
    int[] colors;

    OnTitleClickListener onTitleClickListener;

    public XRadarView(Context context) {
        this(context, null, 0);
        init(null);
    }

    public XRadarView(Context context, @Nullable AttrSet attrs) {
        this(context, attrs, 0);
        init(attrs);
    }

    public XRadarView(Context context, @Nullable AttrSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttrSet attrSet) {

        if (attrSet != null) {
            count = attrSet.getAttr(ATTRIBUTE_COUNT).isPresent() ? attrSet.getAttr(ATTRIBUTE_COUNT).get().getIntegerValue() : 6;
            layerCount = attrSet.getAttr(ATTRIBUTE_LAYER_COUNT).isPresent() ? attrSet.getAttr(ATTRIBUTE_LAYER_COUNT).get().getIntegerValue() : 6;
            drawableSize = attrSet.getAttr(ATTRIBUTE_DRAWABLE_SIZE).isPresent() ? attrSet.getAttr(ATTRIBUTE_DRAWABLE_SIZE).get().getIntegerValue() : 40;
            drawablePadding = attrSet.getAttr(ATTRIBUTE_DRAWABLE_PADDING).isPresent() ? attrSet.getAttr(ATTRIBUTE_DRAWABLE_PADDING).get().getIntegerValue() : 10;
            descPadding = attrSet.getAttr(ATTRIBUTE_DESC_PADDING).isPresent() ? attrSet.getAttr(ATTRIBUTE_DESC_PADDING).get().getIntegerValue() : 5;
            titleSize = attrSet.getAttr(ATTRIBUTE_TITLE_SIZE).isPresent() ? attrSet.getAttr(ATTRIBUTE_TITLE_SIZE).get().getIntegerValue() : 40;
            dataSize = attrSet.getAttr(ATTRIBUTE_DATA_SIZE).isPresent() ? attrSet.getAttr(ATTRIBUTE_DATA_SIZE).get().getIntegerValue() : 30;
            radarPercent = attrSet.getAttr(ATTRIBUTE_RADAR_PERCENT).isPresent() ? attrSet.getAttr(ATTRIBUTE_RADAR_PERCENT).get().getFloatValue() : 0.7f;
            startColor = attrSet.getAttr(ATTRIBUTE_START_COLOR).isPresent() ? attrSet.getAttr(ATTRIBUTE_START_COLOR).get().getColorValue() : new Color(RgbPalette.parse("#80FFCC33"));
            endColor = attrSet.getAttr(ATTRIBUTE_END_COLOR).isPresent() ? attrSet.getAttr(ATTRIBUTE_END_COLOR).get().getColorValue() : new Color(RgbPalette.parse("#80FFFFCC"));
            cobwebColor = attrSet.getAttr(ATTRIBUTE_COBWEB_COLOR).isPresent() ? attrSet.getAttr(ATTRIBUTE_COBWEB_COLOR).get().getColorValue() : new Color(RgbPalette.parse("#80444444"));
            dataColor = attrSet.getAttr(ATTRIBUTE_DATA_COLOR).isPresent() ? attrSet.getAttr(ATTRIBUTE_DATA_COLOR).get().getColorValue() : new Color(RgbPalette.parse("#00000000"));
            singleColor = attrSet.getAttr(ATTRIBUTE_SINGLE_COLOR).isPresent() ? attrSet.getAttr(ATTRIBUTE_SINGLE_COLOR).get().getColorValue() : new Color(RgbPalette.parse("#80CC0000"));
            titleColor = attrSet.getAttr(ATTRIBUTE_TITLE_COLOR).isPresent() ? attrSet.getAttr(ATTRIBUTE_TITLE_COLOR).get().getColorValue() : new Color(RgbPalette.parse("#80000000"));
            pointColor = attrSet.getAttr(ATTRIBUTE_POINT_COLOR).isPresent() ? attrSet.getAttr(ATTRIBUTE_POINT_COLOR).get().getColorValue() : new Color(RgbPalette.parse("#80333366"));
            borderColor = attrSet.getAttr(ATTRIBUTE_BORDER_COLOR).isPresent() ? attrSet.getAttr(ATTRIBUTE_BORDER_COLOR).get().getColorValue() : new Color(RgbPalette.parse("#80333366"));
            radiusColor = attrSet.getAttr(ATTRIBUTE_RADIUS_COLOR).isPresent() ? attrSet.getAttr(ATTRIBUTE_RADIUS_COLOR).get().getColorValue() : new Color(RgbPalette.parse("#80CCCCCC"));
            boundaryWidth = attrSet.getAttr(ATTRIBUTE_BOUNDARY_WIDTH).isPresent() ? attrSet.getAttr(ATTRIBUTE_BOUNDARY_WIDTH).get().getIntegerValue() : 5;
            pointRadius = attrSet.getAttr(ATTRIBUTE_POINT_RADIUS).isPresent() ? attrSet.getAttr(ATTRIBUTE_POINT_RADIUS).get().getIntegerValue() : 10;
            enabledBorder = attrSet.getAttr(ATTRIBUTE_ENABLED_BORDER).isPresent() && attrSet.getAttr(ATTRIBUTE_ENABLED_BORDER).get().getBoolValue();
            enabledAnimation = !attrSet.getAttr(ATTRIBUTE_ENABLED_ANIMATION).isPresent() || attrSet.getAttr(ATTRIBUTE_ENABLED_ANIMATION).get().getBoolValue();
            enabledShowPoint = !attrSet.getAttr(ATTRIBUTE_ENABLED_SHOW_POINT).isPresent() || attrSet.getAttr(ATTRIBUTE_ENABLED_SHOW_POINT).get().getBoolValue();
            enabledPolygon = !attrSet.getAttr(ATTRIBUTE_ENABLED_POLYGON).isPresent() || attrSet.getAttr(ATTRIBUTE_ENABLED_POLYGON).get().getBoolValue();
            enabledShade = !attrSet.getAttr(ATTRIBUTE_ENABLED_SHADE).isPresent() || attrSet.getAttr(ATTRIBUTE_ENABLED_SHADE).get().getBoolValue();
            enabledRadius = !attrSet.getAttr(ATTRIBUTE_ENABLED_RADIUS).isPresent() || attrSet.getAttr(ATTRIBUTE_ENABLED_RADIUS).get().getBoolValue();
            enabledText = !attrSet.getAttr(ATTRIBUTE_ENABLED_TEXT).isPresent() || attrSet.getAttr(ATTRIBUTE_ENABLED_TEXT).get().getBoolValue();
            animDuration = attrSet.getAttr(ATTRIBUTE_ANIMATION_DURATION).isPresent() ? attrSet.getAttr(ATTRIBUTE_ANIMATION_DURATION).get().getIntegerValue() : 1000;

        }

        titleRects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            titleRects.add(null);
        }

        angle = (float) (Math.PI * 2 / count);

        cobwebPaint = new Paint();
        cobwebPaint.setColor(cobwebColor);
        cobwebPaint.setAntiAlias(true);
        cobwebPaint.setStyle(Paint.Style.STROKE_STYLE);

        dataPaint = new Paint();
        dataPaint.setColor(dataColor);
        dataPaint.setAntiAlias(true);
        dataPaint.setStyle(Paint.Style.FILLANDSTROKE_STYLE);

        singlePaint = new Paint();
        singlePaint.setColor(singleColor);
        singlePaint.setAntiAlias(true);
        singlePaint.setStyle(Paint.Style.FILLANDSTROKE_STYLE);

        titlePaint = new TextPaint();
        titlePaint.setTextSize(titleSize);
        titlePaint.setColor(titleColor);
        titlePaint.setAntiAlias(true);
        titlePaint.setMultipleLine(true);

        layerPaint = new Paint();
        layerPaint.setAntiAlias(true);
        layerPaint.setStyle(Paint.Style.FILLANDSTROKE_STYLE);

        pointPaint = new Paint();
        pointPaint.setStyle(Paint.Style.FILL_STYLE);
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(pointColor);

        radiusPaint = new Paint();
        radiusPaint.setStyle(Paint.Style.FILL_STYLE);
        radiusPaint.setAntiAlias(true);
        radiusPaint.setColor(radiusColor);

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.FILLANDSTROKE_STYLE);
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(boundaryWidth);

        loadAnimation(enabledAnimation);

        colors = new int[count];
        drawables = new int[count];
        titles = new CharSequence[count];
        percents = new double[count];
        values = new CharSequence[count];

        addDrawTask(this);
        setLayoutRefreshedListener(new RefreshListener());
        setTouchEventListener(this);
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        HiLog.info(LABEL_LOG, "%{public}s", "onTouchEvent called");
        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                int x = (int) touchEvent.getPointerPosition(touchEvent.getIndex()).getX();
                int y = (int) touchEvent.getPointerPosition(touchEvent.getIndex()).getY();
                for (int i = 0; i < titleRects.size(); i++) {
                    Rect rect = titleRects.get(i);
                    if (rect != null && rect.contains(x, y, x, y)) {
                        if (onTitleClickListener != null) {
                            onTitleClickListener.onTitleClick(XRadarView.this, i, x, y, rect);
                            return true;
                        }
                    }
                }
                break;
        }
        return false;
    }

    /**
     * RefreshListener updates when view is relayout
     */
    class RefreshListener implements LayoutRefreshedListener {
        @Override
        public void onRefreshed(Component component) {
            int w = component.getWidth();
            int h = component.getHeight();
            radius = Math.min(h, w) / 2 * radarPercent;
            MAX_TEXT_WIDTH = (int) (Math.min(h, w) / 2 * (1 - radarPercent));
            //The central coordinates
            centerX = w / 2;
            centerY = h / 2;

            if (regionShader == null && shaderColors != null) {
                Point[] points = new Point[]{
                        new Point(getLeft(), getTop()),
                        new Point(getRight(), getBottom())
                };
                regionShader = new LinearShader(points, shaderPositions, shaderColors, Shader.TileMode.CLAMP_TILEMODE);
            }

            invalidate();
        }
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        if (enabledShade) {
            drawLayer(canvas, startColor, endColor);
        } else {
            drawLayer(canvas, startColor, startColor);
        }
        if (enabledPolygon) {
            drawPolygon(canvas);
        }
        if (enabledRadius) {
            drawRadius(canvas);
        }
        if (enabledText) {
            drawText(canvas);
        }
        if (colors == null || colors.length == 0) {
            drawRegion(canvas, currentScale);
        } else {
            drawRegionWithColor(canvas, currentScale);
        }
        if (enabledShowPoint) {
            drawPoint(canvas, currentScale);
        }
        if (enabledBorder) {
            drawBorder(canvas, currentScale);
        }
    }

    /**
     * Draw area border.
     *
     * @param canvas instance of Canvas
     * @param scale  scale to calculate x and y coordinates
     */
    private void drawBorder(Canvas canvas, float scale) {
        float curX, curY;
        float nextX, nextY;
        for (int i = 0; i < count - 1; i++) {
            curX = (float) (centerX + Math.cos(angle * i + Math.PI / 2) * radius * percents[i] * scale);
            curY = (float) (centerY - Math.sin(angle * i + Math.PI / 2) * radius * percents[i] * scale);

            nextX = (float) (centerX + Math.cos(angle * (i + 1) + Math.PI / 2) * radius * percents[i + 1] * scale);
            nextY = (float) (centerY - Math.sin(angle * (i + 1) + Math.PI / 2) * radius * percents[i + 1] * scale);

            canvas.drawLine(curX, curY, nextX, nextY, borderPaint);
        }

        curX = (float) (centerX + Math.cos(angle * (count - 1) + Math.PI / 2) * radius * percents[count - 1] * scale);
        curY = (float) (centerY - Math.sin(angle * (count - 1) + Math.PI / 2) * radius * percents[count - 1] * scale);
        nextX = (float) (centerX + Math.cos(angle * 0 + Math.PI / 2) * radius * percents[0] * scale);
        nextY = (float) (centerY - Math.sin(angle * 0 + Math.PI / 2) * radius * percents[0] * scale);
        canvas.drawLine(curX, curY, nextX, nextY, borderPaint);

    }

    /**
     * Draw percent points.
     *
     * @param canvas instance of Canvas
     * @param scale  scale to calculate x and y coordinates
     */
    private void drawPoint(Canvas canvas, float scale) {
        for (int i = 0; i < count; i++) {
            int x, y;
            x = (int) (centerX + scale * percents[i] * radius * Math.cos(angle * i + Math.PI / 2));
            y = (int) (centerY - scale * percents[i] * radius * Math.sin(angle * i + Math.PI / 2));
            canvas.drawCircle(x, y, pointRadius, pointPaint);
        }
    }

    /**
     * Draws a line between the center of the circle and the vertex
     *
     * @param canvas instance of Canvas
     */
    private void drawRadius(Canvas canvas) {
        for (int i = 0; i < count; i++) {
            canvas.drawLine(centerX, centerY, (float) (centerX + Math.cos(angle * i + Math.PI / 2) * radius), (float) (centerY - Math.sin(angle * i + Math.PI / 2) * radius), radiusPaint);
        }
    }

    /**
     * Draw cobwebs
     *
     * @param canvas instance of Canvas
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float r = radius / layerCount;
        for (int i = layerCount; i >= 1; i--) {
            float curR = r * i;
            path.reset();
            if (isCircle) {
                path.addCircle(centerX, centerY, curR, Path.Direction.CLOCK_WISE);
            } else {
                for (int j = 0; j < count; j++) {
                    if (j == 0) {
                        path.moveTo(centerX, centerY - curR);
                    } else {
                        path.lineTo((float) (centerX + Math.cos(angle * j + Math.PI / 2) * curR), (float) (centerY - Math.sin(angle * j + Math.PI / 2) * curR));
                    }
                }
                path.close();
            }
            canvas.drawPath(path, cobwebPaint);
        }
    }

    /**
     * Draw the colors of each layer
     *
     * @param canvas     instance of Canvas
     * @param startColor start color
     * @param endColor   end color
     */
    private void drawLayer(Canvas canvas, Color startColor, Color endColor) {
        Path path = null;
        Path prePath = null;
        float r = radius / layerCount;
        for (int i = layerCount; i >= 0; i--) {
            float curR = r * i;
            path = new Path();
            for (int j = 0; j < count; j++) {
                if (isCircle) {
                    path.addCircle(centerX, centerY, curR, Path.Direction.CLOCK_WISE);
                } else {
                    if (j == 0) {
                        path.moveTo(centerX, centerY - curR);
                    } else {
                        path.lineTo((float) (centerX + Math.cos(angle * j + Math.PI / 2) * curR), (float) (centerY - Math.sin(angle * j + Math.PI / 2) * curR));
                    }
                }
            }

            if (prePath != null) {
                if (i != 0) {
                    //    prePath.op(path, Path.Op.DIFFERENCE);
                    prePath.close();
                    // Calculate the gradient color
                    int a0 = Color.alpha(startColor.getValue());
                    int r0 = (startColor.getValue() >> 16) & 0xFF;
                    int g0 = (startColor.getValue() >> 8) & 0xFF;
                    int b0 = startColor.getValue() & 0xFF;

                    int a1 = Color.alpha(endColor.getValue());
                    int r1 = (endColor.getValue() >> 16) & 0xFF;
                    int g1 = (endColor.getValue() >> 8) & 0xFF;
                    int b1 = endColor.getValue() & 0xFF;

                    int a2 = (int) (1.0 * i * (a1 - a0) / layerCount + a0);
                    int r2 = (int) (1.0 * i * (r1 - r0) / layerCount + r0);
                    int g2 = (int) (1.0 * i * (g1 - g0) / layerCount + g0);
                    int b2 = (int) (1.0 * i * (b1 - b0) / layerCount + b0);

                    layerPaint.setColor(new Color(Color.argb(a2, r2, g2, b2)));
                } else {
                    prePath.close();
                    layerPaint.setColor(startColor);
                }
                canvas.drawPath(prePath, layerPaint);
            }
            prePath = path;

        }
    }

    /**
     * Draw text and icons.
     * <B>Note:</B> Currently Rich text functionality not implemented because of SpannableString class not available in OHOS.
     * And also dataSize attribute is also connected with this implementation. So for better understand please refer android library.
     *
     * @param canvas instance of Canvas
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics();
        for (int i = 0; i < count; i++) {
            float x = 0f, y = 0f, curAngle;

            x = (float) (centerX + (radius) * Math.cos(angle * i + Math.PI / 2));
            y = (float) (centerY - (radius) * Math.sin(angle * i + Math.PI / 2));
            curAngle = (float) (angle * i + Math.PI / 2);
            // The rest is in the range of 0-2PI
            curAngle = (float) (curAngle % (2 * Math.PI));
            CharSequence ss;
            if (values == null || values[i] == null) {
                ss = titles[i];
            } else {
                ss = titles[i] + "\n" + values[i];
            }
            if (drawables == null) {
                drawables = new int[count];
            }
            if (Math.abs(curAngle - 3 * Math.PI / 2) < 0.1 || Math.abs(curAngle - Math.PI / 2) < 0.1) {
                if (Math.abs(curAngle - Math.PI / 2) < 0.1) {
                    drawMultiLinesTextAndIcon(canvas, x - MAX_TEXT_WIDTH / 2, y, ss, drawables[i], 1, i);
                } else if (Math.abs(curAngle - Math.PI * 3 / 2) < 0.1) {
                    drawMultiLinesTextAndIcon(canvas, x - MAX_TEXT_WIDTH / 2, y, ss, drawables[i], -1, i);
                } else {
                    drawMultiLinesTextAndIcon(canvas, x - MAX_TEXT_WIDTH / 2, y, ss, drawables[i], 0, i);
                }
            } else if (curAngle >= 0 && curAngle < Math.PI / 2) {
                drawMultiLinesTextAndIcon(canvas, x + descPadding, y, ss, drawables[i], 0, i);
            } else if (curAngle > 3 * Math.PI / 2 && curAngle <= Math.PI * 2) {
                drawMultiLinesTextAndIcon(canvas, x + descPadding, y, ss, drawables[i], 0, i);
            } else if (curAngle > Math.PI / 2 && curAngle <= Math.PI) {
                drawMultiLinesTextAndIcon(canvas, x - MAX_TEXT_WIDTH, y, ss, drawables[i], 0, i);
            } else if (curAngle >= Math.PI && curAngle < 3 * Math.PI / 2) {
                drawMultiLinesTextAndIcon(canvas, x - MAX_TEXT_WIDTH, y, ss, drawables[i], 0, i);
            }
        }
    }

    /**
     * Resize pixelmap into required size.
     *
     * @param mediaId media resource id.
     * @return resized pixelmap.
     */
    private PixelMap getResizePixelmap(int mediaId) {
        Optional<PixelMap> optional = Utils.getPixelMap(getContext(), mediaId);
        PixelMap pixelMap = optional.isPresent() ? optional.get() : null;
        if (pixelMap == null) {
            return null;
        }
        int width = pixelMap.getImageInfo().size.width;
        int height = pixelMap.getImageInfo().size.height;
        if (width <= 0 || height <= 0) {
            return null;
        }

        // Set the size you want
        int newWidth = drawableSize;
        int newHeight = drawableSize;

        // Calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // Gets the matrix parameter that you want to scale
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // Get new pictures
        PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
        initializationOptions.size = new Size(newWidth, newHeight);

        PixelMap newBm = PixelMap.create(pixelMap, initializationOptions);
        pixelMap.release();
        return newBm;
    }

    /**
     * Draw an area in one color
     *
     * @param canvas instance of Canvas
     * @param scale  scale to calculate x and y coordinates
     */
    private void drawRegion(Canvas canvas, float scale) {
        canvas.save();
        singlePaint.setColor(singleColor);
        if (enabledRegionShader) {
            singlePaint.setShader(regionShader, Paint.ShaderType.LINEAR_SHADER);
        } else {
            singlePaint.setShader(null, Paint.ShaderType.LINEAR_SHADER);
        }
        List<Point> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int x, y;
            x = (int) (centerX + scale * percents[i] * radius * Math.cos(angle * i + Math.PI / 2));
            y = (int) (centerY - scale * percents[i] * radius * Math.sin(angle * i + Math.PI / 2));
            Point p = new Point(x, y);
            list.add(p);
        }

        Path path = new Path();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                path.moveTo(list.get(i).getPointX(), list.get(i).getPointY());
            } else {
                path.lineTo(list.get(i).getPointX(), list.get(i).getPointY());
            }
        }
        path.close();
        canvas.drawPath(path, singlePaint);
        canvas.restore();
    }

    /**
     * Draw areas in a variety of colors
     *
     * @param canvas instance of Canvas
     * @param scale  scale to calculate x and y coordinates
     */
    private void drawRegionWithColor(Canvas canvas, float scale) {
        canvas.save();
        int colorSize = colors.length;
        List<Point> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int x, y;
            x = (int) (centerX + scale * percents[i] * radius * Math.cos(angle * i + Math.PI / 2));
            y = (int) (centerY - scale * percents[i] * radius * Math.sin(angle * i + Math.PI / 2));
            Point p = new Point(x, y);
            list.add(p);
        }

        Path path = new Path();
        for (int i = 0; i < list.size() - 1; i++) {
            path.reset();
            path.moveTo(centerX, centerY);
            path.lineTo(list.get(i).getPointX(), list.get(i).getPointY());
            path.lineTo(list.get(i + 1).getPointX(), list.get(i + 1).getPointY());
            path.lineTo(centerX, centerY);
            path.close();
            dataPaint.setColor(new Color(colors[i % colorSize]));
            canvas.drawPath(path, dataPaint);
        }
        path.reset();
        path.moveTo(centerX, centerY);
        path.lineTo(list.get(list.size() - 1).getPointX(), list.get(list.size() - 1).getPointY());
        path.lineTo(list.get(0).getPointX(), list.get(0).getPointY());
        path.lineTo(centerX, centerY);
        path.close();
        dataPaint.setColor(new Color(colors[(list.size() - 1) % colorSize]));
        canvas.drawPath(path, dataPaint);
        canvas.restore();
    }

    /**
     * Draw multiple lines of text
     *
     * @param canvas        instance of Canvas
     * @param x             x coordinate
     * @param y             y coordinate
     * @param text          instance of CharSequence to draw text
     * @param drawable      icon drawable
     * @param verticalValue vertical value
     * @param position      position
     */
    private void drawMultiLinesTextAndIcon(Canvas canvas, float x, float y, CharSequence text, int drawable, int verticalValue, int position) {
        int drawableAvaiable = 1;
        try {
            this.getContext().getResourceManager().getResource(drawable);
            drawableAvaiable = 1;
        } catch (Exception e) {
            drawableAvaiable = 0;
        }
        int allowWidth = MAX_TEXT_WIDTH - descPadding;
        int rectWidth = allowWidth;
        Rect rect;
        if (drawable != -1) {
            if (allowWidth > drawableSize * drawableAvaiable + drawablePadding) {
                allowWidth = allowWidth - drawableSize * drawableAvaiable - drawablePadding;
            }
        }

        titlePaint.setTextSize(titleSize);
        SimpleTextLayout layout = new SimpleTextLayout(text.toString(), titlePaint, new Rect(), allowWidth, true);
        canvas.save();
        if (verticalValue == 1) {
            canvas.translate(x, y - layout.getHeight() - descPadding);
            rect = new Rect((int) x, (int) (y - layout.getHeight() - descPadding), (int) x + rectWidth, (int) (y - layout.getHeight() - descPadding) + layout.getHeight());
        } else if (verticalValue == -1) {
            canvas.translate(x, y + descPadding);
            rect = new Rect((int) x, (int) (y + descPadding), (int) x + rectWidth, (int) (y + descPadding) + layout.getHeight());
        } else {
            canvas.translate(x, y - layout.getHeight() / 2);
            rect = new Rect((int) x, (int) (y - layout.getHeight() / 2), (int) x + rectWidth, (int) (y - layout.getHeight() / 2) + layout.getHeight());
        }
        titleRects.set(position, rect);
        layout.drawText(canvas);
        canvas.restore();//Don't forget restore

        // Draw the icon
        if (drawableSize * drawableAvaiable != 0) {
            PixelMap pixelmap = getResizePixelmap(drawable);
            PixelMapHolder pixelMapHolder = new PixelMapHolder(pixelmap);
            if (pixelmap != null) {
                if (verticalValue == 1) {
                    canvas.drawPixelMapHolder(pixelMapHolder, x + layout.getWidth(), y - drawableSize * drawableAvaiable / 2 - layout.getHeight() / 2 - descPadding, titlePaint);
                } else if (verticalValue == -1) {
                    canvas.drawPixelMapHolder(pixelMapHolder, x + layout.getWidth(), y + descPadding + layout.getHeight() / 2 - drawableSize * drawableAvaiable / 2, titlePaint);
                } else {
                    canvas.drawPixelMapHolder(pixelMapHolder, x + layout.getWidth(), y - drawableSize * drawableAvaiable / 2, titlePaint);
                }
                pixelmap.release();
            }
        }

    }

    /**
     * Load animation and update current scale value.
     *
     * @param enabled Boolean value to identify animation is enabled or not.
     */
    public void loadAnimation(boolean enabled) {
        if (!enabled) {
            currentScale = 1;
            invalidate();
        } else {
            AnimatorValue valueAnimator = new AnimatorValue();
            valueAnimator.setCurveType(Animator.CurveType.ACCELERATE);
            valueAnimator.setDuration(animDuration);
            valueAnimator.setValueUpdateListener((animatorValue, v) -> {
                currentScale = v;
                invalidate();
            });
            valueAnimator.start();
        }
    }

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }

    public interface OnTitleClickListener {
        void onTitleClick(XRadarView view, int position, int touchX, int touchY, Rect titleRect);
    }

    /**
     * Set icons array to drawables variable.
     *
     * @param drawables int array of resource ids.
     */
    public void setDrawables(int[] drawables) {
        this.drawables = drawables;
        invalidate();
    }

    /**
     * Set percent values array.
     *
     * @param percents double array of percent values.
     */
    public void setPercents(double[] percents) {
        this.percents = percents;
        invalidate();
    }

    /**
     * Set titles array.
     *
     * @param titles CharSequence array of titles.
     */
    public void setTitles(CharSequence[] titles) {
        this.titles = titles;
        invalidate();
    }

    /**
     * Set percent values array in CharSequence form.
     *
     * @param values CharSequence array of percent values.
     */
    public void setValues(CharSequence[] values) {
        this.values = values;
        invalidate();
    }

    /**
     * Get colors array of multi-color region.
     *
     * @return int array of colors.
     */
    public int[] getColors() {
        return colors;
    }

    /**
     * Set colors array for multi-color region.
     *
     * @param colors int array of colors value.
     */
    public void setColors(int[] colors) {
        this.colors = colors;
        invalidate();
    }

    /**
     * Get count (count of edges in polygon).
     *
     * @return count in int form.
     */
    public int getCount() {
        return count;
    }

    /**
     * Set count
     *
     * @param count Edges in polygon.
     */
    public void setCount(int count) {
        this.count = count;
        angle = (float) (Math.PI * 2 / count);
        invalidate();
    }

    /**
     * Get layer count.
     *
     * @return layer count in int form.
     */
    public int getLayerCount() {
        return layerCount;
    }

    /**
     * Set layer count.
     *
     * @param layerCount The layer count.
     */
    public void setLayerCount(int layerCount) {
        this.layerCount = layerCount;
        invalidate();
    }

    /**
     * Get drawable(icon) size.
     *
     * @return Drawable(icon) size in int form.
     */
    public int getDrawableSize() {
        return drawableSize;
    }

    /**
     * Set drawable(icon) size.
     *
     * @param drawableSize The drawable(icon) size.
     */
    public void setDrawableSize(int drawableSize) {
        this.drawableSize = drawableSize;
        invalidate();
    }

    /**
     * Get drawable(icon) padding.
     *
     * @return Drawable(icon) padding in int form.
     */
    public int getDrawablePadding() {
        return drawablePadding;
    }

    /**
     * Set drawable(icon) padding.
     *
     * @param drawablePadding Drawable(icon) padding.
     */
    public void setDrawablePadding(int drawablePadding) {
        this.drawablePadding = drawablePadding;
        invalidate();
    }

    /**
     * Get desc padding.
     *
     * @return Desc padding in int form.
     */
    public int getDescPadding() {
        return descPadding;
    }

    /**
     * Set desc padding.
     *
     * @param descPadding Desc padding.
     */
    public void setDescPadding(int descPadding) {
        this.descPadding = descPadding;
        invalidate();
    }

    /**
     * Get title size.
     *
     * @return Title size in int form.
     */
    public int getTitleSize() {
        return titleSize;
    }

    /**
     * Set title size.
     *
     * @param titleSize The title size.
     */
    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
        invalidate();
    }

    /**
     * Get Data(Percent value) text size.
     *
     * @return Data(Percent value) text size in int form.
     */
    public int getDataSize() {
        return dataSize;
    }

    /**
     * Set Data(Percent value) text size.
     *
     * @param dataSize Data(Percent value) text size.
     */
    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
        invalidate();
    }

    /**
     * Get radar percent.
     *
     * @return Radar percent in float form.
     */
    public float getRadarPercent() {
        return radarPercent;
    }

    /**
     * Set radar percent.
     *
     * @param radarPercent The radar percent.
     */
    public void setRadarPercent(float radarPercent) {
        this.radarPercent = radarPercent;
        invalidate();
    }

    /**
     * Get start color of gradient ring.
     *
     * @return Start color in Color instance form.
     */
    public Color getStartColor() {
        return startColor;
    }

    /**
     * Set start color of gradient ring.
     *
     * @param startColor The start color.
     */
    public void setStartColor(Color startColor) {
        this.startColor = startColor;
        invalidate();
    }

    /**
     * Get end color of gradient ring.
     *
     * @return End color in Color instance form.
     */
    public Color getEndColor() {
        return endColor;
    }

    /**
     * Set end color of gradient ring.
     *
     * @param endColor The end color.
     */
    public void setEndColor(Color endColor) {
        this.endColor = endColor;
        invalidate();
    }

    /**
     * Return the status of animation (Enabled or Disabled)
     *
     * @return true if the status of animation is enabled.
     */
    public boolean isEnabledAnimation() {
        return enabledAnimation;
    }

    /**
     * Sets whether animation is enable or not.
     * It will reset the animation if the status has changed.
     *
     * @param enabledAnimation true if enable
     */
    public void setEnabledAnimation(boolean enabledAnimation) {
        this.enabledAnimation = enabledAnimation;
        invalidate();
    }

    /**
     * Return the status of showPoint (Enabled or Disabled)
     *
     * @return true if the status of showPoint is enabled.
     */
    public boolean isEnabledShowPoint() {
        return enabledShowPoint;
    }

    /**
     * Sets whether showPoint is enable or not.
     * It will reset the view if the status has changed.
     *
     * @param enabledShowPoint true if enable
     */
    public void setEnabledShowPoint(boolean enabledShowPoint) {
        this.enabledShowPoint = enabledShowPoint;
        invalidate();
    }

    /**
     * Get the cobweb color.
     *
     * @return Cobweb color in Color instance form.
     */
    public Color getCobwebColor() {
        return cobwebColor;
    }

    /**
     * Set the cobweb color.
     *
     * @param cobwebColor The cobweb color.
     */
    public void setCobwebColor(Color cobwebColor) {
        this.cobwebColor = cobwebColor;
        cobwebPaint.setColor(cobwebColor);
        invalidate();
    }

    /**
     * Get the Data(percent value) text color.
     *
     * @return Data(percent value) color in Color instance form.
     */
    public Color getDataColor() {
        return dataColor;
    }

    /**
     * Set the Data(percent value) text color.
     *
     * @param dataColor The Data(percent value) text color.
     */
    public void setDataColor(Color dataColor) {
        this.dataColor = dataColor;
        dataPaint.setColor(dataColor);
        invalidate();
    }

    /**
     * Get the title color.
     *
     * @return Title color in Color instance form.
     */
    public Color getTitleColor() {
        return titleColor;
    }

    /**
     * Set the title color.
     *
     * @param titleColor The title color.
     */
    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
        titlePaint.setColor(titleColor);
        invalidate();
    }

    /**
     * Get the point color.
     *
     * @return Point color in Color instance form.
     */
    public Color getPointColor() {
        return pointColor;
    }

    /**
     * Set the point color.
     *
     * @param pointColor The point color.
     */
    public void setPointColor(Color pointColor) {
        this.pointColor = pointColor;
        pointPaint.setColor(pointColor);
        invalidate();
    }

    /**
     * Get the point radius.
     *
     * @return Point radius in int form.
     */
    public int getPointRadius() {
        return pointRadius;
    }

    /**
     * Set the point radius.
     *
     * @param pointRadius The point radius.
     */
    public void setPointRadius(int pointRadius) {
        this.pointRadius = pointRadius;
        invalidate();
    }

    /**
     * Return the status of area border (Enabled or Disabled)
     *
     * @return true if the status of area border is enabled.
     */
    public boolean isEnabledBorder() {
        return enabledBorder;
    }

    /**
     * Sets whether area border is enable or not.
     * It will reset the view if the status has changed.
     *
     * @param enabledBorder true if border enable.
     */
    public void setEnabledBorder(boolean enabledBorder) {
        this.enabledBorder = enabledBorder;
        invalidate();
    }

    /**
     * Get the border color.
     *
     * @return Border color in Color instance form.
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * Set the border color.
     *
     * @param borderColor The border color.
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    /**
     * Get the boundary width.
     *
     * @return Boundary width in int form.
     */
    public int getBoundaryWidth() {
        return boundaryWidth;
    }

    /**
     * Set the boundary width.
     *
     * @param boundaryWidth The boundary width.
     */
    public void setBoundaryWidth(int boundaryWidth) {
        this.boundaryWidth = boundaryWidth;
    }

    /**
     * Return the status of draw polygon (Enabled or Disabled)
     *
     * @return true if the status of polygon is enabled.
     */
    public boolean isEnabledPolygon() {
        return enabledPolygon;
    }

    /**
     * Sets whether polygon is draw on view or not.
     * It will reset the view if the status has changed.
     *
     * @param enabledPolygon true if polygon enable.
     */
    public void setEnabledPolygon(boolean enabledPolygon) {
        this.enabledPolygon = enabledPolygon;
        invalidate();
    }

    /**
     * Return the status of area outline (Circular or not)
     *
     * @return true if the status of area outline is circular.
     */
    public boolean isCircle() {
        return isCircle;
    }

    /**
     * Sets whether area outline is circular or not.
     * It will reset the view if the status has changed.
     *
     * @param circle true if area outline is circular.
     */
    public void setCircle(boolean circle) {
        isCircle = circle;
        invalidate();
    }

    /**
     * Return the status of shade (enabled or disabled)
     *
     * @return true if the status of shade is enabled.
     */
    public boolean isEnabledShade() {
        return enabledShade;
    }

    /**
     * Sets whether shade is enable or not.
     * It will reset the view if the status has changed.
     *
     * @param enabledShade true if shade is enable.
     */
    public void setEnabledShade(boolean enabledShade) {
        this.enabledShade = enabledShade;
        invalidate();
    }

    /**
     * Return the status of radius (enabled or disabled)
     *
     * @return true if the status of radius is enabled.
     */
    public boolean isEnabledRadius() {
        return enabledRadius;
    }

    /**
     * Sets whether radius is enable or not.
     * It will reset the view if the status has changed.
     *
     * @param enabledRadius true if radius is enable.
     */
    public void setEnabledRadius(boolean enabledRadius) {
        this.enabledRadius = enabledRadius;
        invalidate();
    }

    /**
     * Return the status of title (enabled or disabled)
     *
     * @return true if the status of title is enabled.
     */
    public boolean isEnabledText() {
        return enabledText;
    }

    /**
     * Sets whether title is enable or not.
     * It will reset the view if the status has changed.
     *
     * @param enabledText true if title is enable.
     */
    public void setEnabledText(boolean enabledText) {
        this.enabledText = enabledText;
        invalidate();
    }

    /**
     * Get the single color of area region.
     *
     * @return Single color in Color instance form.
     */
    public Color getSingleColor() {
        return singleColor;
    }

    /**
     * Set the single color.
     *
     * @param singleColor The color of area region when multi-color mode is disable.
     */
    public void setSingleColor(Color singleColor) {
        this.singleColor = singleColor;
        singlePaint.setColor(singleColor);
        invalidate();
    }

    /**
     * Return the status of region shader (enabled or disabled)
     *
     * @return true if the status of region shader is enabled.
     */
    public boolean isEnabledRegionShader() {
        return this.enabledRegionShader;
    }

    /**
     * Sets whether region shader is enable or not.
     * It will reset the view if the status has changed.
     *
     * @param enabled true if region shader is enable.
     */
    public void setEnabledRegionShader(boolean enabled) {
        this.enabledRegionShader = enabled;
        postLayout();
        invalidate();
    }

    /**
     * Get the region shader color array.
     *
     * @return Region shader color array
     */
    public Color[] getShaderColors() {
        return shaderColors;
    }

    /**
     * Get the shader positions array.
     *
     * @return Shader positions float array.
     */
    public float[] getShaderPositions() {
        return shaderPositions;
    }

    /**
     * Set colors and positions for region shader.
     *
     * @param colors    colors array.
     * @param positions positions float array.
     */
    public void setRegionShaderConfig(Color[] colors, float[] positions) {
        this.shaderColors = colors;
        this.shaderPositions = positions;
        postLayout();
        invalidate();
    }

    /**
     * Get path rect.
     *
     * @return instance of RectF.
     */
    public RectF getPathRect() {
        List<Point> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int x, y;
            x = (int) (centerX + percents[i] * radius * Math.cos(angle * i + Math.PI / 2));
            y = (int) (centerY - percents[i] * radius * Math.sin(angle * i + Math.PI / 2));
            Point p = new Point(x, y);
            list.add(p);
        }

        Path path = new Path();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                path.moveTo(list.get(i).getPointX(), list.get(i).getPointY());
            } else {
                path.lineTo(list.get(i).getPointX(), list.get(i).getPointY());
            }
        }
        path.close();
        RectF rect = new RectF();
        path.computeBounds(rect);
        return rect;
    }
}
