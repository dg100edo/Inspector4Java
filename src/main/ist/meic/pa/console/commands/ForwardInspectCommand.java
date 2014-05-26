package ist.meic.pa.console.commands;

import ist.meic.pa.Inspector;

public class ForwardInspectCommand extends Command {

	public ForwardInspectCommand(String[] args, Inspector inspector) {
		super(args, inspector);
	}

	@Override
	public void execute() {
		inspector.forwardInspect();
	}

	@Override
	public String printResult() {
		return inspector.printInspect();
	}

}
