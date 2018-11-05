/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.ballerinalang.test.expressions.builtinoperations;

import org.ballerinalang.launcher.util.BAssertUtil;
import org.ballerinalang.launcher.util.BCompileUtil;
import org.ballerinalang.launcher.util.BRunUtil;
import org.ballerinalang.launcher.util.CompileResult;
import org.ballerinalang.model.values.BBoolean;
import org.ballerinalang.model.values.BByte;
import org.ballerinalang.model.values.BByteArray;
import org.ballerinalang.model.values.BFloat;
import org.ballerinalang.model.values.BFloatArray;
import org.ballerinalang.model.values.BIntArray;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BRefValueArray;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BStringArray;
import org.ballerinalang.model.values.BTable;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.model.values.BXMLItem;
import org.ballerinalang.model.values.BXMLSequence;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This class contains the test cases to clone operation
 * @version 0.983.0
 */

public class CloneOperationTest {

    private CompileResult result;
    private CompileResult negativeResult;

    @BeforeClass
    public void setup() {
        result = BCompileUtil.compile("test-src/expressions/builtinoperations/clone-operation.bal");
        negativeResult = BCompileUtil.compile("test-src/expressions/builtinoperations/clone-operation-negative.bal");
    }

    @Test
    public void testCloneInt() {
        BValue[] results = BRunUtil.invoke(result, "cloneInt");
        Assert.assertNotNull(results);
        Assert.assertEquals(((BInteger) results[0]).intValue(), 12);
        Assert.assertEquals(((BInteger) results[1]).intValue(), 10);
        Assert.assertEquals(((BInteger) results[2]).intValue(), 13);
    }

    @Test
    public void testCloneFloat() {
        BValue[] results = BRunUtil.invoke(result, "cloneFloat");
        Assert.assertNotNull(results);
        Assert.assertEquals(((BFloat) results[0]).floatValue(), 12.01);
        Assert.assertEquals(((BFloat) results[1]).floatValue(), 10.01);
        Assert.assertEquals(((BFloat) results[2]).floatValue(), 13.01);
    }

    @Test
    public void testCloneByte() {
        BValue[] results = BRunUtil.invoke(result, "cloneByte");
        Assert.assertNotNull(results);
        Assert.assertEquals(((BByte) results[0]).byteValue(), (byte) 234);
        Assert.assertEquals(((BByte) results[1]).byteValue(), (byte) 100);
        Assert.assertEquals(((BByte) results[2]).byteValue(), (byte) 133);
    }

    @Test
    public void testCloneBoolean() {
        BValue[] results = BRunUtil.invoke(result, "cloneBoolean");
        Assert.assertNotNull(results);
        Assert.assertFalse(((BBoolean) results[0]).booleanValue());
        Assert.assertTrue(((BBoolean) results[1]).booleanValue());
        Assert.assertTrue(((BBoolean) results[2]).booleanValue());
    }

    @Test
    public void testCloneString() {
        BValue[] results = BRunUtil.invoke(result, "cloneString");
        Assert.assertNotNull(results);
        Assert.assertEquals((results[0]).stringValue(), "BBBB");
        Assert.assertEquals((results[1]).stringValue(), "AAAA");
        Assert.assertEquals((results[2]).stringValue(), "CCCC");
    }

    @Test
    public void testCloneXML() {
        BValue[] results = BRunUtil.invoke(result, "cloneXML");

        testCloneOnXMLs((BXMLItem) results[0], "Charlos", 123, 21);
        testCloneOnXMLs((BXMLItem) results[1], "Alex", 123, 21);
        testCloneOnXMLs((BXMLItem) results[2], "Alex", 5000, 21);
    }

    private void testCloneOnXMLs(BXMLItem bxmlItem, String name, int id, int age) {

        BXMLSequence sequence = (BXMLSequence) bxmlItem.children("name");
        BXMLItem firstItem = (BXMLItem) sequence.value().get(0);
        BString textValue = firstItem.getTextValue();
        Assert.assertEquals(textValue.stringValue(), name);

        sequence = (BXMLSequence) bxmlItem.children("id");
        firstItem = (BXMLItem) sequence.value().get(0);
        textValue = firstItem.getTextValue();
        Assert.assertEquals(textValue.intValue(), id);

        sequence = (BXMLSequence) bxmlItem.children("age");
        firstItem = (BXMLItem) sequence.value().get(0);
        textValue = firstItem.getTextValue();
        Assert.assertEquals(textValue.intValue(), age);
    }

    @Test
    public void testCloneJSON() {
        BValue[] results = BRunUtil.invoke(result, "cloneJSON");
        testValuesInJSON((BMap) results[0], "Charlos", 21, 123, new Object[]{1, "EE", 12.3});
        testValuesInJSON((BMap) results[1], "Alex", 21, 123, new Object[]{1, "EE", 12.3});
        testValuesInJSON((BMap) results[2], "Alex", 21, 5000, new Object[]{1, "EE", 12.3});
    }

    private void testValuesInJSON(BMap bMap, String name, int age, int id, Object[] arr) {
        Assert.assertEquals(bMap.get("name").stringValue(), name);
        Assert.assertEquals(((BInteger) bMap.get("age")).intValue(), age);
        Assert.assertEquals(((BInteger) bMap.get("id")).intValue(), id);

        BRefValueArray array = (BRefValueArray) bMap.get("otherData");
        testJSONArray(arr, array);
    }

    @Test
    public void testCloneJSONArray() {
        BValue[] results = BRunUtil.invoke(result, "cloneJSONArray");
        testJSONArray(new Object[]{100, "EE", 12.3}, (BRefValueArray) results[0]);
        testJSONArray(new Object[]{1, "EE", 12.3}, (BRefValueArray) results[1]);
        testJSONArray(new Object[]{1, "EE", 300.5}, (BRefValueArray) results[2]);
    }

    private void testJSONArray(Object[] arr, BRefValueArray array) {
        testRefArray(arr, array);
    }

    private void testRefArray(Object[] arr, BRefValueArray array) {
        Assert.assertEquals(((BInteger) array.getBValue(0)).intValue(), ((Integer) arr[0]).intValue());
        Assert.assertEquals((array.getBValue(1)).stringValue(), arr[1]);
        Assert.assertEquals(((BFloat) array.getBValue(2)).floatValue(), arr[2]);
    }

    @Test
    public void testCloneIntArray() {
        BValue[] results = BRunUtil.invoke(result, "cloneIntArray");
        testIntArray(new long[]{100, 2, 3}, (BIntArray) results[0]);
        testIntArray(new long[]{1, 2, 3}, (BIntArray) results[1]);
        testIntArray(new long[]{1, 2, 300}, (BIntArray) results[2]);
    }

    private void testIntArray(long[] arr, BIntArray array) {
        for (int i = 0; i < arr.length; i++) {
            Assert.assertEquals(array.get(i), arr[i]);
        }
    }

    @Test
    public void testCloneByteArray() {
        BValue[] results = BRunUtil.invoke(result, "cloneByteArray");
        testByteArray(new byte[]{100, 2, 3}, (BByteArray) results[0]);
        testByteArray(new byte[]{1, 2, 3}, (BByteArray) results[1]);
        testByteArray(new byte[]{1, 2, (byte) 234}, (BByteArray) results[2]);
    }

    private void testByteArray(byte[] arr, BByteArray array) {
        for (int i = 0; i < arr.length; i++) {
            Assert.assertEquals(array.get(i), arr[i]);
        }
    }

    @Test
    public void testCloneFloatArray() {
        BValue[] results = BRunUtil.invoke(result, "cloneFloatArray");
        testFloatArray(new double[]{100.5, 2.0, 3.0}, (BFloatArray) results[0]);
        testFloatArray(new double[]{1.0, 2.0, 3.0}, (BFloatArray) results[1]);
        testFloatArray(new double[]{1.0, 2.0, 300.5}, (BFloatArray) results[2]);
    }

    private void testFloatArray(double[] arr, BFloatArray array) {
        for (int i = 0; i < arr.length; i++) {
            Assert.assertEquals(array.get(i), arr[i]);
        }
    }

    @Test
    public void testCloneStringArray() {
        BValue[] results = BRunUtil.invoke(result, "cloneStringArray");
        testStringArray(new String[]{"XX", "B", "C"}, (BStringArray) results[0]);
        testStringArray(new String[]{"A", "B", "C"}, (BStringArray) results[1]);
        testStringArray(new String[]{"A", "B", "YY"}, (BStringArray) results[2]);
    }

    private void testStringArray(String[] arr, BStringArray array) {
        for (int i = 0; i < arr.length; i++) {
            Assert.assertEquals(array.get(i), arr[i]);
        }
    }

    @Test
    public void testCloneUnionArray() {
        BValue[] results = BRunUtil.invoke(result, "cloneUnionArray");
        testRefArray(new Object[]{100, "EE", 12.3}, (BRefValueArray) results[0]);
        testRefArray(new Object[]{1, "EE", 12.3}, (BRefValueArray) results[1]);
        testRefArray(new Object[]{1, "EE", 300.5}, (BRefValueArray) results[2]);
    }

    @Test
    public void testCloneUnion() {
        BValue[] results = BRunUtil.invoke(result, "cloneUnion");
        Assert.assertEquals(((BInteger) results[0]).intValue(), 100);
        Assert.assertEquals(((BInteger) results[1]).intValue(), 1);
        Assert.assertEquals(((BFloat) results[2]).floatValue(), 300.5);
    }

    @Test
    public void testCloneConstrainedJSON() {
        BValue[] results = BRunUtil.invoke(result, "cloneConstrainedJSON");

        testConstrainedJSON((BMap) results[0], "Charlos", 1, 300.5);
        testConstrainedJSON((BMap) results[1], "Jane", 1, 300.5);
        testConstrainedJSON((BMap) results[2], "Jane", 1, 400.5);
    }

    private void testConstrainedJSON(BMap bMap, String name, int id, double salary) {
        Assert.assertEquals(((BInteger) bMap.get("id")).intValue(), id);
        Assert.assertEquals(bMap.get("name").stringValue(), name);
        Assert.assertEquals(((BFloat) bMap.get("salary")).floatValue(), salary);
    }

    @Test
    public void testCloneTable() {
        BValue[] results = BRunUtil.invoke(result, "cloneTable");
        Assert.assertNotNull(results);

        Object[][] expectedValues = new Object[][]{
                new Object[]{1, "Jane", 300.50},
                new Object[]{2, "Anne", 100.50},
                new Object[]{3, "John", 400.50},
                };
        testValuesOnTable((BTable) results[0], expectedValues);

        expectedValues = new Object[][]{
                new Object[]{1, "Jane", 300.50},
                new Object[]{2, "Anne", 100.50},
                };
        testValuesOnTable((BTable) results[1], expectedValues);

        expectedValues = new Object[][]{
                new Object[]{1, "Jane", 300.50},
                new Object[]{2, "Anne", 100.50},
                new Object[]{3, "John", 400.50},
                };
        testValuesOnTable((BTable) results[2], expectedValues);
    }

    private void testValuesOnTable(BTable table, Object[][] expectedValues) {
        int i = 0;
        while (table.hasNext()) {
            BMap<String, BValue> next = table.getNext();
            Assert.assertEquals(((BInteger) next.get("id")).intValue(), ((Integer) expectedValues[i][0]).intValue());
            Assert.assertEquals(next.get("name").stringValue(), expectedValues[i][1]);
            Assert.assertEquals(((BFloat) next.get("salary")).floatValue(), expectedValues[i][2]);
            i++;
        }
        Assert.assertEquals(expectedValues.length, i);
    }

    @Test
    public void testCloneMap() {
        BValue[] results = BRunUtil.invoke(result, "cloneMap");
        Assert.assertNotNull(results);
        testCloneOnMaps(results[0], "Charlos", 123, 21);
        testCloneOnMaps(results[1], "Alex", 123, 21);
        testCloneOnMaps(results[2], "Alex", 5000, 21);
    }

    @Test
    public void testCloneNegative() {
        Assert.assertEquals(negativeResult.getErrorCount(), 3);
        BAssertUtil.validateError(negativeResult, 0, "too many arguments in call to 'clone()'", 19, 13);
        BAssertUtil.validateError(negativeResult, 1, "Cannot clone a value of a type other than anyData " +
                "(boolean|int|float|decimal|string|xml|table|anydata[]|map<anydata>), but found 'typedesc'", 24, 18);
        BAssertUtil.validateError(negativeResult, 2, "function invocation on type 'typedesc' is not supported", 24, 18);
    }

    private void testCloneOnMaps(BValue val, String name, int id, int age) {
        BMap mapA, mapB, mapC, jsonVal;
        String tmpName;
        long tmpId, tmpAge;
        mapA = (BMap) val;
        mapB = (BMap) mapA.get("zzz");
        mapC = (BMap) mapB.get("yyy");
        jsonVal = (BMap) mapC.get("xxx");
        tmpName = jsonVal.get("name").stringValue();
        tmpId = ((BInteger) jsonVal.get("id")).intValue();
        tmpAge = ((BInteger) jsonVal.get("age")).intValue();
        Assert.assertEquals(tmpName, name);
        Assert.assertEquals(tmpId, id);
        Assert.assertEquals(tmpAge, age);
    }

    @Test
    public void testCloneNil() {
        BValue[] results = BRunUtil.invoke(result, "cloneNil");
        Assert.assertNotNull(results);
        Assert.assertEquals(((BInteger) results[0]).intValue(), 4);
        Assert.assertEquals(((BInteger) results[1]).intValue(), 10);
        Assert.assertEquals(((BInteger) results[2]).intValue(), 5);
    }

    @Test
    public void testReturnValues() {
        BValue[] results = BRunUtil.invoke(result, "cloneReturnValues");
        Assert.assertNotNull(results);
        Assert.assertEquals(((BIntArray) results[0]).get(0), 100);
        Assert.assertEquals(((BIntArray) results[1]).get(0), 20);
        Assert.assertEquals(((BIntArray) results[2]).get(0), 1000);
    }

    @Test
    public void testCloneArrayOfArrays() {
        BValue[] results = BRunUtil.invoke(result, "cloneArrayOfArrays");
        Assert.assertNotNull(results);
        Assert.assertEquals(((BIntArray) ((BRefValueArray) results[0]).get(0)).get(0), 400);
        Assert.assertEquals(((BIntArray) ((BRefValueArray) results[1]).get(0)).get(0), 200);
        Assert.assertEquals(((BIntArray) ((BRefValueArray) results[2]).get(0)).get(0), 500);
    }

    @Test
    public void testCloneTuples() {
        BValue[] results = BRunUtil.invoke(result, "cloneTuple");
        Assert.assertNotNull(results);
        testTupleValues((BRefValueArray) results[0], 100, 400);
        testTupleValues((BRefValueArray) results[1], 100, 200);
        testTupleValues((BRefValueArray) results[2], 100, 500);
    }

    private void testTupleValues(BRefValueArray result, int mapValue, int arrValue) {
        BMap bMap = (BMap) result.get(0);
        BIntArray intArr = (BIntArray) result.get(1);
        Assert.assertEquals(((BInteger) bMap.get("one")).intValue(), mapValue);
        Assert.assertEquals(intArr.get(0), arrValue);
    }

    @Test
    public void testCloneAnyDataRecord() {
        BValue[] results = BRunUtil.invoke(result, "cloneAnydataRecord");
        Assert.assertNotNull(results);
        testCloneRecordValues((BMap) results[0], 100, "Charlos", 300.5);
        testCloneRecordValues((BMap) results[1], 100, "Alex", 300.5);
        testCloneRecordValues((BMap) results[2], 100, "Alex", 400.5);
    }

    private void testCloneRecordValues(BMap bMap, int id, String name, double salary) {
        Assert.assertEquals(((BInteger) bMap.get("id")).intValue(), id);
        Assert.assertEquals(bMap.get("name").stringValue(), name);
        Assert.assertEquals(((BFloat) bMap.get("salary")).floatValue(), salary);
    }
}
