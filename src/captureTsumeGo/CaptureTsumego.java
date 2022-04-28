package captureTsumeGo;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class CaptureTsumego extends JDialog {
  CaptureTsumego t;
  private JPanel mainPanel;

  public CaptureTsumego() {
    try {
      t =
          new CaptureTsumego(null) {
            private static final long serialVersionUID = 1L;

            protected void capture() {
              super.capture();
            }
          };
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void start() {
    try {
      t.open();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static final long serialVersionUID = 1L;

  private int directionIndex = -1; // 1左上2右上3左下4右下
  private int result[];
  private int boardWidth;
  private int boardHeight;
  private int x1 = -1, y1 = -1, x2 = -1, y2 = -1, x3 = -1, y3 = -1;
  private int widthGap, heightGap;
  private double curWidthGap, curHeightGap;
  private int clickTimes = 0;
  private boolean capFailed;
  private int curX, curY;
  private int tempWidthGap;
  private int tempHeightGap;
  //  private int orgx, orgy, endx, endy;
  //  private int orgxMouse, orgyMouse, endxMouse, endyMouse;

  private Dimension screenSize;
  private BufferedImage imageShow;
  private BufferedImage imageOut;
  private Color backGroundColor = new Color(0, 0, 0, 25);

  public void paintMianPanel(Graphics g) {
    if (imageShow != null) {
      g.drawImage(imageShow, 0, 0, this);
    }
  }

  private void drawSelectArea() {
    //	    if (x1 == -1 || y1 == -1)
    //	    	return;
    imageShow = new BufferedImage(getWidth(), getHeight(), TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D) imageShow.getGraphics();

    //	    startX = Math.min(orgxMouse, endxMouse);
    //	    startY = Math.min(orgyMouse, endyMouse);
    //	    capWidth = width;
    //	    capHeight = height;
    g.setColor(backGroundColor);
    g.fillRect(0, 0, getWidth(), getHeight());

    if (clickTimes == 0) {
      g.setColor(new Color(0, 245, 0, 200));
      g.setStroke(new BasicStroke(2));
      g.drawLine(0, curY, getWidth(), curY);
      g.drawLine(curX, 0, curX, getHeight());
    } else if (clickTimes == 1) {
      g.setColor(new Color(0, 245, 0, 200));
      g.setStroke(new BasicStroke(2));
      g.drawLine(0, curY, getWidth(), curY);
      g.drawLine(curX, 0, curX, getHeight());
      g.setColor(new Color(0, 0, 200, 120));
      g.setStroke(new BasicStroke(4));
      tempWidthGap = Math.abs(x1 - curX);
      tempHeightGap = Math.abs(y1 - curY);
      if (curX > x1 && curY > y1) {
        for (int i = 0; i < 5; i++) {
          g.drawLine(x1 + tempWidthGap * i, y1, x1 + tempWidthGap * i, y1 + tempHeightGap * 5);
        }
        for (int i = 0; i < 5; i++) {
          g.drawLine(x1, y1 + tempHeightGap * i, x1 + tempWidthGap * 5, y1 + tempHeightGap * i);
        }
      } else if (curX < x1 && curY > y1) {
        for (int i = 0; i < 5; i++) {
          g.drawLine(x1 - tempWidthGap * i, y1, x1 - tempWidthGap * i, y1 + tempHeightGap * 5);
        }
        for (int i = 0; i < 5; i++) {
          g.drawLine(x1, y1 + tempHeightGap * i, x1 - tempWidthGap * 5, y1 + tempHeightGap * i);
        }
      } else if (curX < x1 && curY < y1) {
        for (int i = 0; i < 5; i++) {
          g.drawLine(x1 - tempWidthGap * i, y1, x1 - tempWidthGap * i, y1 - tempHeightGap * 5);
        }
        for (int i = 0; i < 5; i++) {
          g.drawLine(x1, y1 - tempHeightGap * i, x1 - tempWidthGap * 5, y1 - tempHeightGap * i);
        }
      } else if (curX > x1 && curY < y1) {
        for (int i = 0; i < 5; i++) {
          g.drawLine(x1 + tempWidthGap * i, y1, x1 + tempWidthGap * i, y1 - tempHeightGap * 5);
        }
        for (int i = 0; i < 5; i++) {
          g.drawLine(x1, y1 - tempHeightGap * i, x1 + tempWidthGap * 5, y1 - tempHeightGap * i);
        }
      }

    } else if (clickTimes == 2) {
      int x = Math.min(x1, curX);
      int y = Math.min(y1, curY);
      int width = Math.abs(x1 - curX);
      int height = Math.abs(y1 - curY);
      g.setColor(new Color(0, 0, 200, 200));
      g.setStroke(new BasicStroke(3));
      g.drawRect(x, y, width, height);
      int verticalLines = (int) Math.round(width / curWidthGap);
      int horizonLines = (int) Math.round(height / curHeightGap);   
        for (int i = 1; i < verticalLines; i++) {
          g.drawLine((int) (x + curWidthGap * i), y, (int) (x + curWidthGap * i), y + height);
        }
        for (int i = 1; i < horizonLines; i++) {
          g.drawLine(x, (int) (y + curHeightGap * i), x + width, (int) (y + curHeightGap * i));
        }
      
      int startX= (int) Math.round(x-curWidthGap/2);
      int startY= (int) Math.round(y-curHeightGap/2);
      
      Robot robot;
      try {
        robot = new Robot();
        imageOut = robot.createScreenCapture(new Rectangle(startX, startY,(int) Math.round(width+curWidthGap),
        		(int) Math.round(height+curHeightGap)));
        boardWidth=verticalLines+1;
        boardHeight=horizonLines+1;
        result=  recognizeBoard(imageOut,(float)curHeightGap,(float)curWidthGap,boardHeight,boardWidth);
        g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    //   Color cl= g.getColor();
       //System.out.println("==============");
       for(int yy=0;yy<horizonLines+1;yy++)
        	{ for(int xx=0;xx<verticalLines+1;xx++)
        	{
        		int value=result[yy * (verticalLines+1) + xx];
        	//	System.out.print(value);
        		if(value==1||value ==2)
        		{
        			int xPos=(int)Math.round( x+xx*curWidthGap);
        			int yPos=(int)Math.round(y+yy*curHeightGap);
        			if(value==1)
        			g.setColor(new Color(255,255,0));
        			if(value==2)
        				g.setColor(new Color(255,0,255));
        			fillCircle(g,xPos,yPos,(int)Math.min(curWidthGap/3.5,curHeightGap/3.5));
        		}
        	}
        	//System.out.println("");
        	}
      // g.setColor(cl);
      } catch (Exception e) {
      }
    }
    repaint();
    //		int tx = endx + 5;
    //		int ty = endy + 20;
    //		if (tx + 100 > screenSize.width || ty + 30 > screenSize.height) {
    //			tx = endx - 100;
    //			ty = endy - 30;
    //		}
    //		g.setColor(Color.RED);
    //		g.drawString("w: " + width + ", h: " + height, tx, ty);
  }
  
  private void fillCircle(Graphics2D g, int centerX, int centerY, int radius) {
	    g.fillOval(centerX - radius, centerY - radius, 2 * radius + 1, 2 * radius + 1);
	  }
  
  class MoveColorInfo {
	  public int blackPercent;
	  public int whitePercent;
	  public int pureWhitePercent;
	  public int almostWhitePercent;
	  public boolean trueWhite;
	}
  
  private int[] getRGB(BufferedImage image, int x, int y) {
	    int[] rgb = null;
	    rgb = new int[3];
	    int pixel = image.getRGB(x, y);
	    rgb[0] = (pixel & 0xff0000) >> 16;
	    rgb[1] = (pixel & 0xff00) >> 8;
	    rgb[2] = (pixel & 0xff);
	    return rgb;
	  }
  
  private MoveColorInfo getColorPercent(
	      BufferedImage input,
	      int startX,
	      int startY,
	      int width,
	      int height) {
	    int blackSum = 0;
	    int whiteSum = 0;
	    int pureWhiteSum=0;
	    int almostWhiteSum=0;
	    boolean trueWhite=false;
	    if (startX + width > input.getWidth()) startX = input.getWidth() - width;
	    if (startY + height > input.getHeight()) startY = input.getHeight() - height;
	    for (int y = 0; y < height; y++) {
	      for (int x = 0; x < width; x++) {
	        int rgb[] = getRGB(input, startX + x, startY + y);
	        int red = rgb[0];
	        int blue = rgb[1];
	        int green = rgb[2];
	        if (Math.abs(red - blue) < CaptureGo.grayOffset
	            && Math.abs(blue - green) <CaptureGo.grayOffset
	            && Math.abs(green - red) <CaptureGo.grayOffset) {
	          if (red <= CaptureGo.blackOffset
	              && blue <=CaptureGo.blackOffset 
	              && green <= CaptureGo.blackOffset) {
	            blackSum++;
	          }
	          int whiteValue = 255 - CaptureGo.whiteOffset;
	          if (red >= whiteValue && blue >= whiteValue && green >= whiteValue) {
	            whiteSum++;
	          }
	          int pureWhiteValue = 255 - 30;
	          if (red >= pureWhiteValue && blue >= pureWhiteValue && green >= pureWhiteValue) {
	        	  pureWhiteSum++;
	          }
	          int almostWhiteValue = 255 - 65;
	          if (red >= almostWhiteValue && blue >= almostWhiteValue && green >= almostWhiteValue) {
	        	  almostWhiteSum++;
	          }
	        }
	      }
	    }
	   
	    MoveColorInfo colorInfo = new MoveColorInfo();
	    int total = width * height;
	    colorInfo.blackPercent = (100 * blackSum) / total;
	    colorInfo.whitePercent = (100 * whiteSum) / total;
	    colorInfo.pureWhitePercent=(100 * pureWhiteSum) / total;
	    colorInfo.almostWhitePercent=(100 * almostWhiteSum) / total;
	    if(colorInfo.whitePercent>=CaptureGo.whitePercent)
	    {	    	
	    	int y=(int)Math.round(height/4.0);
	    	  int pureWhiteValue = 255 - 30;
	    	  for (int x = 0; x <  (int)Math.round(width*2.0/6.0); x++) {
	    		  int rgb[] = getRGB(input, startX + x, startY + y);
			        int red = rgb[0];
			        int blue = rgb[1];
			        int green = rgb[2];
	    		  if (red < pureWhiteValue || blue <pureWhiteValue || green< pureWhiteValue) {
	    			  trueWhite=true;
	    			  break;
		          }
	    	  }
	    	 
	    }
	    colorInfo.trueWhite=trueWhite;
	    return colorInfo;
	  }
  
  private int[] recognizeBoard(BufferedImage input,float hGap,float vGap,int boardHeight, int boardWidth) {
	    int hGapInt = Math.round(hGap);
	    int vGapInt = Math.round(vGap);

	    int resultValue[] = new int[boardHeight * boardWidth];

	    for (int y = 0; y < boardHeight; y++) {
	      for (int x = 0; x <boardWidth; x++) {
	        MoveColorInfo colorInfo =
	            getColorPercent(
	                input,
	                Math.round(x * vGap),
	                Math.round(y * hGap),
	                vGapInt,
	                hGapInt);
	        boolean isBlack = colorInfo.blackPercent >= CaptureGo.blackPercent;//BoardSyncTool.config.blackPercent;
	        boolean isWhite = false;
	        if (isBlack) {
	          resultValue[y * boardWidth + x] = 1;
	        } else {
	          if (colorInfo.whitePercent >= CaptureGo.whitePercent) {
	              if (!colorInfo.trueWhite&&colorInfo.almostWhitePercent-colorInfo.pureWhitePercent<10)
	            	  isWhite = false;
	              else 
	            	  isWhite = true;
	          }
	          if (isWhite) {
	            resultValue[y * boardWidth + x] = 2;
	          } else resultValue[y *boardWidth + x] = 0;
	        }
	      }
	    }
	 return resultValue;	
	  }

  private void bindSelectAreaListener() {
    this.addMouseListener(
        new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
              switch (clickTimes) {
                case 0:
                  x1 = e.getX();
                  y1 = e.getY();
                  break;
                case 1:
                  widthGap = tempWidthGap;
                  heightGap = tempHeightGap;
                  if (tempWidthGap == 0 || tempHeightGap == 0) {
                    capFailed = true;
                    capture();
                  }
                  x2 = curX;
                  y2= curY;
                  if (x2 > x1 && y2 > y1) directionIndex = 1;
                  else if (x2 < x1 && y2 > y1) directionIndex = 2;
                  else if (x2 > x1 && y2 < y1) directionIndex = 3;
                  else if (x2 < x1 && y2 < y1) directionIndex = 4;
                  break;
                case 2:
                  x3 = e.getX();
                  y3 = e.getY();
                  capture();
                  break;
              }
              clickTimes++;
            }
          }
        });

    addMouseMotionListener(
        new MouseAdapter() {
          @Override
          public void mouseMoved(MouseEvent e) {
            curX = e.getX();
            curY = e.getY();
            if (clickTimes == 2) {
              int curWidth = Math.abs(curX - x1);
              int curHeight = Math.abs(curY - y1);
              curWidthGap = curWidth / (double) Math.round(curWidth / (double) widthGap);
              curHeightGap = curHeight / (double) Math.round(curHeight / (double) heightGap);
            }
            drawSelectArea();
          }
        });

    addKeyListener(
        new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
              quit();
            }
          }
        });
  }

  public CaptureTsumego(JDialog owner) throws Exception {
    super(owner);
  }

  public void open() throws Exception {
	setAlwaysOnTop(true);
    bindSelectAreaListener();
    screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    mainPanel =
        new JPanel() {
          @Override
          public void paintComponent(Graphics g) {
            ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
            paintMianPanel(g);
          }
        };
    this.getContentPane().add(mainPanel);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setUndecorated(true);
    this.setBackground(backGroundColor);
    this.setSize(screenSize);
    this.setLocation(0, 0);
    this.setVisible(true);
  }

  protected void close() {
	if(result!=null&&boardWidth>0&&boardHeight>0)
	{
		System.out.println("dx "+this.directionIndex+" bw "+this.boardWidth+" bh "+this.boardHeight);
		String output="# ";
		for(int y=0;y<boardHeight;y++)
		{
			for(int x=0;x<boardWidth;x++)
			{
				if(x<boardWidth-1)
				output+=result[y*boardWidth+x]+" ";
				else
					{output+=result[y*boardWidth+x];
					System.out.println(output);
					output="# ";
					}
			}
		}
	}
	System.out.println("end");
    this.dispose();
  }

  private void capture() {
    if (capFailed) quit();
    else close();
  }

  private void quit() {
	  System.out.println("esc");
    this.dispose();
  }

  public BufferedImage screenCapture(Dimension screenSize) throws AWTException {
    Robot robot = new Robot();
    return robot.createScreenCapture(new Rectangle(0, 0, screenSize.width, screenSize.height));
  }
}

