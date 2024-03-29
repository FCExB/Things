/*
 * Copyright (c) 2012, Oskar Veerhoek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */

package utility;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * @author Oskar
 */
public class OBJLoader {
    public static int createDisplayList(Model m) {
        int displayList = glGenLists(1);
        glNewList(displayList, GL_COMPILE);
        {
            glColor3f(0.4f, 0.27f, 0.17f);
            glMaterialf(GL_FRONT, GL_SHININESS, 128.0f);
            glBegin(GL_TRIANGLES);
            for (Face face : m.faces) {
                Vector3f n1 = m.normals.get((int) face.normal.x - 1);
                glNormal3f(n1.x, n1.y, n1.z);
                Vector3f v1 = m.vertices.get((int) face.vertex.x - 1);
                glVertex3f(v1.x, v1.y, v1.z);
                Vector3f n2 = m.normals.get((int) face.normal.y - 1);
                glNormal3f(n2.x, n2.y, n2.z);
                Vector3f v2 = m.vertices.get((int) face.vertex.y - 1);
                glVertex3f(v2.x, v2.y, v2.z);
                Vector3f n3 = m.normals.get((int) face.normal.z - 1);
                glNormal3f(n3.x, n3.y, n3.z);
                Vector3f v3 = m.vertices.get((int) face.vertex.z - 1);
                glVertex3f(v3.x, v3.y, v3.z);
            }
            glEnd();
        }
        glEndList();
        return displayList;
    }

    private static FloatBuffer reserveData(int size) {
        return BufferUtils.createFloatBuffer(size);
    }

    private static float[] asFloats(Vector3f v) {
        return new float[]{v.x, v.y, v.z};
    }
    
    private static short[] asShorts(Vector3f v) {
        return new short[]{(short)v.x, (short)v.y, (short)v.z};
    }

    public static int[] createVBO(Model model) {
        int vboVertexHandle = glGenBuffers();
        int vboNormalHandle = glGenBuffers();
        FloatBuffer vertices = reserveData(model.faces.size() * 9);
        FloatBuffer normals = reserveData(model.faces.size() * 9);
        for (Face face : model.faces) {
            vertices.put(asFloats(model.vertices.get((int) face.vertex.x - 1)));
            vertices.put(asFloats(model.vertices.get((int) face.vertex.y - 1)));
            vertices.put(asFloats(model.vertices.get((int) face.vertex.z - 1)));
            normals.put(asFloats(model.normals.get((int) face.normal.x - 1)));
            normals.put(asFloats(model.normals.get((int) face.normal.y - 1)));
            normals.put(asFloats(model.normals.get((int) face.normal.z - 1)));
        }
        vertices.flip();
        normals.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
        glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
        glNormalPointer(GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return new int[]{vboVertexHandle, vboNormalHandle};
    }
    
    public static int myCreateVAO(Model model) {
    	
        FloatBuffer vertexDataBuffer = BufferUtils.createFloatBuffer(model.vertices.size()*3);
		for(Vector3f vertex : model.vertices){
			 vertexDataBuffer.put(asFloats(vertex));
		}
       
		vertexDataBuffer.flip();
		
        int vertexBufferObject = glGenBuffers();	       
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
	    glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		ShortBuffer indexDataBuffer = BufferUtils.createShortBuffer(model.faces.size()*3);
		for(Face face : model.faces){
			 indexDataBuffer.put(asShorts(face.vertex));
		}
		
		indexDataBuffer.flip();
		
        int indexBufferObject = glGenBuffers();	       
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);
	    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexDataBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		return glGenVertexArrays();
    }

    public static Model loadModel(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        Model m = new Model();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("v ")) {
            	String[] values = line.split(" ");
            	
            	float x = Float.valueOf(values[1]);
                float y = Float.valueOf(values[2]);
                float z = Float.valueOf(values[3]);
                m.vertices.add(new Vector3f(x, y, z));
            } else if (line.startsWith("vn ")) {
                String[] values = line.split(" ");
            	
            	float x = Float.valueOf(values[1]);
                float y = Float.valueOf(values[2]);
                float z = Float.valueOf(values[3]);
                m.normals.add(new Vector3f(x, y, z));
            } else if (line.startsWith("vt ")) {

            } else if (line.startsWith("f ")) {
            	String[] values = line.split(" ");
            	
            	String[] one = values[1].split("/");
            	String[] two = values[2].split("/");
            	String[] three = values[3].split("/");
            	
            	Vector3f vertexIndices = new Vector3f(Float.valueOf(one[0]),
                        Float.valueOf(two[0]),
                        Float.valueOf(three[0]));
            	
                Vector3f normalIndices = new Vector3f(Float.valueOf(one[2]),
                        Float.valueOf(two[2]),
                        Float.valueOf(three[2]));
                
                m.faces.add(new Face(vertexIndices, normalIndices));
            }
        }
        reader.close();
        return m;
    }
}
