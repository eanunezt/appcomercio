/**
 * 
 */
package com.comercio.pedidos.excepciones;

/**
 * @author enunezt
 *
 */
public class ValidacionExcepcion extends Exception {
	 /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ValidacionExcepcion() {
        super();
    }

	public ValidacionExcepcion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ValidacionExcepcion(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ValidacionExcepcion(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ValidacionExcepcion(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	

}
