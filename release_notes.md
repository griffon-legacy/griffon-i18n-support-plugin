### Compatibility

The class `griffon.plugins.i18n.MessageSourceHolder` is now a singleton. This means all calls of the form

		MessageSourceHolder.getMessageSource()

must now be rewritten as

		MessageSourceHolder.getInstance()getMessageSource()

