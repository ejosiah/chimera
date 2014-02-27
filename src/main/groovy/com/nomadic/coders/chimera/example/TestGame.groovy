package com.nomadic.coders.chimera.example

import com.nomadic.coders.chimera.core.GameCore

import java.awt.Graphics2D

/**
 * Created by jay on 27/02/14.
 */
class TestGame extends GameCore{

    def draw(Graphics2D g){
        g.drawString("Hello World!", 300, 300)
    }

    static main(args){
        def game = new TestGame()
        Thread.start{
            Thread.sleep(10000)
            game.stop()

        }
        game.run()

    }
}
