package mx.itesm.RunRamRun;

import com.badlogic.gdx.graphics.Texture;

public class Lampara extends Objeto {
    public Lampara (Texture textura, float x, float y){
        super(textura, x, y);
    }

    public void moverIzquierda(){
        sprite.setX(sprite.getX()-20);
    }
}
