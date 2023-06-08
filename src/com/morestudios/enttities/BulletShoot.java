package com.morestudios.enttities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.morestudidos.world.Camera;
import com.morstudios.main.Game;

public class BulletShoot extends entity {
	private int dx;
	private int dy;
	private double spd = 4;
	
	private int life = 20,curLife =0;

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
		// TODO Auto-generated constructor stub
	}

	
	public void tick() {
		x+=dx*spd;
		y+=dy*spd;
		life--;
		if(curLife == life) {
			Game.bullets.remove(this);
			return;
			}
		}
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval((int)this.getX() - Camera.x, (int)this.getY() - Camera.y,width,height);
	}
	
}
