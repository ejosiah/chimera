package com.nomadic.coders.chimera.sound

import org.codehaus.groovy.runtime.InvokerHelper

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.Mixer
import javax.sound.sampled.SourceDataLine
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by jay on 05/03/14.
 */
class SoundService implements GroovyInterceptable{
    static ExecutorService executorService
    private static final Object PAUSE_LOCK = new Object()
    private static boolean paused

    static waitIfGamePaused() {
        synchronized (PAUSE_LOCK){
            if(paused){
                PAUSE_LOCK.wait()
            }
        }
    }

    static void setPaused(boolean p){
        if(paused != p){
            synchronized (PAUSE_LOCK){
                paused = p
                if(!paused){
                    PAUSE_LOCK.notifyAll()
                }
            }
        }
    }

    static int maxSimultaneousSoundsFor(AudioFormat format){
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format)
        Mixer mixer = AudioSystem.getMixer(null)
        mixer.getMaxLines info
    }

    static AudioFormat AudioFormatFor(String path){
        AudioSystem.getAudioInputStream(new File(path)).format
    }

    static initWith(int maxSimultaneousSounds){
        executorService = Executors.newFixedThreadPool(maxSimultaneousSounds)
    }

    static initFor(AudioFormat playBackFormat){
        initWith(maxSimultaneousSoundsFor(playBackFormat))
    }

    static void close(){
        executorService.shutdownNow()
    }

    private static cleanup(){
        setPaused(false)

        Mixer mixer = AudioSystem.getMixer(null)
        if(mixer.open){
            mixer.close()
        }
    }

    static shutdown(){
        cleanup()
        executorService.shutdownNow()
    }
}
