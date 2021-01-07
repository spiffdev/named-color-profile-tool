/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id: NCPGenerator.java 6278 2021-01-07 09:30:54Z jeremias $ */

package ch.jm.color.ncp;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.HexDump;
import org.apache.commons.io.IOUtils;

import org.apache.xmlgraphics.java2d.color.CIELabColorSpace;
import org.apache.xmlgraphics.java2d.color.ColorSpaces;
import org.apache.xmlgraphics.java2d.color.NamedColorSpace;
import org.apache.xmlgraphics.java2d.color.RenderingIntent;
import org.apache.xmlgraphics.java2d.color.profile.NamedColorProfile;
import org.apache.xmlgraphics.java2d.color.profile.NamedColorProfileParser;

public class NCPGenerator {

    public void parse(File iccFile) throws IOException {
        InputStream in = new java.io.FileInputStream(iccFile);
        try {
            ICC_Profile profile = ICC_Profile.getInstance(in);
            System.out.println(profile.toString());
            System.out.println(profile.getColorSpaceType());
            System.out.println(profile.getMajorVersion() + "." + profile.getMinorVersion());
            System.out.println("num comps: " + profile.getNumComponents());
            System.out.println("PCS type: " + profile.getPCSType());
            System.out.println("Profile class: " + profile.getProfileClass());
            if (profile.getProfileClass() != ICC_Profile.CLASS_NAMEDCOLOR) {
                throw new IllegalArgumentException("Not a named color profile!");
            }

            byte[] desc = profile.getData(ICC_Profile.icSigProfileDescriptionTag);
            HexDump.dump(desc, 0, System.out, 0);

            byte[] nc = profile.getData(ICC_Profile.icSigNamedColor2Tag);
            HexDump.dump(nc, 0, System.out, 0);

            NamedColorProfileParser parser = new NamedColorProfileParser();
            NamedColorProfile ncp = parser.parseProfile(profile);
            System.out.println(ncp.getProfileName());
            System.out.println(ncp.getCopyright());
            System.out.println(ncp);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private void dump(NamedColorProfile ncp) {
        System.out.println(ncp.getProfileName());
        System.out.println(ncp.getCopyright());
        for (NamedColorSpace ncs : ncp.getNamedColors()) {
            System.out.println(ncs.getColorName() + ": " + ncs.getRGBColor() + " " + getWebRGB(ncs));
        }

    }

    private String getWebRGB(NamedColorSpace ncs) {
        String s = Integer.toHexString(ncs.getRGBColor().getRGB());
        System.out.println(s);
        if (s.length() > 6) {
            s = s.substring(s.length() - 6);
        }
        if (s.length() < 6) {
            s = "000000".substring(s.length()) + s;
        }
        return "#" + s;
    }

    private NamedColorProfile createDemoProfile() {
        CIELabColorSpace lab = ColorSpaces.getCIELabColorSpaceD50();
        NamedColorSpace[] namedColors = new NamedColorSpace[] {
                new NamedColorSpace("Postgelb", lab.toColor(83.25f, 16.45f, 96.89f, 1.0f)),
                //new NamedColorSpace("2718C", lab.toColor(53, 7, -52, 1.0f))
                new NamedColorSpace("MyRed", new Color(255, 0, 0))
                };
        NamedColorProfile ncp = new NamedColorProfile("Named Color Profile Example",
                "The Apache Software Foundation", namedColors, RenderingIntent.AUTO);
        return ncp;
    }

    private NamedColorProfile createWODProfile() {
        //D65 for the PANTONE colors!!!!!!!!!!!!!
        float[] whitepoint = CIELabColorSpace.getD65WhitePoint();

        ColorSpace cmyk = ColorSpaces.getDeviceCMYKColorSpace();
        CIELabColorSpace lab = new CIELabColorSpace(whitepoint);
        Color dunkelblau = new Color(cmyk, new float[] {1f, 0.49f, 0f, 0.7f}, 0f);
        dunkelblau = lab.toColor(34.96f, -0.13f, -15.16f, 1f);
        Color logoblau = new Color(cmyk, new float[] {1f, 0.67f, 0f, 0.23f}, 0f);
        logoblau = lab.toColor(33.61f, 5.43f, -31.52f, 1f);
        Color hellblau = new Color(cmyk, new float[] {0.17f, 0.06f, 0f, 0f}, 0f);
        hellblau = lab.toColor(84.26f, -0.23f, -19.89f, 1f);

        Color schwarz = new Color(cmyk, new float[] {0f, 0f, 0f, 1f}, 0f);
        schwarz = lab.toColor(35.7f, 1.71f, 2.46f, 1f);
        Color dunkelgrau = new Color(cmyk, new float[] {0f, 0f, 0f, 0.8f}, 0f);
        dunkelgrau = lab.toColor(44.86f, 0.66f, -4.49f, 1f);
        Color hellgrau = new Color(cmyk, new float[] {0f, 0f, 0f, 0.4f}, 0f);
        hellgrau = lab.toColor(59.34f, 0.8f, -2.93f, 1f);

        NamedColorSpace[] namedColors = new NamedColorSpace[] {
                new NamedColorSpace("PANTONE 539 U", dunkelblau),
                new NamedColorSpace("PANTONE 288 U", logoblau),
                new NamedColorSpace("PANTONE 2707 U", hellblau),
                new NamedColorSpace("PANTONE Black U", schwarz),
                new NamedColorSpace("PANTONE 7540 U", dunkelgrau),
                new NamedColorSpace("PANTONE Cool Gray 8 U", hellgrau),
                };
        NamedColorProfile ncp = new NamedColorProfile("World Of Documents Color Scheme",
                "Jeremias Maerki", namedColors, RenderingIntent.ABSOLUTE_COLORIMETRIC,
                whitepoint); //Whitepoint requires change in XGC against 1.5!!!
        return ncp;
    }

    public void generateNCP(File iccFile) throws IOException {
        OutputStream out = new java.io.FileOutputStream(iccFile);
        try {
            NamedColorProfile ncp;
            ncp = createDemoProfile();
            //ncp = createWODProfile();
            dump(ncp);

            NCPWriter writer = new NCPWriter();
            writer.writeNCP(ncp, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static void main(String[] args) {
        System.out.println("Apache XML Graphics Commons - Generator for ICC named color profiles");
        System.out.println();
        try {
            NCPGenerator gen = new NCPGenerator();
            File iccFile = new File(args[0]);
            gen.generateNCP(iccFile);
            gen.parse(iccFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

}
