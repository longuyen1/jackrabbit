/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.core.integration.random.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.version.Version;

/**
 * <code>Restore</code>...
 */
public class Restore extends VersionOperation {

    private static final Logger log = LoggerFactory.getLogger(Restore.class);

    public Restore(Session s, String path) {
        super(s, path);
    }

    public NodeIterator execute() throws Exception {
        Node n = getNode();
        Version v = getRandomVersion(false);
        // TODO: for now only call restore on non-base versions. See: JCR-1476
        if (v != null && !n.getBaseVersion().isSame(v)) {
            log.info(n.getPath() + ":" + v.getName());
            n.restore(v, true);
        }
        return wrapWithIterator(n);
    }
}
