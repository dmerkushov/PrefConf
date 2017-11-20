/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.prefconf;

import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class PrefConf {

	public static final String ENV_NODE_PREFIX = "PREFCONF_ENVIRONMENT_";
	public static final String DEFAULT_VAL = "IMPOSSIBLE_9b754cfe-88da-4a78-902c-53743dba4efe";

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

	private void copyPrefsNode (Preferences src, Preferences target) throws BackingStoreException {
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
	}

	private Preferences getConfigurationForEnvironment (String packagePath, String environmentName, Preferences rootNode) throws BackingStoreException {
		String envPackageName;
		if (environmentName == null || environmentName.equals ("")) {
			envPackageName = packagePath;
		} else {
			envPackageName = ENV_NODE_PREFIX + environmentName + "/" + packagePath;
		}

		Preferences envPrefs = rootNode.node (envPackageName);
		if (!envPackageName.equals (packagePath)) {
			Preferences origPrefs = rootNode.node (packagePath);

			copyPrefsNode (origPrefs, envPrefs);
			envPrefs.flush ();
		}
		return envPrefs;
	}

	public Preferences getUserConfigurationForEnvironment (Class clazz, String environmentName) throws BackingStoreException {
		Objects.requireNonNull (clazz, "clazz");

		return getUserConfigurationForEnvironment (clazz.getPackage (), environmentName);
	}

	public Preferences getUserConfigurationForEnvironment (Package pkg, String environmentName) throws BackingStoreException {
		Objects.requireNonNull (pkg, "pkg");

		return getUserConfigurationForEnvironment (pkg.getName ().replaceAll ("\\.", "/"), environmentName);
	}

	public Preferences getUserConfigurationForEnvironment (String nodePath, String environmentName) throws BackingStoreException {
		Objects.requireNonNull (nodePath, "nodePath");

		return getConfigurationForEnvironment (nodePath, environmentName, Preferences.userRoot ());
	}

	public Preferences getSystemConfigurationForEnvironment (Class clazz, String environmentName) throws BackingStoreException {
		Objects.requireNonNull (clazz, "clazz");

		return getSystemConfigurationForEnvironment (clazz.getPackage (), environmentName);
	}

	public Preferences getSystemConfigurationForEnvironment (Package pkg, String environmentName) throws BackingStoreException {
		Objects.requireNonNull (pkg, "pkg");

		return getSystemConfigurationForEnvironment (pkg.getName ().replaceAll ("\\.", "/"), environmentName);
	}

	public Preferences getSystemConfigurationForEnvironment (String nodePath, String environmentName) throws BackingStoreException {
		Objects.requireNonNull (nodePath, "nodePath");

		return getConfigurationForEnvironment (nodePath, environmentName, Preferences.systemRoot ());
	}

}
