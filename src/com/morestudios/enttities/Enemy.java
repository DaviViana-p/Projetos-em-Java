package com.morestudios.enttities;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.text.html.parser.Entity;

import com.morestudidos.world.World;
import com.morstudios.main.Game;

public class Enemy extends entity{
	private double speed = 0.5;
	private int gravidade = 1;
	
	private int maskx = 3;
	private int masky = 4;
	private int maskw = 10;
	private int maskh = 10;
	private int life = 5;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
	}
	public void tick() {
		//GRAVIDADE
				if(World.isFree((int)x,(int)y+gravidade)) {
					y+=gravidade;
				}
		
		if(this.isColiddingPlayer()== false) {
		if((int)x < Game.player.getX()&& World.isFree((int)(x+speed), (int)this.getY())
		&& !isColidding((int)(x+speed), (int)this.getY())){
			x+=speed;
		}
		else if((int)x > Game.player.getX()&& World.isFree((int)(x-speed), (int)this.getY())
				&& !isColidding((int)(x-speed), (int)this.getY())) {
			x-=speed;
		}
		if((int)y < Game.player.getY()&& World.isFree((int)this.getX(), (int)(y+speed))
				&& !isColidding((int)this.getX(), (int)(y+speed))) {
			y+=speed;
		}
		else if ((int)y > Game.player.getY()&& World.isFree((int)this.getX(), (int)(y-speed))
				&& !isColidding((int)this.getX(), (int)(y-speed))) {
			y-=speed;
		}
		}else {
			///colidindo com player
			if(Game.rand.nextInt(100) < 10) {
			Game.player.life--;
			Game.player.isDemaged = true;
				//System.out.println("vida: "+Game.player.life);
				//if (Player.life <= 0) {
				//	System.exit(1);
				
			}
		}
	
		
		collidingBulet();
		if(life <= 0) {
			destroySelf();
			return;}
	}
		
		private void destroySelf() {
			Game.entities.remove(this);
			Game.enemys.remove(this);
		}

		public void collidingBulet() {
		for(int i =0; i < Game.bullets.size(); i++) {
				BulletShoot e = Game.bullets.get(i);
					if(entity.isColidding(this,e)) {
						life--;
						Game.bullets.remove(i);
						return;
				
					}
				}
		}

	
	public boolean isColiddingPlayer() {
		Rectangle enemyCurrent = new Rectangle((int)this.getX() + maskx, (int) this.getY() + masky,maskw,maskh);
		Rectangle player = new Rectangle((int)Game.player.getX(),(int)Game.player.getY(),16,16);
	
	return enemyCurrent.intersects(player);
	}
	public boolean isColidding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky,maskw,maskh);
				for (int i = 0; i < Game.enemys.size(); i++){
			Enemy e = Game.enemys.get(i);
			if (e == this)
				continue;
			Rectangle targetEnemy = new Rectangle((int)e.getX() + maskx,(int)e.getY()+ masky,maskw,maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	
	}
/*	public void render(Graphics g) {
		super.render(g);
		g.setColor(Color.BLUE);
		g.fillRect((int)this.getX()+ maskx - Camera.x, (int)this.getY()+ masky - Camera.y, maskw, maskh);
	}*/
}
