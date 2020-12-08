/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.compress;

/**
 *
 */

public class LibLoader {
    static {
        System.loadLibrary("compress");
    }

    static native void imageCompress(String inputFile, String outputFile, int quality);

    static native void thumbnailCompress(String inputFile, String outputFile, long maxSize);

    /**
     * @param scale scale <-> m/n
     * @param m Scaling factor ： Numerator
     * @param n Scaling factor ： Denominator
     * @param su outSubSample <-> subSample
     * @param subSample the level of chrominance subsampling to be used when generating the JPEG image (see @ref TJSAMP "Chrominance subsampling options".)
     * @param q quality <-> quality
     * @param quality jpegQual the image quality of the generated JPEG image (1 = worst, 100 = best)
     * @param g xform-option(共存) : TJXOPT_GRAY
     * @param hFlip xform-op(互斥) : TJXOP_HFLIP
     * @param vFlip .. : TJXOP_VFLIP
     * @param transpose .. : TJXOP_TRANSPOSE
     * @param transverse .. : TJXOP_TRANSVERSE
     * @param rot90 .. : TJXOP_ROT90
     * @param rot180 .. : TJXOP_ROT180
     * @param rot270 .. : TJXOP_ROT270
     * @param c xform-crop (xform-option : TJXOPT_CROP) :Cropping region <-> w,h,x,y
     * @param c_w ..
     * @param c_h ..
     * @param c_x ..
     * @param c_y ..
     * @param fastUpSample flags(共存) : TJFLAG_FASTUPSAMPLE
     * @param fastDCT .. : TJFLAG_FASTDCT
     * @param accurateDCT .. : TJFLAG_ACCURATEDCT
     * @param input input filename
     * @param output output filename
     */
    static native void compress(boolean scale, int m, int n,
                                          boolean su, int subSample,
                                          boolean q, int quality,
                                          boolean g,
                                          boolean hFlip, boolean vFlip, boolean transpose, boolean transverse, boolean rot90, boolean rot180, boolean rot270,
                                          boolean c, int c_w, int c_h, int  c_x, int  c_y,
                                          boolean fastUpSample, boolean fastDCT, boolean accurateDCT,
                                          String input, String output);

}
