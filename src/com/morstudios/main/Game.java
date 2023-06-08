package com.morstudios.main;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.morestudidos.world.World;
import com.morestudios.enttities.BulletShoot;
import com.morestudios.enttities.Enemy;
import com.morestudios.enttities.Player;
import com.morestudios.enttities.entity;
import com.morestudios.graficos.Spritesheet;
import com.morestudios.graficos.UI;

public class Game extends Canvas implements Runnable, KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static int WIDTH = 240;
	public static int HEIGHT = 160;
	private final int SCALE = 3;
	
	private BufferedImage image;
	
	public static List<entity> entities;
	public static List<Enemy> enemys;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	public static boolean isGrounded;
	public static Random rand;
	
	public UI ui;
	
	
	
	public Game() {
		rand = new Random();
		addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		setFocusable(true);
		requestFocus();
		//inicializando objetos;
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<entity>();
		enemys = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/Spritesheet.png");
		player = new Player(16,0,16,16,spritesheet.getSprite(0, 0, 16, 16));
		entities.add(player);	
		world = new World("/map1.png");
	}
	public void initFrame() {
		frame = new JFrame("jogo 1:");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
			thread = new Thread(this);
			isRunning = true;
			thread.start();
			
	}
	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main (String Args[]){
		Game game = new Game();
		game.start();
		
	}

	public void tick() {	
		for(int i = 0; i <entities.size(); i++) {
			entity e = entities.get(i);
			e.tick();
		}
		for(int i = 0; i <bullets.size();i++) {
			bullets.get(i).tick();
		}
		
	}
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//renderização do game
	//	Graphics2D g2 = (Graphics2D) g;
		//
		world.render(g);
		for(int i = 0; i <entities.size(); i++) {
			entity e = entities.get(i);
			e.render(g);
			ui.render(g);
		}
		for(int i = 0; i <bullets.size();i++) {
			bullets.get(i).render(g);;
		}
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE, HEIGHT*SCALE,null);
		g.setFont(new Font("ARIAL", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("Munição: " + player.municao, 600, 22);
		bs.show();
		
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double times = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;	
			lastTime = now;
			if (delta >=1) {
				tick();
				render();
				frames++;
				delta--;
				
			}
			if(System.currentTimeMillis()- times >=1000) {
				System.out.println("fps"+frames);
				frames = 0;
				times+=1000;
				
			}
				
		
		}
		
		stop();
	}
	public void Character() {
	    final int frameX = world.TILE_SIZE*2;
	    final int frameY = world.TILE_SIZE;
	    final int frameW = world.TILE_SIZE*5;
	    final int frameH = world.TILE_SIZE*10;
	    draSubWindow(frameX, frameY, frameW, frameH, getGraphics());
	}

	public void draSubWindow(int x, int y, int widht, int height, Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;
	    Color c = new Color(0,0,0,210);
	    g.setColor(c);
	    g2.fillRoundRect(x, y, widht, height, 35, 35);

	    c = new Color(255,255,255);
	    g2.setColor(c);
	    g2.setStroke(new BasicStroke(5));
	    g2.drawRoundRect(x+5, y+5, widht-10, height-10, 25, 25);
	}

	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
			
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				 e.getKeyCode() == KeyEvent.VK_D) {
			System.out.println("direita");
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
			System.out.println("esquerda");
		}
		/*if(e.getKeyCode() == KeyEvent.VK_UP ||
					e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
				System.out.println("cima");*/
		/*else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
				System.out.println("baixo");
		} */
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.Shoot = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = true;
		}
		
}
		
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				 e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;

		}
		if(e.getKeyCode() == KeyEvent.VK_UP ||
					e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		} 
			
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			//player.jump = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_I) {
			Character();		
}
	}

}
