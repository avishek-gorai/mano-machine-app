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
    
    private Processor setRandomValues() {
        setAccumulator(new Random().nextInt(0xFFFF));
        return this;
    }

    private int getSequenceCounter() {
        return sequenceCounter;
    }

    private Processor setSequenceCounter(int sequenceCounter) {
        this.sequenceCounter = sequenceCounter;
        return this;
    }

    private Terminal getTerminal() {
        return terminal;
    }

    private Processor setTerminal(Terminal terminal) {
        this.terminal = terminal;
        return this;
    }

    private static int getMaximummemorysize() {
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

    private Processor complementExtendedAccumulatorBit() {
        if (getExtendedAccumulatorBit() == true) {
            clearExtendedAccumulatorBit();
        }
        else {
            setExtendedAccumulatorBit();
        }
        return this;
    }

    private Processor writeMemory(int value) {
        getMemory().write(getAddressRegister(), value);
        return this;
    }
    
    private int readMemory() {
        return getMemory().read(getAddressRegister());
    }

    private Processor incrementDataRegister() {
        setDataRegister(getDataRegister() + 1);
        return this;
    }

    private Processor incrementAddressRegister() {
        setAddressRegister(getAddressRegister() + 1);
        return this;
    }

    private JLabel getAccumulatorLabel() {
        return accumulatorLabel;
    }

    private Processor setAccumulatorLabel(JLabel accumulatorLabel) {
        this.accumulatorLabel = accumulatorLabel;
        return this;
    }

    private Memory getMemory() {
        return memory;
    }

    private Processor setMemory(Memory memory) {
        this.memory = memory;
        return this;
    }

    private Processor incrementProgramCounter() {
        setProgramCounter(getProgramCounter() + 1);
        return this;
    }

    private boolean isIndirectAddress() {
        return indirectAddressBit;
    }

    private Processor setIndirectAddressBit() {
        this.indirectAddressBit = true;
        
        return this;
    }
    
    private Processor clearIndirectAddressBit() {
        this.indirectAddressBit = false;
        
        return this;
    }

    private boolean isStop() {
        return stopFlag;
    }

    private Processor setStopFlag() {
        this.stopFlag = true;
        
        return this;
    }
    
    private Processor clearStopFlag() {
        stopFlag = false;
        
        return this;
    }

    private boolean getExtendedAccumulatorBit() {
        return extendedAccumulatorBit;
    }

    private Processor setExtendedAccumulatorBit() {
        this.extendedAccumulatorBit = true;
        
        return this;
    }
    
    private Processor clearExtendedAccumulatorBit() {
        extendedAccumulatorBit = false;
        
        return this;
    }

    private boolean isInterruptCycle() {
        return interruptCycleFlag;
    }

    private Processor setInterruptCycleFlag() {
        this.interruptCycleFlag = true;
        
        return this;
    }
    
    private Processor clearInterruptCycleFlag() {
        interruptCycleFlag = false;
        
        return this;
    }

    private boolean isInterruptEnabled() {
        return interruptEnabledFlag;
    }

    private Processor setInterruptEnabledFlag() {
        this.interruptEnabledFlag = true;
        
        return this;
    }
    
    private Processor clearInterruptEnabledFlag() {
        this.interruptEnabledFlag = false;
        
        return this;
    }

    private boolean isInputReady() {
        return inputFlag;
    }

    private Processor setInputFlag() {
        this.inputFlag = true;
        
        return this;
    }
    
    private Processor clearInputFlag() {
        this.inputFlag = false;
        
        return this;
    }

    private boolean isOutputReady() {
        return outputFlag;
    }

    private Processor setOutputFlag() {
        this.outputFlag = true;
        return this;
    }
    
    private Processor clearOutputFlag() {
        outputFlag = false;
        
        return this;
    }

    private int getDataRegister() {
        return dataRegister;
    }

    private Processor setDataRegister(int dataRegister) {
        this.dataRegister = dataRegister;
        
        return this;
    }

    private int getAddressRegister() {
        return addressRegister;
    }

    private Processor setAddressRegister(int addressRegister) {
        this.addressRegister = addressRegister;
        
        return this;
    }

    private int getAccumulator() {
        return accumulator;
    }

    private Processor setAccumulator(int accumulator) {
        this.accumulator = accumulator;
        getAccumulatorLabel().setText(Integer.toString(getAccumulator()));
        
        return this;
    }

    private int getInstructionRegister() {
        return instructionRegister;
    }

    private Processor setInstructionRegister(int instructionRegister) {
        this.instructionRegister = instructionRegister;
        
        return this;
    }

    private int getProgramCounter() {
        return programCounter;
    }

    private Processor setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
        
        return this;
    }

    private int getTemporaryRegister() {
        return temporaryRegister;
    }

    private Processor setTemporaryRegister(int temporaryRegister) {
        this.temporaryRegister = temporaryRegister;
        
        return this;
    }

    private int getInputRegister() {
        return inputRegister;
    }

    private Processor setInputRegister(int inputRegister) {
        this.inputRegister = inputRegister;
        
        return this;
    }

    private int getOutputRegister() {
        return outputRegister;
    }

    private Processor setOutputRegister(int outputRegister) {
        this.outputRegister = outputRegister;
        
        return this;
    }
}
