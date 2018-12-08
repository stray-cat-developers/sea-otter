package io.mustelidae.seaotter.constant

enum class ImageFileFormat(val support: Boolean) {
    JPG(true), JPEG(true), JFIF(false), // JPEG (Joint Photographic Experts Group) is a lossy compression method; JPEG-compressed images are usually stored in the JFIF (JPEG File Interchange Format) file format.
    JP2(false), // JPEG 2000 is a compression standard enabling both lossless and lossy storage
    TIFF(true), // The TIFF (Tagged Image File Format) format is a flexible format that normally saves eight bits or sixteen bits per color (red, green, blue) for 24-bit and 48-bit totals, respectively, usually using either the TIFF or TIF filename extension
    GIF(false), // GIF (Graphics Interchange Format) is in normal use limited to an 8-bit palette, or 256 colors (while 24-bit color depth is technically possible).
    BMP(true), // The BMP file format (Windows bitmap) handles graphic files within the Microsoft Windows OS
    PNG(true), // The PNG (Portable Network Graphics) file format was created as a free, open-source alternative to GIF
    PPM(false), PGM(false), PBM(false), PNM(false), // Netpbm format is a family including the portable pixmap file format (PPM), the portable graymap file format (PGM) and the portable bitmap file format (PBM)
    WEBP(false), // WebP is a new open image format that uses both lossless and lossy compression.
    HDR(true), // Most typical raster formats cannot store HDR data (32 bit floating point values per pixel component), which is why some relatively old or complex formats are still predominant here, and worth mentioning separately.
    HEIF(false) // The High Efficiency Image File Format (HEIF) is an image container format that was standardized by MPEG on the basis of the ISO base media file format.
}
