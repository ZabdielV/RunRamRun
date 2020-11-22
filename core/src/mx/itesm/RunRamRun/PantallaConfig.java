package mx.itesm.RunRamRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaConfig extends Pantalla{
    private final Juego juego;// Para hacer setScreen

    //Camara, batch, texturas, render (Dibujar), dispose
    //Fondo

    //Menu (Botones)
    private Stage escenaMenu;//Contenedor de objetos (Botones)

    private Texture texturaFondo,titulo;
    private Preferences prefs;
    private boolean musicPreference;


    //Efecto Sonido
    private Sound efectoClick;

    public PantallaConfig(Juego juego) {
        this.juego=juego;
    }


    @Override
    public void show() {
        texturaFondo=new Texture("pantallaConfig/fondo.png");

        /*Cargar preferencias*/
        prefs=Gdx.app.getPreferences("music");
        musicPreference=prefs.getBoolean("music",true);
        //titulo=new Texture("titulo.png");
        crearMenu();
        crearAudio();
        Gdx.app.log("prefe","preferencias al inicio: "+musicPreference);
    }

    private void crearAudio() {
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
        //Boton de Musica

        Texture texturaBtnM  =new Texture("pantallaConfig/onn.png");
        TextureRegionDrawable trdBtnM=new TextureRegionDrawable(new TextureRegion(texturaBtnM));

        Texture texturaBtnMOff  =new Texture("pantallaConfig/offf.png");

        TextureRegionDrawable trdBtnMOff=new TextureRegionDrawable(new TextureRegion(texturaBtnMOff));
        final Button.ButtonStyle estilo1=new Button.ButtonStyle(trdBtnM,null,null);
        final Button.ButtonStyle estilo2=new Button.ButtonStyle(trdBtnMOff,null,null);
        final ImageButton.ImageButtonStyle estiloPrendido=new ImageButton.ImageButtonStyle(estilo1);
        final ImageButton.ImageButtonStyle estiloApagado=new ImageButton.ImageButtonStyle(estilo2);
        final ImageButton btnM=new ImageButton(trdBtnM);

        if(musicPreference){
            btnM.setStyle(estiloPrendido);
        }
        if(!musicPreference) {
            btnM.setStyle(estiloApagado);
        }

        btnM.setPosition(ANCHO*0.3f,ALTO*0.35f);
        btnM.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                efectoClick.play();
                if(musicPreference){
                    btnM.setStyle(estiloApagado);
                    prefs.putBoolean("music",false);
                    musicPreference=false;


                }
                else {
                    btnM.setStyle(estiloPrendido);
                    prefs.putBoolean("music",true);
                    musicPreference=true;
                }
                prefs.flush();
                Gdx.app.log("prefe","preferencias al tocar boton: "+prefs.getBoolean("music"));

            }
        });

        escenaMenu.addActor(btnM);

        escenaMenu.addActor(btnRegresar);

        Gdx.input.setInputProcessor(escenaMenu);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(texturaFondo,0,0);
        // batch.draw(titulo,ANCHO/2-(titulo.getWidth()/2),ALTO-100);
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
