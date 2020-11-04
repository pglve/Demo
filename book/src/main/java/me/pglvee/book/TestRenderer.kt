/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.book

import android.graphics.Color
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.properties.Delegates


class TestRenderer(val color: Int) : GLSurfaceView.Renderer {

    private val vertexPoints = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )
    private var vertexBuffer by Delegates.notNull<FloatBuffer>()

    init {
        //分配内存空间，每个浮点型占4字节空间
        vertexBuffer = ByteBuffer.allocateDirect(vertexPoints.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        //传入指定的坐标数据
        vertexBuffer.put(vertexPoints)
        vertexBuffer.position(0)
    }

    /**
     * 编译和加载着色器
     * @param shaderType
     * ---顶点着色器：[GLES31.GL_VERTEX_SHADER]
     * ---片段着色器：[GLES31.GL_FRAGMENT_SHADER]
     */
    private fun loadShader(shaderType: Int, sourceCode: String?): Int {
        val shader = GLES31.glCreateShader(shaderType)
        if (shader != 0) {
            //加载到着色器
            GLES31.glShaderSource(shader, sourceCode)
            //编译到着色器
            GLES31.glCompileShader(shader)
            //检测状态
            val compilesStatus = intArrayOf(0)
            GLES31.glGetShaderiv(shader, GLES31.GL_COMPILE_STATUS, compilesStatus, 0)
            if (compilesStatus[0] == 0) {
                val logInfo = GLES31.glGetShaderInfoLog(shader)
                System.err.println(logInfo)
                //创建失败
                GLES31.glDeleteShader(shader)
                return 0
            }
            return shader
        } else {
            //创建失败
            return 0
        }
    }

    /**
     * 链接到着色器
     *
     */
    private fun linkProgram(vertexShader: Int, fragmentShader: Int): Int {
        val program = GLES31.glCreateProgram()
        if (program != 0) {
            //将顶点着色器加入到program
            GLES31.glAttachShader(program, vertexShader)
            //将片段着色器加入到program
            GLES31.glAttachShader(program, fragmentShader)
            //链接着色器program
            GLES31.glLinkProgram(program)
            val linkStatus = intArrayOf(0)
            GLES31.glGetProgramiv(program, GLES31.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == 0) {
                val logInfo = GLES31.glGetProgramInfoLog(program)
                System.err.println(logInfo)
                //创建失败
                GLES31.glDeleteProgram(program)
                return 0
            }
            return program
        } else {
            //创建失败
            return 0
        }
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        //创建顶点着色器
        val vertexCoder = readResource(R.raw.triangle_vertex_shader)
        val vertexShader = loadShader(GLES31.GL_VERTEX_SHADER, vertexCoder)
        //创建片段着色器
        val fragmentCoder = readResource(R.raw.triangle_fragment_shader)
        val fragmentShader = loadShader(GLES31.GL_FRAGMENT_SHADER, fragmentCoder)

        if (vertexShader == 0 || fragmentShader == 0) {
            System.err.println("compile Shader fail")
            return
        }
        val program = linkProgram(vertexShader, fragmentShader)
        //在openGLES环境中使用program
        GLES31.glUseProgram(program)
        //设置背景颜色
        val redF = Color.red(color) / 255f
        val greenF = Color.green(color) / 255f
        val blueF = Color.blue(color) /255f
        val alphaF = Color.alpha(color) /255f
        GLES31.glClearColor(redF, greenF, blueF, alphaF)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        //设置视图窗口
        GLES31.glViewport(0, 0, width, height)
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT or GLES31.GL_DEPTH_BUFFER_BIT)
    }

    override fun onDrawFrame(gl: GL10) {
        //把颜色缓冲区设置为我们预设的颜色
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)

        //准备坐标数据
        GLES31.glVertexAttribPointer(0, 3, GLES31.GL_FLOAT, false, 0, vertexBuffer)
        //启用顶点的句柄
        GLES31.glEnableVertexAttribArray(0)
        GLES31.glEnableVertexAttribArray(1)
        //绘制三个点
        GLES31.glVertexAttribPointer(1, 4, GLES31.GL_FLOAT, false, 0, color)
        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, 3)
        //绘制直线
//        GLES31.glDrawArrays(GLES31.GL_LINE_STRIP, 0, 2)
//        GLES31.glLineWidth(10f)
        //禁止顶点数组的句柄
        GLES31.glDisableVertexAttribArray(0)
        GLES31.glDisableVertexAttribArray(1)
    }

}