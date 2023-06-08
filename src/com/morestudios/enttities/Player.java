package com.morestudios.enttities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.morestudidos.world.Camera;
import com.morestudidos.world.World;
import com.morestudios.graficos.Spritesheet;
import com.morstudios.main.Game;

public class Player extends entity {
	public boolean right,up,left,down;
	public int right_dir = 0,left_dir = 1;
	public int dir = right_dir;
	public int up_dir = 2;
	public int down_dir = 3;
	public int speed =1	;
	public static int gravidade = 2;
	private BufferedImage rightPlayer[];
	private BufferedImage leftPlayer[];
	private BufferedImage upPlayer[];
	private BufferedImage downPlayer[];
	private BufferedImage playerDemage;
	private int frames =0,maxFrames = 5,index = 0, maxIndex = 5;
	private boolean moved;
	public  double life = 100, maxLife = 100;
	public int municao = 0;

	public boolean isDemaged =  false;
	private int demageFrames = 0;
	
	public boolean Shoot = false;
	
	private boolean temArma = false;
	public boolean jump = false;
	public boolean isJumping = false;
	public int jumpHeight = 32;
	public int jumpFrames = 0;
	public int jumpAcceleration =2;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPlayer = new BufferedImage[5];
		leftPlayer = new BufferedImage[5];
		upPlayer = new BufferedImage[5];
		downPlayer = new BufferedImage[5];
		playerDemage = Game.spritesheet.getSprite(0, 96, 16, 16);
		for(int i =0; i <5; i++){
			upPlayer[i] = Game.spritesheet.getSprite(0+(i*16), 16, 16, 16);}
		for(int i =0; i <5; i++){
			downPlayer[i] = Game.spritesheet.getSprite(0+(i*16), 0, 16, 16);}
		for(int i =0; i <5; i++){
			rightPlayer[i] = Game.spritesheet.getSprite(0+(i*16), 32, 16, 16);}
		for(int i =0; i <5; i++){
			leftPlayer[i] = Game.spritesheet.getSprite(0+(i*16), 48, 16, 16);}
	}
	public void tick() {
		//GRAVIDADE
				if(World.isFree((int)x,(int)y+speed)&& isJumping == false) {
					y+=gravidade;
				}
				//pulando
				if(jump) {
					if(!World.isFree((int)this.getX(), (int)this.getY() +1)) {
						isJumping = true;
				}else {
					jump = false;
				}
			}
				if(isJumping) {
					if(World.isFree((int)this.getX(), (int)this.getY()-2)) {
						y-=2;
						jumpFrames+=2;
						if(jumpFrames == jumpHeight) {
							isJumping = false;
							jump = false;
							jumpFrames = 0;
						}
						
					}else {
						isJumping = false;
						jump = false;
						jumpFrames = 0;
					}
				}
				
	

		//MOVIMENTO
		moved = false;
		if(right && World.isFree((int)(x+speed), (int)this.getY())) {
			moved = true;
			dir = right_dir;		
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed), (int)this.getY())){
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		if(up && World.isFree((int)this.getX(), (int)(y- speed))){
			dir = up_dir;
			moved = true;
			y-=speed;
		}
		else if (down && World.isFree((int)this.getX(), (int)(y+ speed))){ 
			dir = down_dir;
			moved = true;
			y+=speed;
		}
	
//MOVENDO
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index == maxIndex) {
					index =0;
				}
				
			}
			
		}
		//COLISAO
		checkCollisionArma();
		checkCollisionLifePack();
		checkCollisionMunicao();
		
		if(isDemaged) {
			this.demageFrames++;
			if(this.demageFrames == 15)
				this.demageFrames = 0;
				isDemaged = false;
			}
		
		// ATIRANDO
		if(Shoot) {
			Shoot = false;		
			if(temArma && municao > 0) {
				municao--;
			int dx = 0;
			int px = 0;
			int py = 3;
			if (dir == right_dir) {
				px = 17;
				dx = 1;
			}else {
				px = -9;
				dx =-1;
			}
			
			BulletShoot Bullet = new BulletShoot((int)this.getX() + px,(int)this.getY() + py,3,3,null,dx,0);
			Game.bullets.add(Bullet);
		}
		}
		
		//RESTARTE GAME
		if(life<=0) {
			Game.entities = new ArrayList<entity>();
			Game.enemys = new ArrayList<Enemy>();
			Game.spritesheet = new Spritesheet("/Spritesheet.png");
			Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(0, 0, 16, 16));
			Game.entities.add(Game.player);	
			Game.world = new World("/map1.png");
			return;
		}
			
		//CAMERA PLAYER
		Camera.x = Camera.clamp((int)this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp((int)this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
			
	}
	
	//METODOS DE COLISAO
	public void checkCollisionMunicao() {
		for(int i = 0; i <Game.entities.size(); i++) {
			entity e = Game.entities.get(i);
			if (e instanceof Bullet) {
				if(entity.isColidding(this,e)) {
					municao+=10;
					//System.out.println("municao:"+ municao);
						Game.entities.remove(i);
						return;
					}
				}
			}
		}
	
	public void checkCollisionLifePack() {
		for(int i = 0; i <Game.entities.size(); i++) {
			entity e = Game.entities.get(i);
			if (e instanceof LifePack) {
				if(entity.isColidding(this,e)) {
					life+=10;
					if(life >= 100) 
						life=100;
						Game.entities.remove(i);
						return;
					}
				}
			}
		}
	public void checkCollisionArma() {
		for(int i = 0; i <Game.entities.size(); i++) {
			entity e = Game.entities.get(i);
			if (e instanceof Weppon) {
				if(entity.isColidding(this,e)) {
					temArma = true;
						Game.entities.remove(i);
						return;
					}
				}
			}
		}
	
	//REENDERIZANDO PLAYER
	public void render(Graphics g) {
		if(!isDemaged) {
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index],(int)this.getX() - Camera.x,(int)this.getY() - Camera.y, null);
				if(temArma) {
					//arma pra direita
					g.drawImage(entity.Arma_direita,(int)this.getX()+6 - Camera.x,(int)this.getY()-2 - Camera.y, null);
				}
			}else if(dir == left_dir) {
				g.drawImage(leftPlayer[index],(int)this.getX() - Camera.x,(int)this.getY() - Camera.y, null);
				if(temArma) {
					//arma pra esquerda
					g.drawImage(entity.Arma_esquerda,(int)this.getX()-12 - Camera.x,(int)this.getY()-2 - Camera.y, null);
				}
			}else if(dir == up_dir) {
			g.drawImage(upPlayer[index],(int)this.getX() - Camera.x,(int)this.getY() - Camera.y, null);
			if(temArma) {
				//arma pra cima(ainda por fazer
				g.drawImage(entity.Arma_up,(int)this.getX()-7 - Camera.x,(int)this.getY()+2 - Camera.y, null);
			}
		}
			else if(dir == down_dir) {
				g.drawImage(downPlayer[index],(int)this.getX() - Camera.x,(int)this.getY() - Camera.y, null);
				if(temArma) {
					//arma pra down(ainda por fazer)
					g.drawImage(entity.Arma_esquerda,(int)this.getX()-7 - Camera.x,(int)this.getY()+2 - Camera.y, null);
				}
			}
		
		}
		//RENDER PLAYER LEVANDO DANO
		else {
			g.drawImage(playerDemage, (int)this.getX() - Camera.x,(int)this.getY() - Camera.y, null);
			if(dir == right_dir) {
				if(temArma) {
					//arma pra direita
					g.drawImage(entity.Arma_direita,(int)this.getX()+2 - Camera.x,(int)this.getY()+2 - Camera.y, null);
			}}else if(dir == left_dir) {
				if(temArma) {
					//arma pra esquerda
					g.drawImage(entity.Arma_esquerda,(int)this.getX()-7 - Camera.x,(int)this.getY()+2 - Camera.y, null);
				}
			}
		}
	}
}


