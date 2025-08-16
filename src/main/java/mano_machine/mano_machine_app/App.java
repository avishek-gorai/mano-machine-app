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

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * I represent the Mano Machine application.
 * 
 * @author Avishek Gorai
 */
class App 
extends JFrame {
    private static final long serialVersionUID = -5996279147708093557L;
    private static final int appFrameHeight = 300, appFrameWidth = 300;
    private Processor computer;
    
    App() {
        super("Mano Machine");
        setComputer(new Processor());
        setJMenuBar(new AppMenuBar());
        add(getComputer(), BorderLayout.CENTER);
        setSize(App.getAppframewidth(), App.getAppframeheight());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
    
    private static int getAppframeheight() {
        return appFrameHeight;
    }

    private static int getAppframewidth() {
        return appFrameWidth;
    }

    private Processor getComputer() {
        return computer;
    }

    private App setComputer(Processor computer) {
        this.computer = computer;
        
        return this;
    }
}
