package com.nomadic.coders.chimera.example

import com.nomadic.coders.chimera.core.GameCore

import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.Window
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener

/**
 * Created by jebhomenye on 28/02/2014.
 */
class MouseTest extends GameCore implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener{

    static final def COLORS = [Color.white, Color.black, Color.yellow, Color.magenta]
    static final int TRAIL_SIZE = 10

    def trailList
    boolean trailMode
    int colorIndex


    @Override
    void init(){
        super.init()
        trailList = [] as LinkedList

        Window window = screen.fullScreenWindow
        window.addMouseListener this
        window.addMouseMotionListener this
        window.addMouseWheelListener this
        window.addKeyListener this
    }

    @Override
    def draw(Graphics2D g) {
        int limit = trailList.size()

        if(limit && !trailMode){
            limit = 1
        }

        g.setColor screen.background
        g.setRenderingHint RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON

        g.setColor screen.foreground
        for(i in 0..<limit){
            def p = trailList[i]
            g.drawString "Hello World", p.x, p.y
        }

    }

    @Override
    void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    void keyPressed(KeyEvent e) {
        if(e.keyCode == KeyEvent.VK_ESCAPE)
            stop()
    }

    @Override
    void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    void mousePressed(MouseEvent mouseEvent) {
        trailMode = !trailMode
    }

    @Override
    void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    void mouseEntered(MouseEvent mouseEvent) {
        mouseMoved(mouseEvent)
    }

    @Override
    void mouseExited(MouseEvent mouseEvent) {
        mouseMoved(mouseEvent)
    }

    @Override
    void mouseDragged(MouseEvent mouseEvent) {
        mouseMoved(mouseEvent)
    }

    @Override
    void mouseMoved(MouseEvent e) {
        Point p = [e.x, e.y]
        trailList << p
        while(trailList.size() > TRAIL_SIZE){
            trailList.removeLast()
        }
    }

    @Override
    void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        colorIndex = (colorIndex + mouseWheelEvent.wheelRotation) % COLORS.size()

        if(colorIndex < 0){
            colorIndex += COLORS.size()
        }
    }

    static main(args){
        new MouseTest().run()
    }
}

class Point{
    int x
    int y
    Point(int x, int y){
        this.x = x
        this.y = y
    }
}