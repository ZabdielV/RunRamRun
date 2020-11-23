package mx.itesm.RunRamRun;

import com.badlogic.gdx.Gdx;
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

/*

Pantalla encargada de reproducir la historia

 */
public class PantallaStory extends Pantalla {
    private Juego juego;
    private int numeroEscena=1;


    private Texture texturaEscena1,texturaEscena2,texturaEscena3,texturaEscena4;

    //Efecto Sonido
    private Sound efectoClick;


    private Stage escenaMenu;

    public PantallaStory(Juego juego) {
        this.juego=juego;
    }

    @Override
    public void show() {
        texturaEscena1=new Texture("pantallaAyuda/s1.png");
        texturaEscena2=new Texture("pantallaAyuda/s2.png");
        texturaEscena3=new Texture("pantallaAyuda/s3.png");
        texturaEscena4=new Texture("pantallaAyuda/s4.png");

        crearMenu();
        crearAudio();
    }

    private void crearAudio() {
        AssetManager manager=new AssetManager();
        manager.load("sounds/Click.mp3",Sound.class);

        manager.finishLoading();
        efectoClick=manager.get("sounds/Click.mp3");
    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);
        //btnRegresar
        Texture texturaBtnRegresar  =new Texture("botonesMenu/btnRegreso.png");
        TextureRegionDrawable trdBtnRegresar=new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));

        ImageButton btnRegresar=new ImageButton(trdBtnRegresar);
        btnRegresar.setPosition(ANCHO*0.1f,ALTO*0.1f, Align.center);
        //Programar el evento de click
        btnRegresar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                efectoClick.play();
                juego.setScreen(new PantallaAyuda(juego));
            }
        });
        //btnNext
        Texture texturaBtnNext  =new Texture("pantallaAyuda/next.png");
        TextureRegionDrawable trdBtnNext=new TextureRegionDrawable(new TextureRegion(texturaBtnNext));

        Texture texturaBtnNextInverso  =new Texture("pantallaAyuda/nextInverso.png");
        TextureRegionDrawable trdBtnNextInverso=new TextureRegionDrawable(new TextureRegion(texturaBtnNextInverso));

        ImageButton btnNext=new ImageButton(trdBtnNext,trdBtnNextInverso);
        btnNext.setPosition(ANCHO-60,ALTO*0.45f,Align.center);
        //Programar el evento de click
        btnNext.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                efectoClick.play();

                numeroEscena++;
            }
        });
        escenaMenu.addActor(btnNext);
        escenaMenu.addActor(btnRegresar);
        Gdx.input.setInputProcessor(escenaMenu);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        organizarEscenas();

        batch.end();
        escenaMenu.draw();

    }

    private void organizarEscenas() {
        if(numeroEscena==1)
            batch.draw(texturaEscena1,0,0);
        else if(numeroEscena==2){
            batch.draw(texturaEscena2,0,0);
        }
        else if(numeroEscena==3){
            batch.draw(texturaEscena3,0,0);
        }else {
            batch.draw(texturaEscena4,0,0);
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
        texturaEscena1.dispose();
        texturaEscena2.dispose();
        texturaEscena3.dispose();
        texturaEscena4.dispose();
        batch.dispose();
    }
}
