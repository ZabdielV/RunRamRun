package mx.itesm.equipo5;

import com.badlogic.gdx.Screen;
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
    private Pantalla siguientePantalla;

    // Carga
    private int avance;

    // Mensajes
    private Texto texto;

    public PantallaCargando(Juego juego, Pantalla siguientePantalla) {
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

    private void cargarRecursos(Pantalla siguientePantalla) {
        manager = juego.getManager();
        avance = 0;
        cargarRecursosJuego();
    }

    private void cargarRecursosJuego() {
        manager.load("sounds/Gameplay.mp3", Music.class);
        manager.load("sounds/Invincibility.mp3",Music.class);
        manager.load("sounds/Click.mp3", Sound.class);
        manager.load("sounds/Salto.mp3",Sound.class);
        manager.load("sounds/Muerte.mp3",Sound.class);
        manager.load("sounds/gameOver.mp3",Sound.class);
        manager.load("sounds/Coin2.mp3",Sound.class);

        manager.finishLoading();
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0.3f,0.3f,0.7f);

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        spriteCargando.draw(batch);
        texto.mostrarMensaje(batch, "Cargando:" + avance, ANCHO*0.8f, ALTO * 0.2f);
        batch.end();

        // Actualizar
        timerAnimacion -= delta;
        if (timerAnimacion <= 0){
            timerAnimacion = TIEMPO_FRAMES;
            spriteCargando.rotate(15);
        }
        actualizarCarga();
    }

    private void actualizarCarga() {

        if (manager.update()){ // Terminó
            juego.setScreen(new PantallaJugando(juego));
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

    }
}
