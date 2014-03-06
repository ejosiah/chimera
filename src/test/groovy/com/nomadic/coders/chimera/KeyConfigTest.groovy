package com.nomadic.coders.chimera

import com.nomadic.coders.chimera.input.GameAction
import com.nomadic.coders.chimera.input.InputManager

import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JLayeredPane
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.border.Border
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Cursor
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent

/**
 * Created by jebhomenye on 04/03/2014.
 */
class KeyConfigTest extends MenuTest{
    static final String INSTRUCTIONS = new File("instructions.txt").text

    JPanel dialog
    JButton okButton
    List inputs

    @Override
    void init(){
        super.init()

        inputs = []

        JPanel configPanel = new JPanel(new GridLayout(5, 2, 2, 2))
        addActionConfig configPanel, moveLeft
        addActionConfig configPanel, moveRight
        addActionConfig configPanel, jump
        addActionConfig configPanel, pause
        addActionConfig configPanel, exit

        JPanel bottomPanel = new JPanel(new FlowLayout())
        okButton = new JButton("OK")
        okButton.focusable = false
        okButton.addActionListener this
        bottomPanel.add okButton

        JPanel topPanel = new JPanel(new FlowLayout())
        topPanel.add(new JLabel(INSTRUCTIONS))

        Border border = BorderFactory.createLineBorder Color.BLACK

        dialog = new JPanel(new BorderLayout())
        dialog.add topPanel, BorderLayout.NORTH
        dialog.add configPanel, BorderLayout.CENTER
        dialog.add bottomPanel, BorderLayout.SOUTH
        dialog.border = border
        dialog.visible = false
        dialog.size = dialog.getPreferredSize()
        int w = (screen.width - dialog.width)/2
        int h = (screen.height - dialog.width)/2
        dialog.setLocation w , h
        dialog.cursor = Cursor.getPredefinedCursor Cursor.HAND_CURSOR
        screen.fullScreenWindow.layeredPane.add dialog, JLayeredPane.MODAL_LAYER
    }

    void addActionConfig(JPanel panel, GameAction action) {
        JLabel label = [action.name, JLabel.RIGHT]
        InputComponent input = new InputComponent(action)
        panel.add(label)
        panel.add(input)
        inputs.add(input)
    }

    @Override
    void actionPerformed(ActionEvent e){
        super.actionPerformed(e)
        if(e.source == okButton){
            configAction.tap()
        }
    }

    @Override
    void checkSystemInput(){
        super.checkSystemInput()
        if(configAction.isPressed()){
            dialog.visible = !dialog.visible
            setPaused(dialog.visible)
        }
    }

    private resetInputs(){
        inputs.each{ it.text = null}
    }

    class InputComponent extends JTextField{

        GameAction gameAction

        InputComponent(GameAction action){
            this.gameAction = action
            setText()
            enableEvents(KeyEvent.KEY_EVENT_MASK
                    | MouseEvent.MOUSE_EVENT_MASK
                    | MouseEvent.MOUSE_MOTION_EVENT_MASK
                    | MouseEvent.MOUSE_WHEEL_EVENT_MASK)
        }

        private setText(){
            String text = inputManager.getMapedKeysFor(gameAction).join(', ')
            synchronized (treeLock){
                setText(text)
            }
        }

        private mapGameAction(int code, boolean isMouseMap){
            if(inputManager.getMapedKeysFor(gameAction).size() >= 3){
                inputManager.clear(gameAction)
            }
            if(isMouseMap){
                inputManager.mapToMouse(gameAction, InputManager.MouseCode.values()[code])
            }else{
                inputManager.mapToKey(gameAction, code)
            }
            resetInputs()
            screen.fullScreenWindow.requestFocus()
        }

        @Override
        void processKeyEvent(KeyEvent e){
            if(e.ID == e.KEY_PRESSED){
                if(e.keyCode == KeyEvent.VK_BACK_SPACE && inputManager.getMapedKeysFor(gameAction) > 0){
                    inputManager.clear(gameAction)
                    setText("")
                    screen.fullScreenWindow.requestFocus()
                }else{
                    mapGameAction(e.getKeyCode(), false)
                }
            }
            e.consume()
        }

        @Override
        void processMouseEvent(MouseEvent e){
            if(e.ID == e.MOUSE_PRESSED){
                if(hasFocus()){
                    InputManager.MouseCode code = inputManager.getMouseButtonCode(e)
                    mapGameAction(code.ordinal(), true)
                }else{
                    requestFocus()
                }
            }
            e.consume()
        }

        @Override
        void processMouseMotionEvent(MouseEvent e){
            e.consume()
        }

        @Override
        void processMouseWheelEvent(MouseWheelEvent e){
            if(hasFocus()){
                InputManager.MouseCode code = InputManager.MouseCode.WHEEL_DOWN
                if(e.getWheelRotation() < 0){
                    code = InputManager.MouseCode.WHEEL_UP
                }
                mapGameAction(code.ordinal(), true)
            }
            e.consume()
        }
    }

    static main(args){
        new KeyConfigTest().run()
    }
}

