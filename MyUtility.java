import java.util.*;
import javax.swing.*;
import java.lang.reflect.*;

/*
 * Programmer: Tyrone Tolbert, Jr,
 * File name: MyUtility.java
 * 
 * 
 */

public class MyUtility {
	static Map map = new HashMap();
	static int[][][] a1 = {{{1,2},{3},{4}},{{5}},{{6}}};
	static int[][][][] a2 = {{{{7,8}}},{{{9,10},{11},{12}},{{13}},{{14}}}};
	static int[][][][][] a3 = {{{{{15,16}}}},{{{{17,18}}},{{{19,20},{21},{22}},{{23}},{{24}}}}};
	
	//Menu Options
	static final String[] INITIAL_OPTIONS = 
		{"1. Display Array Contents",
		 "2. Clone an Array",
		 "3. Modify an Array",
		 "4. Invoke Instance Method",
		 "5. View non-array Instance",
		 "6. Exit"
		};
	static final String[] ARGUMENT_OPTIONS = 
		{"1. Reference Name",
		 "2. Primitive Value",
		 "3. String Value"
		};
	static final String[] PRIMITIVE_OPTIONS = 
		{"1. boolean", "2. byte", "3. short",
		 "4. int", "5. long", "6. float",
		 "7. double", "8. char"
		};
	
	public static Object aClone(Object array) throws Exception { 
		//Recursively clones a multi-dimensional array
		Object a = Array.newInstance(array.getClass().getComponentType(), Array.getLength(array));
		if (!array.getClass().getComponentType().isArray()) { //If component not array, no need to aClone
			for (int i = 0; i < Array.getLength(array); i++)
				Array.set(a, i, Array.get(array, i));
			return a;
		}                                                     //If component is array, must aClone again
		for (int i = 0; i < Array.getLength(array); i++)
			Array.set(a, i, aClone(Array.get(array, i)));
		return a;
	}
	
	public static void promptForaClone() {
		//Menu dialog, leading to aClone method call
		String instanceName;
		do { // Get instance name
			instanceName = (String)JOptionPane.showInputDialog(null, "Instance Name?", "Call MyUtility.aClone()", JOptionPane.PLAIN_MESSAGE);
			if (instanceName == null)
				return;
		} while (!map.containsKey(instanceName));
		try {
			map.put((String)JOptionPane.showInputDialog(null, "Return Value Name?", "Call MyUtility.aClone()", JOptionPane.PLAIN_MESSAGE), aClone(map.get(instanceName)));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Caught exception ".concat(e.toString()));
		}
	}
	
	public static Object aModify(Object a1, int[] dim1,
								 Object a2, int[] dim2) {
		//Replaces a portion of one array with another array
		try {
			Object r = aClone(a2);
			Object firstAr = a1,
				   secondAr = r;
			for (int i = 0; i < dim1.length; i++) {
				firstAr = Array.get(firstAr, dim1[i]);
			}
			for (int i = 0; i < dim2.length-1; i++)
				secondAr = Array.get(secondAr, dim2[i]);
			Array.set(secondAr, dim2[dim2.length-1], firstAr);
			return r;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Caught exception ".concat(e.toString()));
		}
		return null;
	}
	
	public static void promptForModify() {
		//Menu dialog for aModify call
		String instanceNameC, instanceNameM;
		do { // Array to copy
			instanceNameC = (String)JOptionPane.showInputDialog(null, "Instance Name to Copy?", "Modify an Array", JOptionPane.PLAIN_MESSAGE);
			if (instanceNameC == null)
				return;
		} while (!map.containsKey(instanceNameC));
		
		String[] dimC = ((String)JOptionPane.showInputDialog(null, "Dimensions? Separate with spaces", "Modify an Array", JOptionPane.PLAIN_MESSAGE)).split(" ");
		
		do { // Array to Modify
			instanceNameM = (String)JOptionPane.showInputDialog(null, "Instance Name to Modify?", "Modify an Array", JOptionPane.PLAIN_MESSAGE);
			if (instanceNameM == null)
				return;
		} while (!map.containsKey(instanceNameM));
		
		String[] dimM = ((String)JOptionPane.showInputDialog(null, "Dimensions? Separate with spaces", "Modify an Array", JOptionPane.PLAIN_MESSAGE)).split(" ");
		
		try {
			map.put((String)JOptionPane.showInputDialog(null, "Return Value Name?", "Modify an Array", JOptionPane.PLAIN_MESSAGE), aModify(map.get(instanceNameC), toIntAr(dimC), map.get(instanceNameM), toIntAr(dimM)));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Caught exception ".concat(e.toString()));
		}
	}
	
	public static int[] toIntAr(String[] s) {
		//Converts array of String values to an array of int values
		int[] r = new int[s.length];
		for (int i = 0; i < r.length; i++)
			r[i] = Integer.parseInt(s[i]);
		return r;
	}
	
	public static void aDisplay() {
		//Displays array in text form in a JTextArea
		String instanceName;
		do { // Get instance name
			instanceName = (String)JOptionPane.showInputDialog(null, "Instance Name?", "Display Array Contents", JOptionPane.PLAIN_MESSAGE);
			if (instanceName == null)
				return;
		} while (!map.containsKey(instanceName));
		JTextArea text = new JTextArea(11,20);
		JScrollPane scroll = new JScrollPane(text);
		try {
			display(map.get(instanceName), text);
			JOptionPane.showMessageDialog(null, scroll, "Display " + instanceName, JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Caught exception ".concat(e.toString()));
		}
	}
	
	public static void display(Object array, JTextArea text) throws Exception { 
		//Recursively adds elements to the TextArea
		if (!array.getClass().getComponentType().isArray()) { //If component not array
			String s = "";
			for (int i = 0; i < Array.getLength(array); i++)
				s = s + Array.get(array, i) + " ";
			text.append(s + "\n");
			return;
		}                                                     //If component is array
		for (int i = 0; i < Array.getLength(array); i++)
			display(Array.get(array, i), text);
		return;
	}

	public static void invokeInstanceMethod() {
		/*Invokes instance method to object existing in the Map
		class name, method name and argument must be provided
		
		If the method takes no argument, enter "null" or "" (no text)
		If it takes multiple arguments, separate them with spaces
		
		If the method returns a value, the value will be stored in the Map
		otherwise, it is simply displayed
		
		EXAMPLE
		
		Object of type String in the map is called theString with value "Abc"
		We want to call "Abc".endsWith("c") and store return value true with the key theBool
		
		The instance name would be theString
		The method name would be endsWith
		The argument type would be java.lang.String
		The argument value would be java.lang.String, with value "c"
		The return key would be theBool, stored in the Map with value "c"
		
		*/
		
		String instanceName;
		do { // Get instance name
			instanceName = (String)JOptionPane.showInputDialog(null, "Instance Name?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE);
			if (instanceName == null)
				return;
		} while (!map.containsKey(instanceName));
		
		try { // Get the method
		String methodName = (String)JOptionPane.showInputDialog(null, "Method Name?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE);
		String[] methodArgumentTypes = ((String)JOptionPane.showInputDialog(null, "Method Argument Types?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE)).split(" ");

		Class instance = (map.get(instanceName)).getClass();

		Class[] argumentTypes = new Class[methodArgumentTypes.length];
		if (methodArgumentTypes[0].equals("null") || methodArgumentTypes[0].equals(""))
			argumentTypes = null;
		else
			for (int i=0; i < argumentTypes.length; i++)
				argumentTypes[i] = Class.forName(methodArgumentTypes[i]);
			Method method = instance.getMethod(methodName, argumentTypes);
			Object[] argumentValues = new Object[methodArgumentTypes.length];
			if (argumentTypes == null)
				argumentValues = null;
			else
				for (int i=0; i < argumentValues.length; i++) {
					int argType = Integer.parseInt(((String)(JOptionPane.showInputDialog(null,
							/*int value of selection */ "Type of arg value?",
														"Argument Values",
														JOptionPane.QUESTION_MESSAGE, null,
														ARGUMENT_OPTIONS, ARGUMENT_OPTIONS[0]))).substring(0,1));
					switch (argType) {
					case 1: // Reference Name
						String referenceName;
						do {
							referenceName = (String)JOptionPane.showInputDialog(null, "Reference Name?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE);
							if (referenceName.toUpperCase().equals("NEVER MIND"))
								return;
							} while (!map.containsKey(referenceName));
						argumentValues[i] = map.get(referenceName);
						break;
					case 2: // Primitive Value
						argumentValues[i] = getPrimitiveValue();
						break;
					case 3: // String Value
						argumentValues[i] = (String)JOptionPane.showInputDialog(null, "String Value?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE);
						break;
					}
				}
			
			if (method.getReturnType().toString().equals("void"))
				method.invoke(map.get(instanceName), argumentValues);
			else
				map.put((String)JOptionPane.showInputDialog(null, "Return Value Name?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE), method.invoke(map.get(instanceName), argumentValues));
			JOptionPane.showMessageDialog(null,"Successfully called method ".concat(instanceName).concat(".").concat(methodName));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Caught exception ".concat(e.toString()));
		}
	}
	
	static Object getPrimitiveValue() {
		Object r = null;
		int primType = Integer.parseInt(((String)(JOptionPane.showInputDialog(null,
				/*int value of selection */ "Type of prim value?",
											"Argument Values",
											JOptionPane.QUESTION_MESSAGE, null,
											PRIMITIVE_OPTIONS, PRIMITIVE_OPTIONS[0]))).substring(0,1));
		switch (primType) {
		case 1: //boolean
			Object[] bool = {"True", "False"};
			r = Boolean.valueOf(JOptionPane.showOptionDialog(null,
				    "Value of the boolean", "True/False", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, bool, bool[0]) == 0);
			break;
		case 2: //byte
			r = Byte.valueOf((String)JOptionPane.showInputDialog(null, "Value?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE));
			break;
		case 3: //short
			Short.valueOf((String)JOptionPane.showInputDialog(null, "Value?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE));
			break;
		case 4: //int
			r = Integer.valueOf((String)JOptionPane.showInputDialog(null, "Value?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE));
			break;
		case 5: //long
			r = Long.valueOf((String)JOptionPane.showInputDialog(null, "Value?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE));
			break;
		case 6: //float
			r = Float.valueOf((String)JOptionPane.showInputDialog(null, "Value?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE));
			break;
		case 7: //double
			r = Double.valueOf((String)JOptionPane.showInputDialog(null, "Value?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE));
			break;
		case 8: //char
			r = (Character)(((String)JOptionPane.showInputDialog(null, "Method Name?", "Invoke Instance Method", JOptionPane.PLAIN_MESSAGE)).charAt(0));
			break;
		}
		return r;
	}
	
	static void viewInstance() {
		String instanceName;
		do { // Get instance name
			instanceName = (String)JOptionPane.showInputDialog(null, "Instance Name?", "View non-array Instance", JOptionPane.PLAIN_MESSAGE);
			if (instanceName == null)
				return;
		} while (!map.containsKey(instanceName));
		try {
			JOptionPane.showMessageDialog(null, map.get(instanceName));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Caught exception ".concat(e.toString()));
		}
	}
	
	public static void main(String[] args) {
		
		System.out.println();
		map.put("a1", a1);
		map.put("a2", a2);
		map.put("a3", a3);
		JOptionPane.showMessageDialog(null, "Assignment 2: MyUtility.java\n"
				+ "Arrays with keys a1, a2, a3\n"
				+ "are initially put in the map");
		do {
			try {
				int selection = Integer.parseInt(((String)(JOptionPane.showInputDialog(null,
				/*int value of selection */ "Select from the following options.",
											"Arrays",
											JOptionPane.QUESTION_MESSAGE, null,
											INITIAL_OPTIONS, INITIAL_OPTIONS[0]))).substring(0,1));
				switch (selection) {
				case 1: aDisplay();
						break;
				case 2: promptForaClone();
						break;
				case 3: promptForModify();
						break;
				case 4: invokeInstanceMethod();
						break;
				case 5: viewInstance();
						break;
				case 6: System.exit(0);
				
				}
			} catch (NullPointerException e) { // User hits "Cancel" at the home window
				System.exit(0);
			}
		} while (true);
	}
}
