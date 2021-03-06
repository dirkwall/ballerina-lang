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

# Represents a connection which will be used for subscription.
public type Listener object {

    *AbstractListener;
    private Connection? connection = ();

    # Creates a new consumer. A new connection will be created if a refernece to a connection is not provided.
    #
    # + c - An already established connection or configuration to create a new connection.
    public function __init(ConnectionConfig|Connection c) {
        if (c is Connection) {
            self.connection = c;
        } else {
            self.connection = new Connection(c);
        }
    }

    # Binds the NATS consumer to a service.
    #
    # + serviceType - Type descriptor of the service.
    # + name - Name of the service.
    # + return - Nil or error upon failure to register listener.
    public function __attach(service serviceType, string? name = ()) returns error? {
        return self.create(serviceType, name);
    }

    function create(service serviceType, string? name) returns error? = external;

    # Starts the listener in the lifecyle.
    #
    # + return - Error or ().
    public function __start() returns error? {
        //ignore : since connection can be re-used between multiple listeners
    }

    # Stops the listener in the lifecyle.
    #
    # + return - error or ().
    public function __stop() returns error? {
        //ignore : since connection can be re-used between multiple listeners
    }

};
