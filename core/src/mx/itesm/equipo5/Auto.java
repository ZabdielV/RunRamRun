package mx.itesm.equipo5;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Auto extends Objeto {
    private Texture texturaAuto;

    public Auto (Texture textura, float x, float y){
        texturaAuto= textura;
        sprite.setPosition(x,y);
    }

    public void moverseIzquierda(){
        sprite.setX(sprite.getX()-15);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texturaAuto, sprite.getX(), sprite.getY());
    }
}
