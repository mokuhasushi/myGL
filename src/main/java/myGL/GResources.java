package myGL;

import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;

public class GResources {
    int vertex_buffer, element_buffer;
    int [] textures = new int[2];
    int vertex_shader, fragment_shader, program;

    HashMap<String, Integer> uniforms = new HashMap<>();
    HashMap<String, Integer> attributes = new HashMap<>();

    float fade_factor = 1.0f;

    public HashMap<String, Integer> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Integer> attributes) {
        this.attributes = attributes;
    }

    public int getVertex_buffer() {
        return vertex_buffer;
    }

    public void setVertex_buffer(int vertex_buffer) {
        this.vertex_buffer = vertex_buffer;
    }

    public int getElement_buffer() {
        return element_buffer;
    }

    public void setElement_buffer(int element_buffer) {
        this.element_buffer = element_buffer;
    }

    public int[] getTextures() {
        return textures;
    }

    public void setTextures(int[] textures) {
        this.textures = textures;
    }

    public void addUniform(String key, Integer value) {
        uniforms.put(key, value);
    }
    public void addUniform(String key){
        this.addUniform(key, glGetUniformLocation(this.program, key));
    }

    public void addAttribute(String key, Integer value) {
        attributes.put(key, value);
    }
    public void addAttribute(String key) {
        this.addAttribute(key, glGetAttribLocation(this.program, key));
    }

    public int getVertex_shader() {
        return vertex_shader;
    }

    public void setVertex_shader(int vertex_shader) {
        this.vertex_shader = vertex_shader;
    }
    public void setVertex_shader(String source) {
        this.setVertex_shader(make_shader(GL_VERTEX_SHADER, source));
    }


    public int getFragment_shader() {
        return fragment_shader;
    }

    public void setFragment_shader(int fragment_shader) {
        this.fragment_shader = fragment_shader;
    }
    public void setFragment_shader(String source){
        this.setFragment_shader(make_shader(GL_FRAGMENT_SHADER,source));
    }

    public int getProgram() {
        return program;
    }

    public void setProgram(int program) {
        this.program = program;
    }
    public void setProgram() {
        this.program = make_program(this.vertex_shader, this.fragment_shader);
    }

    public HashMap<String, Integer> getUniforms() {
        return uniforms;
    }

    public void setUniforms(HashMap<String, Integer> uniforms) {
        this.uniforms = uniforms;
    }

    public int getUniform(String key) {
        return uniforms.get(key);
    }
    public int getAttribute(String key) {
        return attributes.get(key);
    }

    public float getFade_factor() {
        return fade_factor;
    }

    public void setFade_factor(float fade_factor) {
        this.fade_factor = fade_factor;
    }

    public int make_program(int vertex_shader, int fragment_shader) {
        int program  = glCreateProgram();
        glAttachShader(program, vertex_shader);
        glAttachShader(program, fragment_shader);
        glLinkProgram(program);

        return program;
    }

    public int make_shader(int type, String source) {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);

        return shader;
    }

}
