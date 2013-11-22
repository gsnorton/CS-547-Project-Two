package atlantis.networking;

import java.io.Serializable;
import java.util.ArrayList;

public class CommandLockStep implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Command> frameCommands;
	int frameNum;
	
	public CommandLockStep(int frameNum) {
		this.frameNum = frameNum;
		frameCommands = new ArrayList<Command>();
	}
	
	public void addCommand(Command command) {
		frameCommands.add(command);
	}
}
