package edu.century.lifProject;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.JFrame;

public class CellGridCanvas extends Canvas implements Runnable, KeyListener
{
	
	private static final long serialVersionUID = -3286682838434340596L;
	// Creating private objects to be initialized later.
	private Thread thread;
	private JFrame frame;
	
	// Initializing variables for the game loop.
	private boolean running = false;
	private double UPDATE_CAP = 1.0/30.0;
	private int fps;
	private Font fpsFont;
	
	// Initializing variables for the dimensions of the window.
	private final int WIDTH = 646, HEIGHT = 509;
	private final String TITLE = "Game of Life";
		
	// Game of life variables.
	int gen = 0;
	Map map = new Map(Constants.BLINKER);
	int rows = map.getRows();
	int columns = map.getColumns();
	Color BACKGROUND = new Color(255,255,255);
	Color LIFE = new Color(150,150,150);
	boolean pause = true;
	boolean drawGrid = false;
	LifeNode logicGrid[][] = new LifeNode[rows][columns]; 

	
	
	// Main method creates the driver object which starts the game loop.
	public static void main(String[] args)
	{
		new CellGridCanvas();
	}
		
	// Initializing window and making "listGrid"
	public CellGridCanvas()
	{
		//Using map to make lifeNode array
		establish(logicGrid); 
		
		//setting up frame
		initFrame(); 
	
		// Starting the game loop.
		start();
        
        try
        {
        	fpsFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("pixel.TTF"))).deriveFont(Font.PLAIN, 16);
        } catch(Exception e){}
	}
	
	/**
	 * Starts a new Light Weight process
	 * 
	 * @param none
	 * @return none
	 */
	public synchronized void start()
	{
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	/**
	 * Ends this Light Weight Process
	 * 
	 * @param none
	 * @return none
	 */
	public synchronized void stop()
	{
		try
		{
			thread.join();
			running = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This Light Weight Process's main loop. Control transfers from {@link #start}
	 * 
	 * @param
	 */
	public void run()
	{
		  running = true;
		  
		  boolean render = false;
		  double firstTime = 0;
		  double lastTime = System.nanoTime() / 1000000000.0;
		  double passedTime = 0;
		  double unprocessedTime = 0;
		  
		  double frameTime = 0;
		  int frames = 0;
		  fps = 0;
		  
		  while(running) 
		  {
			  render = false;
			  firstTime = System.nanoTime() / 1000000000.0;
			  passedTime = firstTime - lastTime;
			  lastTime = firstTime;
		   
			  unprocessedTime += passedTime;
			  frameTime += passedTime;
		   
			  while(unprocessedTime >= UPDATE_CAP) 
			  {
				  unprocessedTime -= UPDATE_CAP;
				  render = true;
				  
				  if(frameTime >= 1.0)
				  {
					  frameTime = 0;
					  fps = frames;
					  frames = 0;
				  }
				  
				  tick();
			  }
			  if(render) 
			  {
			   render();
			   frames++;
			  }
		  }
	     stop();
	}
	
	private void tick() {}
	
	/**
	 * This uses the paint method from the newly created window to show everything on screen.
	 * 
	 * @param None
	 * @return None
	 */
	private void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}

		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		
		// Sets the background color to black.
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//g.setColor(Color.lightGray);
		//update();
		
		g.setColor(LIFE);
		displayGeneration(g);
		
		if(!pause)
		nextGeneration();
		
		g.setColor(Color.BLACK);
		g.setFont(fpsFont);
		g.drawString("FPS: " + Integer.toString(fps), 1, 22);
		g.drawString("GEN: " + Integer.toString(gen), 1, 44);
		g.drawString("GRID: " + Integer.toString(rows) + " " + Integer.toString(columns), 1, 66);
		
		
		// Disposes of extra resources and shows the buffer strategy.
		g.dispose();
		bs.show();
	}

	
	/**
	 * Displays current generation to window
	 * @param g A Graphics2D Obj. In this instance, one containing a BufferStrategy.
	 * @return None
	 */
	public void displayGeneration(Graphics2D g)
	{
		for(int i = 0; i < rows-1; i++)
		{
			for(int n = 0; n < columns; n++)
			{
				g.setColor(LIFE);
				if(map.grid[i][n] == 1)
					g.fillRect(n*8,i*8,8,8);
				
				if(drawGrid)
				{
					g.setColor(Color.BLACK);
					g.drawRect(n*8, i*8, 8, 8);
				}
			}
		}
	}
	
//	public void nextGeneration()
//	{
//		gen++;
//		int[][] future = new int[rows][columns];
//		
//		for(int m = 1; m < rows - 1; m++)
//		{
//			for(int n = 1; n < columns - 1; n++)
//			{
//				int aliveNeighbours = 0;
//                for (int i = -1; i <= 1; i++)
//                    for (int j = -1; j <= 1; j++)
//                        aliveNeighbours += map.grid[m + i][n + j];
//                
//                aliveNeighbours -= map.grid[m][n];
//                
//                // Cell is lonely and dies
//                if ((map.grid[m][n] == 1) && (aliveNeighbours < 2))
//                    future[m][n] = 0;
// 
//                // Cell dies due to over population
//                else if ((map.grid[m][n] == 1) && (aliveNeighbours > 3))
//                    future[m][n] = 0;
// 
//                // A new cell is born
//                else if ((map.grid[m][n] == 0) && (aliveNeighbours == 3))
//                    future[m][n] = 1;
//                
//                else
//                	future[m][n] = map.grid[m][n];
//			}
//		}
//		
//		map.grid = future;
//	}
	
	public void nextGeneration()
	{
		gen++;
		LifeNode[][] future = new LifeNode[rows][columns];
		establish(future); 
		
		for(int m = 1; m < rows - 1; m++)
		{
			for(int n = 1; n < columns - 1; n++)
			{
				int aliveNeighbours = logicGrid[m][n].determineNebighors(); 
                
                // Cell is lonely and dies
                if ((logicGrid[m][n].getStatus() == 1) && (aliveNeighbours < 2))
                	future[m][n].setStatus(0);
 
                // Cell dies due to over population
                else if ((logicGrid[m][n].getStatus() == 1) && (aliveNeighbours > 3))
                    future[m][n].setStatus(0);
 
                // A new cell is born
                else if ((logicGrid[m][n].getStatus() == 0) && (aliveNeighbours == 3))
                	future[m][n].setStatus(1);
                
                else
                	future[m][n].setStatus(logicGrid[m][n].getStatus()); 
			}
		}
		
		logicGrid = future; 
		updateMap(); 
	}
	
	/**
	 * Instantiates a lifeNode grid complete with edges based on current 
	 * status of map array. 
	 * @param grid
	 * @returns None
	 */
	public void establish(LifeNode[][] grid)
	{
		for(int m = 0; m < rows; m++)
		{
			for(int n = 0; n < columns; n++)
			{
				grid[m][n] = new LifeNode(map.grid[m][n], m, n);
                       
			}
		}
		
		for(int m = 1; m < rows - 1; m++)
		{
			for(int n = 1; n < columns - 1; n++)
			{
				  for (int i = -1; i <= 1; i++)
		              for (int j = -1; j <= 1; j++)
		              {
		              	if(i != 0 || j != 0)
		              		grid[m][n].list.addToTail(logicGrid[m+i][n+j]); 
		              }	
                       
			}
		}
		
		
	}
	
	/**
	 * Updates map array based on status of logicGrid, primarily for display.
	 * @param None
	 * @return none
	 */
	public void updateMap()
	{
		for(int m = 1; m < rows - 1; m++)
		{
			for(int n = 1; n < columns - 1; n++)
			{
				map.grid[m][n] = logicGrid[m][n].getStatus(); 
                       
			}
		}
	}
	
	/**
	 * Initializes all frame components and Adds KeyListeners
	 * 
	 * @param None
	 * @return None
	 */
	public void initFrame()
	{
		// Initializing the JFrame.
		frame = new JFrame(TITLE);
				
		// Setting the size of the JFrame.
		frame.setSize(WIDTH, HEIGHT);
				
		// Setting the default close operation so the program stops running after you close the window.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		// Setting the window to not be resizable.
		frame.setResizable(false);
				
		// Setting the window to be in the center of the screen.
		frame.setLocationRelativeTo(null);
				
		// Adding the frame to the driver.
		frame.add(this);
				
		// Making the frame visible.
		frame.setVisible(true);
				
		// Packing the frame.
		frame.pack();
				
		addKeyListener(this);
	}

	
	//keyListners
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyChar();
		
		if(key == 't')
			nextGeneration();
		if(key == 'g')
			drawGrid = !drawGrid;
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
			pause = !pause;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

}