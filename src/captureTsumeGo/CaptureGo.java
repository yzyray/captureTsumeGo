package captureTsumeGo;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class CaptureGo extends JFrame{
public static int grayOffset=80;
public static int blackOffset=120;
public static int whiteOffset=120;
public static int blackPercent=25;
public static int whitePercent=15;

	public CaptureGo() {
		 SwingUtilities.invokeLater(
				    new Runnable() {
				      public void run() {
				        CaptureTsumego captureTsumego = new CaptureTsumego();
				        captureTsumego.start();
				      }
				    });
	}
	public static void main(String[] args) {
		 if (args.length == 5) {
		      try {		    	  
		    	  blackOffset = Integer.parseInt(args[0]);
		    	  blackPercent = Integer.parseInt(args[1]);
		    	  whiteOffset = Integer.parseInt(args[2]);
		    	  whitePercent = Integer.parseInt(args[3]);
		    	  grayOffset = Integer.parseInt(args[4]);
		      } catch (NumberFormatException e) {
		        e.printStackTrace();
		      }
		    }
		new CaptureGo();
	  }
}
