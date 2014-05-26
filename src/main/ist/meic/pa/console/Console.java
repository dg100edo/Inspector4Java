package ist.meic.pa.console;

import ist.meic.pa.Inspector;
import ist.meic.pa.console.commands.BackInspectCommand;
import ist.meic.pa.console.commands.Command;
import ist.meic.pa.console.commands.ForwardInspectCommand;
import ist.meic.pa.console.commands.InspectFieldCommand;
import ist.meic.pa.console.commands.MethodCallCommand;
import ist.meic.pa.console.commands.ModifyFieldCommand;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Console of the inspector for java application
 * Process input and create correspondent command
 * Run the command and print the result (or error)
 */

public class Console {
	private static final String PROMPT = "> ";
	
	private static final char INSPECT = 'i';
	private static final char MODIFY_FIELD = 'm';
	private static final char METHOD_CALL = 'c';
	private static final char QUIT = 'q';
	private static final char FORWARD = 'f';
	private static final char BACK = 'b';
	private static final char HELP = 'h';
	
	private Inspector inspector;
	private Scanner input;
	private PrintStream output;
	
	public Console(Inspector inspector, InputStream in, OutputStream out){
		this.inspector = inspector;
		this.input = new Scanner(in);
		this.output = new PrintStream(out);
	}	
	
	public void start(){
		while(true){
			output.print(PROMPT);
			String line = input.nextLine();
			if(line.isEmpty()) continue;
			if(line.charAt(0) == QUIT && line.length() == 1) return;
			String result = processInput(line);
			output.println(result);
		}
	}

	private String processInput(String line) {
		String args [] = line.split(" ");
		Command command;
		if(args[0].length() > 1)
			args[0] = "-";// To invalidate commands with wrong length
		switch (args[0].charAt(0)) {
			case INSPECT:
				command = new InspectFieldCommand(args, inspector);
				break;
			case MODIFY_FIELD:
				command = new ModifyFieldCommand(args, inspector);
				break;
			case METHOD_CALL:
				command = new MethodCallCommand(args, inspector);
				break;
			case FORWARD:
				command = new ForwardInspectCommand(args, inspector);
				break;
			case BACK:
				command = new BackInspectCommand(args, inspector);
				break;
			case HELP:
				return helpMessage();
			default:
				return "Unknown command - use '" + HELP + "' to get help";
		}
		try{
			command.execute();
			return command.printResult();
		}catch(Exception e){
			//e.printStackTrace();
			return errorMessage(e, line);
		}
	}

	private String helpMessage() {
		String message = "";
		message += "COMMANDS:\n\n";
		message += INSPECT + " <field-name> \n";
		message += "	to inspect field <field-name> of current inspect object or object saved with <variable-name> \n";
		message += MODIFY_FIELD + " <field-name> <new-value> \n";
		message += "	to change value of field <field-name> to <new-value> \n";
		message += METHOD_CALL + " <method-name> <arg1> ... <argn> \n";
		message += "	to call method <method-name> of the current inspect object\n";
		message += FORWARD + " - to move forward in the inspection list\n";
		message += BACK + " - to move back in the inspection list\n";
		message += QUIT + " - to exit inspection";
		return  message;
	}

	private String errorMessage(Exception e, String input) {
		String message = "";
		message += "Error during command '" + input + "' execution: \n";
		message += e.getClass().getSimpleName() + " " + e.getMessage();
		return message;
	}	
}
