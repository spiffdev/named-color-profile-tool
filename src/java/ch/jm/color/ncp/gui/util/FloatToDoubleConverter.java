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

/* $Id: FloatToDoubleConverter.java 1076 2010-07-10 05:22:18Z jeremias $ */

package ch.jm.color.ncp.gui.util;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;

/**
 * Converts double values to float values and vice-versa.
 */
public class FloatToDoubleConverter extends AbstractConverter {

    FloatToDoubleConverter(ValueModel floatSubject) {
        super(floatSubject);
    }

    /**
     * Creates and returns a ValueModel that converts Float to Double,
     * and vice versa.
     *
     * <strong>Constraints:</strong> The subject is of type <code>Float</code>.
     *
     * @param floatSubject  a Float ValueModel
     * @return a ValueModel that converts Double to Float
     * @throws NullPointerException if the subject is {@code null}
     */
    public static ValueModel createFloatToDoubleConverter(
            ValueModel floatSubject) {
        return new FloatToDoubleConverter(floatSubject);
    }

    /**
     * Converts the subject's value and returns a
     * corresponding <code>Double</code>.
     *
     * @param subjectValue the subject's value
     * @return the converted subjectValue
     * @throws ClassCastException if the subject value is not of type
     *     <code>Double</code>
     */
    @Override
    public Object convertFromSubject(Object subjectValue) {
        if (subjectValue == null) {
            return 0;
        }
        return ((Number)subjectValue).doubleValue();
    }

    /**
     * Converts a <code>Double</code>
     * and sets it as new Float value.
     *
     * @param newValue  the <code>Double</code> object that shall be converted
     * @throws ClassCastException if the new value is not of type
     *     <code>Double</code>
     */
    public void setValue(Object newValue) {
        subject.setValue(((Number)newValue).floatValue());
    }

}