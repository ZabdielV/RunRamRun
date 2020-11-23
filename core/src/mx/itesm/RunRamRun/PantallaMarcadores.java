package mx.itesm.RunRamRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaMarcadores extends Pantalla {

    private final Juego juego;// Para hacer setScreen

    //Camara, batch, texturas, render (Dibujar), dispose
    //Fondo

    //Menu (Botones)
    private Stage escenaMenu;//Contenedor de objetos (Botones)

    private Texture texturaFondo,titulo;


    //Efecto Sonido
    private Sound efectoClick;

    //Preferencias
    private Preferences marc1;
    private Preferences marc2;
    private Preferences marc3;
    private int puntos1;
    private int puntos2;
    private int puntos3;

    //Texto
    private Texto texto;

    public PantallaMarcadores(Juego juego) {
        this.juego=juego;
    }


    @Override
    public void show() {
        texturaFondo=new Texture("pantallaMarca/fondo.png");


        crearMenu();
        crearAudio();
        crearTexto();
        cargarPreferencias();
    }

    private void cargarPreferencias() {
        /*Cargar preferencias*/
        marc1=Gdx.app.getPreferences("m1");
        puntos1=marc1.getInteger("m1",0);

        marc2=Gdx.app.getPreferences("m2");
        puntos2=marc2.getInteger("m2",0);

        marc3=Gdx.app.getPreferences("m3");
        puntos3=marc3.getInteger("m3",0);
    }

    private void crearTexto() {
        texto=new Texto("game.fnt");
    }

    private void crearAudio() {
        //Cargar preferencias
        //Preguntar si la preferencia de sonido es true
        AssetManager manager=new AssetManager();
        manager.load("sounds/Click.mp3",Sound.class);

        manager.finishLoading();//Espera a cargar todos los recursos
        efectoClick=manager.get("sounds/Click.mp3");
    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);
        //btnRegresar
        Texture texturaBtnRegresar  =new Texture("botonesMenu/btnRegreso.png");
        TextureRegionDrawable trdBtnRegresar=new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));

        Texture texturaBtnRegresarInverso=new Texture("botonesMenu/btnRegresoInverso.png");
        TextureRegionDrawable trdBtnRegresarInverso=new TextureRegionDrawable(new TextureRegion(texturaBtnRegresarInverso));

        ImageButton btnRegresar=new ImageButton(trdBtnRegresar,trdBtnRegresarInverso);
        btnRegresar.setPosition(btnRegresar.getWidth(),btnRegresar.getHeight(), Align.center);
        //Programar el evento de click
        btnRegresar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                efectoClick.play();
                //Cambiamos de pantalla (el objeto juego, setScreen)
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        escenaMenu.addActor(btnRegresar);
        Gdx.input.setInputProcessor(escenaMenu);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(texturaFondo,0,0);
       dibujarTexto();
        batch.end();
        escenaMenu.draw();
    }

    private void dibujarTexto() {
        texto.mostrarMensaje(batch,"1",ANCHO*0.33f,ALTO*0.7f);
        texto.mostrarMensaje(batch,"2",ANCHO*0.33f,ALTO*0.6f);
        texto.mostrarMensaje(batch,"3",ANCHO*0.33f,ALTO*0.5f);
        texto.mostrarMensaje(batch,""+puntos1,ANCHO*0.57f,ALTO*0.7f);
        texto.mostrarMensaje(batch,""+puntos2,ANCHO*0.57f,ALTO*0.6f);
        texto.mostrarMensaje(batch,""+puntos3,ANCHO*0.57f,ALTO*0.5f);
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
       // titulo.dispose();
        batch.dispose();
    }
}
