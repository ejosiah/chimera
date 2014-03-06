package com.nomadic.coders.chimera.sound.filters

import com.nomadic.coders.chimera.graphics.Sprite

/**
 * Created by jebhomenye on 05/03/2014.
 */
class ThreeDFilter extends SoundFilter {
    private static final int NO_OF_SHIFTING_SAMPLES = 500
    Sprite source
    Sprite listener
    int maxDistance
    float lastVolume

    ThreeDFilter(Sprite source, Sprite listener, int maxDistance){
        this.source = source
        this.listener = listener
        this.maxDistance = maxDistance
        this.lastVolume = 0.0f
    }


    @Override
    void filter(byte[] samples, int offset, int length) {
        if(!source || !listener){
            return
        }

        float distance = listener.distanceFrom(source)
        println "distance $distance"

        float newVolume = (maxDistance - distance) / maxDistance
        newVolume = newVolume > 0 ? newVolume : 0

        int shift = 0
        for(int i in offset..<((offset+length)/2)){
           float volume = newVolume
           if(shift < NO_OF_SHIFTING_SAMPLES){
               volume = lastVolume + (newVolume - lastVolume) * shift /NO_OF_SHIFTING_SAMPLES
               shift++
           }
           short oldSample = getSample(samples, i * 2)
           short newSample = oldSample * volume
           setSample(samples, i*2, newSample)
        }
        lastVolume = newVolume
    }
}
