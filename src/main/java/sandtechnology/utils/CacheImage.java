package sandtechnology.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class CacheImage {
    private final File path;
    private final String relativePath;
    private String CQCode;
    private long lastAccessed = System.currentTimeMillis();

    public CacheImage(File absolutePath, String relativePath) {
        this.path = absolutePath;
        this.relativePath = relativePath;
    }

    public String toCQCode() {
        if (CQCode == null) {
            CQCode = "[CQ:image,file=" + relativePath.substring(1).replace('/', '\\') + "]";
        }
        return CQCode;
    }

    CacheImage markAccessed() {
        this.lastAccessed = System.currentTimeMillis();
        return this;
    }

    public long getLastAccessed() {
        return lastAccessed;
    }

    public File getFile() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CacheImage)) return false;
        CacheImage image = (CacheImage) o;
        return path.equals(image.path) &&
                CQCode.equals(image.CQCode);
    }

    @Override
    public String toString() {
        return "CacheImage{" + "path=" + path + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, CQCode);
    }

    public static class ImagePlaceHolder extends CacheImage {

        private static final Path pathPlaceHolder = Paths.get(".");
        private final ImagePlaceHolder.Reason reason;
        private final String url;

        public ImagePlaceHolder(String url, ImagePlaceHolder.Reason reason) {
            super(pathPlaceHolder.toFile(), url);
            this.url = url;
            this.reason = reason;
        }

        public ImagePlaceHolder(String url) {
            this(url, ImagePlaceHolder.Reason.No_Image_Mode);
        }

        @Override
        public String toString() {
            switch (reason) {
                case Image_Not_Found:
                    return "[图片未找到]\n";
                case Upload_Failed:
                    return "[图片上传失败，原链接：" + url + "]\n";
                case Download_Failed:
                    return "[图片下载失败，原链接：" + url + "]\n";
                case Null:
                    return "[无图片]\n";
                default:
                    return "[图片链接：" + url + "]\n";
            }

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            ImagePlaceHolder that = (ImagePlaceHolder) o;
            return reason == that.reason && Objects.equals(url, that.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), reason, url);
        }

        public enum Reason {
            Image_Not_Found,
            Upload_Failed,
            Download_Failed,
            No_Image_Mode,
            Null
        }
    }
}
