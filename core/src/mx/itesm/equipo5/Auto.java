package mx.itesm.equipo5;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Auto extends Objeto {


    public Auto (Texture textura, float x, float y){
    super(textura, x, y);
    }

    public void moverIzquierda(){
        sprite.setX(sprite.getX()-20);
    }


}
