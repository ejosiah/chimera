package com.nomadic.coders.chimera.sound


/**
 * Created by jay on 05/03/14.
 */
class SoundTest {

    static main(args){
        SoundManager.instance = new SoundManager(1)
        Sound sound = new Sound("sounds/voice.wav")

        sound.play(true)

        Thread.start{
            Thread.sleep 5000
            SoundManager.instance.paused = true
            Thread.sleep 2000
            SoundManager.instance.paused = false
            Thread.sleep 2000
            SoundManager.instance.shutdownNow()
        }
    }
}
