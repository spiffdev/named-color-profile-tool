/*
 * Copyright 2010 Jeremias Maerki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

/* $Id: ColorModel.java 1076 2010-07-10 05:22:18Z jeremias $ */

package ch.jm.color.ncp.gui.model;

import java.awt.Color;

import org.apache.xmlgraphics.java2d.color.CIELabColorSpace;
import org.apache.xmlgraphics.java2d.color.ColorSpaces;

import com.jgoodies.binding.beans.Model;

public class ColorModel extends Model implements ColorPropertyConstants {

    private CIELabColorSpace labColorSpace = ColorSpaces.getCIELabColorSpaceD50();

    private String colorName = "My Color";

    private float[] lab = new float[3];
    private Color col;

    public ColorModel() {
        updateColor();
    }

    private void updateColor() {
        Color old = this.col;
        Color value = old;
        try {
            value = labColorSpace.toColor(lab, 1.0f);
            firePropertyChange(PROPERTY_COLOR, old, value);
            this.col = value;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setColorName(String value) {
        String old = this.colorName;
        this.colorName = value;
        firePropertyChange(PROPERTY_COLOR_NAME, old, value);
    }

    public String getColorName() {
        return this.colorName;
    }

    public void setLabL(float value) {
        float old = this.lab[0];
        this.lab[0] = value;
        firePropertyChange(PROPERTY_COMP_L, old, value);
        updateColor();
    }

    public float getLabL() {
        return this.lab[0];
    }

    public void setLabA(float value) {
        float old = this.lab[1];
        this.lab[1] = value;
        firePropertyChange(PROPERTY_COMP_A, old, value);
        updateColor();
    }

    public float getLabA() {
        return this.lab[1];
    }

    public void setLabB(float value) {
        float old = this.lab[2];
        this.lab[2] = value;
        firePropertyChange(PROPERTY_COMP_B, old, value);
        updateColor();
    }

    public float getLabB() {
        return this.lab[2];
    }

    public Color getColor() {
        return this.col;
    }

}
