// Copyright (c) 2019 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/artemis;
import ballerina/filepath;

public function testSimpleSend() {
    artemis:Producer prod = new({host: "localhost", port: 61616}, "simple_queue", addressConfig = {autoCreated:false});
    var err = prod->send("Hello World");
    err = prod->close();
}

public function testSimpleSslSend() {
    artemis:Producer prod = new({host: "localhost", port:5500,
    secureSocket: {trustStore: {path: checkpanic filepath:absolute(checkpanic filepath:build("src", "test",
    "resources", "security", "keystore", "ballerinaTruststore.p12")), password: "ballerina"}}},
    "simple_queue", addressConfig = {autoCreated:false});
    var err = prod->send("Sending over ssl");
    err = prod->close();
}
