package com.nomadic.coders.chimera.sound


/**
 * Created by jay on 05/03/14.
 */
class SoundTest {

    static main(args){
        Sound.initWith(10)
        Sound sound = new Sound("sounds/voice.wav")

        sound.play(true)

        Thread.start{
            Thread.sleep 5000
            Sound.paused = true
            Thread.sleep 2000
            Sound.paused = false
            sound.play(true)
            Thread.sleep 2000
            sound.play(true)
            println sound.shutdown()
          //  println "shutdown"
        }
    }
}
