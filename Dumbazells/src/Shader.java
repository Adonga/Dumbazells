import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

class Shader {
    private int vertID;
    private int fragID;
    private int programID;

    public Shader(String vertexFile, String fragmentFile) {
        vertID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        fragID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        GL20.glShaderSource(vertID, getProgramCode(vertexFile));
        compileAndCheck(vertID);

        GL20.glShaderSource(fragID, getProgramCode(fragmentFile));
        compileAndCheck(fragID);

        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertID);
        GL20.glAttachShader(programID, fragID);
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        if(GL20.glGetShader(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE
                || GL20.glGetShader(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetProgramInfoLog(programID, 2048).trim());
        } else {
            System.out.println("Compiled and linked " + vertexFile + ", " + fragmentFile + " successfully!");
        }
    }

    private void compileAndCheck(int id) {
        GL20.glCompileShader(id);
        if(GL20.glGetShader(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(id, 2048).trim());
        }
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void end() {
        GL20.glUseProgram(0);
    }

    ByteBuffer loadSource(String fileName) {
        byte[] code = null;
        try {
            code = Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(code.length);
        buffer.put(code);
       // showBuffer(buffer);
        return buffer;
    }

    private void showBuffer(ByteBuffer b) {
        for (int i = 0; i < b.capacity(); ++i)
            System.out.print((char) b.get(i));
        System.out.println("");
    }

    private ByteBuffer getProgramCode(String filename) {

        InputStream fileInputStream = null;
        byte[] shaderCode = null;

        try
        {
            if (fileInputStream == null)
                fileInputStream = new FileInputStream(filename);
            DataInputStream dataStream = new DataInputStream(fileInputStream);
            dataStream.readFully(shaderCode = new byte[ fileInputStream.available() ]);
            fileInputStream.close();
            dataStream.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        ByteBuffer shaderPro = BufferUtils.createByteBuffer(shaderCode.length);

        shaderPro.put(shaderCode);
        shaderPro.flip();

      //  showBuffer(shaderPro);
        return shaderPro;
    }

    public void setUniform(String location, int value) {
        GL20.glUniform1i(GL20.glGetUniformLocation(programID, location), value);
    }

    public void setUniform(String location, float value) {
        GL20.glUniform1f(GL20.glGetUniformLocation(programID, location), value);
    }
}