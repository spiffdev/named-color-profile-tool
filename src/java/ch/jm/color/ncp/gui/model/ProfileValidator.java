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

/* $Id: ProfileValidator.java 1219 2011-01-20 08:08:36Z jeremias $ */

package ch.jm.color.ncp.gui.model;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;

public class ProfileValidator implements Validator<ProfileModel>, ProfilePropertyConstants {

    public ValidationResult validate(ProfileModel model) {
        System.out.println("Validating...");
        PropertyValidationSupport support =
            new PropertyValidationSupport(model, MODEL_NAME);

        if (ValidationUtils.isBlank(model.getProfileName())) {
            support.addError(PROPERTY_PROFILE_NAME, "is mandatory");
        }

        System.out.println("Validation results: " +support.getResult().size());
        return support.getResult();
    }

}
