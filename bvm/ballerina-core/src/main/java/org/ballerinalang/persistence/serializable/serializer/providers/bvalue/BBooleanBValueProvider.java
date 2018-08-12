/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * you may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ballerinalang.persistence.serializable.serializer.providers.bvalue;

import org.ballerinalang.model.values.BBoolean;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.persistence.serializable.serializer.BValueDeserializer;
import org.ballerinalang.persistence.serializable.serializer.BValueSerializer;
import org.ballerinalang.persistence.serializable.serializer.SerializationBValueProvider;

/**
 * Provide mapping between {@link Class} and {@link BValue} representation of it.
 */
public class BBooleanBValueProvider implements SerializationBValueProvider {
    @Override
    public String typeName() {
        return getType().getSimpleName();
    }

    @Override
    public Class<?> getType() {
        return BBoolean.class;
    }

    @Override
    public BValue toBValue(Object object, BValueSerializer serializer) {
        if (object instanceof BBoolean) {
            BBoolean bInteger = (BBoolean) object;
            return BValueProviderHelper.wrap(typeName(), bInteger);
        }
        throw BValueProviderHelper.incorrectObjectType(object, typeName());
    }

    @Override
    public Object toObject(BValue bValue, BValueDeserializer bValueDeserializer) {
        if (bValue instanceof BMap) {
            @SuppressWarnings("unchecked")
            BMap<String, BValue> wrapper = (BMap<String, BValue>) bValue;
            return BValueProviderHelper.getPayload(wrapper);
        }
        throw BValueProviderHelper.deserializationIncorrectType(bValue, typeName());
    }
}
