package mx.itesm.equipo5;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaJugando extends Pantalla {
    private final Juego juego;

    private Texture texturaFondo;

    private Stage escenaMenu;//Contenedor de objetos (Botones)

    //Personaje
    //Ramiro
    private mx.itesm.equipo5.Ramiro ramiro;
    private Texture texturaRamiroMov1,texturaRamiroMov2,texturaRamiroMov3,texturaRamiroSalto,texturaRamiroAgachado;
    private float timerAnimaRamiro;
    private final float TIEMPO_FRAME_Ramiro=1;
    private float tiempoAgachado=0;
    private float tiempoSaltando=0;

    //botonAgachado
    private Texture texturaBtnAgachado;


    public PantallaJugando(Juego juego){
        this.juego=juego;
    }

    @Override
    public void show() {
        texturaFondo=new Texture("pantallaJugando/fondo1.png");
        crearMenu();//Inicializa los botones
        crearRamiro();
        crearBotonDisparo();
        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearBotonDisparo() {
        texturaBtnAgachado=new Texture("pantallaJugando/botonAbajo.png");
    }

    private void crearRamiro() {
        texturaRamiroMov1= new Texture("pantallaJugando/personaje1/mov1.png");
        texturaRamiroMov2= new Texture("pantallaJugando/personaje1/mov2.png");
        texturaRamiroMov3= new Texture("pantallaJugando/personaje1/mov3.png");
        texturaRamiroSalto= new Texture("pantallaJugando/personaje1/salto.png");
        texturaRamiroAgachado= new Texture("pantallaJugando/personaje1/agachado.png");
        ramiro=new Ramiro(texturaRamiroMov1,texturaRamiroMov2,texturaRamiroMov3,texturaRamiroSalto,texturaRamiroAgachado,150,50);
    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);
        //btnJugar
        Texture texturaBtnPausa=new Texture("pantallaJugando/botonPausa.png");
        TextureRegionDrawable trdBtnPausa=new TextureRegionDrawable(new TextureRegion(texturaBtnPausa));

        //Inicializar boton Pausa
        ImageButton btnPausa=new ImageButton(trdBtnPausa);
        btnPausa.setPosition(ANCHO-100,ALTO-100, Align.center);

        //Programar el evento de click
        btnPausa.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                //Cambiamos de pantalla (el objeto juego, setScreen)
             //   juego.setScreen(new PantallaPausa(juego));
            }
        });


       // escenaMenu.addActor(btnPausa);
        Gdx.input.setInputProcessor(escenaMenu);
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0.2f,0.2f,0.2f);
        batch.setProjectionMatrix(camara.combined);

        actualizarRamiro(delta);

        batch.begin();
        batch.draw(texturaFondo,0,0);
        batch.draw(texturaBtnAgachado,ANCHO-texturaBtnAgachado.getWidth()*2f,ALTO*0.2f);
        ramiro.render(batch);


        batch.end();




        escenaMenu.draw();
    }

    private void actualizarRamiro(float delta) {
        timerAnimaRamiro += delta;
        if (ramiro.getEstado() == mx.itesm.equipo5.EstadoRamiro.AGACHADO) {//Se establece un tiempo de 1 segundo agachado
            tiempoAgachado += delta;
        }
        if (ramiro.getEstado() == mx.itesm.equipo5.EstadoRamiro.SALTO) {//Se establece un timpo de 1s saltando
            tiempoSaltando += delta;
            if (tiempoSaltando>=0&&tiempoSaltando<TIEMPO_FRAME_Ramiro/4) {
                ramiro.moverArriba();
            }
            else if (tiempoSaltando>=TIEMPO_FRAME_Ramiro/4&&tiempoSaltando<TIEMPO_FRAME_Ramiro/2){
                ramiro.moverAbajo();
            }
        }
            if (timerAnimaRamiro >= TIEMPO_FRAME_Ramiro / 4) {
                //Cambiar de estado
                if (ramiro.getEstado() == mx.itesm.equipo5.EstadoRamiro.MOV1) {
                    ramiro.setEstado(mx.itesm.equipo5.EstadoRamiro.MOV2);
                } else if (ramiro.getEstado() == mx.itesm.equipo5.EstadoRamiro.MOV2) {
                    ramiro.setEstado(mx.itesm.equipo5.EstadoRamiro.MOV3);
                } else if (ramiro.getEstado() == mx.itesm.equipo5.EstadoRamiro.MOV3) {
                    ramiro.setEstado(mx.itesm.equipo5.EstadoRamiro.MOV1);
                } else if (ramiro.getEstado() == mx.itesm.equipo5.EstadoRamiro.AGACHADO && tiempoAgachado >= TIEMPO_FRAME_Ramiro / 2) {
                    ramiro.setEstado(mx.itesm.equipo5.EstadoRamiro.MOV1);
                    tiempoAgachado = 0;
                } else if (ramiro.getEstado() == mx.itesm.equipo5.EstadoRamiro.SALTO && tiempoSaltando >= TIEMPO_FRAME_Ramiro / 2) {
                    ramiro.setEstado(mx.itesm.equipo5.EstadoRamiro.MOV1);
                    tiempoSaltando = 0;
                    ramiro.sprite.setY(50);
                }


                timerAnimaRamiro = 0;//Reinicioa el conteo


            }


        }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaFondo.dispose();

    }

    private class ProcesadorEntrada implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 v = new Vector3(screenX,screenY,0);
            camara.unproject(v);//Transforma las coordenadas...
            //Toco el boton de agachado???
            float xBoton=ANCHO-texturaBtnAgachado.getWidth()*2f;
            float yBoton=ALTO*0.2f;
            float anchBoton=texturaBtnAgachado.getWidth();
            float altBoton=texturaBtnAgachado.getHeight();
            Rectangle rectBotonAgachado= new Rectangle(xBoton,yBoton,anchBoton,altBoton);
            if(rectBotonAgachado.contains(v.x,v.y)){
                //Agachar???

                ramiro.setEstado(mx.itesm.equipo5.EstadoRamiro.AGACHADO);
            }else if (v.x<=ANCHO/2){//Aqui salta ramiro si se presiona culaquier parte de la pantalla
                //SALTO

                ramiro.setEstado(EstadoRamiro.SALTO);
            }


            return true;/////*****//////Si el usuario proceso un evento
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
}
