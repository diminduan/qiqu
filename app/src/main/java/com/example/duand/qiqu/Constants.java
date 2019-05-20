package com.example.duand.qiqu;


import android.annotation.SuppressLint;


public class Constants {

    public static final String ARGS = "args";
    public static final String  VMURL = "http://10.0.2.2:8080/Qiqu_Test/";
    public static final String URL= "http://192.168.137.1:8080/Qiqu_Test/";
    public static final String newUrl = "http://139.199.156.122:8088/";

    @SuppressLint("SdCardPath")
    public static String head_savepath = "sdcard/myHead/";  //头像存储路径
    public static String back_savepath = "sdcard/myBackGround/";  //背景存储路径

    public static String route_savepath = "sdcard/route_icon/";  //route_icon存储路径

    public enum SerializerFeature {
        QuoteFieldNames,
        UseSingleQuotes,
        WriteMapNullValue,
        WriteEnumUsingToString,
        UseISO8601DateFormat,
        /**
         * @since 1.1
         */
        WriteNullListAsEmpty,
        /**
         * @since 1.1
         */
        WriteNullStringAsEmpty,
        /**
         * @since 1.1
         */
        WriteNullNumberAsZero,
        /**
         * @since 1.1
         */
        WriteNullBooleanAsFalse,
        /**
         * @since 1.1
         */
        SkipTransientField,
        /**
         * @since 1.1
         */
        SortField,
        /**
         * @since 1.1.1
         */
        @Deprecated
        WriteTabAsSpecial,
        /**
         * @since 1.1.2
         */
        PrettyFormat,
        /**
         * @since 1.1.2
         */
        WriteClassName,

        /**
         * @since 1.1.6
         */
        DisableCircularReferenceDetect,

        /**
         * @since 1.1.9
         */
        WriteSlashAsSpecial,

        /**
         * @since 1.1.10
         */
        BrowserCompatible,

        /**
         * @since 1.1.14
         */
        WriteDateUseDateFormat,

        /**
         * @since 1.1.15
         */
        NotWriteRootClassName,

        /**
         * @since 1.1.19
         */
        DisableCheckSpecialChar,

        /**
         * @since 1.1.35
         */
        BeanToArray
        ;

        private SerializerFeature(){
            mask = (1 << ordinal());
        }

        private final int mask;

        public final int getMask() {
            return mask;
        }

        public static boolean isEnabled(int features, SerializerFeature feature) {
            return (features & feature.getMask()) != 0;
        }

        public static int config(int features, SerializerFeature feature, boolean state) {
            if (state) {
                features |= feature.getMask();
            } else {
                features &= ~feature.getMask();
            }

            return features;
        }
    }



}
