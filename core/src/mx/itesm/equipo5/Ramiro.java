package mx.itesm.equipo5;

import com.badlogic.gdx.graphics.Texture;






public class Ramiro extends Objeto{

    private final float DX_RAMIRO=1;
    private final float DY_RAMIRO=5;
    private Texture texturaMov1;
    private Texture texturaMov2;
    private Texture texturaMov3;
    private Texture texturaSalto;
    private Texture texturaAgachado;

    private EstadoRamiro estado;

    public Ramiro(Texture textura, float x, float y){
        super(textura, x, y);
    }
    public Ramiro(Texture texturaMov1,Texture texturaMov2,Texture texturaMov3,Texture texturaSalto,Texture texturaAgachado, float x, float y){
        super(texturaMov1, x, y);
    this.texturaMov1=texturaMov1;
    this.texturaMov2=texturaMov2;
    this.texturaMov3=texturaMov3;
    this.texturaSalto=texturaSalto;
    this.texturaAgachado=texturaAgachado;
    estado=EstadoRamiro.MOV1;
    }

    public EstadoRamiro getEstado() {
        return estado;
    }

    public void setEstado(EstadoRamiro estado) {
        this.estado = estado;

        switch (estado){
            case MOV1:
                sprite.setTexture(texturaMov1);
                break;
            case MOV2:
                sprite.setTexture(texturaMov2);
                break;
            case MOV3:
                sprite.setTexture(texturaMov3);
                break;
            case AGACHADO:
                sprite.setTexture(texturaAgachado);
                break;
            case SALTO:
                sprite.setTexture(texturaSalto);
                break;


        }
    }

    public void moverDerecha() {
        sprite.setX(sprite.getX()+ DX_RAMIRO);
    }
    public void moverArriba() {
        sprite.setY(sprite.getY()+ DY_RAMIRO);
    }
    public void moverAbajo() {
        sprite.setY(sprite.getY()- DY_RAMIRO);
    }
}
