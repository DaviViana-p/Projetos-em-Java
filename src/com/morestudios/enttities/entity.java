package com.morestudios.enttities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.morestudidos.world.Camera;
import com.morstudios.main.Game;

public class entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(144, 32, 16, 16);
	public static BufferedImage WEPON_EN = Game.spritesheet.getSprite(80, 0, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(96, 0, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(113, 144, 16, 16);
	public static BufferedImage ARVORE_EN = Game.spritesheet.getSprite(0, 144, 16, 16);
	public static BufferedImage Arma_direita = Game.spritesheet.getSprite(80,16 , 16, 16);
	public static BufferedImage Arma_esquerda = Game.spritesheet.getSprite(96, 16, 16, 16);
	public static BufferedImage Arma_up = Game.spritesheet.getSprite(96, 0, 16, 16);
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	private BufferedImage sprite;
	private int maskx,masky,mwidth,mheight;
	
	public entity (int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height; 
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	public void setX(int newX) {
		this.x= newX;
	}
	public void setY(int newY) {
		this.y= newY;
	}
	
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	//logica da entidade
	public void tick() {
		
		
	}
	
	public static boolean isColidding(entity e1,entity e2 ) {
		Rectangle e1Mask = new Rectangle((int)e1.getX() + e1.maskx, (int)e1.getY()+e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle((int)e2.getX() + e2.maskx, (int)e2.getY()+e2.masky, e2.mwidth, e2.mheight);

		return e1Mask.intersects(e2Mask);
	}
	//renderizar entidade
	public void render(Graphics g){
		g.drawImage(sprite, (int)this.getX() - Camera.x,(int)this.getY() - Camera.y, null);
		//g.setColor(Color.red);
		//g.fillRect((int)this.getX() + this.maskx - Camera.x,(int)this.getY() + this.masky - Camera.y,mwidth,mheight);
	}
}
