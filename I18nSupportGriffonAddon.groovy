import griffon.core.GriffonApplication
import griffon.plugins.i18n.DefaultMessageSource
import griffon.plugins.i18n.MessageSourceHolder

class I18nSupportGriffonAddon {

    private static final String DEFAULT_I18N_FILE = 'messages'
    public static final String DEFAULT_PROVIDER = 'i18n-support'

    def getMessageSource = { -> MessageSourceHolder.messageSource }

    def getMessage = { Object... args -> MessageSourceHolder.messageSource.getMessage(* args) }

    void addonInit(GriffonApplication app) {
        String basename = app.config.i18n?.basename ?: DEFAULT_I18N_FILE
        MessageSourceHolder.provider = app.config.i18n?.provider ?: DEFAULT_PROVIDER
        MessageSourceHolder.registerMessageSource(DEFAULT_PROVIDER, new DefaultMessageSource(basename))

        MetaClass mc = app.metaClass
        mc.getMessageSource = getMessageSource
        mc.getI18n = getMessageSource
        mc.getMessage = getMessage
    }

    def methods = [
            getMessage: getMessage
    ]

    def props = [
            messageSource: [
                    get: getMessageSource
            ],
            i18n: [
                    get: getMessageSource
            ]
    ]
}
