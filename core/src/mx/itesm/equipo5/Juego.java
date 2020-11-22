package mx.itesm.equipo5;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class Juego extends Game {

	private AssetManager manager;

	@Override
	public void create () {
		manager = new AssetManager();
		setScreen(new PantallaMenu(this));  //Pasamos el controlador (.setScreen)
	}

	public AssetManager getManager(){
		return manager;
	}

	@Override
	public void render () {
	super.render();
	}
	
	@Override
	public void dispose () {

	}
}
