package ist.meic.pa.console.commands;

import java.lang.reflect.Field;

import ist.meic.pa.Inspector;
import ist.meic.pa.exception.CommandExecutionException;

public class ModifyFieldCommand extends Command {

	public ModifyFieldCommand(String[] args, Inspector inspector) {
		super(args, inspector);
	}

	@Override
	public void execute() throws Exception {
		if(args.length < 3)
			throw new CommandExecutionException("input error - expected command format is 'm <field-name> <new-value>'");
		Object inspect = inspector.getInspect();
		String fieldName = args[1];
		int newValue = Integer.parseInt(args[2]);
		Field field = inspector.getField(inspect.getClass(), fieldName);
		field.setAccessible(true);
		field.set(inspect, newValue);	
	}

	@Override
	public String printResult() {
		return inspector.printInspect();
	}
}
