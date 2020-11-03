package mx.itesm.equipo5;

import com.badlogic.gdx.graphics.Texture;

public class Pajaro extends Objeto{

    public Pajaro (Texture textura, float x, float y){
        super(textura, x, y);
    }

    public void moverIzquierda(){
        sprite.setX(sprite.getX()-20);
    }
}
