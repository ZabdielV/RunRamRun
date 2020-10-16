package mx.itesm.equipo5;

import com.badlogic.gdx.Game;

public class Juego extends Game {
	@Override
	public void create () {
		setScreen(new PantallaMenu(this));  //Pasamos el controlador (.setScreen)
	}

	@Override
	public void render () {
	super.render();
	}
	
	@Override
	public void dispose () {

	}
}
