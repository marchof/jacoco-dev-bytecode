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

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * Recalculate Frames with ASM.
 */
public class FrameRecalculator {

	public static byte[] recalculate(byte[] input) {
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		new ClassReader(input).accept(writer, 0);
		return writer.toByteArray();
	}
	
}
