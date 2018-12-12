package de.fu_berlin.inf.dpp.ui.browser_functions

import org.apache.log4j.Logger
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public object TypeRefinery {
	
	val LOG = Logger.getLogger(TypeRefinery::class.java)
	val gson = Gson()

	@JvmStatic
	public fun <T> refine ( argument : Any?, targetType: Class<T> ): T?{
		if(argument == null){
			return null
		}
		
		when(argument){
			is Boolean -> return getTypedArgument(argument, targetType)
			is Double -> return getTypedArgument(argument, targetType)
			is String -> return getTypedArgument(argument, targetType)
		}
		
		var argTypeName = argument::class.java.name
		var targetTypeName = targetType.name
		LOG.warn("Tried to refine argument of unexpected type " + argTypeName
            + " to " + targetTypeName)
		throw IllegalArgumentException("Cannot refine object of type "
            + argTypeName + " to " + targetTypeName)
	}
	
	/**
     * Arguments passed in as Strings were <code>string</code>s in JavaScript.
     * They can represent actual Strings or JSON objects.
     * 
     * @param jsArgument
     *            As given by the browser from the JavaScript world. Needs to be
     *            a plain String or a JSON-ized object.
     * @param targetType
     *            The type the Java world actually expects
     * @return A String, if targetType is <code>String.class</code>;
     *         deserialized object otherwise.
     */
	@Suppress("UNCHECKED_CAST")
	inline fun <T> getTypedArgument(jsArgument : String, targetType : Class<T>):T{
		if(targetType.equals(String::class.java)){
			return jsArgument as T	
		}
		
		var javaArgument :T?
		try{
			javaArgument = gson.fromJson(jsArgument, targetType)
		} catch ( e: JsonSyntaxException){
			LOG.error("could not deserialize given argument " + jsArgument
                + " to instance of class " + targetType.getName())
			throw IllegalArgumentException()
		}
		
		return javaArgument as T
	}
	
	/**
     * Arguments passed in as Booleans were <code>boolean</code>s in JavaScript.
     * They always represent Booleans.
     * 
     * @param jsArgument
     *            As given by the browser from the JavaScript world
     * @param targetType
     *            Must be <code>Boolean.class</code>
     */
	@Suppress("UNCHECKED_CAST")
	inline fun <T> getTypedArgument(jsArgument : Boolean, targetType : Class<T>):T{		
		if (targetType.name.toLowerCase().contains("boolean")) {
            LOG.error("expected to see an instance of class "
                + targetType.getName() + ", but got a Boolean instead")
			throw IllegalArgumentException()
		}
		
		return jsArgument as T
	}
	
	/**
     * Arguments passed in as Doubles were <code>number</code>s in JavaScript.
     * They can represent actual Doubles, Floats, Longs, or Integers, or their
     * primitive counterparts.
     * 
     * @param jsArgument
     *            As given by the browser from the JavaScript world
     * @param targetType
     *            Must be any of <code>Double.class</code>,
     *            <code>Float.class</code>, <code>Long.class</code>, or
     *            <code>Integer.class</code>, or their primitive counterparts
     * @return A Double, Float, Long, or Integer
     */
    @Suppress("UNCHECKED_CAST")
	fun <T> getTypedArgument(jsArgument : Double, targetType : Class<T>):T{
		when(targetType){
			Integer::class.java -> return jsArgument.toInt() as T
			Int::class.java -> return jsArgument.toInt() as T
			Long::class.java -> return jsArgument.toLong() as T
			Float::class.java -> return jsArgument.toFloat() as T
			Double::class.java -> return jsArgument.toDouble() as T
		}
		
        LOG.error("expected to see an instance of class "
            + targetType.getName() + ", but got a Double instead");
        throw IllegalArgumentException();
	}
}