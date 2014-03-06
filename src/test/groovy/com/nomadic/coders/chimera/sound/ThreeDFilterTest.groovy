package com.nomadic.coders.chimera.sound

import com.nomadic.coders.chimera.core.GameCore
import com.nomadic.coders.chimera.graphics.Animation
import com.nomadic.coders.chimera.graphics.Sprite
import com.nomadic.coders.chimera.input.GameAction
import com.nomadic.coders.chimera.input.InputManager
import com.nomadic.coders.chimera.sound.filters.ThreeDFilter
import com.nomadic.coders.chimera.sound.io.FilteredSoundStream
import com.nomadic.coders.chimera.sound.io.LoopingByteInputStream
import java.awt.event.KeyEvent

import java.awt.Color
import java.awt.Graphics2D
import java.awt.Image

/**
 * Created by jay on 05/03/14.
 */
class ThreeDFilterTest extends GameCore {

    Sprite fly
    Sprite listener
    InputManager inputManager
    GameAction exit

    Sound bzzSound
    InputStream bzzSoundStream

    @Override
    void init(){
        super.init()
        Sound.initWith(1)
        exit = new GameAction("exit", GameAction.Behavior.DETECT_INITIAL_PRESS_ONLY)
        inputManager = new InputManager(screen)
        inputManager.mapToKey exit, KeyEvent.VK_ESCAPE
        inputManager.cursor = InputManager.INVISIBLE_CURSOR

        createSprites()

        bzzSound = new Sound("sounds/fly-bzz.wav")

        ThreeDFilter filter = new ThreeDFilter( fly, listener, screen.height)

        bzzSound.play(true, filter)
    }

    void createSprites() {
        Animation animation = new Animation()
        new File("images/").eachFile{
            if(it.name.contains("fly")){
                Image image = loadImage(it.toURL())
                animation.addFrames(image, 50)
            }
        }
        fly = new Sprite(animation)
        Image ear = loadImage("images/ear.png")
        animation = new Animation()
        animation.addFrames(ear, 0)

        listener = new Sprite(animation)
        listener.x = (screen.width - listener.width) * 0.5
        listener.y = (screen.height - listener.height) * 0.5
    }

    @Override
    void update(long elapsedTime){
        if(exit.isPressed()){
            stop()
            Sound.shutdown()
        }else{
            listener.update(elapsedTime)
            fly.update(elapsedTime)
            fly.x = inputManager.mouseX
            fly.y = inputManager.mouseY
        }
    }

    @Override
    void stop(){
        super.stop()
        bzzSoundStream.close()
    }

    @Override
    def draw(Graphics2D g) {
        g.color = new Color(0x33CC33)
        g.fillRect 0, 0, screen.width, screen.height

        g.drawImage listener.image, listener.intX, listener.intY, null
        g.drawImage fly.image, fly.intX, fly.intY, null
    }

    static main(args){
        new ThreeDFilterTest().run()
    }
}
