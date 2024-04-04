package tr.edu.ku.comp302;


import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.handler.FrameHandler;

/**
 * Hello world!
 */
public class App 
{
    
    public static void main(String[] args)
    {

        LanceOfDestiny lanceOfDestiny = new LanceOfDestiny();
        FrameHandler frameHandler = new FrameHandler(lanceOfDestiny);
        frameHandler.startGame();

        System.out.println("Hi World!");
        
    }
}
