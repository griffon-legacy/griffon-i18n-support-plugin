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

import griffon.core.GriffonApplication
import griffon.plugins.i18n.DefaultMessageSource
import griffon.plugins.i18n.MessageSourceHolder
import griffon.plugins.i18n.I18nEnhancer

/**
 * @author Alexander Klein
 * @author Andres Almiray
 */
class I18nSupportGriffonAddon {
    private static final String DEFAULT_I18N_FILE = 'messages'
    public static final String DEFAULT_PROVIDER = 'i18n-support'

    void addonInit(GriffonApplication app) {
        String basename = app.config.i18n?.basename ?: DEFAULT_I18N_FILE
        MessageSourceHolder.instance.provider = app.config.i18n?.provider ?: DEFAULT_PROVIDER
        MessageSourceHolder.instance.registerMessageSource(DEFAULT_PROVIDER, new DefaultMessageSource(basename))
    }

    void addonPostInit(GriffonApplication app) {
        I18nEnhancer.enhance(app.metaClass)
        def types = app.config?.i18n?.injectInto ?: ['view', 'model', 'controller']
        for(String type : types) {
            for(GriffonClass gc : app.artifactManager.getClassesOfType(type)) {
                I18nEnhancer.enhance(gc.metaClass)
            }
        }
    }
}
