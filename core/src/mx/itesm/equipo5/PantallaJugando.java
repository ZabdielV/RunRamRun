package mx.itesm.equipo5;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaJugando extends Pantalla {
    private final Juego juego;

    //Boton de pausa
    private Texture btnPausa;

    //Barra de corazones: Salud
    private Array<Texture> arrCorazones;
    //Vidas
    private int vidas=3;

    //Personaje Ramiro
    private mx.itesm.equipo5.Ramiro ramiro;
    private Texture texturaRamiroMov1;

    //Fondos
    private Texture texturaFondo1;
    private Texture texturaFondoCopy;
    private Texture texturaFondo2;
    private Texture texturaFondo3;
    private Texture texturaFondo4;
    private Texture texturaFondo5;
    //Transiciones
    //private Texture ruralUrbano;
    private boolean transicionUrbanaRural= false;

    //Efecto sonido
    private Sound efectoClick;
    private Sound efetoSalto;

    //Texto
    private Texto texto;

    //Puntos  //Los puntos que gana en el marcador. MARCADOR
    private float puntos= 0;//

    private float xFondo;
    private int cambiosFondo=0;//Cuenta las veces que se ha movido el fondo...

    //Estados del juego
    private EstadoJuego estadoJuego = EstadoJuego.JUGANDO;

    //Pausa
    private EscenaPausa escenaPausa;

    //HUD
    private Stage escenaHUD;
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;

    // Musica
    private Music musicaFondo;

    //Enemigos
    private Texture texturaCamioneta;
    private Texture texturaCocheLujo;
    private float posicionAuto= ANCHO;
    private Texture texturaCarrito;

    private float timerCreacionEnemigo;
    private final float TIEMPO_CREACION_ENEMIGO = 30;
    private boolean cicloTimer= true;

    public PantallaJugando(Juego juego){
        this.juego = juego;
    }

    @Override
    public void show() {
        crearHUD();
        crearFondos();
        crearRamiro();
        crearTexto();
        crearAudio();
        crearCorazones();
        crearEnemigos();

        //Boton de pausa
        btnPausa=new Texture("pantallaJugando/botonPausa.png");

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearEnemigos() {
            texturaCamioneta = new Texture("pantallaJugando/Enemigos/camioneta.png");
            texturaCocheLujo = new Texture("pantallaJugando/Enemigos/carro.png");

        //texturaCarrito = new Texture("pantallaJugando/Enemigos/camioneta.png"); Cuando se implemente escuela
    }

    private void crearCorazones() {
        arrCorazones=new Array<>(vidas);
        for (int i = 0; i <vidas ; i++) {
            Texture corazones=new Texture("pantallaJugando/corazon.png");
            arrCorazones.add(corazones);
        }

    }

    private void crearAudio() {
        AssetManager manager=new AssetManager();
        manager.load("sounds/Gameplay.mp3",Music.class);
        manager.load("sounds/Click.mp3",Sound.class);
        manager.load("sounds/Salto.mp3",Sound.class);

        manager.finishLoading();//Espera a cargar todos los recursos
        musicaFondo = manager.get("sounds/Gameplay.mp3");
        musicaFondo.setVolume(0.2f);
        musicaFondo.setLooping(true);
        musicaFondo.play();
        efectoClick=manager.get("sounds/Click.mp3");
        efetoSalto=manager.get("sounds/Salto.mp3");
    }

    private void crearTexto() {
        texto=new Texto("game.fnt");
    }

    private void crearFondos() {
        texturaFondo1 =new Texture("pantallaJugando/Mapas/Rural/nivelRural1_1.png");
        texturaFondo2=new Texture("pantallaJugando/Mapas/Rural/nivelRural1_2.png");
        texturaFondo3=new Texture("pantallaJugando/Mapas/Rural/nivelRural1_3.png");
        texturaFondo4=new Texture("pantallaJugando/Mapas/Rural/nivelRural1_4.png");
        texturaFondo5=new Texture("pantallaJugando/Mapas/Rural/nivelRural1_5.png");

        texturaFondoCopy= texturaFondo1;

    }

    private void crearHUD() {//Pausa
        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        camaraHUD.position.set(ANCHO/2, ALTO/2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);

        //Escena
        //escenaHUD = new Stage(vistaHUD);

        //Boton Pausa
        /*
        btnPausa= new Texture("pantallaJugando/botonPausa.png");
        TextureRegionDrawable regionBtnPausa= new TextureRegionDrawable(new TextureRegion(btnPausa));

        ImageButton btnPausa= new ImageButton(regionBtnPausa);
        btnPausa.setPosition(ANCHO*0.87f, ALTO*.78f);

        escenaHUD.addActor(btnPausa);
        */


    }

    private void crearRamiro() {
        texturaRamiroMov1= new Texture("pantallaJugando/personaje1/movRamiro.png");//Sprite de movimientos
        ramiro=new Ramiro(texturaRamiroMov1,150,50);
    }

    @Override
    public void render(float delta) {

        borrarPantalla(0.2f,0.2f,0.2f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturaFondo1,xFondo,0);
        batch.draw(texturaFondoCopy,xFondo+ texturaFondo1.getWidth(),0);

        if (estadoJuego == EstadoJuego.JUGANDO){
            actualizar();
            dibujarTexto();
            dibujarCorazones();
            dibujarEnemigos();
            ramiro.render(batch);
            batch.draw(btnPausa,ANCHO-100-btnPausa.getWidth()*0.5f,ALTO-100-btnPausa.getHeight()*0.5f);
        }

        batch.end();

        //HUD
        // batch.setProjectionMatrix(camaraHUD.combined);
        //escenaHUD.draw();

        if (estadoJuego == EstadoJuego.PAUSADO) {   //Implementar la pausa


        batch.setProjectionMatrix(camaraHUD.combined);
            escenaPausa.draw();

            batch.begin();
            batch.draw(escenaPausa.pausa,ANCHO*0.4f,ALTO*0.90f);

            batch.end();
        }
    }

    private void dibujarEnemigos() {
        if (cambiosFondo <25 && transicionUrbanaRural == false){
            batch.draw(texturaCamioneta, posicionAuto,50);
        } else {
            batch.draw(texturaCocheLujo, posicionAuto, 50);
        }
    }

    private void dibujarCorazones() {
        for (int i = 0; i <arrCorazones.size; i++) {
            batch.draw(arrCorazones.get(i),ANCHO*0.02f+i*105,ALTO*0.8f);
        }

    }

    private void dibujarTexto() {
        int puntosInt=(int)puntos;
        texto.mostrarMensaje(batch,"Points: "+puntosInt,ANCHO*0.6f+80,ALTO*0.9f+20);
    }

    private void actualizar() {
        moverFondo();
        actualizarEnemigos();
    }

    private void actualizarEnemigos() {
        if (cicloTimer == true){
            actualizarPosicionAuto();
        }
        timerCreacionEnemigo+= Gdx.graphics.getDeltaTime();
        if (timerCreacionEnemigo >= TIEMPO_CREACION_ENEMIGO){
            timerCreacionEnemigo = 0;
            posicionAuto = ANCHO;
            cicloTimer = true;
        }
        if (cambiosFondo <25 && transicionUrbanaRural == false){
            if (posicionAuto + texturaCamioneta.getWidth() < 0){
                posicionAuto = ANCHO+ 5;
                cicloTimer=false;
            }
        } else {
            if (cambiosFondo> 25 && posicionAuto + texturaCocheLujo.getWidth() < 0){
                posicionAuto = ANCHO+ 5;
                cicloTimer=false;
            }
        }
    }

    private void actualizarPosicionAuto() {
        posicionAuto -= 15;
    }

    private void moverFondo() {//Mueve y cambia los fondos ademas modifica marcador dependiento de distancia
        xFondo-=10;
        if(xFondo==-texturaFondo1.getWidth()){
            xFondo=0;
            cambiosFondo++;
        }
        if (cambiosFondo < 5){
            puntos += 3*0.03f;
        }
        if(cambiosFondo>=5){//Encargardo de cargar el fondo 2
            texturaFondoCopy=texturaFondo2;
            puntos += 3*0.03f;
        }
        if(cambiosFondo>=6){
            texturaFondo1=texturaFondo2;
            puntos += 3*0.03f;
        }
        if(cambiosFondo>=10){//Encargardo de cargar el fondo 3
            texturaFondoCopy=texturaFondo3;
            puntos += 3*0.04f;
        }
        if(cambiosFondo>=11){
            texturaFondo1=texturaFondo3;
            puntos += 3*0.04f;
        }
        if (cambiosFondo >= 15){
            texturaFondoCopy= texturaFondo4;
            puntos += 3*0.04f;
        }
        if (cambiosFondo >= 16){
            texturaFondo1= texturaFondo4;
            puntos += 3*0.05f;
        }
        if (cambiosFondo >= 19){
            texturaFondoCopy= texturaFondo5;
            puntos += 3*0.05f;
        }
        if (cambiosFondo >= 20){
            texturaFondo1= texturaFondo5;
            puntos += 3*0.05f;
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
        texturaFondoCopy.dispose();
        texturaFondo2.dispose();
        texturaFondo3.dispose();
        texturaFondo4.dispose();
        texturaFondo5.dispose();

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
            Rectangle pausaBotonPausa= new Rectangle(ANCHO-100-btnPausa.getWidth()*0.5f, ALTO-100-btnPausa.getHeight()*0.5f,
                     btnPausa.getWidth(),btnPausa.getHeight());

            if(pausaBotonPausa.contains(v.x,v.y)){//Pausa
                if(estadoJuego==EstadoJuego.JUGANDO){
                    estadoJuego=EstadoJuego.PAUSADO;
                    //Crear escena Pausa
                    if(escenaPausa==null){
                        escenaPausa=new EscenaPausa(vistaHUD,batch);
                    }
                    Gdx.input.setInputProcessor(escenaPausa);
                    efectoClick.play();
                }

               // juego.setScreen(new PantallaPausa(juego));
                //estadoJuego = EstadoJuego.PAUSADO;

            } else if (v.x <= camara.position.x && ramiro.getEstado() == EstadoRamiro.CAMINADO &&
            estadoJuego != EstadoJuego.PAUSADO){ //Aqui salta ramiro si se presiona culaquier parte de la pantalla
                 //SALTO
                efetoSalto.play();
                ramiro.setEstado(EstadoRamiro.SALTANDO);
            }else if(estadoJuego==EstadoJuego.PAUSADO){
                estadoJuego=EstadoJuego.JUGANDO;
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
        PIERDE,
        GANA,
        REINICIO
    }

    private class EscenaPausa extends Stage{
        //Titulo de Pausa
        Texture pausa;


    public EscenaPausa(Viewport vista, SpriteBatch batch){
        super(vista,batch);


        pausa=new Texture("pantallaPausa/Pausa.png");

        Pixmap pixmap=new Pixmap((int)(ANCHO*0.85f),(int)(0.8f*ALTO), Pixmap.Format.RGBA8888);
        pixmap.setColor(0,0,0,0.5f);

        pixmap.fillRectangle(0,0,pixmap.getWidth(),pixmap.getHeight());

        Texture texture= new Texture(pixmap);
        Image imgPausa=new Image(texture);
        imgPausa.setPosition(ANCHO/2-pixmap.getWidth()/2f,ALTO/2-pixmap.getHeight()/2f);
        this.addActor(imgPausa);

        //Boton de Continuar
        Texture texturaContinuar=new Texture("pantallaPausa/Cont.png");
        TextureRegionDrawable regionCont=new TextureRegionDrawable(new TextureRegion(texturaContinuar));
        ImageButton btnContinuar=new ImageButton(regionCont);
        btnContinuar.setPosition(640,ALTO-200, Align.center);
        btnContinuar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                estadoJuego=EstadoJuego.JUGANDO;
                efectoClick.play();
                Gdx.input.setInputProcessor(new ProcesadorEntrada());
            }
        });
        this.addActor(btnContinuar);
        //Boton reiniciar
        Texture texturaReset=new Texture("pantallaPausa/Reset.png");
        TextureRegionDrawable regionReset=new TextureRegionDrawable(new TextureRegion(texturaReset));
        ImageButton btnReset=new ImageButton(regionReset);
        btnReset.setPosition(640,ALTO-357, Align.center);
        btnReset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                estadoJuego=EstadoJuego.JUGANDO;
                efectoClick.play();
                juego.setScreen(new PantallaJugando(juego));
            }
        });
        this.addActor(btnReset);

        //Boton de Salir
        Texture texturaSalir=new Texture("pantallaPausa/Salir.png");
        TextureRegionDrawable regionSalir=new TextureRegionDrawable(new TextureRegion(texturaSalir));
        ImageButton btnSalir=new ImageButton(regionSalir);
        btnSalir.setPosition(640,ALTO-513, Align.center);
        btnSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                estadoJuego=EstadoJuego.JUGANDO;
                efectoClick.play();
                Gdx.input.setInputProcessor(new ProcesadorEntrada());
                juego.setScreen(new PantallaMenu(juego));
            }
        });
        this.addActor(btnSalir);
    }

    }
}
