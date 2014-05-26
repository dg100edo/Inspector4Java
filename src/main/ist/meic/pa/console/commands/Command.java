package ist.meic.pa.console.commands;

import ist.meic.pa.Inspector;

/**
 * Inspector4Java abstract command
 * Have the arguments of the command and the result
 * Each concrete command implement the execute and printResult methods
 * Each command know the inspector that he perform work for (the context)
 */
public abstract class Command {
	protected Inspector inspector;
	protected String[] args;
	protected Object result;
	
	public Command(String[] args, Inspector inspector) {
		this.args = args;
		this.inspector = inspector;
	}

	public Object getResult(){
		return result;		
	}
	
	public abstract String printResult();
		
	public abstract void execute() throws Exception;
}
