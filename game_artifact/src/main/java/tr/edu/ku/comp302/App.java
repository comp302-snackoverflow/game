package tr.edu.ku.comp302;

import tr.edu.ku.comp302.ui.frame.MainFrame;

/**
 * Hello world!
 */
public class App 
{
    public static MainFrame mainFrame;
    public static void main(String[] args)
    {
        mainFrame = new MainFrame();
        mainFrame.displayGamePanel();

        System.out.println("Hi World!");
        
    }
}
