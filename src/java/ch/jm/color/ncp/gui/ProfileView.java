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

/* $Id: ProfileView.java 1219 2011-01-20 08:08:36Z jeremias $ */

package ch.jm.color.ncp.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

import ch.jm.color.ncp.gui.model.ColorModel;
import ch.jm.color.ncp.gui.model.ProfilePresentationModel;
import ch.jm.color.ncp.gui.model.ProfilePropertyConstants;
import ch.jm.color.ncp.gui.util.SelectAllFocusListener;
import ch.jm.color.ncp.gui.util.Utils;
import ch.jm.color.ncp.gui.util.WhitePoint;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;

public class ProfileView extends JPanel implements ProfilePropertyConstants {

    private static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);
    private static final Insets DEF_INSETS = new Insets(3, 6, 3, 6); // top, left, bottom, right;

    private ProfilePresentationModel model;

    private ColorView colorView;

    public ProfileView(ProfilePresentationModel model) {
        super(new GridBagLayout());
        this.model = model;
        initComponents();
        ResourceMap resource = getContext().getResourceMap(ProfileView.class);
        resource.injectComponents(this);

        model.getValidationResultModel().addPropertyChangeListener(
                ValidationResultModel.PROPERTYNAME_RESULT,
                new ValidationChangeHandler());
        updateComponentTreeMandatoryAndSeverity(
                model.getValidationResultModel().getResult()
        );
    }

    public ColorView getColorView() {
        return this.colorView;
    }

    private ApplicationContext getContext() {
        return Application.getInstance().getContext();
    }

    private void initComponents() {
        SelectAllFocusListener.decorate(this);

        GridBagConstraints c = new GridBagConstraints();

        initGridBagConstraints(c);
        JComponent comp = addStringTextField(c, PROPERTY_PROFILE_NAME);
        ValidationComponentUtils.setMandatory(comp, true);
        ValidationComponentUtils.setMessageKey(comp, MODEL_NAME + "." + PROPERTY_PROFILE_NAME);


        c.gridx = 0;
        c.gridy = 1;
        addStringTextField(c, PROPERTY_PROFILE_COPYRIGHT);

        c.gridx = 0;
        c.gridy = 2;

        c.weightx = 0.0;
        c.anchor = GridBagConstraints.LINE_START; //BASELINE_TRAILING
        c.fill = GridBagConstraints.NONE;
        JLabel whitePointLabel = new JLabel();
        whitePointLabel.setName(PROPERTY_WHITEPOINT + "Label");
        add(whitePointLabel, c);

        c.gridx = GridBagConstraints.RELATIVE;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        ValueModel whitePointModel = new PropertyAdapter(model, PROPERTY_WHITEPOINT, true);
        ComboBoxAdapter<WhitePoint> adapter = new ComboBoxAdapter<WhitePoint>(
                WhitePoint.values(), whitePointModel);
        JComboBox whitePointBox = new JComboBox(adapter);
        whitePointBox.setName(PROPERTY_WHITEPOINT);
        add(whitePointBox, c);
        whitePointLabel.setLabelFor(whitePointBox);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        add(new JSeparator(), c);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        JSplitPane detailPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        detailPanel.setName("detailPanel");
        detailPanel.setBackground(Color.ORANGE);
        add(detailPanel, c);

        SelectionInList<ColorModel> colorSelection = model.getColorSelection();
        JList colorList = BasicComponentFactory.createList(
                colorSelection,
                new ColorListCellRenderer());
        colorList.setPreferredSize(new Dimension(120, 0));
        detailPanel.add(colorList, JSplitPane.LEFT);
        colorSelection.setSelectionIndex(0);

        PresentationModel<ColorModel> colorModel = new PresentationModel<ColorModel>(colorSelection);
        colorModel.addBeanPropertyChangeListener(
                Utils.createDebugPropertyChangeListener());

        this.colorView = new ColorView(colorModel);
        detailPanel.add(colorView, JSplitPane.RIGHT);

        detailPanel.resetToPreferredSizes();
        colorList.requestFocusInWindow();

        //ValidationComponentUtils.updateComponentTreeMandatoryBackground(this);
    }

    private void updateComponentTreeMandatoryAndSeverity(ValidationResult result) {
        ValidationComponentUtils.updateComponentTreeSeverityBackground(
                this, result);
    }

    private final class ValidationChangeHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println("Property Change Event: " + evt.getPropertyName());
            ValidationResult result = (ValidationResult)evt.getNewValue();
            updateComponentTreeMandatoryAndSeverity(result);
        }
    }

    /**
     * Used to renders ColorModels in JLists and JComboBoxes. If the combo box
     * selection is null, an empty text <code>""</code> is rendered.
     */
    private static final class ColorListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);

            ColorModel colorModel = (ColorModel)value;
            setText(colorModel == null ? "" : (" " + colorModel.getColorName()));
            return component;
        }
    }


    private JComponent addStringTextField(GridBagConstraints c, String propName) {
        c.weightx = 0.0;
        c.anchor = GridBagConstraints.LINE_START; //BASELINE_TRAILING
        c.fill = GridBagConstraints.NONE;
        JLabel textFieldLabel = new JLabel();
        textFieldLabel.setName(propName + "Label");
        add(textFieldLabel, c);

        c.gridx = GridBagConstraints.RELATIVE;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        JTextField textField = BasicComponentFactory.createTextField(
                model.getModel(propName), false);
        textField.setName(propName);
        add(textField, c);

        textFieldLabel.setLabelFor(textField);

        return textField;
    }

    private void initGridBagConstraints(GridBagConstraints c) {
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = DEF_INSETS;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
    }


}
