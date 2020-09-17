package mx.itesm.equipo5;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*
//Representa Cualquier elemento grafico que esta en pantalla

 */
public class Objeto {
    protected Sprite sprite;//Las subclasses pueden acceder/modificar directamente el Sprite.

    public Objeto(Texture textura,float x,float y){
        sprite=new Sprite(textura);
        sprite.setPosition(x,y);

    }
    public Objeto(){

    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);

    }
}
