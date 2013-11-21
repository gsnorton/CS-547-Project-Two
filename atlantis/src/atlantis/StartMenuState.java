package atlantis;

import jig.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StartMenuState extends BasicGameState {

	private TextField serverAddr;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		serverAddr = new TextField(container, container.getDefaultFont(),
				170, 195, 320, 25);
		serverAddr.setBackgroundColor(Color.darkGray);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.drawImage(ResourceManager.getImage(AtlantisGame.CREATE_GAME),
				280, 350);
		g.setColor(Color.orange);
		g.drawString("Server address:", 25, 200);
		serverAddr.render(container, g);
		g.drawImage(ResourceManager.getImage(AtlantisGame.JOIN_GAME),
				500, 190);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// capture mouse click
		Input input = container.getInput();
		if (input.isMousePressed(input.MOUSE_LEFT_BUTTON)) {
			int posX = input.getMouseX();
			int posY = input.getMouseY();
			if (posX > 280 && posX < 505 && posY > 350 && posY < 393) {
				// TODO: create game -- enter PlayingState with server mode
			} else if (posX > 500 && posX < 725 && posY > 190 && posY < 233) {
				String address = serverAddr.getText();
				
				/* Probably need some "Connecting" status messages, a timeout,
				 * and error handling here. This is bare bones just to get
				 * things moving.  
				 */
				
				GameStatus game_status = AtlantisGame.getGameStatus();
				
				try {
					if (game_status.connect(address)) {
						while (!game_status.connected())
							Thread.sleep(100);
					}
				} catch (Exception e) {
					System.err.println(e);
				}
				
				game.enterState(AtlantisGame.PLAYING);
				// TODO: join game -- enter PlayingState with client mode
			}
		} else if (serverAddr.hasFocus() && input.isKeyDown(Input.KEY_ENTER)) {
			String address = serverAddr.getText();
			// TODO: join game -- enter PlayingState with client mode
		}
	}

	@Override
	public int getID() {
		return AtlantisGame.MENU;
	}

}
