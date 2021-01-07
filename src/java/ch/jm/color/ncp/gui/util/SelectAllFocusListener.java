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

/* $Id: SelectAllFocusListener.java 1219 2011-01-20 08:08:36Z jeremias $ */

package ch.jm.color.ncp.gui.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class SelectAllFocusListener extends FocusAdapter {

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getComponent() instanceof JTextComponent) {
            final JTextComponent tc = (JTextComponent)e.getComponent();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    tc.selectAll();
                }

            });
        } else {
            throw new UnsupportedOperationException(
                    "selectAll() on focusGained() only works for JTextComponent subclasses");
        }
    }

    public static void decorate(Container container) {
        final FocusListener selectAllListener = new SelectAllFocusListener();
        container.addContainerListener(new ContainerAdapter() {

            @Override
            public void componentAdded(ContainerEvent e) {
                Component c = e.getChild();
                if (c instanceof JTextComponent) {
                    c.addFocusListener(selectAllListener);
                }

            }
        });
    }

}