package mx.itesm.RunRamRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class PantallaMenu extends Pantalla {
    private final Juego juego;// Para hacer setScreen

    //Camara, batch, texturas, render (Dibujar), dispose
    //Fondo

    //Menu (Botones)
    private Stage escenaMenu;//Contenedor de objetos (Botones)

    //Imagenes
    private Texture texturaFondo;

    //Efecto Sonido
    private Sound efectoClick;

    public PantallaMenu(Juego juego) {
        this.juego=juego;
    }



    @Override
    public void show() {
        texturaFondo=new Texture("fondoNuevo.png");

       //titulo=new Texture("titulo.png");
        crearMenu();
        crearAudio();
        Gdx.input.setCatchKey(Input.Keys.BACK,false);
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
        //btnJugar
        Texture texturaBtnJugar=new Texture("botonesMenu/btnJugar.png");
        TextureRegionDrawable trdBtnJugar=new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));
        //InversoBotonJugar
        Texture texturaBtnJugarInverso=new Texture("botonesMenu/btnJugarInverso.png");
        TextureRegionDrawable trdBtnJugarInverso=new TextureRegionDrawable(new TextureRegion(texturaBtnJugarInverso));
        //Inicializar boton Jugar
        ImageButton btnJugar=new ImageButton(trdBtnJugar,trdBtnJugarInverso);
        btnJugar.setPosition(ANCHO/2+200,ALTO-130, Align.center);

        //btnMarcadores*************
        Texture texturaBtnMarcadores=new Texture("botonesMenu/btnMarca.png");
        TextureRegionDrawable trdBtnMarcadores=new TextureRegionDrawable(new TextureRegion(texturaBtnMarcadores));

        Texture texturaBtnMarcaInverso=new Texture("botonesMenu/btnMarcaInverso.png");
        TextureRegionDrawable trdBtnMarcaInverso=new TextureRegionDrawable(new TextureRegion(texturaBtnMarcaInverso));
        //Inicializar boton Marcadores
        ImageButton btnMarcadores=new ImageButton(trdBtnMarcadores,trdBtnMarcaInverso);
        btnMarcadores.setPosition(ANCHO/2+200,ALTO-290, Align.center);

        //btnAbout
        Texture texturaBtnAcerca =new Texture("botonesMenu/btnAbout.png");
        TextureRegionDrawable trdBtnAcerca=new TextureRegionDrawable(new TextureRegion(texturaBtnAcerca));

        Texture texturaBtnAboutInverso=new Texture("botonesMenu/btnAboutInverso.png");
        TextureRegionDrawable trdBtnAboutInverso=new TextureRegionDrawable(new TextureRegion(texturaBtnAboutInverso));

        //Inicializar boton About
        ImageButton btnAcerca=new ImageButton(trdBtnAcerca,trdBtnAboutInverso);
        btnAcerca.setPosition(texturaBtnAcerca.getWidth(),texturaBtnAcerca.getHeight()-55, Align.center);

        //btnConfig
        Texture texturaBtnConfig=new Texture("botonesMenu/btnConfig.png");
        TextureRegionDrawable trdBtnConfig=new TextureRegionDrawable(new TextureRegion(texturaBtnConfig));

        Texture texturaBtnConfigInverso=new Texture("botonesMenu/btnConfigInverso.png");
        TextureRegionDrawable trdBtnConfigInverso=new TextureRegionDrawable(new TextureRegion(texturaBtnConfigInverso));

        //Inicializar boton Config
        ImageButton btnConfig=new ImageButton(trdBtnConfig,trdBtnConfigInverso);
        btnConfig.setPosition(ANCHO-texturaBtnConfig.getWidth()+30,texturaBtnConfig.getHeight()-20, Align.center);

        //btnHowToPlay
        Texture texturaBtnAyuda=new Texture("botonesMenu/btnHow.png");
        TextureRegionDrawable trdBtnAyuda=new TextureRegionDrawable(new TextureRegion(texturaBtnAyuda));

        Texture texturaBtnAyudaInverso=new Texture("botonesMenu/btnHowInverso.png");
        TextureRegionDrawable trdBtnAyudaInverso=new TextureRegionDrawable(new TextureRegion(texturaBtnAyudaInverso));

        //Inicializar boton HowToPlay
        ImageButton btnAyuda=new ImageButton(trdBtnAyuda,trdBtnAyudaInverso);
        btnAyuda.setPosition(ANCHO*0.52f+200,ALTO-450, Align.center);

        //Programar el evento de click
        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                efectoClick.play();
                //Cambiamos de pantalla (el objeto juego, setScreen)
                juego.setScreen(new PantallaCargando(juego, Pantallas.JUGANDO));
            }
        });

        btnMarcadores.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                efectoClick.play();
                //Cambiamos de pantalla (el objeto juego, setScreen)
                juego.setScreen(new PantallaMarcadores(juego));
            }
        });

        btnAcerca.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                efectoClick.play();
                //Cambiamos de pantalla (el objeto juego, setScreen)
                juego.setScreen(new PantallaAcerca(juego));
            }
        });
        btnAyuda.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                efectoClick.play();
                //Cambiamos de pantalla (el objeto juego, setScreen)
                juego.setScreen(new PantallaAyuda(juego));
            }
        });

        btnConfig.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                efectoClick.play();
                //Cambiamos de pantalla (el objeto juego, setScreen)
                juego.setScreen(new PantallaConfig(juego));
            }
        });
        escenaMenu.addActor(btnJugar);
        escenaMenu.addActor(btnMarcadores);
        escenaMenu.addActor(btnAcerca);
        escenaMenu.addActor(btnConfig);
        escenaMenu.addActor(btnAyuda);

        Gdx.input.setInputProcessor(escenaMenu);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(texturaFondo,0,0);
        //batch.draw(titulo,ANCHO/2-(titulo.getWidth()*0.5f),ALTO-170);
        batch.end();
        escenaMenu.draw();

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
        //titulo.dispose();
        batch.dispose();
    }
}
