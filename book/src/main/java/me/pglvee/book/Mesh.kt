/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.book

import android.graphics.Bitmap
import android.opengl.GLUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.opengles.GL10


/**
 * Mesh is a base class for 3D objects making it easier to create and maintain new primitives.
 */
class Mesh {
    /*// Our vertex buffer.
    private var mVerticesBuffer: FloatBuffer? = null

    // Our index buffer.
    private var mIndicesBuffer: ShortBuffer? = null

    // Our UV texture buffer. New variable.
    private var mTextureBuffer: FloatBuffer? = null

    // Our texture id. New variable.
    private var mTextureId = -1

    // The bitmap we want to load as a texture. New variable.
    private var mBitmap: Bitmap? = null

    // Indicates if we need to load the texture. New variable.
    private var mShouldLoadTexture = false

    // The number of indices.
    private var mNumOfIndices = -1

    // Flat Color
    private val mRGBA = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)

    // Smooth Colors
    private var mColorBuffer: FloatBuffer? = null

    // Translate params.
    var x = 0f
    var y = 0f
    var z = 0f

    // Rotate params.
    var rx = 0f
    var ry = 0f
    var rz = 0f

    *//**
     * Render the mesh.
     *
     * @param gl
     * the OpenGL context to render to.
     *//*
    fun draw(gl: GL10) {
        // Counter-clockwise winding.
        gl.glFrontFace(GL10.GL_CCW)
        // Enable face culling.
        gl.glEnable(GL10.GL_CULL_FACE)
        // What faces to remove with the face culling.
        gl.glCullFace(GL10.GL_BACK)
        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVerticesBuffer)
        // Set flat color
        gl.glColor4f(mRGBA[0], mRGBA[1], mRGBA[2], mRGBA[3])
        // Smooth color
        if (mColorBuffer != null) {
            // Enable the color array buffer to be used during rendering.
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY)
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer)
        }

        // New part...
        if (mShouldLoadTexture) {
            loadGLTexture(gl)
            mShouldLoadTexture = false
        }
        if (mTextureId != -1 && mTextureBuffer != null) {
            gl.glEnable(GL10.GL_TEXTURE_2D)
            // Enable the texture state
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)

            // Point to our buffers
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer)
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId)
        }
        // ... end new part.
        gl.glTranslatef(x, y, z)
        gl.glRotatef(rx, 1f, 0f, 0f)
        gl.glRotatef(ry, 0f, 1f, 0f)
        gl.glRotatef(rz, 0f, 0f, 1f)

        // Point out the where the color buffer is.
        gl.glDrawElements(
            GL10.GL_TRIANGLES, mNumOfIndices,
            GL10.GL_UNSIGNED_SHORT, mIndicesBuffer
        )
        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)

        // New part...
        if (mTextureId != -1 && mTextureBuffer != null) {
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
        }
        // ... end new part.

        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE)
    }

    *//**
     * Set the vertices.
     *
     * @param vertices
     *//*
    protected fun setVertices(vertices: FloatArray) {
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder())
        mVerticesBuffer = vbb.asFloatBuffer()
        mVerticesBuffer.put(vertices)
        mVerticesBuffer.position(0)
    }

    *//**
     * Set the indices.
     *
     * @param indices
     *//*
    protected fun setIndices(indices: ShortArray) {
        // short is 2 bytes, therefore we multiply the number if
        // vertices with 2.
        val ibb = ByteBuffer.allocateDirect(indices.size * 2)
        ibb.order(ByteOrder.nativeOrder())
        mIndicesBuffer = ibb.asShortBuffer()
        mIndicesBuffer.put(indices)
        mIndicesBuffer.position(0)
        mNumOfIndices = indices.size
    }

    *//**
     * Set the texture coordinates.
     *
     * @param textureCoords
     *//*
    protected fun setTextureCoordinates(textureCoords: FloatArray) { // New
        // function.
        // float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        val byteBuf = ByteBuffer
            .allocateDirect(textureCoords.size * 4)
        byteBuf.order(ByteOrder.nativeOrder())
        mTextureBuffer = byteBuf.asFloatBuffer()
        mTextureBuffer.put(textureCoords)
        mTextureBuffer.position(0)
    }

    *//**
     * Set one flat color on the mesh.
     *
     * @param red
     * @param green
     * @param blue
     * @param alpha
     *//*
    protected fun setColor(red: Float, green: Float, blue: Float, alpha: Float) {
        mRGBA[0] = red
        mRGBA[1] = green
        mRGBA[2] = blue
        mRGBA[3] = alpha
    }

    *//**
     * Set the colors
     *
     * @param colors
     *//*
    protected fun setColors(colors: FloatArray) {
        // float has 4 bytes.
        val cbb = ByteBuffer.allocateDirect(colors.size * 4)
        cbb.order(ByteOrder.nativeOrder())
        mColorBuffer = cbb.asFloatBuffer()
        mColorBuffer.put(colors)
        mColorBuffer.position(0)
    }

    *//**
     * Set the bitmap to load into a texture.
     *
     * @param bitmap
     *//*
    fun loadBitmap(bitmap: Bitmap?) { // New function.
        mBitmap = bitmap
        mShouldLoadTexture = true
    }

    *//**
     * Loads the texture.
     *
     * @param gl
     *//*
    private fun loadGLTexture(gl: GL10) { // New function
        // Generate one texture pointer...
        val textures = IntArray(1)
        gl.glGenTextures(1, textures, 0)
        mTextureId = textures[0]

        // ...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId)

        // Create Nearest Filtered Texture
        gl.glTexParameterf(
            GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
            GL10.GL_LINEAR.toFloat()
        )
        gl.glTexParameterf(
            GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
            GL10.GL_LINEAR.toFloat()
        )

        // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
        gl.glTexParameterf(
            GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
            GL10.GL_CLAMP_TO_EDGE.toFloat()
        )
        gl.glTexParameterf(
            GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
            GL10.GL_REPEAT.toFloat()
        )

        // Use the Android GLUtils to specify a two-dimensional texture image
        // from our bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0)
    }*/
}