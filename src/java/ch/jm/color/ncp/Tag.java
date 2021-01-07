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

/* $Id: Tag.java 1076 2010-07-10 05:22:18Z jeremias $ */

package ch.jm.color.ncp;

class Tag {

    private int signature;
    private byte[] data;

    public Tag(int signature, byte[] data) {
        this.signature = signature;
        this.data = data;
    }

    public int getSignature() {
        return this.signature;
    }

    public String getSignatureAsString() {
        StringBuffer sb = new StringBuffer();
        sb.append((char)(this.signature >> 24 & 0xFF));
        sb.append((char)(this.signature >> 16 & 0xFF));
        sb.append((char)(this.signature >> 8 & 0xFF));
        sb.append((char)(this.signature & 0xFF));
        return sb.toString();
    }

    public byte[] getData() {
        return this.data;
    }

    public int getDataLength() {
        return this.data.length;
    }

    public int getPaddingLength() {
        int len = getDataLength();
        int pad = 4 - (len % 4);
        if (pad < 4) {
            return pad;
        } else {
            return 0;
        }
    }

    /** {@inheritDoc} */
    public String toString() {
        return "Tag " + getSignatureAsString() + ": " + getDataLength() + " bytes";
    }
}
