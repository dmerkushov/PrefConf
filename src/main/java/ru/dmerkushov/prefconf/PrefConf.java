/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.prefconf;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class PrefConf {

	public static final String ENV_NODE_PREFIX = "PREFCONF_ENVIRONMENT_";
	public static final String DEFAULT_VAL = "IMPOSSIBLE_9b754cfe-88da-4a78-902c-53743dba4efe";
	public static final AtomicReference defaultEnvironmentName = new AtomicReference ("default");

	////////////////////////////////////////////////////////////////////////////
	// PrefConf is a singleton class
	////////////////////////////////////////////////////////////////////////////
	private static PrefConf _instance;

	/**
	 * Get the single instance of PrefConf
	 *
	 * @return The same instance of PrefConf every time the method is called
	 */
	public static synchronized PrefConf getInstance () {
		if (_instance == null) {
			_instance = new PrefConf ();
		}
		return _instance;
	}

	private PrefConf () {
	}
	////////////////////////////////////////////////////////////////////////////

	private void copyPrefsNode (Preferences src, Preferences target) {
		try {
			String[] subnodeNames = src.childrenNames ();

			for (String subnodeName : subnodeNames) {
				Preferences srcSubnodePrefs = src.node (subnodeName);
				Preferences targetSubnodePrefs = target.node (subnodeName);
				copyPrefsNode (srcSubnodePrefs, targetSubnodePrefs);
			}

			String[] keys = src.keys ();

			for (String key : keys) {
				String value = src.get (key, DEFAULT_VAL);
				target.put (key, value);
			}
		} catch (BackingStoreException | RuntimeException ex) {
			throw new PrefConfException (ex);
		}
	}

	private Preferences getConfigurationForEnvironment (String packagePath, String environmentName, Preferences rootNode) {
		try {
			if (environmentName == null || environmentName.equals ("")) {
				environmentName = getDefaultEnvironmentName ();
			}
			String envPackageName = ENV_NODE_PREFIX + environmentName + "/" + packagePath;

			Preferences envPrefs = rootNode.node (envPackageName);
			if (!envPackageName.equals (packagePath)) {
				Preferences origPrefs = rootNode.node (packagePath);

				copyPrefsNode (origPrefs, envPrefs);
				envPrefs.flush ();
			}
			return envPrefs;
		} catch (BackingStoreException | RuntimeException ex) {
			throw new PrefConfException (ex);
		}
	}

	public Preferences getUserConfigurationForEnvironment (Class clazz, String environmentName) {
		Objects.requireNonNull (clazz, "clazz");

		return getUserConfigurationForEnvironment (clazz.getPackage (), environmentName);
	}

	public Preferences getUserConfiguration (Class clazz) {
		return getUserConfigurationForEnvironment (clazz, null);
	}

	public Preferences getUserConfigurationForEnvironment (Package pkg, String environmentName) {
		Objects.requireNonNull (pkg, "pkg");

		return getUserConfigurationForEnvironment (pkg.getName ().replaceAll ("\\.", "/"), environmentName);
	}

	public Preferences getUserConfiguration (Package pkg) {
		return getUserConfigurationForEnvironment (pkg, null);
	}

	public Preferences getUserConfigurationForEnvironment (String nodePath, String environmentName) {
		Objects.requireNonNull (nodePath, "nodePath");

		return getConfigurationForEnvironment (nodePath, environmentName, Preferences.userRoot ());
	}

	public Preferences getUserConfiguration (String nodePath) {
		return getUserConfigurationForEnvironment (nodePath, null);
	}

	public Preferences getSystemConfigurationForEnvironment (Class clazz, String environmentName) {
		Objects.requireNonNull (clazz, "clazz");

		return getSystemConfigurationForEnvironment (clazz.getPackage (), environmentName);
	}

	public Preferences getSystemConfigurationForEnvironment (Class clazz) {
		return getSystemConfigurationForEnvironment (clazz, null);
	}

	public Preferences getSystemConfigurationForEnvironment (Package pkg, String environmentName) {
		Objects.requireNonNull (pkg, "pkg");

		return getSystemConfigurationForEnvironment (pkg.getName ().replaceAll ("\\.", "/"), environmentName);
	}

	public Preferences getSystemConfigurationForEnvironment (Package pkg) {
		return getSystemConfigurationForEnvironment (pkg, null);
	}

	public Preferences getSystemConfigurationForEnvironment (String nodePath, String environmentName) {
		Objects.requireNonNull (nodePath, "nodePath");

		return getConfigurationForEnvironment (nodePath, environmentName, Preferences.systemRoot ());
	}

	public Preferences getSystemConfigurationForEnvironment (String nodePath) {
		return getSystemConfigurationForEnvironment (nodePath, null);
	}

	public static String getDefaultEnvironmentName () {
		return defaultEnvironmentName.toString ();
	}

	public static void setDefaultEnvironmentName (String defaultEnvironmentName) {
		Objects.requireNonNull (defaultEnvironmentName, "defaultEnvironmentName");

		PrefConf.defaultEnvironmentName.set (defaultEnvironmentName);
	}

}
