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

/* $Id: ColorView.java 1219 2011-01-20 08:08:36Z jeremias $ */

package ch.jm.color.ncp.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

import ch.jm.color.ncp.gui.model.ColorModel;
import ch.jm.color.ncp.gui.model.ColorPropertyConstants;
import ch.jm.color.ncp.gui.util.ColorToRGBConverter;
import ch.jm.color.ncp.gui.util.ColorToXYZConverter;
import ch.jm.color.ncp.gui.util.FloatToDoubleConverter;
import ch.jm.color.ncp.gui.util.SelectAllFocusListener;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.validation.view.ValidationComponentUtils;

public class ColorView extends JPanel implements ColorPropertyConstants {

    private static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);
    private static final Insets DEF_INSETS = new Insets(3, 6, 3, 6); // top, left, bottom, right;
    private static final Insets END_GROUP_INSETS = new Insets(3, 6, 9, 6); // top, left, bottom, right;

    private static final Format FLOAT_FORMAT = new DecimalFormat("0.00####");
    private static final Format INT_FORMAT = new DecimalFormat("0");

    private static enum ValueType {
        FLOAT(FLOAT_FORMAT), INT(INT_FORMAT);

        private Format format;

        private ValueType(Format format) {
            this.format = format;
        }

        public Format getFormat() {
            return this.format;
        }
    };

    private PresentationModel<ColorModel> model;

    private JPanel colorPreview = new JPanel();

    public ColorView(PresentationModel<ColorModel> model) {
        super(new GridBagLayout());
        this.model = model;

        initComponents();
        ResourceMap resource = getContext().getResourceMap(ColorView.class);
        resource.injectComponents(this);
    }

    private ApplicationContext getContext() {
        return Application.getInstance().getContext();
    }

    private void initComponents() {
        SelectAllFocusListener.decorate(this);

        GridBagConstraints c = new GridBagConstraints();
        initGridBagConstraints(c);

        c.insets = END_GROUP_INSETS;
        c.anchor = GridBagConstraints.LINE_START; //BASELINE_LEADING
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        JLabel colorNameLabel = new JLabel();
        colorNameLabel.setName(PROPERTY_COLOR_NAME + "Label");
        add(colorNameLabel, c);

        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 5;
        JTextField colorName = BasicComponentFactory.createTextField(
                model.getModel(PROPERTY_COLOR_NAME));
        colorName.setName(PROPERTY_COLOR_NAME);
        add(colorName, c);
        colorNameLabel.setLabelFor(colorName);
        ValidationComponentUtils.setMandatory(colorName, true);

        c.gridwidth = 1;
        c.gridy = 2;
        addComponentLabelValuePair(c, PROPERTY_COMP_L, ValueType.FLOAT);
        addComponentLabelValuePair(c, PROPERTY_COMP_A, ValueType.FLOAT);
        addComponentLabelValuePair(c, PROPERTY_COMP_B, ValueType.FLOAT);

        c.gridy = 3;
        addXYZComponentLabelValuePair(c, PROPERTY_COMP_X, 0);
        addXYZComponentLabelValuePair(c, PROPERTY_COMP_Y, 1);
        addXYZComponentLabelValuePair(c, PROPERTY_COMP_Z, 2);

        c.gridy = 4;
        addRGBComponentLabelValuePair(c, PROPERTY_RGB_R, 0);
        addRGBComponentLabelValuePair(c, PROPERTY_RGB_G, 1);
        addRGBComponentLabelValuePair(c, PROPERTY_RGB_B, 2);

        initGridBagConstraints(c);
        c.insets = new Insets(9, 6, 9, 6);
        c.gridy = 5;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 6;
        c.weighty = 1.0;
        colorPreview.setName("colorPreview");
        if (model.getBean() != null) {
            colorPreview.setBackground(model.getBean().getColor());
        }
        add(colorPreview, c);

        model.addBeanPropertyChangeListener(PROPERTY_COLOR, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                Color col = (Color)evt.getNewValue();
                colorPreview.setBackground(col);
            }
        });
        model.getBeanChannel().addValueChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("Selection changed: " + evt + " " + evt.getPropertyName());
                ColorModel colorModel = model.getBean();
                if (colorModel != null) {
                    colorPreview.setBackground(colorModel.getColor());
                }
            }
        });
    }

    private void createComponentLabel(GridBagConstraints c, String name) {
        c.insets = DEF_INSETS;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        JLabel aCompLabel = new JLabel();
        aCompLabel.setName(name + "Label");
        add(aCompLabel, c);
    }

    private void addComponentTextField(GridBagConstraints c, String name, JTextField compText) {
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        compText.setName(name);
        add(compText, c);
        ValidationComponentUtils.setMandatory(compText, true);
    }

    private void addComponentLabelValuePair(GridBagConstraints c, String name, ValueType type) {
        createComponentLabel(c, name);

        ValueModel m = model.getModel(name);
        if (type == ValueType.FLOAT) {
            m = FloatToDoubleConverter.createFloatToDoubleConverter(m);
        }
        JTextField compText = BasicComponentFactory.createFormattedTextField(
                m, type.getFormat());

        addComponentTextField(c, name, compText);
    }

    private void addXYZComponentLabelValuePair(GridBagConstraints c, String name, int component) {
        createComponentLabel(c, name);

        ValueModel m = model.getModel(PROPERTY_COLOR);
        m = ColorToXYZConverter.createConverter(m, component);
        JTextField compText = BasicComponentFactory.createFormattedTextField(
                m, FLOAT_FORMAT);

        addComponentTextField(c, name, compText);
    }

    private void addRGBComponentLabelValuePair(GridBagConstraints c, String name, int component) {
        createComponentLabel(c, name);

        ValueModel m = model.getModel(PROPERTY_COLOR);
        m = ColorToRGBConverter.createConverter(m, component);
        JTextField compText = BasicComponentFactory.createFormattedTextField(
                m, INT_FORMAT);

        addComponentTextField(c, name, compText);
    }

    private void initGridBagConstraints(GridBagConstraints c) {
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = ZERO_INSETS;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
    }

    @Override
    public void requestFocus() {
        Component first = getComponent(0);
        if (first instanceof JLabel) {
            first = ((JLabel)first).getLabelFor();
        }
        first.requestFocusInWindow();
    }
}
