//
// Created by pinggl on 2020/12/8.
//


#include <jni.h>
#include <string>
#include <cstdio>
#include <cstdlib>
#include <cerrno>
#include <android/log.h>
#include "turbojpeg.h"

#define printf(format, ...)  __android_log_print(ANDROID_LOG_INFO, "compress-jni", format, ##__VA_ARGS__)

#define THROW(action, message, label) { \
    printf("ERROR in line %d while %s:\n%s\n", __LINE__, action, message); \
    retval = -1;  goto lab##label; \
}

#define THROW_TJ(action, label)  THROW(action, tjGetErrorStr2(tjInstance), label)
#define THROW_UNIX(action, label)  THROW(action, strerror(errno), label)


#define DEFAULT_SUBSAMP  TJSAMP_444
#define DEFAULT_QUALITY  95

const char *subsampName[TJ_NUMSAMP] = {
        "4:4:4", "4:2:2", "4:2:0", "Grayscale", "4:4:0", "4:1:1"
};

const char *colorspaceName[TJ_NUMCS] = {
        "RGB", "YCbCr", "GRAY", "CMYK", "YCCK"
};

tjscalingfactor *scalingFactors = nullptr;
int numScalingFactors = 0;

/* DCT filter example.  This produces a negative of the image. */
static int customFilter(short *coeffs, tjregion arrayRegion,
                        tjregion planeRegion, int componentIndex,
                        int transformIndex, tjtransform *transform) {
    int i;
    for (i = 0; i < arrayRegion.w * arrayRegion.h; i++)
        coeffs[i] = -coeffs[i];
    return 0;
}

static void usage(char *programName) {
    int i;

    printf("\nUSAGE: %s <Input image> <Output image> [options]\n\n", programName);

    printf("Input and output images can be in Windows BMP or PBMPLUS (PPM/PGM) format.  If\n");
    printf("either filename ends in a .jpg extension, then the TurboJPEG API will be used\n");
    printf("to compress or decompress the image.\n\n");

    printf("Compression Options (used if the output image is a JPEG image)\n");
    printf("--------------------------------------------------------------\n\n");

    printf("-subsamp <444|422|420|gray> = Apply this level of chrominance subsampling when\n");
    printf("     compressing the output image.  The default is to use the same level of\n");
    printf("     subsampling as in the input image, if the input image is also a JPEG\n");
    printf("     image, or to use grayscale if the input image is a grayscale non-JPEG\n");
    printf("     image, or to use %s subsampling otherwise.\n\n", subsampName[DEFAULT_SUBSAMP]);

    printf("-q <1-100> = Compress the output image with this JPEG quality level\n");
    printf("     (default = %d).\n\n", DEFAULT_QUALITY);

    printf("Decompression Options (used if the input image is a JPEG image)\n");
    printf("---------------------------------------------------------------\n\n");

    printf("-scale M/N = Scale the input image by a factor of M/N when decompressing it.\n");
    printf("(M/N = ");
    for (i = 0; i < numScalingFactors; i++) {
        printf("%d/%d", scalingFactors[i].num, scalingFactors[i].denom);
        if (numScalingFactors == 2 && i != numScalingFactors - 1)
            printf(" or ");
        else if (numScalingFactors > 2) {
            if (i != numScalingFactors - 1)
                printf(", ");
            if (i == numScalingFactors - 2)
                printf("or ");
        }
    }
    printf(")\n\n");

    printf("-hflip, -vflip, -transpose, -transverse, -rot90, -rot180, -rot270 =\n");
    printf("     Perform one of these lossless transform operations on the input image\n");
    printf("     prior to decompressing it (these options are mutually exclusive.)\n\n");

    printf("-grayscale = Perform lossless grayscale conversion on the input image prior\n");
    printf("     to decompressing it (can be combined with the other transform operations\n");
    printf("     above.)\n\n");

    printf("-crop WxH+X+Y = Perform lossless cropping on the input image prior to\n");
    printf("     decompressing it.  X and Y specify the upper left corner of the cropping\n");
    printf("     region, and W and H specify the width and height of the cropping region.\n");
    printf("     X and Y must be evenly divible by the MCU block size (8x8 if the input\n");
    printf("     image was compressed using no subsampling or grayscale, 16x8 if it was\n");
    printf("     compressed using 4:2:2 subsampling, or 16x16 if it was compressed using\n");
    printf("     4:2:0 subsampling.)\n\n");

    printf("General Options\n");
    printf("---------------\n\n");

    printf("-fastupsample = Use the fastest chrominance upsampling algorithm available in\n");
    printf("     the underlying codec.\n\n");

    printf("-fastdct = Use the fastest DCT/IDCT algorithms available in the underlying\n");
    printf("     codec.\n\n");

    printf("-accuratedct = Use the most accurate DCT/IDCT algorithms available in the\n");
    printf("     underlying codec.\n\n");
}

int mainCompress(
        /**scale : m/n*/
        bool sc, int m, int n,
        /**outSubSample : subsamp*/
        bool su, int subsamp,
        /**quality*/
        bool q, int qual,
        /**xform-option*/
        bool g,
        /**xform-op*/
        bool hflip, bool vflip, bool transpose, bool transverse, bool rot90, bool rot180,
        bool rot270,
        /**xform-customFilter*/
        bool custom,
        /**xform-crop*/
        bool c, int c_w, int c_h, int c_x, int c_y,
        /**flags*/
        bool fastupsample, bool fastdct, bool accuratedct,
        /**intput filename and output filename*/
        const char *input, const char *output) {
    tjscalingfactor scalingFactor = {1, 1};
    int outSubsamp = -1, outQual = -1;
    tjtransform xform;
    int flags = 0;
    int width, height;
    char *inFormat, *outFormat;
    FILE *jpegFile = nullptr;
    unsigned char *imgBuf = nullptr, *jpegBuf = nullptr;
    int retval = 0, i, pixelFormat = TJPF_UNKNOWN;
    tjhandle tjInstance = nullptr;

    if ((scalingFactors = tjGetScalingFactors(&numScalingFactors)) == nullptr) {
        THROW_TJ("getting scaling factors", mainbailout)
    }
    memset(&xform, 0, sizeof(tjtransform));
    ///*参数设置----------------------------------start*/
    if (sc) {
        for (int j = 0; j < numScalingFactors; j++) {
            if ((double) m / (double) n ==
                (double) scalingFactors[j].num / (double) scalingFactors[j].denom) {
                scalingFactor = scalingFactors[j];
                break;
            }
        }
    }
    if (su) outSubsamp = subsamp;
    if (q) outQual = qual;
    if (g) xform.options |= TJXOPT_GRAY;
    if (hflip) xform.op = TJXOP_HFLIP;
    if (vflip) xform.op = TJXOP_VFLIP;
    if (transpose) xform.op = TJXOP_TRANSPOSE;
    if (transverse) xform.op = TJXOP_TRANSVERSE;
    if (rot90) xform.op = TJXOP_ROT90;
    if (rot180) xform.op = TJXOP_ROT180;
    if (rot270) xform.op = TJXOP_ROT270;
    if (custom) xform.customFilter = customFilter;
    if (c) {
        xform.r.w = c_w;
        xform.r.h = c_h;
        xform.r.x = c_x;
        xform.r.y = c_y;
        xform.options |= TJXOPT_CROP;
    }
    if (fastupsample) flags |= TJFLAG_FASTUPSAMPLE;
    if (fastdct) flags |= TJFLAG_FASTDCT;
    if (accuratedct) flags |= TJFLAG_ACCURATEDCT;

    inFormat = strrchr((char *) input, '.');
    outFormat = strrchr((char *) output, '.');
    inFormat = &inFormat[1];
    outFormat = &outFormat[1];
    ///*参数设置----------------------------------end*/

    if (!strcasecmp(inFormat, "jpg") || !strcasecmp(inFormat, "jpeg")) {
        /* Input image is a JPEG image.  Decompress and/or transform it. */
        long size;
        int inSubsamp, inColorspace;
        int doTransform = (xform.op != TJXOP_NONE || xform.options != 0 ||
                           xform.customFilter != nullptr);
        unsigned long jpegSize;

        /* Read the JPEG file into memory. */
        if ((jpegFile = fopen(input, "rb")) == nullptr) {
            THROW_UNIX("opening input file", mainbailout);
        }
        if (fseek(jpegFile, 0, SEEK_END) < 0 || ((size = ftell(jpegFile)) < 0) ||
            fseek(jpegFile, 0, SEEK_SET) < 0) {
            THROW_UNIX("determining input file size", mainbailout);
        }
        if (size == 0) {
            THROW("determining input file size", "Input file contains no data", mainbailout);
        }
        jpegSize = (unsigned long) size;
        if ((jpegBuf = tjAlloc(jpegSize)) == nullptr) {
            THROW_UNIX("allocating JPEG buffer", mainbailout);
        }

        if (fread(jpegBuf, jpegSize, 1, jpegFile) < 1) {
            THROW_UNIX("reading input file", mainbailout);
        }
        fclose(jpegFile);
        jpegFile = nullptr;

        if (doTransform) {
            /* Transform it. */
            unsigned char *dstBuf = nullptr;  /* Dynamically allocate the JPEG buffer */
            unsigned long dstSize = 0;

            if ((tjInstance = tjInitTransform()) == nullptr) {
                THROW_TJ("initializing transformer", mainbailout);
            }
            xform.options |= TJXOPT_TRIM;
            if (tjTransform(tjInstance, jpegBuf, jpegSize, 1, &dstBuf, &dstSize, &xform, flags) <
                0) {
                THROW_TJ("transforming input image", mainbailout);
            }

            tjFree(jpegBuf);
            jpegBuf = dstBuf;
            jpegSize = dstSize;
        } else {
            if ((tjInstance = tjInitDecompress()) == nullptr) {
                THROW_TJ("initializing decompressor", mainbailout);
            }
        }

        if (tjDecompressHeader3(tjInstance, jpegBuf, jpegSize, &width, &height, &inSubsamp,
                                &inColorspace) < 0) {
            THROW_TJ("reading JPEG header", mainbailout);
        }

        printf("%s Image:  %d x %d pixels, %s subsampling, %s colorspace\n",
               (doTransform ? "Transformed" : "Input"),
               width, height, subsampName[inSubsamp], colorspaceName[inColorspace]);

        if ((!strcasecmp(outFormat, "jpg") || !strcasecmp(outFormat, "jpeg")) && doTransform &&
            scalingFactor.num == 1 && scalingFactor.denom == 1 && outSubsamp < 0 && outQual < 0) {
            /* Input image has been transformed, and no re-compression options
               have been selected.  Write the transformed image to disk and exit. */
            if ((jpegFile = fopen(output, "wb")) == nullptr) {
                THROW_UNIX("opening output file", mainbailout);
            }
            if (fwrite(jpegBuf, jpegSize, 1, jpegFile) < 1) {
                THROW_UNIX("writing output file", mainbailout);
            }
            fclose(jpegFile);
            jpegFile = nullptr;
            goto labmainbailout;
        }

        /* Scaling and/or a non-JPEG output image format and/or compression options
           have been selected, so we need to decompress the input/transformed image. */
        width = TJSCALED(width, scalingFactor);
        height = TJSCALED(height, scalingFactor);
        if (outSubsamp < 0)
            outSubsamp = inSubsamp;
        pixelFormat = TJPF_BGRX;
        if ((imgBuf = tjAlloc(width * height * tjPixelSize[pixelFormat])) == nullptr) {
            THROW_UNIX("allocating uncompressed image buffer", mainbailout);
        }


        if (tjDecompress2(tjInstance, jpegBuf, jpegSize, imgBuf, width, 0, height, pixelFormat,
                          flags) < 0) {
            THROW_TJ("decompressing JPEG image", mainbailout);
        }

        tjFree(jpegBuf);
        jpegBuf = nullptr;
        tjDestroy(tjInstance);
        tjInstance = nullptr;
    } else {
        /* Input image is not a JPEG image.  Load it into memory. */
        if ((imgBuf = tjLoadImage(input, &width, 1, &height, &pixelFormat, 0)) == nullptr) {
            THROW_TJ("loading input image", mainbailout);
        }
        if (outSubsamp < 0) {
            if (pixelFormat == TJPF_GRAY)
                outSubsamp = TJSAMP_GRAY;
            else
                outSubsamp = TJSAMP_444;
        }
        printf("Input Image:  %d x %d pixels\n", width, height);
    }

    printf("Output Image (%s):  %d x %d pixels", outFormat, width, height);

    if (!strcasecmp(outFormat, "jpg") || !strcasecmp(outFormat, "jpeg")) {
        /* Output image format is JPEG.  Compress the uncompressed image. */
        unsigned long jpegSize = 0;
        jpegBuf = nullptr;  /* Dynamically allocate the JPEG buffer */

        if (outQual < 0)
            outQual = DEFAULT_QUALITY;
        printf(", %s subsampling, quality = %d\n", subsampName[outSubsamp], outQual);


        if ((tjInstance = tjInitCompress()) == nullptr) {
            THROW_TJ("initializing compressor", mainbailout);
        }
        if (tjCompress2(tjInstance, imgBuf, width, 0, height, pixelFormat, &jpegBuf, &jpegSize,
                        outSubsamp, outQual,
                        flags) < 0) {
            THROW_TJ("compressing image", mainbailout);
        }

        tjDestroy(tjInstance);
        tjInstance = nullptr;

        /* Write the JPEG image to disk. */
        if ((jpegFile = fopen(output, "wb")) == nullptr) {
            THROW_UNIX("opening output file", mainbailout);
        }
        if (fwrite(jpegBuf, jpegSize, 1, jpegFile) < 1) {
            THROW_UNIX("writing output file", mainbailout);
        }

        tjDestroy(tjInstance);
        tjInstance = nullptr;
        fclose(jpegFile);
        jpegFile = nullptr;
        tjFree(jpegBuf);
        jpegBuf = nullptr;

    } else {
        /* Output image format is not JPEG.  Save the uncompressed image directly to disk. */
        printf("\n");
        if (tjSaveImage(output, imgBuf, width, 0, height, pixelFormat, 0) < 0) {
            THROW_TJ("saving output image", mainbailout);
        }
    }

    labmainbailout:
    if (imgBuf) tjFree(imgBuf);
    if (tjInstance) tjDestroy(tjInstance);
    if (jpegBuf) tjFree(jpegBuf);
    if (jpegFile) fclose(jpegFile);
    return retval;
}

int thumbnailCompress(const char *input, const char *output, unsigned long maxSize) {
    int retval = 0;
    int quality = 100;
    tjhandle tjInstance = nullptr;
    unsigned char *jpegBuf = nullptr;
    unsigned char *imgBuf = nullptr;
    FILE *jpegFile = nullptr;
    int width, height, inSubsamp, inColorspace;
    unsigned long jpegSize_out = 0;
    unsigned char *jpegBuf_out = nullptr;
    long size;
    unsigned long jpegSize;
    /* Read the JPEG file into memory. */
    if ((jpegFile = fopen(input, "rb")) == nullptr) {
        THROW_UNIX("opening input file", thumbout);
    }
    if (fseek(jpegFile, 0, SEEK_END) < 0 || ((size = ftell(jpegFile)) < 0) || fseek(jpegFile, 0, SEEK_SET) < 0) {
        THROW_UNIX("determining input file size", thumbout);
    }
    if (size == 0) {
        THROW("determining input file size", "Input file contains no data", thumbout);
    }
    jpegSize = (unsigned long) size;
    if ((jpegBuf = tjAlloc(jpegSize)) == nullptr) {
        THROW_UNIX("allocating JPEG buffer", thumbout);
    }

    if (fread(jpegBuf, jpegSize, 1, jpegFile) < 1) {
        THROW_UNIX("reading input file", thumbout);
    }
    fclose(jpegFile);
    jpegFile = nullptr;

    if ((tjInstance = tjInitDecompress()) == nullptr) {
        THROW_TJ("initializing decompressor", thumbout);
    }

    if (tjDecompressHeader3(tjInstance, jpegBuf, jpegSize, &width, &height, &inSubsamp,
                            &inColorspace) < 0) {
        THROW_TJ("reading JPEG header", thumbout);
    }
    printf("Input Image:  %d x %d pixels, %s subsampling, %s colorspace, %ld byte\n", width, height,
           subsampName[inSubsamp],
           colorspaceName[inColorspace], jpegSize);

    if ((imgBuf = tjAlloc(width * height * tjPixelSize[TJPF_BGRX])) == nullptr) {
        THROW_UNIX("allocating uncompressed image buffer", thumbout);
    }

    if (tjDecompress2(tjInstance, jpegBuf, jpegSize, imgBuf, width, 0, height, TJPF_BGRX, 0) < 0) {
        THROW_TJ("decompressing JPEG image", thumbout);
    }

    tjDestroy(tjInstance);
    tjInstance = nullptr;
    jpegSize_out = jpegSize;

    do {
        tjFree(jpegBuf_out);
        jpegBuf_out = nullptr;

        printf("Output Image :  %d x %d pixels", width, height);

        /* Output image format is JPEG.  Compress the uncompressed image. */
        unsigned long scale = jpegSize_out / maxSize;
        if (scale > 5) scale = 5;
        quality -= (10 * (int) scale);
        printf(", %s subsampling, quality = %d\n", subsampName[inSubsamp], quality);

        if ((tjInstance = tjInitCompress()) == nullptr) {
            THROW_TJ("initializing compressor", thumbout);
        }

        if (tjCompress2(tjInstance, imgBuf, width, 0, height, TJPF_BGRX, &jpegBuf_out,
                        &jpegSize_out, inSubsamp,
                        quality, 0) < 0) {
            THROW_TJ("compressing image", thumbout);
        }

    } while (jpegSize_out > maxSize && maxSize > 0);

    /* Input image has been transformed, and no re-compression options
              have been selected.  Write the transformed image to disk and exit. */
    if ((jpegFile = fopen(output, "wb")) == nullptr) {
        THROW_UNIX("opening output file", thumbout);
    }
    /* Write the JPEG image to disk. */
    if (fwrite(jpegBuf_out, jpegSize_out, 1, jpegFile) < 1) {
        THROW_UNIX("writing output file", thumbout);
    }

    tjDestroy(tjInstance);
    tjInstance = nullptr;
    fclose(jpegFile);
    jpegFile = nullptr;
    tjFree(jpegBuf_out);
    jpegBuf_out = nullptr;

    labthumbout:
    if (imgBuf) tjFree(imgBuf);
    if (tjInstance) tjDestroy(tjInstance);
    if (jpegBuf_out) tjFree(jpegBuf_out);
    if (jpegFile) fclose(jpegFile);
    return retval;
}


extern "C" JNIEXPORT void JNICALL
Java_me_pglvee_compress_LibLoader_compress(JNIEnv *env, jclass clazz,
                                           jboolean scale, jint m, jint n, jboolean su,
                                           jint subsamp, jboolean q, jint qual, jboolean g,
                                           jboolean hflip, jboolean vflip, jboolean transpose,
                                           jboolean transverse, jboolean rot90, jboolean rot180,
                                           jboolean rot270,
                                           jboolean c, jint c_w, jint c_h, jint c_x, jint c_y,
                                           jboolean fastupsample, jboolean fastdct,
                                           jboolean accuratedct,
                                           jstring input, jstring output) {
    const char *src = env->GetStringUTFChars(input, nullptr);
    const char *dst = env->GetStringUTFChars(output, nullptr);
    mainCompress(
            scale, m, n,
            su, subsamp,
            q, qual,
            g,
            hflip, vflip, transpose, transverse, rot90, rot180, rot270,
            false,
            c, c_w, c_h, c_x, c_y,
            fastupsample, fastdct, accuratedct,
            src, dst);
}

extern "C" JNIEXPORT void JNICALL
Java_me_pglvee_compress_LibLoader_imageCompress(JNIEnv *env, jclass clazz, jstring in, jstring out,
                                                jint quality) {
    const char *src = env->GetStringUTFChars(in, nullptr);
    const char *dst = env->GetStringUTFChars(out, nullptr);
    mainCompress(
            false, 1, 1,
            false, 0,
            true, quality,
            false,
            false, false, false, false, false, false, false,
            false,
            false, 0, 0, 0, 0,
            false, false, false,
            src, dst);
}

extern "C" JNIEXPORT void JNICALL
Java_me_pglvee_compress_LibLoader_thumbnailCompress(JNIEnv *env, jclass clazz, jstring in,
                                                    jstring out, jlong maxSize) {
    const char *src = env->GetStringUTFChars(in, nullptr);
    const char *dst = env->GetStringUTFChars(out, nullptr);
    thumbnailCompress(src, dst, maxSize);
}

