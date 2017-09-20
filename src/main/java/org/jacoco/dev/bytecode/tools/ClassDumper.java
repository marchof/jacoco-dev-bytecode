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
package org.jacoco.dev.bytecode.tools;

import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;
import org.objectweb.asm.util.TraceMethodVisitor;

/**
 * Write out class files as text.
 */
public class ClassDumper {

	public static void dump(byte[] classdef, PrintWriter out) {
		new ClassReader(classdef).accept(new TraceClassVisitor(out), ClassReader.EXPAND_FRAMES);
	}

	public static void dump(byte[] classdef, String method, PrintWriter out) {
		Textifier textifier = new Textifier();
		new ClassReader(classdef).accept(new ClassVisitor(Opcodes.ASM5) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature,
					String[] exceptions) {
				if (method.equals(name)) {
					return new TraceMethodVisitor(textifier);
				}
				return null;
			}
		}, ClassReader.EXPAND_FRAMES);
		textifier.text.forEach(out::print);
	}

}
