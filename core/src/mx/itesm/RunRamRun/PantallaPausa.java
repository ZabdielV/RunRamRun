package mx.itesm.RunRamRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaPausa extends Pantalla{
    private final Juego juego;// Para hacer setScreen

    //Camara, batch, texturas, render (Dibujar), dispose
    //Fondo

    //Menu (Botones)
    private Stage escenaMenu;//Contenedor de objetos (Botones)
    //Fondo
    private Texture texturaFondo;
    public PantallaPausa(Juego juego) {
        this.juego=juego;
    }

    @Override
    public void show() {
        texturaFondo=new Texture("pantallaPausa/fondoP.png");
        crearMenu();
    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);
        //btnContinuar
        Texture texturaBtnContinuar  =new Texture("pantallaPausa/Cont.png");
        TextureRegionDrawable trdBtnContinuar=new TextureRegionDrawable(new TextureRegion(texturaBtnContinuar));
        ImageButton btnContinuar=new ImageButton(trdBtnContinuar);
        btnContinuar.setPosition(640,ALTO-200, Align.center);

        //btnReset
        Texture texturaBtnReset  =new Texture("pantallaPausa/Reset.png");
        TextureRegionDrawable trdBtnReset=new TextureRegionDrawable(new TextureRegion(texturaBtnReset));
        ImageButton btnReset=new ImageButton(trdBtnReset);
        btnReset.setPosition(640,ALTO-313, Align.center);

        //btnConfig
        Texture texturaBtnConfig =new Texture("pantallaPausa/Config.png");
        TextureRegionDrawable trdBtnConfig=new TextureRegionDrawable(new TextureRegion(texturaBtnConfig));
        ImageButton btnConfig=new ImageButton(trdBtnConfig);
        btnConfig.setPosition(640,ALTO-426, Align.center);

        //btnSalir
        Texture texturaBtnSalir  =new Texture("pantallaPausa/Salir.png");
        TextureRegionDrawable trdBtnSalir=new TextureRegionDrawable(new TextureRegion(texturaBtnSalir));
        ImageButton btnSalir=new ImageButton(trdBtnSalir);
        btnSalir.setPosition(640,ALTO-539, Align.center);

        //Listeners
        btnContinuar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                juego.setScreen(new PantallaJugando(juego));
            }
        });
        btnReset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                juego.setScreen(new PantallaJugando(juego));
            }
        });
        btnConfig.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                juego.setScreen(new PantallaConfig(juego));
            }
        });
        btnSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaMenu(juego));

            }
        });
        escenaMenu.addActor(btnContinuar);
        escenaMenu.addActor(btnReset);
        escenaMenu.addActor(btnConfig);
        escenaMenu.addActor(btnSalir);
        Gdx.input.setInputProcessor(escenaMenu);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(texturaFondo,0,0);
        //   batch.draw(titulo,ANCHO/2-(titulo.getWidth()/2),ALTO-100);
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
        // titulo.dispose();
        batch.dispose();
    }
}
