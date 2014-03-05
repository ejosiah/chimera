package com.nomadic.coders.chimera.audio

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.Line
import javax.sound.sampled.SourceDataLine

/**
 * Created by jay on 04/03/14.
 */
class Sound {

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

    void play(){
        InputStream source = new ByteArrayInputStream(sample)
        println "sample size ${sample.length}"

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format)
        SourceDataLine line = AudioSystem.getLine(info)

        int bufferSize = format.frameSize * Math.round(format.sampleRate / 10)
        println "sample rate ${bufferSize * 10}"
        line.open(format, bufferSize)
        line.start()
        source.eachByte(bufferSize){ buffer, length -> line.write(buffer, 0, length) }

        line.drain()
        line.close()
    }

    static main(args){
        Sound sound = new Sound("sounds/wave.wav")
        sound.play()
        System.exit 0
    }
}
