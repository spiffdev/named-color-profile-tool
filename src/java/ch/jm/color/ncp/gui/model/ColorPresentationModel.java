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

/* $Id: ColorPresentationModel.java 1076 2010-07-10 05:22:18Z jeremias $ */

package ch.jm.color.ncp.gui.model;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueHolder;

public class ColorPresentationModel extends PresentationModel<ColorModel> {

    public ColorPresentationModel(ColorModel color) {
        super(new ValueHolder(color, true));
        initEventHandling();
    }

    private void initEventHandling() {
        getModel(ColorModel.PROPERTY_COLOR_NAME).addValueChangeListener(
                new ColorNameChangeHandler());
        /*
        getModel(ColorModel.PROPERTY_COMP_L).addValueChangeListener(
                new LabComponentChangeHandler());
                */
    }

    /*
    public float getL() {
        return (Float)getModel(ColorModel.PROPERTY_COMP_L).getValue();
    }*/

    public String getColorName() {
        return (String)getModel(ColorModel.PROPERTY_COLOR_NAME).getValue();
    }

    public Color getColor() {
        return (Color)getModel(ColorModel.PROPERTY_COLOR).getValue();
    }

    private final class ColorNameChangeHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange(
                    ColorModel.PROPERTY_COLOR_NAME,
                    null,
                    getColorName());
        }
    }

    /*
    private final class LabComponentChangeHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange(
                    ColorModel.PROPERTY_COMP_L,
                    null,
                    getL());
        }
    }*/
}
