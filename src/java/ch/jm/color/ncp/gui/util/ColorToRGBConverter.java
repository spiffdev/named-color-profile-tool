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

/* $Id: ColorToRGBConverter.java 1076 2010-07-10 05:22:18Z jeremias $ */

package ch.jm.color.ncp.gui.util;

import java.awt.Color;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;

/**
 * Converts Colors values to sRGB component values and vice-versa.
 */
public class ColorToRGBConverter extends AbstractConverter {

    private int component;

    ColorToRGBConverter(ValueModel colorSubject, int component) {
        super(colorSubject);
        this.component = component;
    }

    public static ValueModel createConverter(
            ValueModel colorSubject, int component) {
        return new ColorToRGBConverter(colorSubject, component);
    }

    @Override
    public Object convertFromSubject(Object subjectValue) {
        Color col = (Color)subjectValue;
        if (col == null) {
            return 0;
        }
        switch (component) {
        case 0:
            return col.getRed();
        case 1:
            return col.getGreen();
        case 2:
            return col.getBlue();
        default:
            throw new IllegalStateException("Invalid component: " + component);
        }
    }

    public void setValue(Object newValue) {
        throw new UnsupportedOperationException();
    }

}