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

/* $Id: ProfilePresentationModel.java 1219 2011-01-20 08:08:36Z jeremias $ */

package ch.jm.color.ncp.gui.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import ch.jm.color.ncp.gui.util.WhitePoint;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;

public class ProfilePresentationModel extends PresentationModel<ProfileModel>
            implements ProfilePropertyConstants {

    private SelectionInList<ColorModel> colorSelection = new SelectionInList<ColorModel>(
            getBean().getColors());

    private final ValidationResultModel validationResultModel;

    public ProfilePresentationModel(ProfileModel model) {
        super(new ValueHolder(model, true));
        validationResultModel = new DefaultValidationResultModel();
        updateValidationResult();
        initEventHandling();
    }

    private void initEventHandling() {
        PropertyChangeListener handler = new ValidationUpdateHandler();
        addBeanPropertyChangeListener(handler);
        getBeanChannel().addValueChangeListener(handler);
    }

    // Validation
    public ValidationResultModel getValidationResultModel() {
        return validationResultModel;
    }

    private void updateValidationResult() {
        System.out.println("updateValidationResult()");
        ProfileModel model = getBean();
        ValidationResult result = new ProfileValidator().validate(model);
        validationResultModel.setResult(result);
    }

    // Accessors

    public WhitePoint getWhitePoint() {
        return (WhitePoint)getModel(ProfilePropertyConstants.PROPERTY_WHITEPOINT).getValue();
    }

    public void setWhitePoint(WhitePoint value) {
        getModel(ProfilePropertyConstants.PROPERTY_WHITEPOINT).setValue(value);
    }

    public SelectionInList<ColorModel> getColorSelection() {
        return this.colorSelection;
    }

    public void newColor() {
        ColorModel colorModel = getBean().newColor();
        getColorSelection().setSelection(colorModel);
    }

    public void deleteCurrentColor() {
        ColorModel colorModel = getColorSelection().getSelection();
        getBean().delete(colorModel);

    }

    private final class ValidationUpdateHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            updateValidationResult();
        }

    }

}
