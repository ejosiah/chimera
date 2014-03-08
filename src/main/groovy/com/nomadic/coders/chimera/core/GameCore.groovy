package com.nomadic.coders.chimera.core

import com.nomadic.coders.chimera.graphics.Screen

import javax.swing.ImageIcon
import java.awt.Color
import java.awt.DisplayMode
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints

/**
 * Created by jebhomenye on 27/02/2014.
 */
abstract class GameCore {
    public static final int FONT_SIZE = 24

    static final List<DisplayMode> POSSIBLE_MODES = [
            new DisplayMode(800, 600, 32, 0),
            new DisplayMode(800, 600, 24, 0),
            new DisplayMode(800, 600, 16, 0),
            new DisplayMode(640, 480, 32, 0),
            new DisplayMode(640, 480, 24, 0),
            new DisplayMode(640, 480, 16, 0)
    ]

    volatile boolean isRunning
    protected Screen screen

    void stop(){
        isRunning = false
    }

    void run(){
        try{
            init()
            gameLoop()
        }finally{
            screen.restoreScreen()
            lazyExit()
        }
    }

    static lazyExit(){
        Thread thread = new Thread({
            try{
                Thread.sleep 2000
            }finally{
                System.exit 0
            }
        })
        thread.daemon = true
        thread.start()
    }

    void init(){
        screen = new Screen()
        DisplayMode displayMode = screen.findFirstCompatibleMode(POSSIBLE_MODES)
        screen.createFullScreen displayMode

        screen.font =  [ 'Dialog', Font.PLAIN, FONT_SIZE] as Font
        screen.background = Color.blue
        screen.foreground = Color.white

        isRunning = true
    }

    static loadImage(String filename){
        return new ImageIcon(filename).image
    }

    static loadImage(URL url){
        new ImageIcon((url)).image
    }

    void gameLoop(){
        long startTime = System.currentTimeMillis()
        long currentTime = startTime

        while(isRunning){
            long elapsedTime = System.currentTimeMillis() - currentTime
            currentTime += elapsedTime

            update(elapsedTime)

            Graphics2D g = screen.graphics
            draw(g)
            g.dispose()
            screen.update()

        }
    }

    void update(long elapsedTime){

    }

    abstract  draw(Graphics2D g)

    void setRenderingHint(Graphics2D g){
        g.setRenderingHint RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    }
}
