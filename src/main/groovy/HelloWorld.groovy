import groovy.util.logging.Log4j

/**
 * Created by jay on 27/02/14.
 */
@Log4j
class HelloWorld {

    static main(args){
        def name = "Josiah"
        if(log.infoEnabled){
            log.info "Hello World"
        }
        log.info "Hello World"
        log.erorr "Hell World"
        println "Hello, World!"
        println "Hello $name"
    }
}
