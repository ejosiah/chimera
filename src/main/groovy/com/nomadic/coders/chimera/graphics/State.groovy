package com.nomadic.coders.chimera.graphics

import groovy.transform.EqualsAndHashCode

/**
 * Created by jay on 02/03/14.
 */
@EqualsAndHashCode
abstract class State {
    static State STATE_LESS = new State("state less") {}
    String name

    State(String name){
        this.name = name
    }
}
