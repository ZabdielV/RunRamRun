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


    //Boton de pausa
    private Texture btnPausa;
    //
    //Personaje Ramiro
    private mx.itesm.equipo5.Ramiro ramiro;
    private Texture texturaRamiroMov1;

    //Fondo
    private Texture texturaFondo1;
    private Texture texturaFondo1Copy;
    private Texture texturaFondo2;

    private float xFondo=0;
    private int cambiosFondo=0;//Cuenta las veces que se ha movido el fondo...

    public PantallaJugando(Juego juego){
        this.juego=juego;
    }

    @Override
    public void show() {
        texturaFondo1 =new Texture("pantallaJugando/f1.png");
        texturaFondo1Copy =new Texture("pantallaJugando/f1.png");
        texturaFondo2=new Texture("pantallaJugando/f2.png");
        crearBotones();

        crearRamiro();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearBotones() {
        btnPausa=new Texture("pantallaJugando/botonPausa.png");
    }


    private void crearRamiro() {
        texturaRamiroMov1= new Texture("pantallaJugando/personaje1/movRamiro.png");//Sprite de movimientos


        ramiro=new Ramiro(texturaRamiroMov1,150,50);
    }



    @Override
    public void render(float delta) {
        actualizar();

        borrarPantalla(0.2f,0.2f,0.2f);
        batch.setProjectionMatrix(camara.combined);



        batch.begin();
        batch.draw(texturaFondo1,xFondo,0);
        batch.draw(texturaFondo1Copy,xFondo+ texturaFondo1.getWidth(),0);
        batch.draw(btnPausa,ANCHO-100-btnPausa.getWidth()*0.5f,ALTO-100-btnPausa.getHeight()*0.5f);
        //batch.draw(texturaBtnAgachado,ANCHO-texturaBtnAgachado.getWidth()*2f,ALTO*0.2f);//El boton de agachar
        ramiro.render(batch);

        batch.end();


    }

    private void actualizar() {
        moverFondo();//Encargado de mover el fondo
        actualizarRamiro();//Encargado de mover a Ramiro, No se usa
    }

    private void moverFondo() {//Mueve y cambia los fondos
        xFondo-=10;
        if(xFondo==-texturaFondo1.getWidth()){
            xFondo=0;
            cambiosFondo++;
            //Gdx.app.log("cambios","cambios "+cambiosFondo);
        }
        if(cambiosFondo>=5){//Encargardo de cargar el fondo 2
            texturaFondo1Copy=texturaFondo2;

        }
        if(cambiosFondo>=6){
            texturaFondo1=texturaFondo2;

        }

    }

    private void actualizarRamiro() {
        //ramiro.sprite.setX(ramiro.sprite.getX()+5);
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaFondo1.dispose();

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
            //boton de Pausa
            float xBoton=ANCHO-100-btnPausa.getWidth()*0.5f;
            float yBoton=ALTO-100-btnPausa.getHeight()*0.5f;
            float anchBoton=btnPausa.getWidth();
            float altBoton=btnPausa.getHeight();
            Rectangle pausaBoton= new Rectangle(xBoton,yBoton,anchBoton,altBoton);
            if(pausaBoton.contains(v.x,v.y)){
                juego.setScreen(new PantallaPausa(juego));
            }
             else if (v.x<=ANCHO/2 &&ramiro.getEstado()==EstadoRamiro.CAMINADO){//Aqui salta ramiro si se presiona culaquier parte de la pantalla
                //SALTO

                ramiro.setEstado(EstadoRamiro.SALTANDO);
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
