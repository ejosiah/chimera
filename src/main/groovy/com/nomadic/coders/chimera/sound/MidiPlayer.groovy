package com.nomadic.coders.chimera.sound

import javax.sound.midi.MetaEventListener
import javax.sound.midi.MetaMessage
import javax.sound.midi.MidiSystem
import javax.sound.midi.Sequencer

/**
 * Created by jebhomenye on 06/03/2014.
 */
class MidiPlayer implements MetaEventListener {

    static final int END_OF_TRACK = 47

    Sequencer sequencer
    boolean loop
    boolean paused

    MidiPlayer(){
        sequencer = MidiSystem.getSequencer()
        sequencer?.open()
        sequencer?.addMetaEventListener this
    }

    static javax.sound.midi.Sequence getSequence(String filename){
        MidiSystem.getSequence(new File(filename))
    }

    void play(javax.sound.midi.Sequence sequence, boolean loop=false){
        if(sequence){
            sequencer?.sequence = sequence
            sequencer?.start()
            this.loop = loop
        }
    }

    @Override
    void meta(MetaMessage metaMessage) {
        if(metaMessage.type == END_OF_TRACK && sequencer?.open && loop){
            sequencer?.tickPosition = 0
            sequencer?.start()
        }
    }

    void stop(){
        if(sequencer?.open){
            sequencer?.stop()
            sequencer?.microsecondPosition = 0
        }

    }

    void close(){
        if(sequencer?.open){
            sequencer.close()
        }
    }

    void setPaused(boolean p){
        if(paused != p){
            paused = p
            if(paused){
                sequencer?.stop()
            }
        }else{
            sequencer?.start()
        }
    }
}
