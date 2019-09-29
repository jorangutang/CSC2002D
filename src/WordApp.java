//package skeletonCodeAssgnmt2;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;


import java.util.Scanner;
import java.util.concurrent.*;
//model is separate from the view.

public class WordApp {
//shared variables
	static int noWords=4;
	static int totalWords;

   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;

	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	static WordRecord[] words;
	static volatile boolean done;  //must be volatile
	static 	Score score = new Score();

	static WordPanel w;
	static volatile String text = "";
    static JFrame frame;
    static JLabel missed;
    static JLabel caught;
    static JLabel scr;
	
	
	
	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
    	frame = new JFrame("WordGame");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setSize(frameX,frameY);
 
    	
		w = new WordPanel(words,yLimit);
		w.setSize(frameX,yLimit+100);
	    g.add(w);
	    
	    
	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
	    caught =new JLabel("Caught: " + score.getCaught() + "    ");
	    missed =new JLabel("Missed:" + score.getMissed()+ "    ");
	    scr =new JLabel("Score:" + score.getScore()+ "    ");
	    txt.add(caught);
	    txt.add(missed);
	    txt.add(scr);



	    //[snip]
  
	    final JTextField textEntry = new JTextField("",20);
	   	textEntry.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent evt) {
	          text = textEntry.getText();
	          //[snip]
	          textEntry.setText("");
	          textEntry.requestFocus();

	      }
	    });
       txt.add(textEntry);
       txt.setMaximumSize( txt.getPreferredSize() );
       g.add(txt);
	    
	    JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
	   	JButton startB = new JButton("Start");
		
			// add the listener to the jbutton to handle the "pressed" event
			startB.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  //[snip]
                  startB.setEnabled(false);
                  WordApp.StartGame();
		    	  textEntry.requestFocus();  //return focus to the text entry field

		      }
		    });
		JButton endB = new JButton("End");

				endB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
			          WordPanel.done = false;
			          WordPanel.inc = 0;
			    	  startB.setEnabled(true);
			    	  score.resetScore();
                      caught.setText("Caught: " + score.getCaught() + "    ");
                      missed.setText("Missed:" + score.getMissed()+ "    ");
                      scr.setText("Score:" + score.getScore()+ "    ");
                      UpdateScreen();
			      }
			    });

        JButton PauseplayB = new JButton("Pause/Resume");
        PauseplayB.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("in");
                if (WordPanel.done == true){
                    System.out.println("done");
                    WordPanel.done = false;
                }
                else { WordPanel.done = true;
                    WordPanel.inc = 0;
                    StartGame();
                }
            }
        });


        b.add(startB);
		b.add(endB);
		b.add(PauseplayB);
		
		g.add(b);
    	
      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);     
       	//frame.pack();  // don't do this - packs it into small space
        frame.setVisible(true);

		
	}

    public static void UpdateScreen(){
	    for (int i = 0; i < noWords; i++){
	        words[i].resetPos();
	        words[i].getWord();
        }
    }
    public static void ViewinfoBox()
    {
        JOptionPane.showMessageDialog(null, "Your Score: " + score.getScore(), "Results:", JOptionPane.INFORMATION_MESSAGE);
    }
	
    public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt();
			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
				//System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			}
			dictReader.close();
		} catch (IOException e) {
	        System.err.println("Problem reading file " + filename + " default dictionary will be used");
	    }
		return dictStr;

	}


	public static void StartGame(){
        WordPanel.done = true;
        for (int i = 0; i < noWords; i++) {
            Thread Wthread = new Thread(w);
            Wthread.start();
            try {
                Wthread.sleep(2);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }


	public static void main(String[] args) {
    	
		//deal with command line arguments, make defaults
		//totalWords = Integer.parseInt(args[0]);  //total words to fall
        totalWords = 6;

		//noWords = Integer.parseInt(args[1]); // total words falling at any point
		noWords = 4;
        assert(totalWords>=noWords); // this could be done more neatly
		String[] tmpDict = null;
		//= getDictFromFile(args[2]); //file of words
		if (tmpDict!=null) {
			dict= new WordDictionary(tmpDict);
		}
		WordRecord.dict=dict; //set the class dictionary for the words.
		
		words = new WordRecord[noWords];  //shared array of current words
		
		//[snip]
		
		setupGUI(frameX, frameY, yLimit);

		int x_inc=(int)frameX/noWords;
	  	//initialize shared array of current words

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
		}


    }

}
