class I18nSupportGriffonPlugin {
    // the plugin version
    String version = '0.1'
    // the version or versions of Griffon the plugin is designed for
    String griffonVersion = '0.9.5-rc2 > *'
    // the other plugins this plugin depends on
    Map dependsOn = [:]
    // resources that are included in plugin packaging
    List pluginIncludes = []
    // the plugin license
    String license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    List toolkits = []
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    List platforms = []
    // URL where documentation can be found
    String documentation = ''
    // URL where source can be found
    String source = 'https://github.com/griffon/griffon-i18n-support-plugin'

    List authors = [
        [
            name: 'Alexander Klein',
            email: 'info@aklein.org'
        ]
    ]
    String title = 'Plugin for basic i18n-support. Multiple implementations will depend on this plugin.'
    // accepts Markdown syntax. See http://daringfireball.net/projects/markdown/ for details
    String description = '''
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
- `\\#a` will result in *#a*
- `#{date.format('yyyy/MM/dd')}` will result in a formatted String of the date `date` provided by a Map argument

Additionally, on any artifact or the app-instance, you can get hold off the MessageSource instance with `messageSource` or `i18n`.

Configuration
-------------
    i18n.basename = 'messages'
    i18n.provider = 'i18n-support'

Examples
--------
message.properties:

    // Should work on all implementations
    key.static = This is just a text
    key.dynamic.byIndex = The key {0} has the value {1}
    key.dynamic.byKey = The key {key} has the value {value}
    // Works on this implementation
    key.dynamic.byIndex.impl = The key #0 has the value #{1}
    key.dynamic.byKey.impl = The key #key has the value #{value}
    key.dynamic.fancy.impl = \\#key = #{value*10}

message_de.properties:

    key.static = Dies ist nur ein Text

Your code in an english environment (Default Locale):

    assert 'This is just a text' == getMessage('key.static')
    assert 'Test' == getMessage('key.not.existing', 'Test')
    assert 'Dies ist nur ein Text' == getMessage('key.static', Locale.GERMAN)
    assert 'The key X has the value 100' == getMessage('key.dynamic.byIndex', ['X', 100])
    assert 'The key X has the value 100' == getMessage('key.dynamic.byIndex', [_0: 'X', _1: 100])
    assert 'The key X has the value 100' == getMessage('key.dynamic.byKey', [key: 'X', value: 100])
    assert 'The key X has the value 100' == getMessage('key.dynamic.byIndex.impl', ['X', 100])
    assert 'The key X has the value 100' == getMessage('key.dynamic.byIndex.impl', [_0: 'X', _1: 100])
    assert 'The key X has the value 100' == getMessage('key.dynamic.byKey.impl', [key: 'X', value: 100])
    assert '#key = 1000' == getMessage('key.dynamic.fancy.impl', [key: 'X', value: 100])
'''
}
