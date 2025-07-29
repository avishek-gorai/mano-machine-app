package mano_machine.mano_machine_app;

import java.awt.GridBagConstraints;

/**
 * This class represents the constraints for processor compoments like memory, terminal and register views.
 * 
 * @author Avishek Gorai
 */
public class ComputerLayoutConstraint
extends GridBagConstraints {
    private static final long serialVersionUID = -5358025916504491840L;
    
    ComputerLayoutConstraint() {
        setWeightX(1.0);
        setWeightY(1.0);
        setFill(BOTH);
    }

    ComputerLayoutConstraint setWeightY(double d) {
        weighty = d;
        return this;
    }
    
    double getWeightY() {
        return weighty;
    }

    ComputerLayoutConstraint setWeightX(double d) {
        weightx = d;
        return this;
    }
    
    double getWeightX() {
        return weightx;
    }

    ComputerLayoutConstraint setGridX(int x) {
        gridx = x;
        return this;
    }
    
    int getGridX() {
        return gridx;
    }
    
    ComputerLayoutConstraint setGridY(int y) {
        gridy = y;
        return this;
    }
    
    int getGridY() {
        return gridy;
    }

    ComputerLayoutConstraint setGridWidth(int i) {
        gridwidth = i;
        return this;
    }
    
    int getGridWidth() {
        return gridwidth;
    }

    ComputerLayoutConstraint setFill(int f) {
        fill = f;
        return this;
    }
    
    int getFill() {
        return fill;
    }

    ComputerLayoutConstraint setGridHeight(int h) {
        gridheight = h;
        return this;
    }
    
    int getGridHeight() {
        return gridheight;
    }

}
