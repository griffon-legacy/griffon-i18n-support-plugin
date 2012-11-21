class I18nSupportGriffonPlugin {
    // the plugin version
    String version = '0.2.1'
    // the version or versions of Griffon the plugin is designed for
    String griffonVersion = '* < 1.1.0'
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
        ],
        [
            name: 'Andres Almiray',
            email: 'aalmiray@yahoo.com'
        ]
    ]
    String title = 'Base i18n support'
    // accepts Markdown syntax. See http://daringfireball.net/projects/markdown/ for details
    String description = '''
Provides basic internationalization support.

 __WARNING__

   _This plugin is no longer supported since Griffon 1.1.0.
   It's usage is highly discouraged and should be uninstalled from *every* application.
   See the [Internationalization][1] chapter of the Griffon Guide for more information_

Other plugin can provide different implementations of this functionality. <br/>
The used implementation can be configured with the configuration key `i18n.provider`. <br/>
The default provider is `i18n-support`.

This implementation uses standard Java `ResourceBundle`s with the bundlename defined by the configuration key `i18n.basename`. <br/>
The default bundlename is `messages`.

Usage
-----

The plugin will inject the following dynamic methods:

 * String getMessage(String key) throws NoSuchMessageException
 * String getMessage(String key, Locale locale) throws NoSuchMessageException
 * String getMessage(String key, Object[] args) throws NoSuchMessageException
 * String getMessage(String key, Object[] args, Locale locale) throws NoSuchMessageException
 * String getMessage(String key, List<?> args) throws NoSuchMessageException
 * String getMessage(String key, List<?> args, Locale locale) throws NoSuchMessageException
 * String getMessage(String key, String defaultMessage)
 * String getMessage(String key, String defaultMessage, Locale locale)
 * String getMessage(String key, Object[] args, String defaultMessage)
 * String getMessage(String key, Object[] args, String defaultMessage, Locale locale)
 * String getMessage(String key, List<?> args, String defaultMessage)
 * String getMessage(String key, List<?> args, String defaultMessage, Locale locale)

If the configured `MessageSourceProvider` delivers a `Extended MessageSource` then the additional methods become available too

 * String getMessage(String key, Map<String, Object> args) throws NoSuchMessageException
 * String getMessage(String key, Map<String, Object> args, Locale locale) throws NoSuchMessageException
 * String getMessage(String key, Map<String, Object> args, String defaultMessage)
 * String getMessage(String key, Map<String, Object> args, String defaultMessage, Locale locale)

These methods are also accessible to any component through the singleton `griffon.plugins.i18n.MessageSourceHolder`.
You can inject these methods to non-artifacts via metaclasses. Simply grab hold of a particular metaclass and call
`I18nEnhancer.enhance(metaClassInstance)`.

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

Additionally, on any artifact or the app-instance, you can get hold of the MessageSource instance with `messageSource` or `i18n`.

Configuration
-------------

Enabling the i18n provider delivered by this plugin requires the application to have the following settings in `Config.groovy`

    i18n.basename = 'messages'
    i18n.provider = 'i18n-support'

### Dynamic method injection

Dynamic methods will be added to MVC members and the application instance by default. You can
change this setting by adding a configuration flag in `griffon-app/conf/Config.groovy`

    i18n.injectInto = ['view', 'model', 'controller', 'service']

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

Testing
-------

Dynamic methods will not be automatically injected during unit testing, because addons are simply not initialized
for this kind of tests. However you can use `I18nEnhancer.enhance(metaClassInstance, messageSourceProviderInstance)` where 
`messageSourceProviderInstance` is of type `griffon.plugins.i18n.MessageSourceProvider`.
The contract for this interface looks like this

    public interface I18nProvider {
        MessageSource getMessageSource();
    }

It's up to you define how the MessageSource is implemented for your tests. For example, here's a atrivial implementation that returns
the message key as the resolved message

    class MyMessageSourceProvider implements MessageSourceProvider {
        final MessageSource messageSource = new MessageSource() {
            String getMessage(String key) throws NoSuchMessageException { key }
            // all other methods implemented in the same fashion
        }
    }
    
This implementation may be used in the following way

    class MyServiceTests extends GriffonUnitTestCase {
        void testSmokeAndMirrors() {
            MyService service = new MyService()
            I18nEnhancer.enhance(service.metaClass, new MyMessageSourceProvider())
            // exercise service methods
        }
    }

[1]: http://griffon.codehaus.org/guide/latest/guide/internationalization.html
'''
}
