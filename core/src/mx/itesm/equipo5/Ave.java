package mx.itesm.equipo5;

import com.badlogic.gdx.graphics.Texture;

public class Ave extends Objeto{

    public Ave (Texture textura, float x, float y){
            super(textura, x, y);
    }

    public void moverIzquierda(){
            sprite.setX(sprite.getX()-20);
            sprite.setY(sprite.getY()+((float)Math.sin( (sprite.getX())/140 ))*1.87f);

    }

}
