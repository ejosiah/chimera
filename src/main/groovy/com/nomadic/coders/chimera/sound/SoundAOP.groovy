package com.nomadic.coders.chimera.sound

import org.codehaus.groovy.runtime.InvokerHelper

/**
 * Created by jay on 05/03/14.
 */
class SoundAOP implements GroovyInterceptable{
    static SoundManager soundManager
    static{
        soundManager = SoundManager.instance
        if(!soundManager){
            throw new RuntimeException("No sound manager active")
        }
    }

    Object invokeMethod(String name, Object args){
        if(name == "play"){
            println "executing sound play in thread"
            soundManager.execute{
                InvokerHelper.getMetaClass(this).invokeMethod this, name, args
            }
        }else{
            InvokerHelper.getMetaClass(this).invokeMethod this, name, args
        }
    }

    static waitIfGamePaused() {
        println "checking if game is paused"
        synchronized (soundManager.pauseLock){
            if(soundManager.paused){
                println "waiting to be unpaused"
                soundManager.pauseLock.wait()
            }
        }
    }
}
