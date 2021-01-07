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

/* $Id: Utils.java 1076 2010-07-10 05:22:18Z jeremias $ */

package ch.jm.color.ncp.gui.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.List;

public class Utils {

    /**
     * Returns a listener that writes bean property changes to the console.
     * The log entry includes the PropertyChangeEvent's source, property name,
     * old value, and new value.
     *
     * @return a debug listener that logs bean changes to the console
     */
    public static PropertyChangeListener createDebugPropertyChangeListener() {
        PropertyChangeListener listener = new DebugPropertyChangeListener();
        debugListeners.add(listener);
        return listener;
    }


    // Debug Listener *********************************************************

    /**
     * Used to hold debug listeners, so they won't be removed by
     * the garbage collector, even if registered by a listener list
     * that is based on weak references.
     *
     * @see #createDebugPropertyChangeListener()
     * @see WeakReference
     */
    private static List<PropertyChangeListener> debugListeners =
        new java.util.LinkedList<PropertyChangeListener>();


    /**
     * Writes the source, property name, old/new value to the system console.
     */
    private static final class DebugPropertyChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println();
            System.out.println("The source: " + evt.getSource());
            System.out.println(
                    "changed '" + evt.getPropertyName()
                  + "' from '" + evt.getOldValue()
                  + "' to '" + evt.getNewValue() + "'.");
        }
    }

}
