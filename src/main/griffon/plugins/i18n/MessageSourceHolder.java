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

package griffon.plugins.i18n;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Klein
 */
public final class MessageSourceHolder implements MessageSourceProvider {
    private Map<String, MessageSource> messageSources = new HashMap<String, MessageSource>();
    private String provider;
    
    private static MessageSourceHolder INSTANCE;
    
    static {
        INSTANCE = new MessageSourceHolder();
    }
    
    public static MessageSourceHolder getInstance() {
        return INSTANCE;
    }

    public MessageSource getMessageSource() {
        return getMessageSource(getProvider());
    }

    public MessageSource getMessageSource(Object constraint) {
        MessageSource messageSource = getMessageSource();
        try {
            if (messageSource instanceof ConstrainedMessageSource)
                return ((ConstrainedMessageSource) messageSource).getMessageSource(constraint);
            else
                return messageSource;
        } catch (ConstraintNotSupportedException e) {
            return messageSource;
        }
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    public void registerMessageSource(String provider, MessageSource messageSource) {
        messageSources.put(provider, messageSource);
    }

    public void unregisterMessageSource(String provider) {
        messageSources.remove(provider);
    }

    public MessageSource getMessageSource(String provider) {
        return messageSources.get(provider);
    }
}
