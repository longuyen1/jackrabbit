/*
 * Copyright 2004-2005 The Apache Software Foundation or its licensors,
 *                     as applicable.
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
package org.apache.jackrabbit.core.version;

import org.apache.jackrabbit.core.*;
import org.apache.jackrabbit.core.state.NodeState;

import javax.jcr.Item;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.nodetype.NodeDef;
import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;

/**
 * This Class implements a version history that extends a node.
 */
public class VersionHistoryImpl extends NodeImpl implements VersionHistory {

    /**
     * the internal version history
     */
    private final InternalVersionHistory history;

    /**
     * creates a new version history node.
     *
     * @param itemMgr
     * @param session
     * @param id
     * @param state
     * @param definition
     * @param listeners
     * @param history
     * @throws RepositoryException
     */
    public VersionHistoryImpl(ItemManager itemMgr, SessionImpl session, NodeId id,
                              NodeState state, NodeDef definition,
                              ItemLifeCycleListener[] listeners,
                              InternalVersionHistory history) throws RepositoryException {
        super(itemMgr, session, id, state, definition, listeners);
        this.history = history;
    }

    /**
     * @see VersionHistory#getRootVersion()
     */
    public Version getRootVersion() throws RepositoryException {
        return (Version) session.getNodeByUUID(history.getRootVersion().getId());
    }

    /**
     * @see VersionHistory#getAllVersions()
     */
    public VersionIterator getAllVersions() throws RepositoryException {
        return new VersionIteratorImpl(session, history.getRootVersion());
    }

    /**
     * @see VersionHistory#getVersion(String)
     */
    public Version getVersion(String versionName)
            throws VersionException, RepositoryException {
        try {
            QName name = QName.fromJCRName(versionName, session.getNamespaceResolver());
            InternalVersion v = history.getVersion(name);
            return v == null ? null : (Version) session.getNodeByUUID(v.getId());
        } catch (IllegalNameException e) {
            throw new RepositoryException(e);
        } catch (UnknownPrefixException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * @see VersionHistory#getVersionByLabel(String)
     */
    public Version getVersionByLabel(String label) throws RepositoryException {
        InternalVersion v = history.getVersionByLabel(label);
        return v == null ? null : (Version) session.getNodeByUUID(v.getId());
    }

    /**
     * @see VersionHistory#addVersionLabel(String, String, boolean)
     */
    public void addVersionLabel(String version, String label, boolean move)
            throws VersionException, RepositoryException {
        try {
            QName name = QName.fromJCRName(version, session.getNamespaceResolver());
            history.addVersionLabel(name, label, move);
        } catch (IllegalNameException e) {
            throw new RepositoryException(e);
        } catch (UnknownPrefixException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * @see VersionHistory#removeVersionLabel(String)
     */
    public void removeVersionLabel(String label) throws RepositoryException {
        history.removeVersionLabel(label);
    }


    /**
     * @see VersionHistory#getVersionLabels
     */
    public String[] getVersionLabels() {
        return new String[0];  // @todo implement VersionHistory#getVersionLabels()
    }

    /**
     * @see VersionHistory#getVersionLabels(Version)
     */
    public String[] getVersionLabels(Version version)
            throws VersionException, RepositoryException {
        return new String[0];  // @todo implement VersionHistory#getVersionLabels(Version)
    }

    /**
     * @see VersionHistory#hasVersionLabel(String)
     */
    public boolean hasVersionLabel(String label) {
        return false;  // @todo implement VersionHistory#hasVersionLabel(String)
    }

    /**
     * @see VersionHistory#hasVersionLabel(Version, String)
     */
    public boolean hasVersionLabel(Version version, String label)
            throws VersionException, RepositoryException {
        return false;  // @todo implement VersionHistory#hasVersionLabel(Version, String)
    }

    /**
     * @see VersionHistory#removeVersionLabel(String)
     */
    public void removeVersion(String label)
            throws UnsupportedRepositoryOperationException, VersionException,
            RepositoryException {
        // @todo implement VersionHistory#removeVersionLabel(String)
    }

    /**
     * @see javax.jcr.Node#getUUID()
     */
    public String getUUID()
            throws UnsupportedRepositoryOperationException, RepositoryException {
        return history.getId();
    }

    /**
     * @see javax.jcr.Item#isSame(javax.jcr.Item)
     */
    public boolean isSame(Item otherItem) {
        if (otherItem instanceof VersionHistoryImpl) {
            // since all version histories live in the same workspace, we can compare the uuids
            return ((VersionHistoryImpl) otherItem).history.getId().equals(history.getId());
        } else {
            return false;
        }
    }
}
