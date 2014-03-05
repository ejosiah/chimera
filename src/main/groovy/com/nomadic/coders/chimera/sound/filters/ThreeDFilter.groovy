package com.nomadic.coders.chimera.sound.filters

import com.nomadic.coders.chimera.graphics.Sprite

/**
 * Created by jebhomenye on 05/03/2014.
 */
class ThreeDFilter extends SoundFilter {
    Sprite source
    Sprite listener
    int maxDistance
    float lastVolume

    ThreeDFilter(Sprite source, Sprite listener, int maxDistance){
        this.source = source
        this.listener = listener
        this.lastVolume = 0.0f
    }


    @Override
    void filter(byte[] samples, int offset, int length) {
        if(!source || !listener){
            return
        }

        float distance = listener.distanceFrom(source)

        float newVolume = maxDistance - distance / maxDistance
        newVolume = newVolume <= 0 ? 0 : newVolume

        int shift
        for(int i=)
    }
}
