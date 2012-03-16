/*
 * Copyright 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.plugins.i18n

/**
 * @author Alexander Klein
 */
class DefaultMessageSource implements ExtendedMessageSource, ConstrainedMessageSource {
    protected String name
    protected ResourceBundle.Control control
    protected ClassLoader loader

    DefaultMessageSource(String bundleName, ClassLoader loader = null, ResourceBundle.Control control = null) {
        this.name = bundleName
        this.control = control
        this.loader = loader
    }

    String getMessage(String key, Locale locale = Locale.getDefault()) throws NoSuchMessageException {
        try {
            getBundle(name, locale).getString(key)
        } catch (MissingResourceException e) {
            throw new NoSuchMessageException(key, locale)
        }
    }

    String getMessage(String key, Object[] args, Locale locale = Locale.getDefault()) throws NoSuchMessageException {
        getMessage(key, createMapFromArguments(args), locale)
    }

    String getMessage(String key, List<Object> args, Locale locale = Locale.getDefault()) throws NoSuchMessageException {
        getMessage(key, createMapFromArguments(args), locale)
    }

    String getMessage(String key, Map<String, Object> args, Locale locale = Locale.getDefault()) throws NoSuchMessageException {
        def message = getMessage(key, locale)
        if (args != null)
            message = fillIn(message, args)
        return message
    }

    String getMessage(String key, String defaultMessage, Locale locale = Locale.getDefault()) {
        def message
        try {
            message = getMessage(key, locale)
        } catch (NoSuchMessageException e) {
            message = defaultMessage
        }
        return message
    }

    String getMessage(String key, Object[] args, String defaultMessage, Locale locale = Locale.getDefault()) {
        getMessage(key, createMapFromArguments(args), defaultMessage, locale)
    }

    String getMessage(String key, List<Object> args, String defaultMessage, Locale locale = Locale.getDefault()) {
        getMessage(key, createMapFromArguments(args), defaultMessage, locale)
    }

    String getMessage(String key, Map<String, Object> args, String defaultMessage, Locale locale = Locale.getDefault()) {
        def message = getMessage(key, defaultMessage, locale)
        if (args != null)
            message = fillIn(message, args)
        return message
    }

    MessageSource getMessageSource(Object constraint) throws ConstraintNotSupportedException {
        if (!(constraint instanceof String))
            throw new ConstraintNotSupportedException(this, constraint);
        return new DefaultMessageSource((String) constraint, loader, control)
    }

    protected ResourceBundle getBundle(String name, Locale locale) {
        if (control == null) {
            if (loader == null)
                ResourceBundle.getBundle(name, locale)
            else
                ResourceBundle.getBundle(name, locale, loader)
        } else {
            if (loader == null)
                ResourceBundle.getBundle(name, locale, control)
            else
                ResourceBundle.getBundle(name, locale, loader, control)
        }
    }

    protected Map createMapFromArguments(def args) {
        Map data = [:]
        if (args instanceof Map) {
            data.putAll(args)
            return args
        }
        int idx = 0
        if (args instanceof Object[])
            args = args.toList()
        if (args instanceof Collection) {
            args.each { arg ->
                if (arg instanceof Map) {
                    data.putAll(arg) T
                } else if (arg instanceof Collection || arg instanceof Object[]) {
                    arg.each { a ->
                        data.put("_$idx".toString(), a)
                        idx++
                    }
                } else {
                    data.put("_$idx".toString(), arg)
                    idx++
                }
            }
        } else if (args != null) {
            data.put('_0', args)
        }
        return data
    }

    protected String fillIn(String template, Map data) {
        def escaped = false
        def checkDigit = false
        def result = new StringBuilder()
        for (int p = 0; p < template.size(); p++) {
            char c = template.charAt(p)
            switch (c) {
                case '\\':
                    escaped = true
                    checkDigit = false
                    break
                case '#':
                    if (escaped) {
                        result << c
                        checkDigit = false
                    } else {
                        result << '$'
                        checkDigit = true
                    }
                    escaped = false
                    break
                case '0'..'9':
                    if (checkDigit)
                        result << '_'
                    result << c
                    escaped = false
                    checkDigit = false
                    break
                case '{':
                    if (escaped)
                        result << '\\'
                    result << c
                    escaped = false
                    checkDigit = false
                    break
                default:
                    result << c
                    escaped = false
                    checkDigit = false
            }
        }
        template = result.toString()
        template = (template =~ /([^\$\\])\{(\d+)\}/).replaceAll(/$1\$\{_$2\}/)
        template = (template =~ /^\{(\d+)\}/).replaceFirst(/\$\{_$1\}/)
        template = (template =~ /\\\{(\d+)\}/).replaceFirst(/\{$1\}/)
        def shell
        if (loader != null)
            shell = new GroovyShell(loader, new Binding(data))
        else
            shell = new GroovyShell(new Binding(data))
        return shell.evaluate("\"$template\"")
    }
}
