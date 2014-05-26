package ist.meic.pa;

import ist.meic.pa.console.Console;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Inspector for java
 * Allow us to:
 * - Inspect object fields (see type, value, modifiers etc)
 * - Modify the value of fields (for int type)
 * - Call methods (that receive int parameters)
 * - Navigate in the object graph
 */
public class Inspector {
	private Console console;
	private List<Object> inspectList;
	private int currentInspect = -1;
	private PrintStream output;
	
	public Inspector() {
		inspectList = new ArrayList<Object>();
		output = new PrintStream(System.err);
		console = new Console(this, System.in, System.err);
	}
	
	public void inspect(Object object){
		setInspect(object);
		output.println(printInspect());
		console.start();
	}
	
	public void setInspect(Object object) {
		inspectList.add(object);
		currentInspect = inspectList.size() - 1; 
	}
	
	public Object getInspect() {
		if(inspectList.isEmpty())
			return null;
		else return inspectList.get(currentInspect);
	}
	
	public void forwardInspect(){
		if(currentInspect < (inspectList.size() - 1))
			currentInspect++;
	}
	
	public void backInspect(){
		if(currentInspect > 0)
			currentInspect--;
	}

	public String printInspect() {
		Object inspect = getInspect();
		String result = inspect + " is an instance of class " + inspect.getClass().getName();
		result += "\n--------------\n";
		ArrayList<Field> fields = getFields(inspect.getClass());
		for(Field field : fields){
			field.setAccessible(true);
			int modifiers = field.getModifiers();
			if(!Modifier.isStatic(modifiers)){
				String modifier = Modifier.toString(modifiers);
				String type = field.getType().getName();
				String name = field.getName();
				String value = "";
				try {
					value = field.get(inspect).toString();
				} catch (Exception e) {/*intentional- means that field has no value*/}
				result += modifier + " " + type + " " + name + (value.isEmpty() ? "" : " = " + value) + "\n"; 
			}
		}
		return result;
	}
	
	public ArrayList<Field> getFields(Class<?> cls){
		ArrayList<Field> fields = new ArrayList<Field>(Arrays.asList(cls.getDeclaredFields()));
	    Class<?> superClass = cls.getSuperclass();
	    if(superClass != null){	    	
			ArrayList<Field> superClassFields = getFields(superClass);
			fields.addAll(superClassFields);
		}
	    return fields;
	}
	
	public Field getField(Class<?> cls, String fieldName){
		ArrayList<Field> fields = getFields(cls);
		for(Field field : fields){
			if(field.getName().equals(fieldName))
				return field;
		}
		return null;
	}
	
	public Method getMethod(Class<?> cls, String methodName, Class<?> [] paramethersType) throws NoSuchMethodException{
		try {
			Method method = cls.getDeclaredMethod(methodName, paramethersType);
			return method;
		} catch (NoSuchMethodException e) {
			Class<?> objectSuperClass = cls.getSuperclass();
			if(objectSuperClass != null)
				return getMethod(objectSuperClass, methodName, paramethersType);
			throw e;
		}
	}
}
