package mx.itesm.equipo5;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaJugando extends Pantalla {
    private final Juego juego;

    //Boton de pausa
    private Texture btnPausa;

    //Personaje Ramiro
    private mx.itesm.equipo5.Ramiro ramiro;
    private Texture texturaRamiroMov1;

    //Fondo
    private Texture texturaFondo1;
    private Texture texturaFondo1Copy;
    private Texture texturaFondo2;

    private float xFondo;
    private int cambiosFondo=0;//Cuenta las veces que se ha movido el fondo...

    //Estados del juego
    private EstadoJuego estadoJuego = EstadoJuego.JUGANDO;

    //HUD
    private Stage escenaHUD;
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;


    public PantallaJugando(Juego juego){
        this.juego = juego;
    }

    @Override
    public void show() {
        crearHUD();

        texturaFondo1 =new Texture("pantallaJugando/f1.png");
        texturaFondo1Copy =new Texture("pantallaJugando/f1.png");
        texturaFondo2=new Texture("pantallaJugando/f2.png");

        crearRamiro();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

     private void crearHUD() {
        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        camaraHUD.position.set(ANCHO/2, ALTO/2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);

        //Escena
        escenaHUD = new Stage(vistaHUD);

        //Boton Pausa
        btnPausa= new Texture("pantallaJugando/botonPausa.png");
        TextureRegionDrawable regionBtnPausa= new TextureRegionDrawable(new TextureRegion(btnPausa));

        ImageButton btnPausa= new ImageButton(regionBtnPausa);
        btnPausa.setPosition(ANCHO*0.87f, ALTO*.78f);

        escenaHUD.addActor(btnPausa);

    }


    private void crearRamiro() {

        texturaRamiroMov1= new Texture("pantallaJugando/personaje1/movRamiro.png");//Sprite de movimientos
        ramiro=new Ramiro(texturaRamiroMov1,150,50);

    }



    @Override
    public void render(float delta) {

        if (estadoJuego == EstadoJuego.JUGANDO){
            actualizarCamara();
        }


        borrarPantalla(0.2f,0.2f,0.2f);

        batch.setProjectionMatrix(camara.combined);


        batch.begin();

        batch.draw(texturaFondo1,xFondo,0);
        batch.draw(texturaFondo1Copy,xFondo+ texturaFondo1.getWidth(),0);

        ramiro.render(batch);

        batch.end();

        //HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        escenaHUD.draw();

        if (estadoJuego == EstadoJuego.PAUSADO) {
        //Implementar la pausa

        }
    }

    private void actualizarCamara() {

        moverFondo();//Encargado de mover el fondo, sin Tiled
        actualizarRamiro();//Encargado de mover a Ramiro

    }

    private void actualizarRamiro() {
        ramiro.sprite.setX(camara.position.x - ANCHO*0.3f);
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
            Rectangle pausaBotonPausa= new Rectangle(camara.position.x+(ANCHO*0.37f), camara.position.y+(ALTO*.37f),
                     btnPausa.getWidth(),btnPausa.getHeight());

            if(pausaBotonPausa.contains(v.x,v.y)){
                //juego.setScreen(new PantallaPausa(juego));
                estadoJuego = EstadoJuego.PAUSADO;

            } else if (v.x <= camara.position.x && ramiro.getEstado() == EstadoRamiro.CAMINADO &&
            estadoJuego != EstadoJuego.PAUSADO){//(v.x<=ANCHO/2){ //Aqui salta ramiro si se presiona culaquier parte de la pantalla
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

    private enum EstadoJuego {
        JUGANDO,
        PAUSADO,
        REINICIO
    }
}
