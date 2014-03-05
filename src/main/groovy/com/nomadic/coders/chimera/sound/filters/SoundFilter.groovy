package com.nomadic.coders.chimera.sound.filters

/**
 * Created by jebhomenye on 05/03/2014.
 */
abstract class SoundFilter {

    void reset(){
        // do nothing
    }

    int getRemainingSize(){
        return 0
    }

    abstract void filter(byte[] samples, int offset, int length)

    static short getSample(byte[]  buffer, int position){
        return (((buffer[position+1] & 0xFF) << 8 )
                | (buffer[position] & 0xFF)) as Short
    }

    static void setSample(byte[] buffer, int pos, short sample){
        buffer[pos] = (sample & 0xFF) as Byte
        buffer[pos+1] = ((sample >> 8) & 0xFF) as Byte
    }

}
