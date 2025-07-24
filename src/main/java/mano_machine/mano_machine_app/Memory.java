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

class Memory 
extends JPanel {
    private static final long serialVersionUID = 7343726309379579355L;
    private static final int maximumSize = 4096;
    private int ram[];
    
    Memory() {
        setRam(new int [Memory.getMaximumsize()]);
    }
    
    int read(int address) {
        return getRam()[address];
    }
    
    Memory write(int address, int value) {
        getRam()[address] = value;
        return this;
    }

    int[] getRam() {
        return ram;
    }

    Memory setRam(int[] ram) {
        this.ram = ram;
        return this;
    }

    static int getMaximumsize() {
        return maximumSize;
    }
}
