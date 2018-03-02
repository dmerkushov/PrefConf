/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.prefconf;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class PrefConfException extends RuntimeException {

	public PrefConfException () {
	}

	public PrefConfException (String message) {
		super (message);
	}

	public PrefConfException (String message, Throwable cause) {
		super (message, cause);
	}

	public PrefConfException (Throwable cause) {
		super (cause);
	}

}
