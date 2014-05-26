package ist.meic.pa.console.commands;

import ist.meic.pa.Inspector;
import ist.meic.pa.exception.CommandExecutionException;

import java.lang.reflect.Method;

public class MethodCallCommand extends Command {

	public MethodCallCommand(String[] args, Inspector inspector) {
		super(args, inspector);
	}

	@Override
	public void execute() throws Exception{
		if(args.length < 2)
			throw new CommandExecutionException("input error - expected command format is 'm <method-name> <arg-1> ... <arg-n>'");
		Object inspect = inspector.getInspect();
		String methodName = args[1];
		int methodNumArgs = args.length - 2;
		
		Class<?> [] paramethersType = new Class<?>[methodNumArgs];
		Object [] paramethers = new Object[methodNumArgs];
		
		for(int i = 0; i < methodNumArgs; i++){
			paramethersType[i] = int.class;
			paramethers[i] = Integer.parseInt(args[i + 2]);
		}
		
		Method method = inspector.getMethod(inspect.getClass(), methodName, paramethersType);	
		method.setAccessible(true);
		result = method.invoke(inspect, paramethers);
	}

	@Override
	public String printResult() {
		return result.toString();
	}

}
