package com.nomadic.coders.chimera.sound

import javax.sound.midi.MetaEventListener
import javax.sound.midi.MetaMessage
import javax.sound.midi.Sequencer

/**
 * Created by jebhomenye on 06/03/2014.
 */
class MidiTest implements MetaEventListener {
    static final int DRUM_TRACK = 1

    MidiPlayer player

    void run(){
        player = new MidiPlayer()

        javax.sound.midi.Sequence sequence = MidiPlayer.getSequence "sounds/music.midi"

        player.play(sequence, true)

        println "Playing (without drums)..."
        player.sequencer.setTrackMute(DRUM_TRACK, true)
        player.sequencer.addMetaEventListener this
    }

    @Override
    void meta(MetaMessage metaMessage) {
        if(metaMessage.type == MidiPlayer.END_OF_TRACK){
            if(player.sequencer.getTrackMute(DRUM_TRACK)){
                println "Turning on drums"
                player.sequencer.setTrackMute DRUM_TRACK, false
            }
        }else{
            println "Exiting..."
            System.exit 0
        }
    }

    static main(args){
        new MidiTest().run()
    }
}
