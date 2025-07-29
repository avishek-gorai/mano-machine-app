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

import java.awt.GridBagLayout;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * I am the precessor of Mano Machine.
 * 
 * @author Avishek Gorai
 */
class Processor
extends JPanel {
    private static final long serialVersionUID = 7926125363354810277L;  
    private static final int maximumMemorySize = 4096;
    private int sequenceCounter, dataRegister, addressRegister, accumulator, instructionRegister, programCounter, temporaryRegister, inputRegister, outputRegister;
    private boolean indirectAddressBit, stopFlag, extendedAccumulatorBit, interruptCycleFlag, interruptEnabledFlag, inputFlag, outputFlag;
    private Memory memory;
    private JLabel accumulatorLabel;
    private Terminal terminal;

    Processor() {
        setMemory(new Memory(Processor.getMaximummemorysize()));
        setTerminal(new Terminal());
        setAccumulatorLabel(new JLabel());
        setLayout(new GridBagLayout());
        setRandomValues();
        add(new JScrollPane(getTerminal()), new ComputerLayoutConstraint().setGridX(0).setGridY(0).setGridHeight(2));
        add(getMemory(), new ComputerLayoutConstraint().setGridX(1).setGridY(1).setGridWidth(2));
        add(new JLabel("AC"), new ComputerLayoutConstraint().setGridX(1).setGridY(0));
        add(getAccumulatorLabel(), new ComputerLayoutConstraint().setGridX(2).setGridY(0));
        setVisible(true);
    }
    
    Processor setRandomValues() {
        setAccumulator(new Random().nextInt(0xFFFF));
        return this;
    }

    int getSequenceCounter() {
        return sequenceCounter;
    }

    Processor setSequenceCounter(int sequenceCounter) {
        this.sequenceCounter = sequenceCounter;
        return this;
    }

    Terminal getTerminal() {
        return terminal;
    }

    Processor setTerminal(Terminal terminal) {
        this.terminal = terminal;
        return this;
    }

    static int getMaximummemorysize() {
        return maximumMemorySize;
    }

    Processor run() {
        final int AND = 0x0,
                  ADD = 0x1,
                  LDA = 0x2,
                  STA = 0x3,
                  BUN = 0x4,
                  BSA = 0x5,
                ISZ = 0x6,
                REG_IO = 0x7;

        while (!isStop()) {
            setAddressRegister(getProgramCounter());
            setInstructionRegister(getMemory().read(getAddressRegister()));
            incrementProgramCounter();
            setAddressRegister(getInstructionRegister() % 0x1000);
            clearIndirectAddressBit();
            if (getInstructionRegister() > 0x7fff) {
                setIndirectAddressBit();
                setAddressRegister(getMemory().read(getAddressRegister()));
            }
            
            var opcode = getInstructionRegister() / 0x1000;
            
            switch (opcode) {
                case AND -> {
                    setDataRegister(readMemory());
                    setAccumulator(getAccumulator() & getDataRegister());   
                    setSequenceCounter(0);
                }
                
                case ADD -> {
                    setDataRegister(readMemory());
                    setAccumulator(getAccumulator() + getDataRegister());
                    setSequenceCounter(0);
                }
                
                case LDA -> {
                    setDataRegister(readMemory());
                    setAccumulator(getDataRegister());
                    setSequenceCounter(0);
                }
                
                case STA -> {
                    writeMemory(getAccumulator());
                    setSequenceCounter(0);
                }
                
                case BUN -> {
                    setProgramCounter(getAddressRegister());
                    setSequenceCounter(0);
                }
                
                case BSA -> {
                    writeMemory(getProgramCounter());
                    incrementAddressRegister();
                    setProgramCounter(getAddressRegister());
                    setSequenceCounter(0);
                }
                
                case ISZ -> {
                    setDataRegister(readMemory());
                    incrementDataRegister();
                    writeMemory(getDataRegister());
                    if (getDataRegister() == 0) {
                        incrementProgramCounter();
                    }
                    setSequenceCounter(0);
                }
                
                case REG_IO -> {
                    if (isIndirectAddress()) {
                       final int INP = 0x800,
                                OUT = 0x400,
                                SKI = 0x200,
                                SKO = 0x100,
                                ION = 0x80,
                                IOF = 0x40;
                        setSequenceCounter(0);
                        switch (opcode) {
                            case INP -> setAccumulator(getInputRegister());
                            
                            case OUT -> setOutputRegister(getAccumulator() % 0xFF);
                            
                            case SKI -> {
                                if (isInputReady()) {
                                    incrementProgramCounter();
                                }
                            }
                            
                            case SKO -> {
                                if (isOutputReady()) {
                                    incrementProgramCounter();
                                }
                            }
                            
                            case ION -> setInterruptEnabledFlag();
                            
                            case IOF -> clearInterruptEnabledFlag();
                        }
                    }
                    else {
                       final int CLA = 0x800,
                                CLE = 0x400,
                                CMA = 0x200,
                                CME = 0x100,
                                CIR = 0x80,
                                CIL = 0x40,
                                INC = 0x20,
                                SPA = 0x10,
                                SNA = 0x8,
                                SZA = 0x4,
                                SZE = 0x2,
                                HLT = 0x1;
                        setSequenceCounter(0);
                        switch (opcode) {
                            case CLA -> setAccumulator(0);
                            
                            case CLE -> clearExtendedAccumulatorBit();
                            
                            case CMA -> setAccumulator(~getAccumulator());
                            
                            case CME -> complementExtendedAccumulatorBit();
                            
                            case CIR -> {
                                
                            }
                            
                            case CIL -> {
                                
                            }
                            
                            case INC -> setAccumulator(getAccumulator() + 1);
                            
                            case SPA -> {
                                if (getAccumulator() > 0) {
                                    incrementProgramCounter();
                                }
                            }
                            
                            case SNA -> {
                                if (getAccumulator() < 0) {
                                    incrementProgramCounter();
                                }
                            }
                            
                            case SZA -> {
                                if (getAccumulator() == 0) {
                                    incrementProgramCounter();
                                }
                            }
                            
                            case SZE -> {
                                if (getExtendedAccumulatorBit() == false) {
                                    incrementProgramCounter();
                                }
                            }
                            
                            case HLT -> setStopFlag();
                        }
                    }
                }
                
            }
        }
        
        return this;
    }

    Processor complementExtendedAccumulatorBit() {
        if (getExtendedAccumulatorBit() == true) {
            clearExtendedAccumulatorBit();
        }
        else {
            setExtendedAccumulatorBit();
        }
        return this;
    }

    Processor writeMemory(int value) {
        getMemory().write(getAddressRegister(), value);
        return this;
    }
    
    int readMemory() {
        return getMemory().read(getAddressRegister());
    }

    Processor incrementDataRegister() {
        setDataRegister(getDataRegister() + 1);
        return this;
    }

    Processor incrementAddressRegister() {
        setAddressRegister(getAddressRegister() + 1);
        return this;
    }

    JLabel getAccumulatorLabel() {
        return accumulatorLabel;
    }

    Processor setAccumulatorLabel(JLabel accumulatorLabel) {
        this.accumulatorLabel = accumulatorLabel;
        return this;
    }

    Memory getMemory() {
        return memory;
    }

    Processor setMemory(Memory memory) {
        this.memory = memory;
        return this;
    }

    Processor incrementProgramCounter() {
        setProgramCounter(getProgramCounter() + 1);
        return this;
    }

    boolean isIndirectAddress() {
        return indirectAddressBit;
    }

    Processor setIndirectAddressBit() {
        this.indirectAddressBit = true;
        return this;
    }
    
    Processor clearIndirectAddressBit() {
        this.indirectAddressBit = false;
        return this;
    }

    boolean isStop() {
        return stopFlag;
    }

    Processor setStopFlag() {
        this.stopFlag = true;
        return this;
    }
    
    Processor clearStopFlag() {
        stopFlag = false;
        return this;
    }

    boolean getExtendedAccumulatorBit() {
        return extendedAccumulatorBit;
    }

    Processor setExtendedAccumulatorBit() {
        this.extendedAccumulatorBit = true;
        return this;
    }
    
    Processor clearExtendedAccumulatorBit() {
        extendedAccumulatorBit = false;
        return this;
    }

    boolean isInterruptCycle() {
        return interruptCycleFlag;
    }

    Processor setInterruptCycleFlag() {
        this.interruptCycleFlag = true;
        return this;
    }
    
    Processor clearInterruptCycleFlag() {
        interruptCycleFlag = false;
        return this;
    }

    boolean isInterruptEnabled() {
        return interruptEnabledFlag;
    }

    Processor setInterruptEnabledFlag() {
        this.interruptEnabledFlag = true;
        return this;
    }
    
    Processor clearInterruptEnabledFlag() {
        this.interruptEnabledFlag = false;
        return this;
    }

    boolean isInputReady() {
        return inputFlag;
    }

    Processor setInputFlag() {
        this.inputFlag = true;
        return this;
    }
    
    Processor clearInputFlag() {
        this.inputFlag = false;
        return this;
    }

    boolean isOutputReady() {
        return outputFlag;
    }

    Processor setOutputFlag() {
        this.outputFlag = true;
        return this;
    }
    
    Processor clearOutputFlag() {
        outputFlag = false;
        return this;
    }

    int getDataRegister() {
        return dataRegister;
    }

    Processor setDataRegister(int dataRegister) {
        this.dataRegister = dataRegister;
        return this;
    }

    int getAddressRegister() {
        return addressRegister;
    }

    Processor setAddressRegister(int addressRegister) {
        this.addressRegister = addressRegister;
        return this;
    }

    int getAccumulator() {
        return accumulator;
    }

    Processor setAccumulator(int accumulator) {
        this.accumulator = accumulator;
        getAccumulatorLabel().setText(Integer.toString(getAccumulator()));
        return this;
    }

    int getInstructionRegister() {
        return instructionRegister;
    }

    Processor setInstructionRegister(int instructionRegister) {
        this.instructionRegister = instructionRegister;
        return this;
    }

    int getProgramCounter() {
        return programCounter;
    }

    Processor setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
        return this;
    }

    int getTemporaryRegister() {
        return temporaryRegister;
    }

    Processor setTemporaryRegister(int temporaryRegister) {
        this.temporaryRegister = temporaryRegister;
        return this;
    }

    int getInputRegister() {
        return inputRegister;
    }

    Processor setInputRegister(int inputRegister) {
        this.inputRegister = inputRegister;
        return this;
    }

    int getOutputRegister() {
        return outputRegister;
    }

    Processor setOutputRegister(int outputRegister) {
        this.outputRegister = outputRegister;
        return this;
    }
}
