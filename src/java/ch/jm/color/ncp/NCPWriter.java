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

/* $Id: NCPWriter.java 6278 2021-01-07 09:30:54Z jeremias $ */

package ch.jm.color.ncp;

import java.awt.color.ICC_Profile;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;

import org.apache.xmlgraphics.java2d.color.NamedColorSpace;
import org.apache.xmlgraphics.java2d.color.profile.NamedColorProfile;

public class NCPWriter {


    public void writeNCP(NamedColorProfile ncp, OutputStream out) throws IOException {
        DataOutputStream dout = new DataOutputStream(out);
        List<Tag> tags = new java.util.ArrayList<Tag>();
        tags.add(createProfileDescriptionTag(ncp));
        tags.add(createCopyrightTag(ncp));
        tags.add(createMediaWhitePointTag(ncp));
        //TODO Add chromaticAdaptationTag when illumination is not D50
        tags.add(createNamedColorTag(ncp));

        int profileSize = calcProfileSize(tags);
        int pad = 4 - (profileSize % 4);
        if (pad == 4) {
            pad = 0;
        }
        profileSize += pad;
        writeHeader(ncp, profileSize, dout);
        writeTags(tags, dout);
        if (pad != 0) {
            dout.write(new byte[pad]);
        }
    }

    private int calcProfileSize(List<Tag> tags) {
        int tagOffset = 128 + 4 + tags.size() * 12; //offset of first tag

        //Go through tag table
        for (Tag tag : tags) {
            tagOffset += tag.getDataLength();
            tagOffset += tag.getPaddingLength();
        }
        return tagOffset; //is now the size of the profile
    }

    private int writeTags(List<Tag> tags, DataOutputStream out) throws IOException {
        out.writeInt(tags.size());
        int tagOffset = 128 + 4 + tags.size() * 12; //offset of first tag

        //Write tag table
        for (Tag tag : tags) {
            out.writeInt(tag.getSignature());
            out.writeInt(tagOffset);
            int len = tag.getDataLength();
            out.writeInt(len);

            tagOffset += len;
            tagOffset += tag.getPaddingLength();
        }

        for (Tag tag : tags) {
            out.write(tag.getData());
            out.write(new byte[tag.getPaddingLength()]);
        }
        return tagOffset; //is now the size of the profile
    }

    private void writeHeader(NamedColorProfile ncp, int profileSize, DataOutputStream out) throws IOException {
        out.writeInt(profileSize); //Profile size
        out.writeInt(0); //no preferred CMM
        out.writeInt(0x04200000); //Version 4.2.0.0
        out.writeInt(ICC_Profile.icSigNamedColorClass); //nmcl
        out.writeInt(ICC_Profile.icSigLabData); //Canonical input space
        out.writeInt(ICC_Profile.icSigXYZData); //Canonical output space
        writeDateTime(new Date(), out);
        out.writeInt(0x61637370);
        out.writeInt(0); //Primary platform: none
        out.writeInt(0); //Profile flags
        out.writeInt(0); //Manufacturer: not used
        out.writeInt(0); //Device model: not used
        boolean transparency = false;
        boolean matte = true;
        boolean polarityNegative = false;
        boolean blackAndWhiteMedia = false;
        long deviceAttributes = 0;
        if (transparency) {
            deviceAttributes |= 0x01;
        }
        if (matte) {
            deviceAttributes |= 0x02;
        }
        if (polarityNegative) {
            deviceAttributes |= 0x04;
        }
        if (blackAndWhiteMedia) {
            deviceAttributes |= 0x08;
        }
        out.writeLong(deviceAttributes);
        out.writeInt(ncp.getRenderingIntent().getIntegerValue());
        writeXYZ(ncp.getWhitePoint(), out); //illuminant
        out.writeInt(0); //Profile Creator field: not used
        out.write(new byte[16]); //TODO NYI: MD5 checksum, serving as ID
        out.write(new byte[28]); //Reserved

    }

    private Tag createProfileDescriptionTag(NamedColorProfile ncp) throws IOException {
        return new Tag(ICC_Profile.icSigProfileDescriptionTag,
                createMultiLocalizedUnicodeData(ncp.getProfileName()));
    }

    private Tag createCopyrightTag(NamedColorProfile ncp) throws IOException {
        return new Tag(ICC_Profile.icSigCopyrightTag,
                createMultiLocalizedUnicodeData(ncp.getCopyright()));
    }

    private Tag createMediaWhitePointTag(NamedColorProfile ncp) throws IOException {
        return new Tag(ICC_Profile.icSigMediaWhitePointTag,
                createXYZData(ncp.getWhitePoint()));
    }

    private byte[] createMultiLocalizedUnicodeData(String name) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bout);
        out.writeInt(0x6D6C7563); //'mluc'
        out.writeInt(0); //Reserved
        out.writeInt(1); //Only 1 name
        out.writeInt(12); //record size
        out.write("en".getBytes("US-ASCII"));
        out.write("US".getBytes("US-ASCII"));
        byte[] encoded = name.getBytes("UTF-16BE");
        out.writeInt(encoded.length);
        out.writeInt(28); //First offset
        out.write(encoded);
        return bout.toByteArray();
    }

    private byte[] createXYZData(float[] xyz) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bout);
        out.writeInt(0x58595A20); //'XYZ '
        out.writeInt(0); //Reserved
        writeXYZ(xyz, out);
        return bout.toByteArray();
    }

    private Tag createNamedColorTag(NamedColorProfile ncp) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bout);
        out.writeInt(0x6E636C32); //'ncl2'
        out.writeInt(0); //Reserved
        out.writeInt(0); //Flags, not used
        NamedColorSpace[] namedColors = ncp.getNamedColors();
        out.writeInt(namedColors.length);
        out.writeInt(0); //No device coordinates (maybe to be implemented later)
        out.write(new byte[32]); //Prefix for all colors
        out.write(new byte[32]); //Suffix for all colors
        for (int i = 0, c = namedColors.length; i < c; i++) {
            NamedColorSpace ncs = namedColors[i];
            String name = ncs.getColorName();
            if (name.length() > 31) {
                name = name.substring(0, 31);
            }
            byte[] encoded = name.getBytes("US-ASCII");
            int pad = 32 - name.length();
            out.write(encoded);
            out.write(new byte[pad]);
            float[] xyz = ncs.getXYZ();
            for (int j = 0; j < 3; j++) {
                int v = Math.round(xyz[j] * 0x8000);
                out.writeShort(v);
            }

        }
        return new Tag(ICC_Profile.icSigNamedColor2Tag, bout.toByteArray());
    }

    private void writeDateTime(Date time, DataOutputStream out) throws IOException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        out.writeShort(cal.get(Calendar.YEAR));
        out.writeShort(cal.get(Calendar.MONTH) + 1);
        out.writeShort(cal.get(Calendar.DAY_OF_MONTH));
        out.writeShort(cal.get(Calendar.HOUR_OF_DAY));
        out.writeShort(cal.get(Calendar.MINUTE));
        out.writeShort(cal.get(Calendar.SECOND));
    }

    private void writeXYZ(float[] xyz, DataOutputStream out) throws IOException {
        for (int i = 0; i < 3; i++) {
            out.writeInt(toS15Fixed16Number(xyz[i] / 100f)); //Scale to 0.0..1.0
        }
    }

    private int toS15Fixed16Number(float value) {
        float abs = Math.abs(value);
        int result = (((int)abs) & 0xFFFF) << 16;
        float fraction = value - (float)Math.floor(abs);
        int fracInt = Math.round(fraction * 0x10000);
        result |= fracInt & 0xFFFF;
        if (value < 0) {
            result |= 0x01 << 31;
        }
        return result;
    }

}
