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
    private int vidasMaximas=6;

    //Personaje Ramiro
    private mx.itesm.equipo5.Ramiro ramiro;
    private Texture texturaRamiroMov1;

    //Fondos
    private Texture texturaFondoApoyo;
    private Texture texturaFondoBase;
    private Texture texturaFondo1;
    private Texture texturaFondo2;
    private Texture texturaFondo3;
    private Texture texturaFondo4;
    private Texture texturaFondo5;
    //Transiciones
    //private Texture ruralUrbano;
    private boolean transicionUrbanaRural= false;
    private int noAleatorio= 1;

    //Efecto sonido
    private Sound efectoClick;
    private Sound efetoSalto;
    private Sound efectoDamage;
    private Sound efectoGameOver;
    private Sound efectoItem;
    // Musica
    private Music musicaFondo;
    //Texto
    private Texto texto;

    //Puntos  //Los puntos que gana en el marcador. MARCADOR
    private float puntos= 0;//
    private float xFondo=0;  //Pendiente borrar
    private float xFondoBase=0;
    private float xFondoApoyo;
    private int cambiosFondo=0;//Cuenta las veces que se ha movido el fondo...

    //Estados del juego
    private EstadoJuego estadoJuego = EstadoJuego.JUGANDO;
    private EstadoMapa estadoMapa=EstadoMapa.RURAL;
    //Pausa
    private EscenaPausa escenaPausa;
    private EscenaMuerte escenaMuerte;

    //HUD
    private Stage escenaHUD;
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    /*
    IMPORTANTE :Crear array por cada item...
     */
    //Items
    private Array<Corazon> arrCorazonesItem;
    private Corazon corazon;
    private Texture texturaItemCorazon;

    //Sirve para tomar items correctamente
    private float tiempoInmunidadItem=0f;
    private boolean inmunidadItem=false;

    //Enemigos
    /*
    IMPORTANTE: Por cada enemigo crear un arreglo
     */
    private Array<Camioneta> arrEnemigosCamioneta;
    private Camioneta camioneta;
    private Texture texturaCamioneta;
    private Texture texturaCocheLujo;


    //Sirve para quitar vidas correctamente, solo cuando tomas damage
    private float tiempoInmunidad=0f;//Eres inmune un tiempo si tomas damage
    private boolean inmunidad=false;

    //Cada cuanto timepo aparece un enemigo: Frecuencia
    private float timerCrearEnemigo;
    private float TIEMPO_CREA_ENEMIGO = 2.5f;    // VARIABLE
    private float tiempoBase = 2.5f;
    //Cada cuanto tiempo aparece un item:Frecuencia
    private float timerCrearItem;
    private float TIEMPO_CREA_ITEM = 4.0f;    // VARIABLE
    private float tiempoBaseItem = 4.0f;

    public PantallaJugando(Juego juego){
        this.juego = juego;
    }

    @Override
    public void show() {
        crearHUDpausa();
        crearFondos();
        crearRamiro();
        crearTexto();
        crearAudio();
        crearCorazones();
        crearEnemigos();
        crearItemCorazon();

        texturaFondoBase= texturaFondo1;
        texturaFondoApoyo= texturaFondo2;
        xFondoApoyo= xFondoBase + texturaFondoBase.getWidth();

        //Boton de pausa
        btnPausa=new Texture("pantallaJugando/botonPausa.png");

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearItemCorazon() {
        texturaItemCorazon =new Texture("pantallaJugando/corazon.png");
        arrCorazonesItem=new Array<>();
    }

    private void crearEnemigos() {
            //Inicializa texturas
            texturaCamioneta = new Texture("pantallaJugando/Enemigos/camioneta.png");
            texturaCocheLujo = new Texture("pantallaJugando/Enemigos/carro.png");
        //texturaCarrito = new Texture("pantallaJugando/Enemigos/camioneta.png"); Cuando se implemente escuela
            //Inicializa areglos
        arrEnemigosCamioneta=new Array<>();


    }

    private void crearCorazones() {
        arrCorazones=new Array<>(vidasMaximas);
        for (int i = 0; i <vidasMaximas ; i++) {
            Texture corazones=new Texture("pantallaJugando/corazon.png");
            arrCorazones.add(corazones);
        }

    }

    private void crearAudio() {
        AssetManager manager=new AssetManager();
        manager.load("sounds/Gameplay.mp3",Music.class);
        manager.load("sounds/Click.mp3",Sound.class);
        manager.load("sounds/Salto.mp3",Sound.class);
        manager.load("sounds/Muerte.mp3",Sound.class);
        manager.load("sounds/gameOver.mp3",Sound.class);
        manager.load("sounds/Coin2.mp3",Sound.class);

        manager.finishLoading();//Espera a cargar todos los recursos
        //Musica
        musicaFondo = manager.get("sounds/Gameplay.mp3");
        musicaFondo.setVolume(0.2f);
        musicaFondo.setLooping(true);
        musicaFondo.play();
        //Efectos
        efectoClick=manager.get("sounds/Click.mp3");
        efetoSalto=manager.get("sounds/Salto.mp3");
        efectoDamage=manager.get("sounds/Muerte.mp3");
        efectoGameOver=manager.get("sounds/gameOver.mp3");
        efectoGameOver.setVolume(1,0.1f);
        efectoItem=manager.get("sounds/Coin2.mp3");

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
    }

    private void crearHUDpausa() {//Pausa
        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        camaraHUD.position.set(ANCHO/2, ALTO/2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);
    }

    private void crearRamiro() {
        texturaRamiroMov1= new Texture("pantallaJugando/personaje1/movRamiro2.png");//Sprite de movimientos
        ramiro=new Ramiro(texturaRamiroMov1,150,50);
    }

    @Override
    public void render(float delta) {

        borrarPantalla(0.2f,0.2f,0.2f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturaFondoBase,xFondoBase,0);
        batch.draw(texturaFondoApoyo,xFondoApoyo,0);

        if (estadoJuego == EstadoJuego.JUGANDO){
            actualizar();
            dibujarTexto();
            dibujarCorazones();
            dibujarEnemigos();
            dibujarItems();
            ramiro.render(batch);
            batch.draw(btnPausa,ANCHO-100-btnPausa.getWidth()*0.5f,ALTO-100-btnPausa.getHeight()*0.5f);
        }

        batch.end();

        if (estadoJuego == EstadoJuego.PAUSADO) {   //Implementar la pausa

            batch.setProjectionMatrix(camaraHUD.combined);
            escenaPausa.draw();

            batch.begin();
            batch.draw(escenaPausa.pausa,ANCHO*0.4f,ALTO*0.90f);

            batch.end();
        }
        if(estadoJuego==EstadoJuego.PIERDE){//Implementa pantalla muerte
            batch.setProjectionMatrix(camaraHUD.combined);
            escenaMuerte.draw();
            batch.begin();
            int puntosInt=(int)puntos;//Puntos logrados hasta morir
            escenaMuerte.texto.mostrarMensaje(batch,"Score: "+puntosInt,ANCHO*0.5f,ALTO*0.7f);
            batch.draw(escenaMuerte.GameOver,ANCHO*0.34f,ALTO*0.75f);
            batch.end();
        }

    }

    private void dibujarItems() {
        dibujarArregloCorazones();
    }

    private void dibujarArregloCorazones() {
        //Creacion de corazones
        timerCrearItem += Gdx.graphics.getDeltaTime();
        if (timerCrearItem>=TIEMPO_CREA_ITEM) {
            timerCrearItem = 0;
            TIEMPO_CREA_ITEM = tiempoBaseItem + MathUtils.random()*2;

            corazon= new Corazon(texturaItemCorazon,ANCHO,120f+ MathUtils.random(0,2)*100);
            arrCorazonesItem.add(corazon);
        }

        //Si el corazon se paso de la pantalla, lo borra
        for (int i = arrCorazonesItem.size-1; i >= 0; i--) {
            Corazon cora = arrCorazonesItem.get(i);
            if (cora.sprite.getX() < 0- cora.sprite.getWidth()) {
                arrCorazonesItem.removeIndex(i);

            }
        }
    }

    private void dibujarEnemigos() {

        if(estadoMapa==EstadoMapa.RURAL){//El vehiculo es el unico que cambio por escenario
            dibujarArregloCamionetas();
        }

        /*
        IMPLEMENTAR
         */
    //dibujarAuto();
        //dibujarCarroGolf();
        //dibujarPajaro();
        //dibujarTarea();
    }

    private void dibujarArregloCamionetas() {//Crea los enemigos dependiendo un tiempo aleatorio....,Tambien los desaparece si estan fuera de pantalla

        timerCrearEnemigo += Gdx.graphics.getDeltaTime();
        if (timerCrearEnemigo>=TIEMPO_CREA_ENEMIGO) {
            timerCrearEnemigo = 0;
            TIEMPO_CREA_ENEMIGO = tiempoBase + MathUtils.random()*2;
            if (tiempoBase>0) {
                tiempoBase -= 0.01f;
            }
            camioneta= new Camioneta(texturaCamioneta,ANCHO,60f);
            arrEnemigosCamioneta.add(camioneta);
        }

        //Si el vehiculo se paso de la pantalla, lo borra
        for (int i = arrEnemigosCamioneta.size-1; i >= 0; i--) {
            Camioneta camioneta1 = arrEnemigosCamioneta.get(i);
            if (camioneta1.sprite.getX() < 0- camioneta1.sprite.getWidth()) {
                arrEnemigosCamioneta.removeIndex(i);

            }
        }
    }

    private void dibujarCorazones() {
        for (int i = 0; i <vidas; i++) {
            batch.draw(arrCorazones.get(i),ANCHO*0.02f+i*105,ALTO*0.8f);
        }

    }

    private void dibujarTexto() {
        int puntosInt=(int)puntos;
        texto.mostrarMensaje(batch,"Points: "+puntosInt,ANCHO*0.6f+80,ALTO*0.9f+20);
    }


    private void actualizar() {
        moverFondo();
        //actualizarEnemigos();
        actualizaPuntuacion();

        if(estadoMapa==EstadoMapa.RURAL){
            moverCamionetas();
        }
        /*
        IMPLEMENTAR
         */
            //moverPajaro();
        //moverAuto();
        //moverCarroGolf();
        //etc
        moverItemCorazon();
        verificarColisiones();
        verificarMuerte();
    }

    private void moverItemCorazon() {
        for (Corazon cora : arrCorazonesItem) {
            cora.render(batch);
            cora.moverIzquierda();
        }
    }

    /*
    Se verifica si ya no hay corazones...
     */
    private void verificarMuerte() {
        if(vidas==0){
            if(estadoJuego==EstadoJuego.JUGANDO){
                estadoJuego=EstadoJuego.PIERDE;
                //Crear escena Pausa
                if(escenaMuerte==null){
                    escenaMuerte=new EscenaMuerte(vistaHUD,batch);
                }
                Gdx.input.setInputProcessor(escenaMuerte);
                efectoGameOver.play();
                musicaFondo.pause();
            }
        }
    }

    private void quitarCorazones(){//Encargado de un corazon
        efectoDamage.play();
        vidas--;
    }
    private void agregarCorazon() {
        if(vidas<vidasMaximas){
            efectoItem.play();
            vidas++;
        }
    }
/*
Encargado de verificar cualquier colision
 */
    private void verificarColisiones() {

        if (inmunidad){//Para evitar bugs, Ramiro puede tomar damage cada cierto tiempo...
            //Se activa cada vez que toma damage
            tiempoInmunidad+=Gdx.graphics.getDeltaTime();
        }
        if(tiempoInmunidad>=0.6f){//despues de 0.6 seg, vuelve a ser vulnerable
            inmunidad=false;
            tiempoInmunidad=0f;
        }
        if (inmunidadItem){
            tiempoInmunidadItem+=Gdx.graphics.getDeltaTime();
        }
        if(tiempoInmunidadItem>=0.6f){
            inmunidadItem=false;
            tiempoInmunidadItem=0f;
        }
        //Verificar colisiones de Item Corazon.
        for (int i=arrCorazonesItem.size-1; i>=0; i--) {
            Corazon cora = arrCorazonesItem.get(i);

            if(ramiro.sprite.getBoundingRectangle().overlaps(cora.sprite.getBoundingRectangle())){//Colision de Item
                if(inmunidadItem!=true){// asi se evita bugs.

                        arrCorazonesItem.removeIndex(i);
                        agregarCorazon();

                    inmunidadItem=true;
                }

            }

        }

        //Verifica colisiones de camioneta
        if(estadoMapa==EstadoMapa.RURAL){
            for (int i=arrEnemigosCamioneta.size-1; i>=0; i--) {
                Camioneta camioneta = arrEnemigosCamioneta.get(i);

                // Gdx.app.log("Width","width"+vehiculo.sprite.getBoundingRectangle().width);
                if(ramiro.sprite.getBoundingRectangle().overlaps(camioneta.sprite.getBoundingRectangle())){//Colision de camioneta
                    //Ramiro se vulve inmune 0.6 seg

                    if(inmunidad!=true){//Si no ha tomado damage, entoces se vuelve inmune, asi se evita bugs.
                        if(vidas>0){//Mientras te queden vidas en el arreglo
                            quitarCorazones();//Se resta una vida
                        }
                        inmunidad=true;
                    }

                }

            }
        }


    }



    private void moverCamionetas() {//Encargado de dibujar y mover las camionetas
        for (Camioneta cam : arrEnemigosCamioneta) {
            cam.render(batch);

            cam.moverIzquierda();
        }
    }

    private void actualizaPuntuacion() { //Actualiza la puntuacion dependiendo de las veces que se han hecho cambios de fondo.
        if(cambiosFondo <= 10){
            puntos+= 1*0.02f;
        }
        if(cambiosFondo > 10 && cambiosFondo <= 20){
            puntos+= 1*0.03f;
        }
        if(cambiosFondo > 20 && cambiosFondo <= 30){
            puntos+= 1*0.04f;
        }
        if(cambiosFondo > 30 && cambiosFondo <= 40){
            puntos+= 1*0.06f;
        }
        if(cambiosFondo > 40 && cambiosFondo <= 50){
            puntos+= 1*0.09f;
        }
        if(cambiosFondo > 50) {
            puntos += 1 * 0.5f;
        }
    }
    /*
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
                if (cambiosFondo< 502 && posicionAuto + texturaCocheLujo.getWidth() < 0){
                    posicionAuto = ANCHO+ 5;
                    cicloTimer=false;
                }
            }
        }

        private void actualizarPosicionAuto() {
            posicionAuto -= 15;
        }
    */
    private void moverFondo() {//Mueve y cambia los fondos
        ActualizaPosicionesFondos();
        noAleatorio = MathUtils.random(1, 5);   // crea los numeros para cambiar los mapas de manera aleatoria
        if (xFondo == -texturaFondoBase.getWidth()){
            //Cambio de fondo base
            texturaFondoBase= mapaAleatorio(noAleatorio);
        }
        if (xFondo == -(texturaFondoApoyo.getWidth()+texturaFondoBase.getWidth())){
            //Cambio fondo de apoyo
            texturaFondoApoyo= mapaAleatorio(noAleatorio);
        }
    }

    private void ActualizaPosicionesFondos() { // Actualiza las posiciones de los fondos para hacerlos infinitos
        xFondo-=10;             //Es el cursor de la posición
        xFondoBase-=10;
        xFondoApoyo-=10;
        if (xFondo == -texturaFondoBase.getWidth()){
            xFondoBase = xFondoApoyo+texturaFondoApoyo.getWidth();
            cambiosFondo++;
        }
        if (xFondo == -(texturaFondoApoyo.getWidth()+texturaFondoBase.getWidth())) {
            xFondoApoyo = xFondoBase+texturaFondoBase.getWidth();
            xFondo= 0;
            cambiosFondo++;
        }
    }

    private Texture mapaAleatorio(int noAleatorio) { //Asigna el mapa conforme el numero de entrada.
        if(noAleatorio == 1){
            return texturaFondo1;
        }
        if(noAleatorio == 2){
           return texturaFondo2;
        }
        if(noAleatorio == 3){
            return texturaFondo3;
        }
        if(noAleatorio == 4){
            return texturaFondo4;
        }
        if(noAleatorio == 5){
            return texturaFondo5;
        }
        return texturaFondo1;
    }

    @Override
    public void pause(){
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        texturaFondoApoyo.dispose();
        texturaFondoBase.dispose();
        musicaFondo.dispose();
        efetoSalto.dispose();
        efectoClick.dispose();
        efectoGameOver.dispose();
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
                    musicaFondo.pause();
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
                musicaFondo.play();
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

                ///////////////////Implementar reinicio de variables en caso de ser necesario. ////////////////////////
                Gdx.input.setInputProcessor(new ProcesadorEntrada());
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



    private class EscenaMuerte extends Stage{
        //Titulo de GameOver
        Texture GameOver;
        Texto texto;

        public EscenaMuerte(Viewport vista, SpriteBatch batch){
            super(vista,batch);
            GameOver=new Texture("pantallaGameOver/gg.png");
            texto=new Texto("game.fnt");

            Pixmap pixmap=new Pixmap((int)(ANCHO*0.85f),(int)(0.8f*ALTO), Pixmap.Format.RGBA8888);
            pixmap.setColor(0,0,0,0.5f);

            pixmap.fillRectangle(0,0,pixmap.getWidth(),pixmap.getHeight());

            Texture texture= new Texture(pixmap);
            Image imgPausa=new Image(texture);
            imgPausa.setPosition(ANCHO/2-pixmap.getWidth()/2f,ALTO/2-pixmap.getHeight()/2f);
            this.addActor(imgPausa);



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

                    ///////////////////Implementar reinicio de variables en caso de ser necesario. ////////////////////////
                    Gdx.input.setInputProcessor(new ProcesadorEntrada());
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
    /*
    Los estados de toda la clase
     */
    private enum EstadoJuego {
        JUGANDO,
        PAUSADO,
        PIERDE,
        GANA,
        REINICIO
    }
    private enum EstadoMapa {
        RURAL,
        URBANO,
        UNIVERSIDAD
    }



}
