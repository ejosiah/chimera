package com.nomadic.coders.chimera.example

import com.nomadic.coders.chimera.core.GameCore
import groovy.transform.Synchronized

import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

/**
 * Created by jebhomenye on 28/02/2014.
 */
class KeyTest extends GameCore implements KeyListener{
    def messages = [] as LinkedList

    void init(){
        super.init()

        screen.fullScreenWindow.setFocusTraversalKeysEnabled true
        screen.fullScreenWindow.addKeyListener this
        addMessage "KeyInputTest. Press Escape to exit"
    }

    void keyPressed(KeyEvent e){
        def keyCode = e.keyCode

        if(keyCode == KeyEvent.VK_ESCAPE){
            stop()
        }else{
            addMessage "Pressed ${KeyEvent.getKeyText keyCode}"
            e.consume()
        }

    }

    void keyReleased(KeyEvent e){
        int keyCode = e.keyCode
        addMessage "Released ${KeyEvent.getKeyText keyCode}"
        e.consume()
    }

    void keyTyped(KeyEvent e){
        e.consume()
    }

    @Synchronized
    void addMessage(message){
        messages.add message
        if(messages.size() >= screen.height / FONT_SIZE){
            messages.remove 0
        }
    }

    @Synchronized
    def draw(Graphics2D g) {

        def window = screen.fullScreenWindow
        g.setRenderingHint RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON

        g.setColor window.background
        g.fillRect 0, 0, screen.width, screen.height

        g.setColor window.foreground
        def y = FONT_SIZE
        messages.each {
            g.drawString it, 5, y
            y += FONT_SIZE
        }
    }

    static main(args){
        new KeyTest().run()
    }
}
