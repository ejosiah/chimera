package com.nomadic.coders.chimera.graphics

import com.nomadic.coders.chimera.input.InputListener

import javax.swing.JFrame
import java.awt.Color
import java.awt.Cursor
import java.awt.DisplayMode
import java.awt.Font
import java.awt.Graphics2D
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import java.awt.Toolkit
import java.awt.Window
import java.awt.event.KeyListener
import java.awt.event.MouseListener
import java.awt.image.BufferStrategy
import java.awt.image.BufferedImage

/**
 * Created by jebhomenye on 27/02/2014.
 */
class Screen {
    GraphicsDevice device

    Screen(){
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
    }

    List<DisplayMode> getCompatibleDisplayModes(){
        device.getDisplayModes()
    }

    DisplayMode findFirstCompatibleMode(List<DisplayMode> modes){
        modes.find{ mode1 ->
            getCompatibleDisplayModes().find{
                mode2 -> displayModeMatches(mode1, mode2)
            }
        }
    }

    static displayModeMatches(DisplayMode mode1, DisplayMode mode2){
        if(mode1.width != mode2.width || mode1.height != mode2.height){
            return false
        }
        if(mode1.bitDepth != DisplayMode.BIT_DEPTH_MULTI
                && mode2.bitDepth != DisplayMode.BIT_DEPTH_MULTI
                && mode1.bitDepth != mode2.bitDepth){
            return false;
        }
        if(mode1.refreshRate != DisplayMode.REFRESH_RATE_UNKNOWN
                && mode2.refreshRate != DisplayMode.REFRESH_RATE_UNKNOWN
                && mode1.refreshRate != mode2.refreshRate){
            return false
        }
        return true
    }

    void setFullScreen(DisplayMode displayMode){
        JFrame frame = new JFrame()
        frame.setUndecorated true
        frame.setIgnoreRepaint true
        frame.setResizable false

        device.setFullScreenWindow frame
        if(displayMode && device.displayChangeSupported){
            try{
                device.setDisplayMode displayMode
            }catch(e){

            }
        }
        frame.createBufferStrategy(2)
    }

    Graphics2D getGraphics(){
        fullScreenWindow?.bufferStrategy?.drawGraphics as Graphics2D
    }

    void update(){
        if(fullScreenWindow){
            BufferStrategy strategy = fullScreenWindow.bufferStrategy
            if(!strategy.contentsLost()){
                strategy.show()
            }
        }
        Toolkit.defaultToolkit.sync()
    }

    Window getFullScreenWindow(){
        device.fullScreenWindow
    }

    int getWidth(){
        fullScreenWindow?.width ?: 0
    }

    int getHeight(){
        fullScreenWindow?.height ?: 0
    }

    void restoreScreen(){
        fullScreenWindow?.dispose()
        device.setFullScreenWindow null
    }

    BufferedImage createCompatibleImage(int w, int h, int transparency){
        fullScreenWindow?.graphicsConfiguration?.createCompatibleImage(w, h, transparency)
    }

    void setBackground(Color color){
        fullScreenWindow?.setBackground color
    }

    void setForeground(Color color){
        fullScreenWindow?.setForeground color
    }

    void setFont(Font font){
        fullScreenWindow?.setFont font
    }

    Color getBackground(){
        fullScreenWindow?.background
    }

    Color getForeground(){
        fullScreenWindow?.foreground
    }

    void addInputListener(InputListener listener){
        fullScreenWindow.addKeyListener listener
        fullScreenWindow.addMouseListener listener
        fullScreenWindow.addMouseMotionListener listener
        fullScreenWindow.addMouseWheelListener listener
    }

    void setFocusTraversalKeysEnabled(boolean enabled){
        fullScreenWindow.setFocusTraversalKeysEnabled enabled
    }

    void setCursor(Cursor cursor) {
        fullScreenWindow.setCursor cursor
    }
}
