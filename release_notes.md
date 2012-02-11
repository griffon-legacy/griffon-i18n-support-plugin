**i18n-support** provides basic internationalization support.

Other plugin can provide different implementations of this functionality. <br/>
The used implementation can be configured with the configuration key `i18n.provider`. <br/>
The default provider is `i18n-support`.

This implementation uses standard Java `ResourceBundle`s with the bundlename defined by the configuration key `i18n.basename`. <br/>
The default bundlename is `messages`.

Usage
-----
On any artifact or the app-instance, you can get messages by using the method `getMessage(String key, def args = null, String defaultMessage = null, Locale locale = Locale.getDefault())`
where args can be of the types `Object[]`, `List<?>` <br/>
If the implementation implements ExtendedMessageSource (like this implementation) the type of args can be of type `Map<String, ?>` as well.

If `defaultMessage` is not provided and no message with the given key can be found a 'NoSuchMessageException' will be thrown.

If `args` will be provided, the message found under the given key will be used as a template where the following tokens can be used:

### The following should be provided by all implementations
- `{0}` will be replaced by the args-entry at index *0*, if it is a List or an Array, or the args-entry with the key *_0* in a Map
- `{a}` will be replaced by the args-entry the args-entry with the key *a* in a Map

### The following is provided by this implementations additionally
The template will be interpreted as a GString where *#* is treated as *$* <br/>
So the following will be valid:
- `#0` or `#{0}` will be replaced by the args-entry at index *0*, if it is a List or an Array, or the args-entry with the key *_0* in a Map
- `#a` or `#{a}` will be replaced by the args-entry the args-entry with the key *a* in a Map
- `\#a` will result in *#a*
- `#{date.format('yyyy/MM/dd')}` will result in a formatted String of the date `date` provided by a Map argument

Additionally, on any artifact or the app-instance, you can get hold off the MessageSource instance with `messageSource` or `i18n`.

Configuration
-------------
    i18n.basename = 'messages'
    i18n.provider = 'i18n-support'

History
-------
- 0.1 <br/>
  Initial creation

