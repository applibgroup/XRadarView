
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

package com.orzangleli.radarview;

import com.orzangleli.radar.XRadarView;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;
import ohos.agp.utils.Color;
import ohos.app.Context;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class ExampleOhosTest {

    private XRadarView xRadarView;

    @Before
    public void setUp() {
        Context context = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();

        AttrSet attrSet = new AttrSet() {
            @Override
            public Optional<String> getStyle() {
                return Optional.empty();
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Optional<Attr> getAttr(int i) {
                return Optional.empty();
            }

            @Override
            public Optional<Attr> getAttr(String s) {
                return Optional.empty();
            }
        };

        xRadarView = new XRadarView(context, attrSet);
    }

    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("com.orzangleli.radarview", actualBundleName);
    }

    @Test
    public void testCount() {
        xRadarView.setCount(5);
        assertEquals(5, xRadarView.getCount());
    }

    @Test
    public void testLayerCount() {
        xRadarView.setLayerCount(5);
        assertEquals(5, xRadarView.getLayerCount());
    }

    @Test
    public void testColors() {
        int[] colors = new int[]{RgbPalette.parse("#A0ffcc00"), RgbPalette.parse("#A000ff00"),
                RgbPalette.parse("#A00000ff"), RgbPalette.parse("#A0FF00FF"), RgbPalette.parse("#A000FFFF"),
                RgbPalette.parse("#A0FFFF00"), RgbPalette.parse("#A000FF00"), RgbPalette.parse("#A0FF00FF")};

        xRadarView.setColors(colors);
        assertEquals(colors, xRadarView.getColors());
    }

    @Test
    public void testDrawableSize() {
        xRadarView.setDrawableSize(30);
        assertEquals(30, xRadarView.getDrawableSize());
    }

    @Test
    public void testDrawablePadding() {
        xRadarView.setDrawablePadding(10);
        assertEquals(10, xRadarView.getDrawablePadding());
    }

    @Test
    public void testDescPadding() {
        xRadarView.setDescPadding(5);
        assertEquals(5, xRadarView.getDescPadding());
    }

    @Test
    public void testTitleSize() {
        xRadarView.setTitleSize(30);
        assertEquals(30, xRadarView.getTitleSize());
    }

    @Test
    public void testRadarPercent() {
        xRadarView.setRadarPercent(5f);
        assertEquals(5f, xRadarView.getRadarPercent(), 0.0f);
    }

    @Test
    public void testStartColor() {
        xRadarView.setStartColor(Color.RED);
        assertEquals(Color.RED, xRadarView.getStartColor());
    }

    @Test
    public void testEndColor() {
        xRadarView.setEndColor(Color.BLUE);
        assertEquals(Color.BLUE, xRadarView.getEndColor());
    }

    @Test
    public void testEnabledAnimation() {
        xRadarView.setEnabledAnimation(false);
        assertFalse(xRadarView.isEnabledAnimation());
    }

    @Test
    public void testEnabledShowPoint() {
        xRadarView.setEnabledShowPoint(false);
        assertFalse(xRadarView.isEnabledShowPoint());
    }

    @Test
    public void testCobwebColor() {
        xRadarView.setCobwebColor(Color.GREEN);
        assertEquals(Color.GREEN, xRadarView.getCobwebColor());
    }

    @Test
    public void testTitleColor() {
        xRadarView.setTitleColor(Color.BLACK);
        assertEquals(Color.BLACK, xRadarView.getTitleColor());
    }

    @Test
    public void testPointColor() {
        xRadarView.setPointColor(Color.BLUE);
        assertEquals(Color.BLUE, xRadarView.getPointColor());
    }

    @Test
    public void testPointRadius() {
        xRadarView.setPointRadius(5);
        assertEquals(5, xRadarView.getPointRadius());
    }

    @Test
    public void testEnabledBorder() {
        xRadarView.setEnabledBorder(true);
        assertTrue(xRadarView.isEnabledBorder());
    }

    @Test
    public void testBorderColor() {
        xRadarView.setBorderColor(Color.GREEN);
        assertEquals(Color.GREEN, xRadarView.getBorderColor());
    }

    @Test
    public void testBoundaryWidth() {
        xRadarView.setBoundaryWidth(5);
        assertEquals(5, xRadarView.getBoundaryWidth());
    }

    @Test
    public void testEnabledPolygon() {
        xRadarView.setEnabledPolygon(true);
        assertTrue(xRadarView.isEnabledPolygon());
    }

    @Test
    public void testCircle() {
        xRadarView.setCircle(false);
        assertFalse(xRadarView.isCircle());
    }

    @Test
    public void testEnabledShade() {
        xRadarView.setEnabledShade(false);
        assertFalse(xRadarView.isEnabledShade());
    }

    @Test
    public void testEnabledRadius() {
        xRadarView.setEnabledRadius(false);
        assertFalse(xRadarView.isEnabledRadius());
    }

    @Test
    public void testEnabledText() {
        xRadarView.setEnabledText(false);
        assertFalse(xRadarView.isEnabledText());
    }

    @Test
    public void testSingleColor() {
        xRadarView.setSingleColor(Color.GREEN);
        assertEquals(Color.GREEN, xRadarView.getSingleColor());
    }

    @Test
    public void testEnabledRegionShader() {
        xRadarView.setEnabledRegionShader(false);
        assertFalse(xRadarView.isEnabledRegionShader());
    }

    @Test
    public void testShaderRegionConfig() {
        Color[] colors = new Color[]{Color.YELLOW, Color.RED};
        float[] positions = new float[]{0.2f, 0.6f};
        xRadarView.setRegionShaderConfig(colors, positions);
        assertArrayEquals(colors, xRadarView.getShaderColors());
        assertArrayEquals(positions, xRadarView.getShaderPositions(), 0.0f);
    }

}