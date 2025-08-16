/* Copyright (C) 2025 Avishek Gorai
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mano_machine.mano_machine_app;

import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * I represent this application's menu bar.
 * 
 * @author Avishek Gorai
 */
class AppMenuBar 
extends JMenuBar {
    private static final long serialVersionUID = -1214168726493840604L;
    private JMenu fileMenu, appHelpMenu; 

    AppMenuBar() {
        setFileMenu(new JMenu("File"));
        setAppHelpMenu(new JMenu("Help"));
        add(getFileMenu());
        add(getAppHelpMenu());
    }

    private JMenu getAppHelpMenu() {
        return appHelpMenu;
    }

    private AppMenuBar setAppHelpMenu(JMenu appHelpMenu) {
        appHelpMenu.setMnemonic(KeyEvent.VK_H);
        appHelpMenu.add(new AboutMenuItem());
        appHelpMenu.add(new UserManualMenuItem());
       
        this.appHelpMenu = appHelpMenu;
        
        return this;
    }

    private JMenu getFileMenu() {
        return fileMenu;
    }

    private AppMenuBar setFileMenu(JMenu fileMenu) {
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(new LoadMenuItem());
        fileMenu.add(new SaveMenuItem());
        fileMenu.add("Exit").addActionListener((action) -> System.exit(ABORT));
        
        this.fileMenu = fileMenu;
        
        return this;
    }
}
