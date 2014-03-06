package com.nomadic.coders.chimera.sound.io

import com.nomadic.coders.chimera.sound.filters.SoundFilter

/**
 * Created by jebhomenye on 05/03/2014.
 */
class FilteredSoundStream extends FilterInputStream{

    static final int REMAINING_SIZE_UNKNOWN = -1
    int remainingSize
    SoundFilter soundFilter

    FilteredSoundStream(InputStream inputStream, SoundFilter soundFilter) {
        super(inputStream)
        this.soundFilter = soundFilter
        remainingSize = REMAINING_SIZE_UNKNOWN

    }

    @Override
    int read(byte[] samples, int offset, int length){
        int bytesRead = super.read(samples, offset, length)
        if(bytesRead > 0){
           soundFilter.filter(samples, offset, bytesRead)
           return bytesRead
        }

        if(remainingSize == REMAINING_SIZE_UNKNOWN){
            remainingSize = soundFilter.remainingSize
            println "Remaining size : $remainingSize, length: $length"

            // round down to nearest multiple of 4
            // typical frame size
            remainingSize = (remainingSize/4) * 4
        }
        if(remainingSize > 0){
            length = Math.min length, remainingSize

            //clear buffer
            for(i in offset..<(offset+length)){
                samples[i] = 0
            }

            // filter remaining bytes
            soundFilter.filter(samples, offset, length)
            remainingSize -= length

            return length
        }else {
            return -1 // end of stream
        }
    }
}
