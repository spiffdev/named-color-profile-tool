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

/* $Id: WhitePoint.java 1076 2010-07-10 05:22:18Z jeremias $ */

package ch.jm.color.ncp.gui.util;

import org.apache.xmlgraphics.java2d.color.CIELabColorSpace;

public enum WhitePoint {

    D50(CIELabColorSpace.getD50WhitePoint()), D65(CIELabColorSpace.getD65WhitePoint());

    private float[] xyz = new float[3];

    private WhitePoint(float[] xyz) {
        System.arraycopy(xyz, 0, this.xyz, 0, 3);
    }

    public float[] getXYZ() {
        float[] copy = new float[3];
        System.arraycopy(this.xyz, 0, copy, 0, 3);
        return copy;
    }

}
