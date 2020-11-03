package mx.itesm.equipo5;

import com.badlogic.gdx.graphics.Texture;

public class Tarea extends Objeto{

    public Tarea (Texture textura, float x, float y){
        super(textura, x, y);
    }

    public void moverIzquierda(){
        sprite.setX(sprite.getX()-20);
    }
}
