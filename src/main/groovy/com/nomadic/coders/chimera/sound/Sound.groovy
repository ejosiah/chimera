package com.nomadic.coders.chimera.sound

import com.nomadic.coders.chimera.sound.filters.SoundFilter
import com.nomadic.coders.chimera.sound.io.FilteredSoundStream
import com.nomadic.coders.chimera.sound.io.LoopingByteInputStream

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine

/**
 * Created by jay on 04/03/14.
 */
class Sound extends SoundService{

    byte[] sample
    AudioFormat format

    Sound(String path){
        AudioInputStream stream = AudioSystem.getAudioInputStream(new File(path))
        format = stream.format
        createSample(stream)

    }

    void createSample(AudioInputStream stream) {
        int sampleSize = stream.frameLength * format.frameSize
        sample = new byte[sampleSize]

        DataInputStream dataIn = new DataInputStream(stream)
        dataIn.readFully(sample)
    }

    void play(boolean loop=false, SoundFilter filter=null){
        executorService.execute{
            InputStream source = createStream(loop, filter)
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format)
            SourceDataLine line = AudioSystem.getLine(info)

            int bufferSize = format.frameSize * Math.round(format.sampleRate / 10)



            line.open(format, bufferSize)
            line.start()
            source.eachByte(bufferSize){ buffer, length ->
                waitIfGamePaused()
                line.write(buffer, 0, length)
            }

            line.drain()
            line.close()
        }
    }

    InputStream createStream(boolean loop, SoundFilter soundFilter) {
        InputStream inStream = loop ? new LoopingByteInputStream(sample) : new ByteArrayInputStream(sample)
        soundFilter ? new FilteredSoundStream(inStream, soundFilter) : inStream
    }
}
