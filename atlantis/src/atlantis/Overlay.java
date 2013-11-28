package atlantis;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;
import jig.Vector;

public class Overlay {
	Image overlay;
	Image actionMove;
	Image actionAttack;
	Image targetMove;
	Image targetAttack;
	Image pixel;
	
	boolean isDefaultCursorSet = true;
	
	PlayingState playingState;
	public AtlantisEntity selectedUnit = null;
	public short action = 0; // 1 = move, 2 = attack
	
	public Overlay(PlayingState ps) {
		playingState = ps;
		
		// TODO: model of unit on left, minimap on right
		overlay = ResourceManager.getImage(AtlantisGame.OVERLAY);
		actionMove = ResourceManager.getImage(AtlantisGame.ACTION_MOVE);
		actionAttack = ResourceManager.getImage(AtlantisGame.ACTION_ATTACK);
		targetMove = ResourceManager.getImage(AtlantisGame.TARGET_MOVE);
		targetAttack = ResourceManager.getImage(AtlantisGame.TARGET_ATTACK);
		pixel = ResourceManager.getImage(AtlantisGame.PIXEL);
	}
	
	
	// TODO: should also bind actions to hotkeys in another class
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		Input input = container.getInput();
		int x = input.getMouseX();
		int y = input.getMouseY();
		
		if (selectedUnit != null) { // highlight selected unit
			g.setColor(Color.yellow);
			g.drawRect(selectedUnit.getCoarseGrainedMinX(),
					selectedUnit.getCoarseGrainedMinY(),
					selectedUnit.getCoarseGrainedWidth(),
					selectedUnit.getCoarseGrainedHeight());
		}
		
		if (y > 470) { // select action
			if (!isDefaultCursorSet) {
				container.setMouseCursor(AtlantisGame.cursor, 0, 0);
				isDefaultCursorSet = true;
			}
		} else if (action == 0) { // select unit
			if (!isDefaultCursorSet) {
				container.setMouseCursor(AtlantisGame.cursor, 0, 0);
				isDefaultCursorSet = true;
			}
			
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				List<Worker> workers = playingState.getStatus().getWorkers();
				for (Worker worker : workers) {
					if (y > worker.getCoarseGrainedMinY() &&
							y < worker.getCoarseGrainedMaxY() &&
							x > worker.getCoarseGrainedMinX() &&
							x < worker.getCoarseGrainedMaxX()) {
						selectedUnit = worker;
						break;
					}
				}
			}
		} else if (action == 1) { // move
			if (isDefaultCursorSet) {
				container.setMouseCursor(pixel, 0, 0);
				isDefaultCursorSet = false;
			}
			g.drawImage(targetMove, x, y);
			
			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ||
					input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
				System.out.println(selectedUnit.moveTo(new Vector(x, y)));
				action = 0;
				selectedUnit = null;
			}
		} else if (action == 2) { // attack
			if (isDefaultCursorSet) {
				container.setMouseCursor(pixel, 0, 0);
				isDefaultCursorSet = false;
			}
			g.drawImage(targetAttack, x, y);
		}
		
		g.drawImage(overlay, 0, 470);
		if (selectedUnit != null) {
			g.drawImage(actionMove, 290, 520);
			g.drawImage(actionAttack, 350, 520);
		}
		
		if (y > 520 && y < 570 && selectedUnit != null) {
			if (x > 290 && x < 340) { // move button
				// tooltip
				x += 20;
				g.setColor(Color.yellow);
				g.fillRect(x, y, 40, 20);
				g.setColor(Color.black);
				g.drawString("Move", x, y);
				
				if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) action = 1;
			} else if (x > 350 && x < 400) { // attack button
				// tooltip
				x += 20;
				g.setColor(Color.yellow);
				g.fillRect(x, y, 60, 20);
				g.setColor(Color.black);
				g.drawString("Attack", x, y);
				
				if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) action = 2;
			}
		}
	}
}