package com.nomadic.coders.chimera.sound

import com.nomadic.coders.chimera.sound.filters.EchoFilter
import com.nomadic.coders.chimera.sound.io.FilteredSoundStream

/**
 * Created by jebhomenye on 05/03/2014.
 */
class EchoFilterTest {

    static main(args){
        Sound.initWith(1)
        Sound sound = new Sound("sounds/voice.wav")
        EchoFilter filter = new EchoFilter(11025, 0.6F)
        try{
            sound.play(false, filter)
        }finally{
            Sound.shutdown()
        }

    }
}
