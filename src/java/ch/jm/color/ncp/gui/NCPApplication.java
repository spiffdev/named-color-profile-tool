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

/* $Id: NCPApplication.java 1219 2011-01-20 08:08:36Z jeremias $ */

package ch.jm.color.ncp.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;

import ch.jm.color.ncp.gui.model.ProfileModel;
import ch.jm.color.ncp.gui.model.ProfilePresentationModel;
import ch.jm.color.ncp.gui.util.Utils;

public class NCPApplication extends SingleFrameApplication {

    private ProfileModel profileModel;
    private ProfilePresentationModel profilePresentationModel;
    private ProfileView profileView;

    @Override
    protected void startup() {
        /*
        JLabel label = new JLabel();
        label.setName("label");
        show(label);
        */
        getMainFrame().setJMenuBar(createMenuBar());
        show(createMainPanel());
    }

    private JComponent createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        profileModel = new ProfileModel();
        profilePresentationModel = new ProfilePresentationModel(profileModel);
        profilePresentationModel.addBeanPropertyChangeListener(
                Utils.createDebugPropertyChangeListener());
        profileView = new ProfileView(profilePresentationModel);

        /*
        colorModel = new ColorModel();
        // Report changes in all bound Album properties.
        ColorPresentationModel colorPresentationModel = new ColorPresentationModel(colorModel);
        colorPresentationModel.addBeanPropertyChangeListener(
                Utils.createDebugPropertyChangeListener());
        colorView = new ColorView(colorPresentationModel);
        */

        //panel.add(profileView, BorderLayout.NORTH);
        //panel.add(colorView, BorderLayout.CENTER);
        panel.add(profileView, BorderLayout.CENTER);
        panel.add(createToolBar(), BorderLayout.NORTH);
        panel.setBorder(new EmptyBorder(0, 2, 2, 2)); // top, left, bottom, right
        return panel;
    }

    private javax.swing.Action getAction(String actionName) {
        return getContext().getActionMap().get(actionName);
    }

    private JMenu createMenu(String menuName, String[] actionNames) {
        JMenu menu = new JMenu();
        menu.setName(menuName);
        for (String actionName : actionNames) {
            if (actionName.equals("---")) {
                menu.add(new JSeparator());
            } else {
                JMenuItem menuItem = new JMenuItem();
                menuItem.setAction(getAction(actionName));
                menuItem.setIcon(null);
                menu.add(menuItem);
            }
        }
        return menu;
    }

    private JMenuBar createMenuBar() {
        String[] fileMenuActionNames = {
                "newColor",
                "deleteColor",
                "---",
                "quit"
        };
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenu("fileMenu", fileMenuActionNames));
        return menuBar;
    }

    private JComponent createToolBar() {
        String[] toolbarActionNames = {
                "newColor",
                "deleteColor",
                "quit"
        };
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        for (String actionName : toolbarActionNames) {
            JButton button = new JButton();
            button.setAction(getAction(actionName));
            button.setFocusable(false);
            toolBar.add(button);
        }
        return toolBar;
    }

    @Action
    public void saveAs() {
        System.out.println("TEST!");
    }

    @Action
    public void newColor() {
        this.profilePresentationModel.newColor();
        this.profileView.getColorView().requestFocus();
    }

    @Action
    public void deleteColor() {
        this.profilePresentationModel.deleteCurrentColor();
    }

    public static void main(String[] args) {
        launch(NCPApplication.class, args);
    }

}
