/*******************************************************************************
 * Copyright (c) 2009, 2017 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *    
 *******************************************************************************/
package org.jacoco.dev.bytecode;

import java.io.PrintWriter;

import org.jacoco.dev.bytecode.tools.ClassDumper;
import org.jacoco.dev.bytecode.tools.FrameRecalculator;
import org.jacoco.dev.bytecode.tools.TargetLoader;
import org.junit.Test;

public class AALOADnullTest {

	public static class Target implements Runnable {

		public void run() {
			Object[] array = null;
			Object element = array[0];
			// force frame
			if (dummy()) {
				dummy();
			}
			// span scope
			array.toString();
			element.toString();
		}

		static boolean dummy() {
			return false;
		}

	}

	@Test
	public void test() throws Exception {
		PrintWriter out = new PrintWriter(System.out);
		scenario(out);
		out.flush();
	}

	public static void scenario(PrintWriter out) throws Exception {
		byte[] targetClass = TargetLoader.getClassDataAsBytes(Target.class);

		out.println("1. Original Class File");
		ClassDumper.dump(targetClass, "run", out);
		verifyClass(targetClass);

		out.println("2. Frames Recalculated with ASM");
		byte[] recalculatedClass = FrameRecalculator.recalculate(targetClass);
		ClassDumper.dump(recalculatedClass, "run", out);
		verifyClass(recalculatedClass);
	}

	public static void verifyClass(byte[] definition) throws Exception {
		TargetLoader loader = new TargetLoader();
		String name = Target.class.getName();
		Runnable runnable = (Runnable) loader.add(name, definition).newInstance();
		try {
			runnable.run();
		} catch (NullPointerException e) {
		}
	}

}
