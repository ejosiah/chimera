package com.nomadic.coders.chimera.graphics

import groovy.transform.Synchronized

import javax.swing.JComponent
import javax.swing.RepaintManager

/**
 * Created by jebhomenye on 03/03/2014.
 */
class VoidRepaintManager extends RepaintManager {

    static install(){
        RepaintManager repaintManager = new VoidRepaintManager()
        repaintManager.doubleBufferingEnabled = false
        RepaintManager.currentManager = repaintManager
    }

    @Override
    void addInvalidComponent(JComponent c){

    }

    @Override
    void addDirtyRegion(JComponent c, int x, int y, int w, int h){

    }

    @Override
    void markCompletelyDirty(JComponent c){

    }

    @Override
    void paintDirtyRegions(){

    }
}
