package ist.meic.pa.console.commands;

import ist.meic.pa.Inspector;
import ist.meic.pa.exception.CommandExecutionException;

import java.lang.reflect.Field;

public class InspectFieldCommand extends Command {

	private Field field;
	
	public InspectFieldCommand(String[] args, Inspector inspector) {
		super(args, inspector);
	}

	@Override
	public void execute() throws Exception {		
		if(args.length < 2)
			throw new CommandExecutionException("input error - expected command format is 'i <field-name>'");
		Object inspect = inspector.getInspect();
		String fieldName = args[1];
		Field field = inspector.getField(inspect.getClass(), fieldName);
		field.setAccessible(true);
		
		this.field = field;
		
		if(!field.getType().isPrimitive())
			inspector.setInspect(field.get(inspect));
	}

	@Override
	public String printResult() {
		if(!field.getType().isPrimitive()){
			return inspector.printInspect();
		}else{
			try {
				return field.get(inspector.getInspect()).toString();
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}			
	}
}
