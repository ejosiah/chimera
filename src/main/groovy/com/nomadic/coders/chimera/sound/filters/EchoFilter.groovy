package com.nomadic.coders.chimera.sound.filters

/**
 * Created by jebhomenye on 05/03/2014.
 */
class EchoFilter extends SoundFilter{

    short[] delayBuffer
    int delayPos
    float decay

    EchoFilter(int numSamplesToDay, float decay){
        delayBuffer = new short[numSamplesToDay]
        this.decay = decay
    }

    @Override
    void filter(byte[] samples, int offset, int length) {
        offset.step(offset+length, 2){ i ->
            short oldSample = getSample(samples, i)
            short newSample = oldSample + decay * delayBuffer[delayPos]

            setSample(samples, i, newSample)

            delayBuffer[delayPos] = newSample
            delayPos++
            delayPos %= delayBuffer.size()
        }
    }

    @Override
    void reset(){
       delayBuffer = delayBuffer.collect{ 0 }
       delayPos = 0
    }

    @Override
    int getRemainingSize(){
        float finalDecay = 0.01f
        int numRemainingBuffers = Math.ceil(Math.log(finalDecay) / Math.log(decay))
        int bufferSize = delayBuffer.size() * 2
        return bufferSize * numRemainingBuffers
    }
}
