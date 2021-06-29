package com.orzangleli.radar;

import ohos.agp.render.Paint;

public class TextPaint extends Paint {

    // Special value 0 means no background paint
    public int bgColor;
    public int baselineShift;

    public int linkColor;
    public int[] drawableState;
    public float density = 1.0f;
    /**
     * Special value 0 means no custom underline
     */

    public int underlineColor = 0;
    /**
     * Defined as a multiplier of the default underline thickness. Use 1.0f for default thickness.
     */
    public float underlineThickness;

    public TextPaint() {
        super();
    }

    public TextPaint(Paint p) {
        super(p);
    }

    /**
     * Copy the fields from tp into this TextPaint, including the
     * fields inherited from Paint.
     */
    public void set(TextPaint tp) {
        super.set(tp);

        bgColor = tp.bgColor;
        baselineShift = tp.baselineShift;
        linkColor = tp.linkColor;
        drawableState = tp.drawableState;
        density = tp.density;
        underlineColor = tp.underlineColor;
        underlineThickness = tp.underlineThickness;
    }

    /**
     * Defines a custom underline for this Paint.
     *
     * @param color     underline solid color
     * @param thickness underline thickness
     */
    public void setUnderlineText(int color, float thickness) {
        underlineColor = color;
        underlineThickness = thickness;
    }
}
