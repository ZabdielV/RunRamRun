package mx.itesm.RunRamRun;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PantallaCargando extends Pantalla {

    // Sprite de animación
    private Sprite spriteCargando;
    public static final float TIEMPO_FRAMES = 0.25f;
    private float timerAnimacion = TIEMPO_FRAMES;
    private Texture texturaCargando;

    // AssetManager
    private AssetManager manager;

    // Juego
    private Juego juego;

    // Pantallas
    private Pantallas siguientePantalla;

    // Carga
    private int avance;

    // Mensajes
    private Texto texto;

    public PantallaCargando(Juego juego, Pantallas siguientePantalla) {
        this.juego = juego;
        this.siguientePantalla = siguientePantalla;
    }

    @Override
    public void show() {
        texturaCargando = new Texture("pantallaJugando/personaje1/mov3.png");
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(ANCHO/2 - spriteCargando.getWidth()/2, 2* ALTO/3 - spriteCargando.getHeight()/2);
        crearTexto();
        cargarRecursos(siguientePantalla);
    }

    private void crearTexto() {
        texto=new Texto("game.fnt");
    }

    private void cargarRecursos(Pantallas siguientePantalla) {
        manager = juego.getManager();
        avance = 0;
        switch (siguientePantalla) {
            case JUGANDO:
                cargarRecursosJuego();
                break;
        }
    }

    private void cargarRecursosJuego() {
        manager.load("pantallaJugando/botonPausa.png",Texture.class);
        manager.load("pantallaJugando/item/tarea.png",Texture.class);
        manager.load("pantallaJugando/item/rayo.png",Texture.class);
        manager.load("pantallaJugando/corazon.png",Texture.class);
        manager.load("pantallaJugando/Enemigos/camioneta.png",Texture.class);
        manager.load("pantallaJugando/Enemigos/carro.png",Texture.class);
        manager.load("pantallaJugando/Enemigos/carroTec.png",Texture.class);
        manager.load("pantallaJugando/Enemigos/lampara.png",Texture.class);
        manager.load("pantallaJugando/Enemigos/Silla.png",Texture.class);
        manager.load("pantallaJugando/Enemigos/pajaro.png",Texture.class);
        manager.load("pantallaJugando/corazon.png",Texture.class);


        //Mapas Rural
        manager.load("pantallaJugando/Mapas/Rural/nivelRural1_1.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Rural/nivelRural1_2.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Rural/nivelRural1_3.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Rural/nivelRural1_4.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Rural/nivelRural1_5.png",Texture.class);
        //Mapas Urbano
        manager.load("pantallaJugando/Mapas/Urbano/Urbano2_1.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Urbano/Urbano2_2.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Urbano/Urbano2_3.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Urbano/Urbano2_4.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Urbano/Urbano2_5.png",Texture.class);
        //Mapas Universidad
        manager.load("pantallaJugando/Mapas/Universidad/Universidad3_1.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Universidad/Universidad3_2.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Universidad/Universidad3_3.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Universidad/Universidad3_4.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Universidad/Universidad3_5.png",Texture.class);
        //Mapas Salones
        manager.load("pantallaJugando/Mapas/Salones/Salones4_1.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Salones/Salones4_2.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Salones/Salones4_3.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Salones/Salones4_4.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Salones/Salones4_5.png",Texture.class);

        //Transiciones
        manager.load("pantallaJugando/Mapas/Transiciones/ruralUrbano.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Transiciones/urbanaUniversidad.png",Texture.class);
        manager.load("pantallaJugando/Mapas/Transiciones/universidadSalones.png",Texture.class);


        //Ramiro
        manager.load("pantallaJugando/personaje1/movRamiro2.png",Texture.class);
        manager.load("pantallaJugando/personaje1/Invencible.png",Texture.class);





        //Sonido y Music
        manager.load("sounds/Gameplay.mp3", Music.class);
        manager.load("sounds/Invincibility.mp3",Music.class);
        manager.load("sounds/Click.mp3", Sound.class);
        manager.load("sounds/Salto.mp3",Sound.class);
        manager.load("sounds/Muerte.mp3",Sound.class);
        manager.load("sounds/gameOver.mp3",Sound.class);
        manager.load("sounds/Coin2.mp3",Sound.class);

        //Pantalla Pausa
        manager.load("pantallaPausa/Pausa.png",Texture.class);
        manager.load("pantallaPausa/Cont.png",Texture.class);
        manager.load("pantallaPausa/Reset.png",Texture.class);
        manager.load("pantallaPausa/Salir.png",Texture.class);

        //Pantalla Muerte/Game Over
        manager.load("pantallaGameOver/gg.png",Texture.class);

        //Inversos
        manager.load("pantallaPausa/ContInverso.png",Texture.class);
        manager.load("pantallaPausa/ResetInverso.png",Texture.class);
        manager.load("pantallaPausa/SalirInverso.png",Texture.class);
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0.14f, 0.69f, 0.92f);

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        spriteCargando.draw(batch);
        texto.mostrarMensaje(batch, "Loading:" + avance, ANCHO*0.8f, ALTO * 0.2f);
        batch.end();

        // Actualizar
        timerAnimacion -= delta;
        if (timerAnimacion <= 0){
            timerAnimacion = TIEMPO_FRAMES;
            spriteCargando.rotate(-45);
        }
        actualizarCarga();
    }

    private void actualizarCarga() {
        // ¿Cómo va la carga de recursos?
        if (manager.update()) {
            // Terminó!
            switch (siguientePantalla) {
                // Agregar las otras pantallas
                case JUGANDO:
                    juego.setScreen(new PantallaJugando(juego));
                    break;
            }
        }

        avance = (int)(manager.getProgress()*100);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    texturaCargando.dispose();
    }
}
