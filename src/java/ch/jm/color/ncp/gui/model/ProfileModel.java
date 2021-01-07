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

/* $Id: ProfileModel.java 1219 2011-01-20 08:08:36Z jeremias $ */

package ch.jm.color.ncp.gui.model;

import java.util.List;

import ch.jm.color.ncp.gui.util.WhitePoint;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.ObservableList;

public class ProfileModel extends Model implements ProfilePropertyConstants {

    private String profileName;
    private String profileCopyright;
    private WhitePoint whitePoint = WhitePoint.D65;

    private ObservableList<ColorModel> colors = new ArrayListModel<ColorModel>();

    public ProfileModel() {
        ColorModel cm;
        cm = newColor();
        cm = newColor();
        cm.setColorName("Test");
    }

    public void setProfileName(String value) {
        String old = this.profileName;
        this.profileName = value;
        firePropertyChange(PROPERTY_PROFILE_NAME, old, value);
    }

    public String getProfileName() {
        return this.profileName;
    }

    public void setProfileCopyright(String value) {
        String old = this.profileCopyright;
        this.profileCopyright = value;
        firePropertyChange(PROPERTY_PROFILE_COPYRIGHT, old, value);
    }

    public String getProfileCopyright() {
        return this.profileCopyright;
    }

    public void setWhitePoint(WhitePoint value) {
        WhitePoint old = this.whitePoint;
        this.whitePoint = value;
        firePropertyChange(PROPERTY_WHITEPOINT, old, value);
    }

    public WhitePoint getWhitePoint() {
        return this.whitePoint;
    }

    public List<ColorModel> getColors() {
        return this.colors;
    }

    public ColorModel newColor() {
        ColorModel colorModel = new ColorModel();
        this.colors.add(colorModel);
        firePropertyChange(PROPERTY_COLORS, null, null);
        return colorModel;
    }

    public void delete(ColorModel colorModel) {
        boolean removed = this.colors.remove(colorModel);
        if (!removed) {
            System.out.println("color not found for removal");
        }
        firePropertyChange(PROPERTY_COLORS, null, null);

    }

}
