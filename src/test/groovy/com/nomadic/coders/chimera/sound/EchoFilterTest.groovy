package com.nomadic.coders.chimera.sound

import com.nomadic.coders.chimera.sound.filters.EchoFilter
import com.nomadic.coders.chimera.sound.io.FilteredSoundStream

/**
 * Created by jebhomenye on 05/03/2014.
 */
class EchoFilterTest {

    static main(args){
        Sound sound = new Sound("sounds/voice.wav")
        InputStream is = new ByteArrayInputStream(sound.sample)
        EchoFilter filter = new EchoFilter(11025, 0.6F)

        InputStream fis = new FilteredSoundStream(is, filter)
        sound.play(fis)
    }
}
