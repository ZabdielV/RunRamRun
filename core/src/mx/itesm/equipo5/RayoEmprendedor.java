package mx.itesm.equipo5;

import com.badlogic.gdx.graphics.Texture;

public class RayoEmprendedor extends Objeto{

    public RayoEmprendedor(Texture textura, float x, float y) {


        super(textura, x, y);
    }

    public void moverIzquierda() {

        sprite.setX(sprite.getX() - 20);
    }



}
