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
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * 
 * @author Avishek Gorai
 */
public class App 
extends JFrame {
    private static final long serialVersionUID = -5996279147708093557L;
    private Terminal terminalPanel;
    private Processor registerPanel;
    private Memory memoryPanel;
    private Container internals;
    
    App() {
        super("Mano Machine");
        setTerminalPanel(new Terminal());
        setMemoryPanel(new Memory());
        setRegisterPanel(new Processor(getMemoryPanel()));
        getRegisterPanel().setAccumulator(34);
        setInternals(new Container());
        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
    
    Container getInternals() {
        return internals;
    }

    App setInternals(Container internals) {
        internals.setLayout(new GridLayout(0, 1));
        internals.add(getRegisterPanel());
        internals.add(getMemoryPanel());
        add(internals, BorderLayout.EAST);
        this.internals = internals;
        return this;
    }

    Terminal getTerminalPanel() {
        return terminalPanel;
    }

    App setTerminalPanel(Terminal terminalPanel) {
        add(terminalPanel, BorderLayout.CENTER);
        this.terminalPanel = terminalPanel;
        return this;
    }

    Processor getRegisterPanel() {
        return registerPanel;
    }

    App setRegisterPanel(Processor registerPanel) {
        this.registerPanel = registerPanel;
        return this;
    }

    Memory getMemoryPanel() {
        return memoryPanel;
    }

    App setMemoryPanel(Memory memoryPanel) {
        this.memoryPanel = memoryPanel;
        return this;
    }
}
