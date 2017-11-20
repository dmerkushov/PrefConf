/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.prefconf;

import java.util.prefs.Preferences;
import org.junit.Test;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class PrefConfTest {

	PrefConf prefConf;

	public PrefConfTest () {
		prefConf = PrefConf.getInstance ();
	}

	@Test
	public void testGetUserConfigurationForEnvironment () throws Exception {
		String pkgName = "ru/cniiag/agplanning";
		String envName = "std";

		Preferences prefs = prefConf.getUserConfigurationForEnvironment (PrefConfTest.class, envName);
	}

	@Test
	public void testGetSystemConfigurationForEnvironment () throws Exception {
	}

}
