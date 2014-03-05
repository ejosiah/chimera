package com.nomadic.coders.chimera.sound

import com.nomadic.coders.chimera.sound.filters.SoundFilter

/**
 * Created by jebhomenye on 05/03/2014.
 */
class VolumeTest {

    static main(args){
        Sound sound = new Sound("sounds/voice.wav")
        InputStream source = new ByteArrayInputStream(sound.sample)
        sound.play(source)

        // double
        for(int i = 0; i < sound.sample.size(); i+=2){
            short newSample = SoundFilter.getSample(sound.sample, i) * 4
            SoundFilter.setSample(sound.sample, i, newSample)
        }
        Thread.sleep 1000
        source.reset()
        sound.play(source)
    }
}
