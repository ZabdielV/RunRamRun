package mx.itesm.equipo5;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ramiro extends Objeto{

    private Animation<TextureRegion> animacion;//Los frames del personaje...
    private float timerAnimacion;
    private TextureRegion[][] texturasFrames;


    private Texture texturaAgachado;//Agachado texture

    //Salto
    private float yBase;   //Y del piso
    private float tAire; //Tiempo de simulacion <= tiempo de vuelo, entoncs se detiene
    private final float V0=100;
    private final float G=20;
    private float tVuelo;

    private EstadoRamiro estado;

    public Ramiro(Texture textura, float x, float y){
        TextureRegion region=new TextureRegion(textura);//La textura es toda la imagen, la region divide la imagen en frames
        texturasFrames=region.split(155,290);//Una matriz de regiones, seleccion de uan region...

        sprite=new Sprite(texturasFrames[0][4]);//Salto
        sprite.setPosition(x, y);


        TextureRegion[] arrFrames= {texturasFrames[0][4],texturasFrames[0][3],texturasFrames[0][2],texturasFrames[0][1]};
        animacion=new Animation<TextureRegion>(0.1f,arrFrames);//10 cuadros por segundo por que cada uno dura 0.1f
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion=0;

        //SALTO
        yBase=y;
        estado=EstadoRamiro.CAMINADO;
    }



    public EstadoRamiro getEstado() {
        return estado;
    }

    public void setEstado(EstadoRamiro estado) {
        this.estado = estado;
        if(estado==EstadoRamiro.SALTANDO){
            tAire=0;
            tVuelo=2*V0/G;//Permanece en el aire
        }
    }

    public void render(SpriteBatch batch){
        float delta= Gdx.graphics.getDeltaTime();//1/60
        if(estado==EstadoRamiro.CAMINADO){
            timerAnimacion+=delta;//Acumula
            TextureRegion frame=animacion.getKeyFrame(timerAnimacion);//Calcula en base al tiempo el frame correspondiente
            batch.draw(frame,sprite.getX(),sprite.getY());
        }if(estado==EstadoRamiro.SALTANDO){

            tAire+=10*delta;
            float y=yBase+V0*tAire-(0.5f)*G*(tAire*tAire);
            sprite.setY(y);
            super.render(batch);
            if(tAire>=tVuelo){
                sprite.setY(yBase);
                estado=EstadoRamiro.CAMINADO;
                timerAnimacion=0;
            }
        }


    }


}
