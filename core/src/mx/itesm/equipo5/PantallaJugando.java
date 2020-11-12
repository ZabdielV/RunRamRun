package mx.itesm.equipo5;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
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
    private Texture texturaRamiroMov2;
    private float tiempoInmunidadRamiro=0;
    private boolean inmunidadRamiro;

    //Preferencias
    private boolean music;

    //Fondos
    private Texture texturaFondo1_1;
    private Texture texturaFondo1_2;
    private Texture texturaFondo1_3;
    private Texture texturaFondo1_4;
    private Texture texturaFondo1_5;

    private Texture texturaFondo2_1;
    private Texture texturaFondo2_2;
    private Texture texturaFondo2_3;
    private Texture texturaFondo2_4;
    private Texture texturaFondo2_5;

    private Texture texturaFondo3_1;
    private Texture texturaFondo3_2;
    private Texture texturaFondo3_3;
    private Texture texturaFondo3_4;
    private Texture texturaFondo3_5;

    private Texture texturaFondo4_1;
    private Texture texturaFondo4_2;
    private Texture texturaFondo4_3;
    private Texture texturaFondo4_4;
    private Texture texturaFondo4_5;

    private Texture texturaFondoApoyo;
    private Texture texturaFondoBase;
    private boolean modificacionFondoBase =false;  //Contiene el significado si ya se modifico los fondos base.

    //Transiciones
    private boolean transicionUrbanaRural= false;  //checa si la transicion se realizo.
    private int noAleatorio= 1; //Ayuda a asignar los mapas aleatorios, este es el default.

    //Efecto sonido
    private Sound efectoClick;
    private Sound efetoSalto;
    private Sound efectoDamage;
    private Sound efectoGameOver;
    private Sound efectoItem;

    // Musica
    private Music musicaFondo;
    private Music musicaRayo;

    //Texto
    private Texto texto;

    //Puntos  //Los puntos que gana en el marcador. MARCADOR
    private float puntos= 0;//
    private float xFondo=0;  //Pendiente borrar
    private float xFondoBase=0;
    private float xFondoApoyo=3200; //Es el ancho de los fondos
    private int cambiosFondo=0;//Cuenta las veces que se ha movido el fondo...

    //Estados del juego
    private EstadoJuego estadoJuego = EstadoJuego.JUGANDO;
    private EstadoMapa estadoMapa;


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

    private Array<RayoEmprendedor> arrRayoEmprendedor;
    private RayoEmprendedor rayo;
    private Texture texturaRayoEmprendedor;


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

    private Array<AutoGolf> arrEnemigosCarritoGolf;
    private AutoGolf carritoGolf;
    private Texture texturaCarritoGolf;

    private Array<Auto> arrEnemigosAuto;
    private Auto carroLujo;
    private Texture texturaCocheLujo;

    private Array<Lampara> arrEnemigoLampara;
    private Lampara lampara;
    private Texture texturaLampara;

    private Array<Silla> arrEnemigoSilla;
    private Silla silla;
    private Texture texturaSilla;


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
        crearItemRayoEm();
        //Boton de pausa
        btnPausa=new Texture("pantallaJugando/botonPausa.png");
        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearItemRayoEm() {
        texturaRayoEmprendedor =new Texture("pantallaJugando/item/rayo.png");
        arrRayoEmprendedor=new Array<>();

    }

    private void cargarPreferencias() {
        //Preferencia musica
        Preferences musicPre=Gdx.app.getPreferences("music");
        music=musicPre.getBoolean("music",true);
        Gdx.app.log("prefe","preferencias al inicio: "+music);

    }
    private void crearItemCorazon() {
        texturaItemCorazon =new Texture("pantallaJugando/corazon.png");
        arrCorazonesItem=new Array<>();
    }

    private void crearEnemigos() {
        //Inicializa texturas
        texturaCamioneta = new Texture("pantallaJugando/Enemigos/camioneta.png");
        texturaCocheLujo = new Texture("pantallaJugando/Enemigos/carro.png");
        texturaCarritoGolf = new Texture("pantallaJugando/Enemigos/carroTec.png");

        texturaLampara= new Texture("pantallaJugando/Enemigos/Lampara.png");
        texturaSilla= new Texture("pantallaJugando/Enemigos/Silla.png");

            //Inicializa areglos
        arrEnemigosCamioneta=new Array<>();
        arrEnemigosAuto= new Array<>();
        arrEnemigosCarritoGolf= new Array<>();

        arrEnemigoLampara= new Array<>();
        arrEnemigoSilla= new Array<>();
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
        manager.load("sounds/Invincibility.mp3",Music.class);
        manager.load("sounds/Click.mp3",Sound.class);
        manager.load("sounds/Salto.mp3",Sound.class);
        manager.load("sounds/Muerte.mp3",Sound.class);
        manager.load("sounds/gameOver.mp3",Sound.class);
        manager.load("sounds/Coin2.mp3",Sound.class);

        manager.finishLoading();//Espera a cargar todos los recursos
        //Carga las preferencias
        cargarPreferencias();

        //Musica
        musicaFondo = manager.get("sounds/Gameplay.mp3");
        musicaFondo.setVolume(0.8f);
        musicaFondo.setLooping(true);
        if(music){
            musicaFondo.play();
        }
        musicaRayo = manager.get("sounds/Invincibility.mp3");
        musicaRayo.setVolume(0.5f);
        musicaRayo.setLooping(true);

        //Efectos
        efectoClick=manager.get("sounds/Click.mp3");
        efetoSalto=manager.get("sounds/Salto.mp3");
        efectoDamage=manager.get("sounds/Muerte.mp3");
        efectoGameOver=manager.get("sounds/gameOver.mp3");
        efectoGameOver.setVolume(1,0.05f);
        efectoItem=manager.get("sounds/Coin2.mp3");

    }

    private void crearTexto() {
        texto=new Texto("game.fnt");
    }

    private void crearFondos() {
        //Mapas Rural
        texturaFondo1_1 =new Texture("pantallaJugando/Mapas/Rural/nivelRural1_1.png");
        texturaFondo1_2 =new Texture("pantallaJugando/Mapas/Rural/nivelRural1_2.png");
        texturaFondo1_3=new Texture("pantallaJugando/Mapas/Rural/nivelRural1_3.png");
        texturaFondo1_4 =new Texture("pantallaJugando/Mapas/Rural/nivelRural1_4.png");
        texturaFondo1_5 =new Texture("pantallaJugando/Mapas/Rural/nivelRural1_5.png");
        //Mapas Urbano
        texturaFondo2_1=new Texture("pantallaJugando/Mapas/Urbano/Urbano2_1.png");
        texturaFondo2_2=new Texture("pantallaJugando/Mapas/Urbano/Urbano2_2.png");
        texturaFondo2_3=new Texture("pantallaJugando/Mapas/Urbano/Urbano2_3.png");
        texturaFondo2_4=new Texture("pantallaJugando/Mapas/Urbano/Urbano2_4.png");
        texturaFondo2_5=new Texture("pantallaJugando/Mapas/Urbano/Urbano2_5.png");
        //Mapas Universidad
        texturaFondo3_1=new Texture("pantallaJugando/Mapas/Universidad/Universidad3_1.png");
        texturaFondo3_2=new Texture("pantallaJugando/Mapas/Universidad/Universidad3_2.png");
        texturaFondo3_3=new Texture("pantallaJugando/Mapas/Universidad/Universidad3_3.png");
        texturaFondo3_4=new Texture("pantallaJugando/Mapas/Universidad/Universidad3_4.png");
        texturaFondo3_5=new Texture("pantallaJugando/Mapas/Universidad/Universidad3_5.png");
        //Mapas Salones
        texturaFondo4_1=new Texture("pantallaJugando/Mapas/Salones/Salones4_1.png");
        texturaFondo4_2=new Texture("pantallaJugando/Mapas/Salones/Salones4_2.png");
        texturaFondo4_3=new Texture("pantallaJugando/Mapas/Salones/Salones4_3.png");
        texturaFondo4_4=new Texture("pantallaJugando/Mapas/Salones/Salones4_4.png");
        texturaFondo4_5=new Texture("pantallaJugando/Mapas/Salones/Salones4_5.png");

    }

    private void crearHUDpausa() {//Pausa
        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        camaraHUD.position.set(ANCHO/2, ALTO/2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);
    }

    private void crearRamiro() {                //Mejorar Ramiro
        texturaRamiroMov1= new Texture("pantallaJugando/personaje1/movRamiro2.png");//Sprite de movimientos
        texturaRamiroMov2= new Texture("pantallaJugando/personaje1/Invencible.png");//Sprite de movimientos
        ramiro=new Ramiro(texturaRamiroMov1,texturaRamiroMov2,150,50);
    }

    @Override
    public void render(float delta) {

        borrarPantalla(0.2f,0.2f,0.2f);

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        actualizarFondo();                                  //Actualiza solo cosas de Fondo
        batch.draw(texturaFondoBase,xFondoBase,0);
        batch.draw(texturaFondoApoyo,xFondoApoyo,0);

        if (estadoJuego == EstadoJuego.JUGANDO){

            actualizarEnemigosItems();                      //Actualiza solo items y enemigos

            dibujarEnemigos();
            dibujarItems();
            dibujarTexto();
            dibujarCorazones();
            ramiro.render(batch);
            batch.draw(btnPausa,ANCHO-100-btnPausa.getWidth()*0.5f,ALTO-100-btnPausa.getHeight()*0.5f);
        }

        batch.end();

        if (estadoJuego == EstadoJuego.PAUSADO) {

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
        //dibujarArregloCorazones();
        //dibujarArregloRayos();

        timerCrearItem += Gdx.graphics.getDeltaTime();
        if (timerCrearItem>=TIEMPO_CREA_ITEM) {
            timerCrearItem = 0;
            TIEMPO_CREA_ITEM = tiempoBaseItem + MathUtils.random()*2;
            int tipoItem=MathUtils.random(1,2);//Para que los items sean al azar

            if(tipoItem==1){
                rayo= new RayoEmprendedor(texturaRayoEmprendedor,ANCHO,120f+ MathUtils.random(0,2)*100);
                arrRayoEmprendedor.add(rayo);

            }else if(tipoItem==2){
                corazon= new Corazon(texturaItemCorazon,ANCHO,120f+ MathUtils.random(0,2)*100);
                arrCorazonesItem.add(corazon);
            }
            //Gdx.app.log("random","TipoItem :"+tipoItem);

        }


        for (int i = arrRayoEmprendedor.size-1; i >= 0; i--) {
            RayoEmprendedor rayo = arrRayoEmprendedor.get(i);
            if (rayo.sprite.getX() < 0- rayo.sprite.getWidth()) {
                arrRayoEmprendedor.removeIndex(i);

            }
        }
        for (int i = arrRayoEmprendedor.size-1; i >= 0; i--) {
            RayoEmprendedor rayo = arrRayoEmprendedor.get(i);
            if (rayo.sprite.getX() < 0- rayo.sprite.getWidth()) {
                arrRayoEmprendedor.removeIndex(i);

            }
        }





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
        if(estadoMapa==EstadoMapa.URBANO){//El vehiculo es el unico que cambio por escenario
            dibujarArregloCoche();
        }
        if(estadoMapa==EstadoMapa.UNIVERSIDAD){//El vehiculo es el unico que cambio por escenario
            dibujarArregloCarritos();
        }
        if (estadoMapa == EstadoMapa.SALONES){  //AUN NO IMPLEMENTADO
            dibujarArregloSillas();
            dibujarArregloLamparas();
        }

        /*
        IMPLEMENTAR

        //dibujarPajaro();
        //dibujarTarea();

         */
    }

    private void dibujarArregloSillas() {
        timerCrearEnemigo += Gdx.graphics.getDeltaTime();
        if (timerCrearEnemigo>=TIEMPO_CREA_ENEMIGO) {
            timerCrearEnemigo = 0;
            TIEMPO_CREA_ENEMIGO = tiempoBase + MathUtils.random()*2;
            if (tiempoBase>0) {
                tiempoBase -= 0.01f;
            }
            silla = new Silla(texturaSilla,ANCHO,60f);
            arrEnemigoSilla.add(silla);
        }

        //Si la lampara pasa de la pantalla, lo borra
        for (int i = arrEnemigoSilla.size-1; i >= 0; i--) {
            Silla silla = arrEnemigoSilla.get(i);
            if (silla.sprite.getX() < 0- silla.sprite.getWidth()) {
                arrEnemigoSilla.removeIndex(i);

            }
        }
    }

    private void dibujarArregloLamparas() {
        timerCrearEnemigo += Gdx.graphics.getDeltaTime();
        if (timerCrearEnemigo>=TIEMPO_CREA_ENEMIGO) {
            timerCrearEnemigo = 0;
            TIEMPO_CREA_ENEMIGO = tiempoBase + MathUtils.random()*2;
            if (tiempoBase>0) {
                tiempoBase -= 0.01f;
            }
            lampara = new Lampara(texturaLampara,ANCHO,ALTO*0.65f);
            arrEnemigoLampara.add(lampara);
        }

        //Si la lampara pasa de la pantalla, lo borra
        for (int i = arrEnemigoLampara.size-1; i >= 0; i--) {
            Lampara lampara = arrEnemigoLampara.get(i);
            if (lampara.sprite.getX() < 0- lampara.sprite.getWidth()) {
                arrEnemigoLampara.removeIndex(i);

            }
        }
    }

    private void dibujarArregloCarritos() {
        timerCrearEnemigo += Gdx.graphics.getDeltaTime();
        if (timerCrearEnemigo>=TIEMPO_CREA_ENEMIGO) {
            timerCrearEnemigo = 0;
            TIEMPO_CREA_ENEMIGO = tiempoBase + MathUtils.random()*2;
            if (tiempoBase>0) {
                tiempoBase -= 0.01f;
            }
            carritoGolf = new AutoGolf(texturaCarritoGolf,ANCHO,60f);
            arrEnemigosCarritoGolf.add(carritoGolf);
        }

        //Si el vehiculo se paso de la pantalla, lo borra
        for (int i = arrEnemigosCarritoGolf.size-1; i >= 0; i--) {
            AutoGolf carroGolf = arrEnemigosCarritoGolf.get(i);
            if (carroGolf.sprite.getX() < 0- carroGolf.sprite.getWidth()) {
                arrEnemigosCarritoGolf.removeIndex(i);

            }
        }
    }
    private void dibujarArregloCoche() {
        timerCrearEnemigo += Gdx.graphics.getDeltaTime();
        if (timerCrearEnemigo>=TIEMPO_CREA_ENEMIGO) {
            timerCrearEnemigo = 0;
            TIEMPO_CREA_ENEMIGO = tiempoBase + MathUtils.random()*2;
            if (tiempoBase>0) {
                tiempoBase -= 0.01f;
            }
            carroLujo= new Auto(texturaCocheLujo,ANCHO,60f);
            arrEnemigosAuto.add(carroLujo);
        }

        //Si el vehiculo se paso de la pantalla, lo borra
        for (int i = arrEnemigosAuto.size-1; i >= 0; i--) {
            Auto carroLujo1 = arrEnemigosAuto.get(i);
            if (carroLujo1.sprite.getX() < 0- carroLujo1.sprite.getWidth()) {
                arrEnemigosAuto.removeIndex(i);

            }
        }
    }
    private void dibujarArregloCamionetas() {//Crea los enemigos dependiendo un tiempo aleatorio....Tambien los desaparece si estan fuera de pantalla

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


    private void actualizarFondo() {  //Asigna los fondos base y los modifica al recorrerlos.
        actualizaEstadoMapa();
        if (modificacionFondoBase == true && estadoJuego != EstadoJuego.PAUSADO && estadoJuego != EstadoJuego.PIERDE){
            moverFondo();
        } else if (modificacionFondoBase == false && estadoJuego != EstadoJuego.PAUSADO && estadoJuego != EstadoJuego.PIERDE ){
            texturaFondoBase= texturaFondo1_1;
            texturaFondoApoyo= texturaFondo1_2;
            moverFondo();
        }
    }

    private void actualizaEstadoMapa() {  //establece el area en que el jugador esta.
        if(cambiosFondo >= 0 && cambiosFondo < 10){
            estadoMapa= EstadoMapa.RURAL;
        }
        if(cambiosFondo >= 10 && cambiosFondo < 15){
            estadoMapa= EstadoMapa.URBANO;
        }
        if(cambiosFondo >= 15 && cambiosFondo < 20){
            estadoMapa= EstadoMapa.UNIVERSIDAD;
        }
        if(cambiosFondo >= 20 && cambiosFondo < 25){
            estadoMapa= EstadoMapa.SALONES;
        }

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
                if(music){
                    musicaFondo.pause();
                }
            }
        }
    }

    private void quitarCorazones(){//Encargado de un corazon
        if(ramiro.estadoItem== Ramiro.EstadoItem.NORMAL){
            efectoDamage.play();
            vidas--;
        }
    }
    private void agregarCorazon() {
        if(vidas<vidasMaximas){
            efectoItem.play();
            vidas++;
        }
    }
    private void inmunidadRamiro() {
        efectoItem.play();
        ramiro.setEstadoItem(Ramiro.EstadoItem.INMUNE);
        inmunidadItem=true;
        inmunidadRamiro=true;
        if(music){
            musicaFondo.pause();
        }

        musicaRayo.play();
    }
/*
Encargado de verificar cualquier colision
 */
    private void verificarColisiones() {
        if(inmunidadRamiro){
            tiempoInmunidadRamiro += Gdx.graphics.getDeltaTime();
        }
        if(tiempoInmunidadRamiro>=3f){//despues de 0.6 seg, vuelve a ser vulnerable
            inmunidadRamiro=false;
            tiempoInmunidadRamiro=0f;
            ramiro.setEstadoItem(Ramiro.EstadoItem.NORMAL);
            musicaRayo.stop();
            if(music){
                musicaFondo.play();
            }

        }

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
        //Verifica colisiones Rayo emprendedor
        for (int i=arrRayoEmprendedor.size-1; i>=0; i--) {
            RayoEmprendedor rayo= arrRayoEmprendedor.get(i);

            if(ramiro.sprite.getBoundingRectangle().overlaps(rayo.sprite.getBoundingRectangle())){//Colision de Item
                if(inmunidadItem!=true){// asi se evita bugs.

                    arrRayoEmprendedor.removeIndex(i);
                    inmunidadRamiro();

                }

            }

        }

        //Verifica colisiones de camioneta
        if(estadoMapa==EstadoMapa.RURAL){
            for (int i=arrEnemigosCamioneta.size-1; i>=0; i--) {
                Camioneta camioneta = arrEnemigosCamioneta.get(i);

                // Gdx.app.log("Width","width"+vehiculo.sprite.getBoundingRectangle().width);
                if(ramiro.sprite.getBoundingRectangle().overlaps(camioneta.sprite.getBoundingRectangle())){//Colision de camioneta
                    //Ramiro se vuelve inmune 0.6 seg

                    if(inmunidad!=true){//Si no ha tomado damage, entoces se vuelve inmune, asi se evita bugs.
                        if(vidas>0){//Mientras te queden vidas en el arreglo
                            quitarCorazones();//Se resta una vida
                        }
                        inmunidad=true;
                    }

                }

            }
        }

        //Verifica colisiones de carro de Lujo
        if(estadoMapa==EstadoMapa.URBANO){
            for (int i=arrEnemigosAuto.size-1; i>=0; i--) {
                Auto cocheLujo = arrEnemigosAuto.get(i);

                // Gdx.app.log("Width","width"+vehiculo.sprite.getBoundingRectangle().width);
                if(ramiro.sprite.getBoundingRectangle().overlaps(cocheLujo.sprite.getBoundingRectangle())){//Colision de camioneta
                    //Ramiro se vuelve inmune 0.6 seg

                    if(inmunidad!=true){//Si no ha tomado damage, entoces se vuelve inmune, asi se evita bugs.
                        if(vidas>0){//Mientras te queden vidas en el arreglo
                            quitarCorazones();//Se resta una vida
                        }
                        inmunidad=true;
                    }

                }

            }
        }

        //Verifica colisiones de carrito de golf
        if(estadoMapa==EstadoMapa.UNIVERSIDAD){
            for (int i=arrEnemigosCarritoGolf.size-1; i>=0; i--) {
                AutoGolf carritoGolf = arrEnemigosCarritoGolf.get(i);

                // Gdx.app.log("Width","width"+vehiculo.sprite.getBoundingRectangle().width);
                if(ramiro.sprite.getBoundingRectangle().overlaps(carritoGolf.sprite.getBoundingRectangle())){//Colision de camioneta
                    //Ramiro se vuelve inmune 0.6 seg

                    if(inmunidad!=true){//Si no ha tomado damage, entoces se vuelve inmune, asi se evita bugs.
                        if(vidas>0){//Mientras te queden vidas en el arreglo
                            quitarCorazones();//Se resta una vida
                        }
                        inmunidad=true;
                    }

                }

            }
        }

        //Verifica colisiones de las lamparas
        if(estadoMapa==EstadoMapa.SALONES){
            for (int i=arrEnemigoLampara.size-1; i>=0; i--) {
                Lampara lampara = arrEnemigoLampara.get(i);

                // Gdx.app.log("Width","width"+vehiculo.sprite.getBoundingRectangle().width);
                if(ramiro.sprite.getBoundingRectangle().overlaps(lampara.sprite.getBoundingRectangle())){//Colision de camioneta
                    //Ramiro se vuelve inmune 0.6 seg

                    if(inmunidad!=true){//Si no ha tomado damage, entoces se vuelve inmune, asi se evita bugs.
                        if(vidas>0){//Mientras te queden vidas en el arreglo
                            quitarCorazones();//Se resta una vida
                        }
                        inmunidad=true;
                    }

                }

            }
            //Verifica colisiones de las sillas
            for (int i=arrEnemigoSilla.size-1; i>=0; i--) {
                Silla silla = arrEnemigoSilla.get(i);

                // Gdx.app.log("Width","width"+vehiculo.sprite.getBoundingRectangle().width);
                if(ramiro.sprite.getBoundingRectangle().overlaps(silla.sprite.getBoundingRectangle())){//Colision de camioneta
                    //Ramiro se vuelve inmune 0.6 seg

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
    private void moverCarroLujo() {//Encargado de dibujar y mover las camionetas
        for (Auto auto : arrEnemigosAuto) {
            auto.render(batch);

            auto.moverIzquierda();
        }
    }
    private void moverCarritoGolf() {//Encargado de dibujar y mover las camionetas
        for (AutoGolf carrito : arrEnemigosCarritoGolf) {
            carrito.render(batch);

            carrito.moverIzquierda();
        }
    }

    private void moverLamparas() {
        for (Lampara lampara : arrEnemigoLampara) {
            lampara.render(batch);

            lampara.moverIzquierda();
        }
    }

    private void moverSillas() {
        for (Silla silla : arrEnemigoSilla) {
            silla.render(batch);

            silla.moverIzquierda();
        }
    }

    private void actualizaPuntuacion() { //Actualiza la puntuacion dependiendo de las veces que se han hecho cambios de fondo.
        if(cambiosFondo <= 10){
            puntos+= 1*0.03f;
        }
        if(cambiosFondo > 10 && cambiosFondo <= 20){
            puntos+= 1*0.04f;
        }
        if(cambiosFondo > 20 && cambiosFondo <= 30){
            puntos+= 1*0.05f;
        }
        if(cambiosFondo > 30 && cambiosFondo <= 40){
            puntos+= 1*0.07f;
        }
        if(cambiosFondo > 40 && cambiosFondo <= 50){
            puntos+= 1*0.1f;
        }
        if(cambiosFondo > 50) {
            puntos += 1 * 0.25f;
        }
    }

    private void actualizarEnemigosItems() {  //mueve los enemigos e items, por lo mismo verifica si hay colision.
        if(estadoMapa== EstadoMapa.RURAL){
            moverCamionetas();
        }
        if(estadoMapa== EstadoMapa.URBANO){
            moverCarroLujo();
        }
        if(estadoMapa== EstadoMapa.UNIVERSIDAD){
            moverCarritoGolf();
        }
        if (estadoMapa == EstadoMapa.SALONES){
            moverLamparas();
            moverSillas();
        }
        actualizaPuntuacion();
        moverItemCorazon();
        verificarColisiones();
        verificarMuerte();
        moverItemRayo();
         /*
        IMPLEMENTAR

        //moverPajaro();
        //etc
        */
    }


    private void moverItemRayo() {
        for (RayoEmprendedor rayo : arrRayoEmprendedor) {
            rayo.render(batch);
            rayo.moverIzquierda();
        }
    }

    private void moverFondo() {//Mueve y cambia los fondos
        ActualizaPosicionesFondos();
        noAleatorio = MathUtils.random(1, 5);   // crea los numeros para cambiar los mapas de manera aleatoria
        if (xFondo == -texturaFondoBase.getWidth()){
            //Cambio de fondo base
            texturaFondoBase= mapaAleatorio(noAleatorio);
            modificacionFondoBase= true;  //Avisa que ya se modifico los fondos de inicio.
        }
        if (xFondo == -(texturaFondoApoyo.getWidth()+texturaFondoBase.getWidth())){
            //Cambio fondo de apoyo
            texturaFondoApoyo= mapaAleatorio(noAleatorio);
            xFondo= 0; //al pasar el segundo fondo regresa el puntero al primer fondo.
            modificacionFondoBase= true;  //Avisa que ya se modifico los fondos de inicio.
        }
    }

    private void ActualizaPosicionesFondos() { // Actualiza las posiciones de los fondos para hacerlos infinitos
        xFondo-=10;             //Es el cursor de la posición
        xFondoBase-=10;                                                 //Aqui se puede implementar metodo de modificacion de velocidad de fondo.
        xFondoApoyo-=10;
        if (xFondo == -texturaFondoBase.getWidth()){
            xFondoBase = xFondoApoyo+texturaFondoApoyo.getWidth();
            cambiosFondo++;
        }
        if (xFondo == -(texturaFondoApoyo.getWidth()+texturaFondoBase.getWidth())) {
            xFondoApoyo = xFondoBase+texturaFondoBase.getWidth();
            cambiosFondo++;
        }
    }

    private Texture mapaAleatorio(int noAleatorio) { //Asigna el mapa conforme el numero de entrada.
        if (estadoMapa == EstadoMapa.RURAL){
            if(noAleatorio == 1){
                return texturaFondo1_1;
            }
            if(noAleatorio == 2){
                return texturaFondo1_2;
            }
            if(noAleatorio == 3){
                return texturaFondo1_3;
            }
            if(noAleatorio == 4){
                return texturaFondo1_4;
            }
            if(noAleatorio == 5){
                return texturaFondo1_5;
            }
        } else
        if (estadoMapa == EstadoMapa.URBANO) {
            if(noAleatorio == 1){
                return texturaFondo2_1;
            }
            if(noAleatorio == 2){
                return texturaFondo2_2;
            }
            if(noAleatorio == 3){
                return texturaFondo2_3;
            }
            if(noAleatorio == 4){
                return texturaFondo2_4;
            }
            if(noAleatorio == 5){
                return texturaFondo2_5;
            }
        } else if (estadoMapa == EstadoMapa.UNIVERSIDAD) {
            if(noAleatorio == 1){
                return texturaFondo3_1;
            }
            if(noAleatorio == 2){
                return texturaFondo3_2;
            }
            if(noAleatorio == 3){
                return texturaFondo3_3;
            }
            if(noAleatorio == 4){
                return texturaFondo3_4;
            }
            if(noAleatorio == 5){
                return texturaFondo3_5;
            }
        }else if (estadoMapa == EstadoMapa.SALONES) {
            if (noAleatorio == 1) {
                return texturaFondo4_1;
                }
            if (noAleatorio == 2) {
                return texturaFondo4_2;
                }
            if (noAleatorio == 3) {
                return texturaFondo4_3;
                }
            if (noAleatorio == 4) {
                return texturaFondo4_4;
                }
            if (noAleatorio == 5) {
                return texturaFondo4_5;
                }
            }
        return null;
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
        musicaRayo.dispose();
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
                    if(music){
                        musicaFondo.pause();
                    }
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
                if(music){
                    musicaFondo.play();
                }
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
        PIERDE
    }
    private enum EstadoMapa {
        RURAL,
        URBANO,
        UNIVERSIDAD,
        SALONES
    }



}
