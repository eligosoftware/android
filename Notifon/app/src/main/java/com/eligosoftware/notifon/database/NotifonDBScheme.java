package com.eligosoftware.notifon.database;

/**
 * Created by mragl on 08.11.2016.
 */

public class NotifonDBScheme  {
    public static final class Lang{
        public static final String NAME="lang";

        public static final class Cols{
            public static final String NAME="name";
            public static final String FULL_NAME="full_name";
        }
    }
    public static final class Word{
        public static final String NAME="word";

        public static final class Cols{
            public static final String WORD="word";
            public static final String LANGID="langid";
            public static final String KNOW = "know";
            public static final String LEVEL = "level";
        }
    }
    public static final class Descr{
        public static final String NAME="descr";

        public static final class Cols{
            public static final String MEANING="meaning";
            public static final String WORDID="wordid";
            public static final String PARTSP="partsp";
            public static final String LANGID = "langid";
        }
    }
    public static final class SupportedLanguages{
        public static final String NAME="SupportedLanguages";

        public static final class Cols{
            public static final String LANGID="lang_id";
            public static final String DLANGID="dlang_id";
        }
    }
}
