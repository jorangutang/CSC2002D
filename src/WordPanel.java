//package skeletonCodeAssgnmt2;

import com.sun.deploy.net.protocol.chrome.ChromeURLConnection;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.*;

public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done = true;
		private WordRecord[] words;
		private int noWords;
		private int maxY;
		static volatile int inc = 0; // to assign wordrecords to threads


		public void paintComponent(Graphics g) {
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		   //draw the words
		   //animation must be added 
		    for (int i=0;i<noWords;i++){
		    	g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());
		    }

		  }
		
		WordPanel(WordRecord[] words, int maxY) {
			this.words=words; //will this work?
			noWords = words.length;
			done=false;
			this.maxY=maxY;		
		}


        static synchronized void setbacktozero(){
		    WordPanel.inc = 0;
        }

        static synchronized void increment(){
		    inc++;
        }

		public void run() {
            WordRecord current = words[inc];
            WordPanel.increment();
            int total = WordApp.totalWords;


            while (done == true) {

                if (WordApp.score.getTotal() > total) {
                    done = false;
                    WordApp.UpdateScreen();
                    WordApp.ViewinfoBox();
                    break;
                }

                //check word
                current.drop(10);

                try{
                    Thread.sleep(current.getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                repaint();

                if (current.matchWord(WordApp.text)) {
                    WordApp.score.caughtWord(WordApp.text.length());
                    WordApp.setCaught();
                    WordApp.setScore();
                    current.resetWord();
                    repaint();
                }

                if (current.dropped()) {
                    WordApp.score.missedWord();
                    current.resetWord();
                    WordApp.setMissed();
                    repaint();
                }
            }

        }

	}


