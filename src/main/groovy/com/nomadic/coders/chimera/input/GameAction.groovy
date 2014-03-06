package com.nomadic.coders.chimera.input

import groovy.transform.Synchronized

/**
 * Created by jay on 01/03/14.
 */
class GameAction {
    enum Behavior{ NORMAL, DETECT_INITIAL_PRESS_ONLY  }
    enum State{ RELEASED, PRESSED, WAITING_FOR_RELEASE}

    String name
    Behavior behavior
    State state
    int amount

    GameAction(String name){
        this(name, Behavior.NORMAL)
    }

    GameAction(String name, Behavior behavior){
        this.name = name
        this.behavior = behavior
        reset()
    }

    void reset(){
        state = State.RELEASED
        amount = 0
    }

    @Synchronized
    void tap(){
        press()
        release()
    }

    @Synchronized
    void press(){
        press(1)
    }

    @Synchronized
    void press(int amount){
        if(state != State.WAITING_FOR_RELEASE){
            this.amount += amount
            state = State.PRESSED
        }
    }

    @Synchronized
    void release(){
        state = State.RELEASED
    }

    @Synchronized
    boolean isPressed(){
        return getAmount() != 0
    }

    @Synchronized
    int getAmount(){
        int returnVal = amount
        if(returnVal != 0){
            if(state == State.RELEASED){
                amount = 0
            }else if(behavior == Behavior.DETECT_INITIAL_PRESS_ONLY){
                state = State.WAITING_FOR_RELEASE
                amount = 0
            }
        }
        returnVal
    }

    boolean isHeldDown(){
        state != State.RELEASED
    }
}
