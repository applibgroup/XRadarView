package com.orzangleli.radar;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.LinearShader;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.Shader;
import ohos.agp.text.SimpleTextLayout;
import ohos.agp.utils.Color;
import ohos.agp.utils.Matrix;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.TouchEvent;
import com.orzangleli.radar.utils.LogUtil;
import com.orzangleli.radar.utils.Utils;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * XRadarView is a highly customizable radar view.
 * Created by lixiancheng on 2017/9/18.
 */
public class XRadarView extends Component implements Component.DrawTask, Component.TouchEventListener {

    private static final String TAG = XRadarView.class.getSimpleName();

    /* -------------------- ATTRIBUTE MEMBERS -------------------- */

    // Several-sided radar
    private int count = 5;

    // Number of layer
    private int layerCount = 6;

    // Size of icons
    private int drawableSize = 40;

    // Icon and text spacing
    private int drawablePadding = 10;

    // Title and node spacing
    private int descPadding = 5;

    // Title size
    private int titleSize = 40;

    // The ratio of the radar chart graphic to the entire space
    private float radarPercent = 0.7f;

    // When the gradient is turned on, the color at the center of the circle
    private Color startColor = new Color(getContext().getColor(ResourceTable.Color_start_circle));

    // When the gradient is turned on, the color at the outer circle
    private Color endColor = new Color(getContext().getColor(ResourceTable.Color_end_circle));

    // The color of the cobweb line
    private Color cobwebColor;

    // If it is not a multicolored area, it is a single color
    private Color singleColor;

    // The color of the title text
    private Color titleColor;

    // Dot color
    private Color pointColor;

    // The color of the boundary line
    private Color borderColor;

    // The color of the radius line
    private Color radiusColor;

    // The width of the boundary line
    private int boundaryWidth;

    // The size of the dot radius
    private int pointRadius;

    // Whether to draw a boundary line
    private boolean enabledBorder = false;

    // Whether to turn on the animation
    private boolean enabledAnimation = true;

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

    // Whether the outer outline is circular
    private boolean enabledCircularOutline = false;

    // Whether the region is draw with multi-color.
    private boolean enabledMultiColorRegion = false;

    // The duration of the animation
    private int animDuration = 1000;

    /* -------------------- REQUIRED FIELDS -------------------- */

    // Radar chart gradient color array
    private Color[] shaderColors;

    // The location of various color distributions of radar map gradient colors
    private float[] shaderPositions;

    // Text maximum allowable width
    private int maxTextWidth;

    // The center of the circle for each edge
    private float angle;
    // Round x
    private int centerX;
    // Round y
    private int centerY;
    // radius
    private float radius;

    // Area gradient shader
    private Shader regionShader;

    // Whether to paint the radar area as a gradient color
    private boolean enabledRegionShader = false;

    // Whether icons are draw with title.
    private boolean enableIcons = true;

    // The current scale ratio
    private float currentScale;

    private List<Rect> titleRects;

    //array list of node.
    private List<Node> nodeList;
    // array of icons
    private int[] drawables;
    // array of titles
    CharSequence[] titles;
    // Value for each property (0 to 1.0)
    Double[] percents;
    // Numeric text below each title
    CharSequence[] values;
    // array of multi-region area color
    int[] colors;


    /* -------------------- PAINT & DRAW -------------------- */
    private Paint cobwebPaint;
    private Paint multiRegionPaint;
    private Paint singlePaint;
    private Paint titlePaint;
    private Paint layerPaint;
    private Paint pointPaint;
    private Paint radiusPaint;
    private Paint borderPaint;

    /* -------------------- Interfaces -------------------- */
    OnTitleClickListener onTitleClickListener;

    public XRadarView(Context context) {
        this(context, null);
    }

    public XRadarView(Context context, @Nullable AttrSet attrs) {
        this(context, attrs, 0);
    }

    public XRadarView(Context context, @Nullable AttrSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttrSet attrSet) {

        Optional<Attr> attr;
        if (attrSet != null) {

            attr = attrSet.getAttr(Attribute.COUNT);
            count = attr.map(Attr::getIntegerValue).orElse(6);

            attr = attrSet.getAttr(Attribute.LAYER_COUNT);
            layerCount = attr.map(Attr::getIntegerValue).orElse(6);

            attr = attrSet.getAttr(Attribute.DRAWABLE_SIZE);
            drawableSize = attr.map(Attr::getDimensionValue).orElse(40);

            attr = attrSet.getAttr(Attribute.DRAWABLE_PADDING);
            drawablePadding = attr.map(Attr::getDimensionValue).orElse(10);

            attr = attrSet.getAttr(Attribute.DESC_PADDING);
            descPadding = attr.map(Attr::getDimensionValue).orElse(5);

            attr = attrSet.getAttr(Attribute.TITLE_SIZE);
            titleSize = attr.map(Attr::getDimensionValue).orElse(40);

            attr = attrSet.getAttr(Attribute.RADAR_PERCENT);
            radarPercent = attr.map(Attr::getFloatValue).orElse(0.7f);

            attr = attrSet.getAttr(Attribute.START_COLOR);
            startColor = attr.map(Attr::getColorValue)
                    .orElse(new Color(getContext().getColor(ResourceTable.Color_start_circle)));

            attr = attrSet.getAttr(Attribute.END_COLOR);
            endColor = attr.map(Attr::getColorValue)
                    .orElse(new Color(getContext().getColor(ResourceTable.Color_end_circle)));

            attr = attrSet.getAttr(Attribute.COBWEB_COLOR);
            cobwebColor = attr.map(Attr::getColorValue)
                    .orElse(new Color(getContext().getColor(ResourceTable.Color_cobweb)));

            attr = attrSet.getAttr(Attribute.SINGLE_COLOR);
            singleColor = attr.map(Attr::getColorValue)
                    .orElse(new Color(getContext().getColor(ResourceTable.Color_single)));

            attr = attrSet.getAttr(Attribute.TITLE_COLOR);
            titleColor = attr.map(Attr::getColorValue)
                    .orElse(new Color(getContext().getColor(ResourceTable.Color_title)));

            attr = attrSet.getAttr(Attribute.POINT_COLOR);
            pointColor = attr.map(Attr::getColorValue)
                    .orElse(new Color(getContext().getColor(ResourceTable.Color_point)));

            attr = attrSet.getAttr(Attribute.BORDER_COLOR);
            borderColor = attr.map(Attr::getColorValue)
                    .orElse(new Color(getContext().getColor(ResourceTable.Color_border)));

            attr = attrSet.getAttr(Attribute.RADIUS_COLOR);
            radiusColor = attr.map(Attr::getColorValue)
                    .orElse(new Color(getContext().getColor(ResourceTable.Color_radius)));

            attr = attrSet.getAttr(Attribute.BOUNDARY_WIDTH);
            boundaryWidth = attr.map(Attr::getDimensionValue).orElse(5);

            attr = attrSet.getAttr(Attribute.POINT_RADIUS);
            pointRadius = attr.map(Attr::getDimensionValue).orElse(10);

            attr = attrSet.getAttr(Attribute.ENABLED_BORDER);
            enabledBorder = attr.map(Attr::getBoolValue).orElse(false);

            attr = attrSet.getAttr(Attribute.ENABLED_ANIMATION);
            enabledAnimation = attr.map(Attr::getBoolValue).orElse(true);

            attr = attrSet.getAttr(Attribute.ENABLED_SHOW_POINT);
            enabledShowPoint = attr.map(Attr::getBoolValue).orElse(true);

            attr = attrSet.getAttr(Attribute.ENABLED_POLYGON);
            enabledPolygon = attr.map(Attr::getBoolValue).orElse(true);

            attr = attrSet.getAttr(Attribute.ENABLED_SHADE);
            enabledShade = attr.map(Attr::getBoolValue).orElse(true);

            attr = attrSet.getAttr(Attribute.ENABLED_RADIUS);
            enabledRadius = attr.map(Attr::getBoolValue).orElse(true);

            attr = attrSet.getAttr(Attribute.ENABLED_TEXT);
            enabledText = attr.map(Attr::getBoolValue).orElse(true);

            attr = attrSet.getAttr(Attribute.ENABLED_CIRCULAR_OUTLINE);
            enabledCircularOutline = attr.map(Attr::getBoolValue).orElse(false);

            attr = attrSet.getAttr(Attribute.ENABLED_MULTI_COLOR_REGION);
            enabledMultiColorRegion = attr.map(Attr::getBoolValue).orElse(false);

            attr = attrSet.getAttr(Attribute.ANIMATION_DURATION);
            animDuration = attr.map(Attr::getIntegerValue).orElse(1000);
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

        multiRegionPaint = new Paint();
        multiRegionPaint.setAntiAlias(true);
        multiRegionPaint.setStyle(Paint.Style.FILLANDSTROKE_STYLE);

        singlePaint = new Paint();
        singlePaint.setColor(singleColor);
        singlePaint.setAntiAlias(true);
        singlePaint.setStyle(Paint.Style.FILLANDSTROKE_STYLE);

        titlePaint = new Paint();
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

        nodeList = new ArrayList<>();
        colors = new int[count];
        drawables = new int[count];
        titles = new CharSequence[count];
        percents = new Double[count];
        values = new CharSequence[count];

        addDrawTask(this);
        setLayoutRefreshedListener(new RefreshListener());
        setTouchEventListener(this);
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {

        if (touchEvent.getAction() == TouchEvent.PRIMARY_POINT_DOWN) {
            int x = (int) touchEvent.getPointerPosition(touchEvent.getIndex()).getX();
            int y = (int) touchEvent.getPointerPosition(touchEvent.getIndex()).getY();
            for (int i = 0; i < titleRects.size(); i++) {
                Rect rect = titleRects.get(i);
                if (rect != null && rect.contains(x, y, x, y) && onTitleClickListener != null) {
                    onTitleClickListener.onTitleClick(XRadarView.this, i, x, y, rect);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * RefreshListener updates when view is relayout.
     */
    class RefreshListener implements LayoutRefreshedListener {
        @Override
        public void onRefreshed(Component component) {
            int w = component.getWidth();
            int h = component.getHeight();
            radius = Math.min(h, w) / 2.0f * radarPercent;
            maxTextWidth = (int) (Math.min(h, w) / 2.0 * (1 - radarPercent));
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

        if (count > nodeList.size()) {
            LogUtil.error(
                    TAG,
                    "Count value is: " + count
                            + " and size of node list is: " + nodeList.size()
                            + ", Count value must be less than or equal to node list size!");
            return;
        }

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
        if (enabledMultiColorRegion) {
            drawRegionWithColor(canvas, currentScale);
        } else {
            drawRegion(canvas, currentScale);
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
        float curX;
        float curY;
        float nextX;
        float nextY;
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
            int x;
            int y;
            x = (int) (centerX + scale * percents[i] * radius * Math.cos(angle * i + Math.PI / 2));
            y = (int) (centerY - scale * percents[i] * radius * Math.sin(angle * i + Math.PI / 2));
            canvas.drawCircle(x, y, pointRadius, pointPaint);
        }
    }

    /**
     * Draws a line between the center of the circle and the vertex.
     *
     * @param canvas instance of Canvas
     */
    private void drawRadius(Canvas canvas) {
        for (int i = 0; i < count; i++) {
            canvas.drawLine(
                    centerX,
                    centerY,
                    (float) (centerX + Math.cos(angle * i + Math.PI / 2) * radius),
                    (float) (centerY - Math.sin(angle * i + Math.PI / 2) * radius),
                    radiusPaint);
        }
    }

    /**
     * Draw cobwebs.
     *
     * @param canvas instance of Canvas
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float r = radius / layerCount;
        for (int i = layerCount; i >= 1; i--) {
            float curR = r * i;
            path.reset();
            if (enabledCircularOutline) {
                path.addCircle(centerX, centerY, curR, Path.Direction.CLOCK_WISE);
            } else {
                for (int j = 0; j < count; j++) {
                    if (j == 0) {
                        path.moveTo(centerX, centerY - curR);
                    } else {
                        path.lineTo(
                                (float) (centerX + Math.cos(angle * j + Math.PI / 2) * curR),
                                (float) (centerY - Math.sin(angle * j + Math.PI / 2) * curR));
                    }
                }
                path.close();
            }
            canvas.drawPath(path, cobwebPaint);
        }
    }

    /**
     * Draw the colors of each layer.
     *
     * @param canvas     instance of Canvas
     * @param startColor start color
     * @param endColor   end color
     */
    private void drawLayer(Canvas canvas, Color startColor, Color endColor) {
        Path path;
        Path prePath = null;
        float r = radius / layerCount;
        for (int i = layerCount; i >= 0; i--) {
            float curR = r * i;
            path = new Path();
            for (int j = 0; j < count; j++) {
                if (enabledCircularOutline) {
                    path.addCircle(centerX, centerY, curR, Path.Direction.CLOCK_WISE);
                } else {
                    if (j == 0) {
                        path.moveTo(centerX, centerY - curR);
                    } else {
                        path.lineTo(
                                (float) (centerX + Math.cos(angle * j + Math.PI / 2) * curR),
                                (float) (centerY - Math.sin(angle * j + Math.PI / 2) * curR));
                    }
                }
            }

            if (prePath != null) {
                prePath.close();
                setLayerPaintColor(i, startColor, endColor);
                canvas.drawPath(prePath, layerPaint);
            }
            prePath = path;
        }
    }

    /**
     * Set color in layer paint instance on the basis of layer number.
     * If layer number is 0 then, set start color
     * Else calculate the gradient color and set gradient color to it.
     *
     * @param layerNum   layer number
     * @param startColor start color
     * @param endColor   end color
     */
    private void setLayerPaintColor(int layerNum, Color startColor, Color endColor) {
        if (layerNum != 0) {

            // Calculate the gradient color
            int a0 = Color.alpha(startColor.getValue());
            int r0 = (startColor.getValue() >> 16) & 0xFF;
            int g0 = (startColor.getValue() >> 8) & 0xFF;
            int b0 = startColor.getValue() & 0xFF;

            int a1 = Color.alpha(endColor.getValue());
            int r1 = (endColor.getValue() >> 16) & 0xFF;
            int g1 = (endColor.getValue() >> 8) & 0xFF;
            int b1 = endColor.getValue() & 0xFF;

            int a2 = (int) (1.0 * layerNum * (a1 - a0) / layerCount + a0);
            int r2 = (int) (1.0 * layerNum * (r1 - r0) / layerCount + r0);
            int g2 = (int) (1.0 * layerNum * (g1 - g0) / layerCount + g0);
            int b2 = (int) (1.0 * layerNum * (b1 - b0) / layerCount + b0);

            layerPaint.setColor(new Color(Color.argb(a2, r2, g2, b2)));
        } else {
            layerPaint.setColor(startColor);
        }
    }

    /**
     * Draw text and icons.
     * <B>Note:</B> Currently Rich text functionality not implemented
     * because of SpannableString class not available in OHOS.
     * And also dataSize attribute is also connected with this implementation.
     * So for better understand please refer android library.
     *
     * @param canvas instance of Canvas
     */
    private void drawText(Canvas canvas) {
        for (int i = 0; i < count; i++) {
            float x;
            float y;
            float curAngle;

            x = (float) (centerX + (radius) * Math.cos(angle * i + Math.PI / 2));
            y = (float) (centerY - (radius) * Math.sin(angle * i + Math.PI / 2));
            curAngle = (float) (angle * i + Math.PI / 2);

            // The rest is in the range of 0-2PI
            curAngle = (float) (curAngle % (2 * Math.PI));

            CharSequence ss = getTitleCharSequence(i);

            if (!enableIcons) {
                drawables = new int[count];
            }
            if (Math.abs(curAngle - 3 * Math.PI / 2) < 0.1 || Math.abs(curAngle - Math.PI / 2) < 0.1) {
                callWithDiffVerticalVal(canvas, curAngle, x, y, ss, i);
            } else if (curAngle >= 0 && curAngle < Math.PI / 2) {
                drawMultiLinesTextAndIcon(canvas, x + descPadding, y, ss, drawables[i], 0, i);
            } else if (curAngle > 3 * Math.PI / 2 && curAngle <= Math.PI * 2) {
                drawMultiLinesTextAndIcon(canvas, x + descPadding, y, ss, drawables[i], 0, i);
            } else if (curAngle > Math.PI / 2 && curAngle <= Math.PI) {
                drawMultiLinesTextAndIcon(canvas, x - maxTextWidth, y, ss, drawables[i], 0, i);
            } else if (curAngle >= Math.PI && curAngle < 3 * Math.PI / 2) {
                drawMultiLinesTextAndIcon(canvas, x - maxTextWidth, y, ss, drawables[i], 0, i);
            }
        }
    }

    /**
     * Call Draw multi-Line text and icon method with different vertical value.
     *
     * @param canvas   instance of Canvas
     * @param curAngle Curve angle
     * @param x        x coordinate
     * @param y        y coordinate
     * @param ss       instance of CharSequence to draw text
     * @param i        position
     */
    private void callWithDiffVerticalVal(Canvas canvas, float curAngle, float x, float y, CharSequence ss, int i) {
        if (Math.abs(curAngle - Math.PI / 2) < 0.1) {
            drawMultiLinesTextAndIcon(canvas, x - maxTextWidth / 2.0f, y, ss, drawables[i], 1, i);
        } else if (Math.abs(curAngle - Math.PI * 3 / 2) < 0.1) {
            drawMultiLinesTextAndIcon(canvas, x - maxTextWidth / 2.0f, y, ss, drawables[i], -1, i);
        } else {
            drawMultiLinesTextAndIcon(canvas, x - maxTextWidth / 2.0f, y, ss, drawables[i], 0, i);
        }
    }

    /**
     * Get title and value in char sequence object.
     *
     * @param position index of title and value.
     * @return Title and value in the form of CharSequence.
     */
    private CharSequence getTitleCharSequence(int position) {
        CharSequence ss;
        if (values == null || values[position] == null) {
            ss = titles[position];
        } else {
            ss = titles[position] + "\n" + values[position];
        }
        return ss;
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

        PixelMap newPm = PixelMap.create(pixelMap, initializationOptions);
        pixelMap.release();
        return newPm;
    }

    /**
     * Draw an area in one color.
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
            int x;
            int y;
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
     * Draw areas in a variety of colors.
     *
     * @param canvas instance of Canvas
     * @param scale  scale to calculate x and y coordinates
     */
    private void drawRegionWithColor(Canvas canvas, float scale) {
        canvas.save();
        int colorSize = colors.length;
        List<Point> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int x;
            int y;
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
            multiRegionPaint.setColor(new Color(colors[i % colorSize]));
            canvas.drawPath(path, multiRegionPaint);
        }
        path.reset();
        path.moveTo(centerX, centerY);
        path.lineTo(list.get(list.size() - 1).getPointX(), list.get(list.size() - 1).getPointY());
        path.lineTo(list.get(0).getPointX(), list.get(0).getPointY());
        path.lineTo(centerX, centerY);
        path.close();
        multiRegionPaint.setColor(new Color(colors[(list.size() - 1) % colorSize]));
        canvas.drawPath(path, multiRegionPaint);
        canvas.restore();
    }

    /**
     * Draw multiple lines of text.
     *
     * @param canvas        instance of Canvas
     * @param x             x coordinate
     * @param y             y coordinate
     * @param text          instance of CharSequence to draw text
     * @param drawable      icon drawable
     * @param verticalValue vertical value
     * @param position      position
     */
    private void drawMultiLinesTextAndIcon(
            Canvas canvas,
            float x,
            float y,
            CharSequence text,
            int drawable,
            int verticalValue,
            int position) {
        int drawableAvaiable;
        try {
            this.getContext().getResourceManager().getResource(drawable);
            drawableAvaiable = 1;
        } catch (Exception e) {
            drawableAvaiable = 0;
        }
        int allowWidth = maxTextWidth - descPadding;
        int rectWidth = allowWidth;
        Rect rect;
        if (drawable != -1 && allowWidth > drawableSize * drawableAvaiable + drawablePadding) {
            allowWidth = allowWidth - drawableSize * drawableAvaiable - drawablePadding;
        }

        titlePaint.setTextSize(titleSize);
        SimpleTextLayout layout = new SimpleTextLayout(text.toString(), titlePaint, new Rect(), allowWidth, true);
        canvas.save();
        if (verticalValue == 1) {
            canvas.translate(x, y - layout.getHeight() - descPadding);
            rect = new Rect(
                    (int) x,
                    (int) (y - layout.getHeight() - descPadding),
                    (int) x + rectWidth,
                    (int) (y - layout.getHeight() - descPadding) + layout.getHeight());
        } else if (verticalValue == -1) {
            canvas.translate(x, y + descPadding);
            rect = new Rect(
                    (int) x,
                    (int) (y + descPadding),
                    (int) x + rectWidth,
                    (int) (y + descPadding) + layout.getHeight());
        } else {
            canvas.translate(x, y - layout.getHeight() / 2.0f);
            rect = new Rect(
                    (int) x,
                    (int) (y - layout.getHeight() / 2.0),
                    (int) x + rectWidth,
                    (int) (y - layout.getHeight() / 2.0) + layout.getHeight());
        }
        titleRects.set(position, rect);
        layout.drawText(canvas);
        canvas.restore(); //Don't forget restore

        // Draw the icon
        if (drawableSize * drawableAvaiable != 0) {
            PixelMap pixelmap = getResizePixelmap(drawable);
            PixelMapHolder pixelMapHolder = new PixelMapHolder(pixelmap);
            if (pixelmap != null) {
                if (verticalValue == 1) {
                    canvas.drawPixelMapHolder(
                            pixelMapHolder,
                            x + layout.getWidth(),
                            y - drawableSize * drawableAvaiable / 2.0f - layout.getHeight() / 2.0f - descPadding,
                            titlePaint);
                } else if (verticalValue == -1) {
                    canvas.drawPixelMapHolder(
                            pixelMapHolder,
                            x + layout.getWidth(),
                            y + descPadding + layout.getHeight() / 2.0f - drawableSize * drawableAvaiable / 2.0f,
                            titlePaint);
                } else {
                    canvas.drawPixelMapHolder(
                            pixelMapHolder,
                            x + layout.getWidth(),
                            y - drawableSize * drawableAvaiable / 2.0f,
                            titlePaint);
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

    /**
     * Interface to callback title click event.
     */
    public interface OnTitleClickListener {
        void onTitleClick(XRadarView view, int position, int touchX, int touchY, Rect titleRect);
    }

    /**
     * Get node list.
     *
     * @return The node list instance.
     */
    public List<Node> getNodeList() {
        return nodeList;
    }

    /**
     * Set node list and parse titles, values, persents, drawables and colors array from given node list.
     *
     * @param nodeList The node list.
     */
    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
        this.titles = nodeList.stream().map(Node::getTitle).collect(Collectors.toList()).toArray(titles);
        this.values = nodeList.stream().map(Node::getValue).collect(Collectors.toList()).toArray(values);
        this.percents = nodeList.stream().map(Node::getPercent).collect(Collectors.toList()).toArray(percents);
        this.drawables = nodeList.stream().map(Node::getIconRef).collect(Collectors.toList())
                .stream().mapToInt(Integer::intValue).toArray();
        this.colors = nodeList.stream().map(Node::getColor).collect(Collectors.toList())
                .stream().mapToInt(Integer::intValue).toArray();

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
     * Set count.
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
     * Get the radius color.
     *
     * @return Radius color in Color instance form.
     */
    public Color getRadiusColor() {
        return radiusColor;
    }

    /**
     * Set the radius color.
     *
     * @param radiusColor The radius color.
     */
    public void setRadiusColor(Color radiusColor) {
        this.radiusColor = radiusColor;
        radiusPaint.setColor(radiusColor);
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
        borderPaint.setStrokeWidth(boundaryWidth);
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
     * Return the status of area border (Enabled or Disabled).
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
     * Return the status of animation (Enabled or Disabled).
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
     * Return the status of showPoint (Enabled or Disabled).
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
     * Return the status of draw polygon (Enabled or Disabled).
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
     * Return the status of shade (enabled or disabled).
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
     * Return the status of radius (enabled or disabled).
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
     * Return the status of title (enabled or disabled).
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
     * Return the status of area outline (Circular or not).
     *
     * @return true if the status of area outline is circular.
     */
    public boolean isEnabledCircularOutline() {
        return enabledCircularOutline;
    }

    /**
     * Sets whether area outline is circular or not.
     * It will reset the view if the status has changed.
     *
     * @param enabledCircularOutline true if area outline is circular.
     */
    public void setEnabledCircularOutline(boolean enabledCircularOutline) {
        this.enabledCircularOutline = enabledCircularOutline;
        invalidate();
    }

    /**
     * Return the status of region (is draw with multi-color or not).
     *
     * @return true if the status region is draw with multi-color.
     */
    public boolean isEnabledMultiColorRegion() {
        return enabledMultiColorRegion;
    }

    /**
     * Sets whether region is draw with multi-color.
     *
     * @param enabledMultiColorRegion true if region is draw with multi-color.
     */
    public void setEnabledMultiColorRegion(boolean enabledMultiColorRegion) {
        this.enabledMultiColorRegion = enabledMultiColorRegion;
        invalidate();
    }

    /**
     * Return the status of region shader (enabled or disabled).
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
     * Return the status of icons (enabled or disabled).
     *
     * @return true if the status of icons is enabled.
     */
    public boolean isEnableIcons() {
        return enableIcons;
    }

    /**
     * Set whether icons are enable or not.
     *
     * @param enableIcons true if icons is enable.
     */
    public void setEnableIcons(boolean enableIcons) {
        this.enableIcons = enableIcons;
        drawables = nodeList.stream().map(Node::getIconRef).collect(Collectors.toList())
                .stream().mapToInt(Integer::intValue).toArray();
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
}
