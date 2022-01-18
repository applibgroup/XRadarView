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

package com.orzangleli.radar;

/**
 * Model class for node information.
 */
public class Node {

    /**
     * Title of node.
     */
    private CharSequence title = "";

    /**
     * Text value of node.
     */
    private CharSequence value;

    /**
     * Value between (0 to 1.0).
     */
    private double percent;

    /**
     * Icon reference id / resource id of node.
     */
    private int iconRef;

    /**
     * Region area color between this node and next node.
     */
    private int color;

    /**
     * Construct with no parameter.
     */
    public Node() {
    }

    /**
     * Construct to create instance of node with provided parameters.
     *
     * @param title The node title.
     * @param value The node value in text.
     * @param percent The node percent.
     * @param iconRef The resource id of node icon.
     * @param color The color value of node.
     */
    public Node(CharSequence title, CharSequence value, double percent, int iconRef, int color) {
        this.title = title;
        this.value = value;
        this.percent = percent;
        this.iconRef = iconRef;
        this.color = color;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public CharSequence getValue() {
        return value;
    }

    public void setValue(CharSequence value) {
        this.value = value;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getIconRef() {
        return iconRef;
    }

    public void setIconRef(int iconRef) {
        this.iconRef = iconRef;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
