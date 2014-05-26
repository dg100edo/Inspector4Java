package ist.meic.pa.console.commands;

import ist.meic.pa.Inspector;

public class BackInspectCommand extends Command {

	public BackInspectCommand(String[] args, Inspector inspector) {
		super(args, inspector);
	}

	@Override
	public void execute() {
		inspector.backInspect();
	}

	@Override
	public String printResult() {
		return inspector.printInspect();
	}

}
