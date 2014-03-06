package com.nomadic.coders.chimera.sound

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.Mixer
import javax.sound.sampled.SourceDataLine
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by jay on 05/03/14.
 */
class SoundManager extends ThreadPoolExecutor{
    final Object pauseLock = new Object()
    boolean paused

    private SoundManager(AudioFormat format){
        this(maxSimultaneousSoundFor(format))
    }

    SoundManager(int nThreads){
        super(nThreads, nThreads,  0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
        instance = this
    }

    static int poolSize(AudioFormat format){
        int value = maxSimultaneousSoundFor format
    }

    void setPaused(boolean p){
        if(paused != p){
            synchronized (pauseLock){
                paused = p
                if(!paused){
                    pauseLock.notifyAll()
                }
            }
        }
    }

    static int maxSimultaneousSoundFor(AudioFormat format){
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format)
        Mixer mixer = AudioSystem.getMixer(null)
        mixer.getMaxLines info
    }

    static AudioFormat AudioFormatFor(String path){
        AudioSystem.getAudioInputStream(new File(path)).format
    }

}

