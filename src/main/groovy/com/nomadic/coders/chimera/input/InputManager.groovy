package com.nomadic.coders.chimera.input

import com.nomadic.coders.chimera.graphics.Screen
import groovy.transform.Synchronized
import groovy.util.logging.Log4j

import javax.swing.SwingUtilities
import java.awt.Cursor
import java.awt.Point
import java.awt.Robot
import java.awt.Toolkit as TK
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent

/**
 * Created by jay on 01/03/14.
 */
@Log4j
class InputManager implements InputListener {

    public static final INVISIBLE_CURSOR = TK.defaultToolkit.createCustomCursor(
            TK.defaultToolkit.getImage(""), new Point(0, 0), "invisible"
    )

    static enum MouseCode{
        MOVE_LEFT('Mouse left'),
        MOVE_RIGHT('Mouse right'),
        MOVE_UP('Mouse up'),
        MOVE_DOWN('Mouse down'),
        WHEEL_UP('Mouse wheel up'),
        WHEEL_DOWN('Mouse wheel down'),
        BUTTON_1('Mouse button 1'),
        BUTTON_2('Mouse button 2'),
        BUTTON_3('Mouse button 3')

        String value

        MouseCode(String value){
            this.value = value
        }
    }

    Map<Integer, Action> keyActions = [:]
    Map<MouseCode, Action> mouseActions = [:]

    Point mouseLocation
    Point center
    Screen screen
    Robot robot
    boolean isRecentering

    InputManager(Screen screen){
        this.screen = screen
        screen.addInputListener this
        screen.setFocusTraversalKeysEnabled false

        mouseLocation = new Point()
        center = new Point()
    }

    void setCursor(Cursor cursor){
        screen.setCursor cursor
    }

    void setRelativeMouseMode(boolean mode){
        if(mode == relativeMouseMode){
            return
        }
        if(mode){
            try{
                robot = new Robot()
                recenterMouse()
            }catch(e){
                robot = null
                log.warn "couldn't create robot"
            }
        }else{
            robot = null
        }
    }

    boolean isRelativeMouseMode(){
        robot != null
    }

    void mapToKey(Action action, int keyCode){
        keyActions[keyCode] = action
    }

    void mapToMouse(Action action, MouseCode mouseCode){
        mouseActions[mouseCode] = action
    }

    void clear(Action action){
        keyActions.each{ k, v -> if(action == v) keyActions.remove k}
        mouseActions.each{k, v -> if (action == v) mouseActions.remove k}
        action.reset()
    }
    
    def getMapedKeysFor(Action action){
        def list = []
        
        keyActions.each{ k, v -> if(action == v) list << getKeyName(k)  }
        mouseActions.each{ k, v -> if(action == v) list << k.value }
        list
    }

    String getKeyName(int keyCode) {
        KeyEvent.getKeyText keyCode
    }

    void resetAllActions(){
        keyActions.each{ it.value.reset()}
        mouseActions.each{ it.value.reset()}
    }

    int getMouseX(){
        mouseLocation.x
    }

    int getMouseY(){
        mouseLocation.y
    }

    private recenterMouse(){
        if(robot && screen.showing){
            center.x = screen.width/2
            center.y = screen.height/2
            SwingUtilities.convertPointToScreen(center, screen.fullScreenWindow)
            isRecentering = true
            rebot.mouseMouse center.x, center.y
        }
    }

    Action getKeyAction(KeyEvent e){
        keyActions[e.keyCode]
    }

    Action getMouseButtonAction(MouseEvent e){
        MouseCode code = getMouseButtonCode(e)
        mouseActions[code]
    }

    static MouseCode getMouseButtonCode(MouseEvent e){
        switch (e.getButton()){
            case MouseEvent.BUTTON1: return MouseCode.BUTTON_1
            case MouseEvent.BUTTON2: return MouseCode.BUTTON_2
            case MouseEvent.BUTTON3: return MouseCode.BUTTON_3
        }
    }

    @Override
    void keyPressed(KeyEvent e){
        getKeyAction(e)?.press()
        e.consume()
    }

    @Override
    void keyReleased(KeyEvent e){
        getKeyAction(e)?.release()
        e.consume()
    }

    @Override
    void keyTyped(KeyEvent e){
        e.consume()
    }

    @Override
    void mousePressed(MouseEvent e){
        getMouseButtonAction(e)?.press()
    }

    @Override
    void mouseReleased(MouseEvent e){
        getMouseButtonAction(e)?.release()
    }

    @Override
    void mouseClicked(MouseEvent e){
        // ignore
    }

    @Override
    void mouseEntered(MouseEvent e){
        mouseMoved e
    }

    @Override
    void mouseExited(MouseEvent e){
        mouseMoved e
    }

    @Override
    void mouseDragged(MouseEvent e){
        mouseMoved e
    }

    @Synchronized @Override
    void mouseMoved(MouseEvent e){
        if(isRecentering && center.x == e.x && center.y == e.y){
            isRecentering = false
        }else{
            int dx = e.x - mouseLocation.x
            int dy = e.y - mouseLocation.y
            mouseHelper MouseCode.MOVE_LEFT, MouseCode.MOVE_RIGHT, dx
            mouseHelper MouseCode.MOVE_UP, MouseCode.MOVE_DOWN, dy
            if(isRelativeMouseMode()){
                recenterMouse()
            }
        }

        mouseLocation.x = e.x
        mouseLocation.y = e.y
    }

    void mouseHelper(MouseCode positive, MouseCode negative, int amount) {
        Action action = amount < 0 ?  mouseActions[negative] : mouseActions[positive]
        action?.press Math.abs(amount)
        action?.release()
    }

    @Override
    void mouseWheelMoved(MouseWheelEvent e){
        mouseHelper MouseCode.WHEEL_UP, MouseCode.WHEEL_DOWN, e.wheelRotation
    }
}