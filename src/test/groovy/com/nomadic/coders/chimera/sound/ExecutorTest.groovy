package com.nomadic.coders.chimera.sound

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by jebhomenye on 06/03/2014.
 */
class ExecutorTest {
    static ExecutorService executorService = Executors.newSingleThreadExecutor()

    static main(args){
        println "starting main"
        executorService.execute{
            while(true){
                println "Hello World!"
//                Thread.sleep 2000
            }
        }
        Thread.start{
            Thread.sleep 5000
            println "shuting down executor"
            executorService.shutdownNow()
        }
        println "exiting main"
    }
}
