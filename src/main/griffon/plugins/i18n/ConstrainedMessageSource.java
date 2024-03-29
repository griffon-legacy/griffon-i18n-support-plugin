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

import java.util.Locale;
import java.util.Map;

/**
 * Interface for resolving messages, with support for the parameterization and internationalization of such messages.
 * This interface extends MessageSource with support of constraint specific datasources.
 *
 * @author Alexander Klein
 */
public interface ConstrainedMessageSource extends MessageSource {
    MessageSource getMessageSource(Object constraint) throws ConstraintNotSupportedException;
}
