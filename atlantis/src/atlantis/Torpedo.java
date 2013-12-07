package atlantis;

import org.newdawn.slick.Image;

import atlantis.AtlantisEntity.Team;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import jig.Shape;
	
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.newdawn.slick.Animation;

public class Torpedo extends FloatingEntity {
	
	private static final String FACE_U_GRAPHIC_FILE = "atlantis/resource/Torpedo-Up.png";
	private static final String FACE_D_GRAPHIC_FILE = "atlantis/resource/Torpedo-down.png";
	private static final String FACE_L_GRAPHIC_FILE = "atlantis/resource/Torpedo-left.png";
	private static final String FACE_R_GRAPHIC_FILE = "atlantis/resource/Torpedo-right.png";
	
	private static final String MOVE_L_ANIMATION_FILE = "atlantis/resource/Torpedo-Left.png";
	private static final String MOVE_R_ANIMATION_FILE = "atlantis/resource/Torpedo-Right.png";
	private static final String MOVE_U_ANIMATION_FILE = "atlantis/resource/Torpedo-Up.png";
	private static final String MOVE_D_ANIMATION_FILE = "atlantis/resource/Torpedo-Down.png";
	
//	private static final String RED_ICON = "atlantis/resource/red-worker.png";
//	private static final String BLUE_ICON = "atlantis/resource/blue-worker.png";
	
	private static final float MAX_VELOCITY = 0.12f;       /* pixels/mS */
	
	private static final int ANIMATION_FRAMES = 1;
	private static final int ANIMATION_FRAME_DURATION = 200; /* mS */
	
	private static int ANIMATION_FRAME_WIDTH = 50; /* pixels */
	private static int ANIMATION_FRAME_HEIGHT = 200; /* pixels */
	
	public Torpedo() {
		this(0,0);
	}
	
	public Torpedo(float x, float y) {
		this(x, y, STOPPED_VECTOR);
	}

	public Torpedo(float x, float y, Vector movement_direction) {
		super(x, y, movement_direction);
	}
	
	static {
		ResourceManager.loadImage(FACE_D_GRAPHIC_FILE);
		ResourceManager.loadImage(FACE_U_GRAPHIC_FILE);
		ResourceManager.loadImage(FACE_L_GRAPHIC_FILE);
		ResourceManager.loadImage(FACE_R_GRAPHIC_FILE);	
		
		ResourceManager.loadImage(MOVE_D_ANIMATION_FILE);
		ResourceManager.loadImage(MOVE_U_ANIMATION_FILE);
		ResourceManager.loadImage(MOVE_L_ANIMATION_FILE);
		ResourceManager.loadImage(MOVE_R_ANIMATION_FILE);
		
//		ResourceManager.loadImage(RED_ICON);
//		ResourceManager.loadImage(BLUE_ICON);
	}
	
	Image torpedo;
	private static final String BLUETORPEDOIMG_RSC = "atlantis/resource/soldier-torpedo-blue.png";
	private static final String REDTORPEDOIMG_RSC = "atlantis/resource/soldier-torpedo-red.png";
	
	static {
		ResourceManager.loadImage(BLUETORPEDOIMG_RSC);
		ResourceManager.loadImage(REDTORPEDOIMG_RSC);
	}
	
	public Torpedo(final float x, final float y, double theta, Team team) {
		super(x, y);
		
		if (team == Team.BLUE) { 
			torpedo = ResourceManager.getImage(BLUETORPEDOIMG_RSC).copy();
		} else {
			torpedo = ResourceManager.getImage(REDTORPEDOIMG_RSC).copy();
		}
		torpedo.setRotation((float) theta);
		
		addImage(torpedo);
	}
	
	public void update(int delta) {
		translate(Vector.getUnit(getRotation()).scale(delta / 16f));
	}
	
	@Override
	public double getRotation() {
		return torpedo.getRotation();
	}
	
	@Override
	public void setRotation(double theta) {
		torpedo.setRotation((float) theta);
	}
	
	@Override
	void beginMovement(Vector direction) {
		velocity = new Vector(direction.scale(MAX_VELOCITY));
	}

	@Override
	boolean isHandlingCollision() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	boolean moveTo(final Vector destination_position) {
		boolean moving = false;
		Vector move_direction = new Vector(destination_position.getX()-this.getX(), destination_position.getY()-this.getY());
		beginMovement(move_direction);
		return moving;	
	}

	@Override
	Animation getMovementAnimation(Vector move_direction) {
		String animation_filename = getMovementAnimationFilename(move_direction);

		Animation movement_animation = new Animation(
				ResourceManager.getSpriteSheet(animation_filename,
						ANIMATION_FRAME_WIDTH, ANIMATION_FRAME_HEIGHT), 0, 0,
						ANIMATION_FRAMES - 1, 0, true, ANIMATION_FRAME_DURATION, true);

		return movement_animation;
	}
	
	private final String getMovementAnimationFilename(final Vector direction) {
		String animation_filename;

		if (direction.equals(LEFT_UNIT_VECTOR)) {
			animation_filename = MOVE_L_ANIMATION_FILE;		
			ANIMATION_FRAME_WIDTH = 200;
			ANIMATION_FRAME_HEIGHT = 50;
			//remove old image, add new image
			removeImage(ResourceManager.getImage(getStillImageFilename(face_direction)));
			List<Shape> shapes;
			shapes = getShapes();
			for (Shape shape: shapes)
				removeShape(shape);
			this.addImageWithBoundingBox(ResourceManager.getImage(FACE_L_GRAPHIC_FILE));
		} else if (direction.equals(UP_UNIT_VECTOR)) {
			animation_filename = MOVE_U_ANIMATION_FILE;
			ANIMATION_FRAME_WIDTH = 50;
			ANIMATION_FRAME_HEIGHT = 200;
			removeImage(ResourceManager.getImage(getStillImageFilename(face_direction)));
			List<Shape> shapes = getShapes();
			for (Shape shape: shapes)
				removeShape(shape);
			this.addImageWithBoundingBox(ResourceManager.getImage(FACE_U_GRAPHIC_FILE));
		} else if (direction.equals(RIGHT_UNIT_VECTOR)) {
			animation_filename = MOVE_R_ANIMATION_FILE;
			ANIMATION_FRAME_WIDTH = 200;
			ANIMATION_FRAME_HEIGHT = 50;
			removeImage(ResourceManager.getImage(getStillImageFilename(face_direction)));
			List<Shape> shapes = getShapes();
			for (Shape shape: shapes)
				removeShape(shape);
			this.addImageWithBoundingBox(ResourceManager.getImage(FACE_R_GRAPHIC_FILE));
		} else {
			animation_filename = MOVE_D_ANIMATION_FILE;
			ANIMATION_FRAME_WIDTH = 50;
			ANIMATION_FRAME_HEIGHT = 200;
			removeImage(ResourceManager.getImage(getStillImageFilename(face_direction)));
			List<Shape> shapes = getShapes();
			for (Shape shape: shapes)
				removeShape(shape);
			this.addImageWithBoundingBox(ResourceManager.getImage(FACE_D_GRAPHIC_FILE));
		}

		return animation_filename;
	}

	@Override
	String getStillImageFilename(Vector face_direction) {
		String graphic_filename;
		
		if (face_direction.equals(LEFT_UNIT_VECTOR)) {
			graphic_filename = FACE_L_GRAPHIC_FILE;
		} else if (face_direction.equals(UP_UNIT_VECTOR)) {
			graphic_filename = FACE_U_GRAPHIC_FILE;
		} else if (face_direction.equals(RIGHT_UNIT_VECTOR)) {
			graphic_filename = FACE_R_GRAPHIC_FILE;
		} else {
			graphic_filename = FACE_D_GRAPHIC_FILE;
		}

		return graphic_filename;
	}

	@Override
	String getIconFilename() {
//		if (this.getTeam() == null) return RED_ICON;
//		if (this.getTeam() == Team.BLUE) {
//			return BLUE_ICON;
//		} else {
//			return RED_ICON;
//		}
		return null;
	}
}
