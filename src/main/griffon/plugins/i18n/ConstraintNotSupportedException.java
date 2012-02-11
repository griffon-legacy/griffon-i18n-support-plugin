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

/**
 * @author Alexander Klein
 */
public class ConstraintNotSupportedException extends RuntimeException {
    private MessageSource source;
    private Object constraint;

    /**
     * Create a new exception.
     *
     * @param source     message that could not be resolved for given locale
     * @param constraint locale that was used to search for the code within
     */
    public ConstraintNotSupportedException(MessageSource source, Object constraint) {
        super("Constraint " + constraint + " is not supported by this MessageSource: " + source);
        this.source = source;
        this.constraint = constraint;
    }

    /**
     * Get the MessageSource
     *
     * @return The MessageSource instance
     */
    public MessageSource getSource() {
        return source;
    }

    /**
     * Get the constraint
     *
     * @return The constraint object
     */
    public Object getConstraint() {
        return constraint;
    }
}

