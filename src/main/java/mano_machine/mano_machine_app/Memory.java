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

import javax.swing.JPanel;

/**
 * I represnt memory of Mano Machine.
 * 
 * @author Avishek Gorai
 */
class Memory 
extends JPanel {
    private static final long serialVersionUID = 7343726309379579355L;
    private int storage[];
    
    Memory(int size) {
        super();
        setStorage(new int [size]);
        setVisible(true);
    }
    
    int read(int address) {
        return getStorage()[address];
    }
    
    Memory write(int address, int value) {
        getStorage()[address] = value;
        return this;
    }

    int[] getStorage() {
        return storage;
    }

    Memory setStorage(int[] ram) {
        this.storage = ram;
        return this;
    }
}
