package com.nomadic.coders.chimera.core

import com.nomadic.coders.chimera.graphics.Screen

import javax.swing.ImageIcon
import java.awt.Color
import java.awt.DisplayMode
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Image

/**
 * Created by jebhomenye on 27/02/2014.
 */
abstract class GameCore {
    static final int FONT_SIZE = 24

    static final List<DisplayMode> POSSIBLE_MODES = [
            new DisplayMode(800, 600, 32, 0),
            new DisplayMode(800, 600, 24, 0),
            new DisplayMode(800, 600, 16, 0),
            new DisplayMode(640, 480, 32, 0),
            new DisplayMode(640, 480, 24, 0),
            new DisplayMode(640, 480, 16, 0)
    ]

    private boolean isRunning
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
        }
    }

    void init(){
        screen = new Screen()
        DisplayMode displayMode = screen.findFirstCompatibleModes(POSSIBLE_MODES)
        screen.fullScreenWindow displayMode

        screen.font ['Dialog', Font.PLAIN, FONT_SIZE]
        screen.background Color.blue
        screen.foreground Color.white

        isRunning = true
    }

    Image loadImage(String filename){
        return new ImageIcon(filename).image
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
}
