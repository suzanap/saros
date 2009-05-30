package de.fu_berlin.inf.dpp.net;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import de.fu_berlin.inf.dpp.annotations.Component;
import de.fu_berlin.inf.dpp.project.ConnectionSessionListener;
import de.fu_berlin.inf.dpp.util.Util;

/**
 * The RosterTracker is responsible for offering an convenient access for
 * receiving roster listener changes, if one is not interested in tracking
 * whether the connection is changed.
 */
@Component(module = "net")
public class RosterTracker implements ConnectionSessionListener {

    private static final Logger log = Logger.getLogger(RosterTracker.class
        .getName());

    protected List<RosterListener> rosterListeners = new ArrayList<RosterListener>();

    protected XMPPConnection connection;

    protected Roster roster;

    protected RosterListener rosterListener = new RosterListener() {
        public void entriesAdded(Collection<String> addresses) {
            try {
                for (RosterListener listener : rosterListeners) {
                    listener.entriesAdded(addresses);
                }
            } catch (RuntimeException e) {
                log.error("Internal error:", e);
            }
        }

        public void entriesUpdated(Collection<String> addresses) {
            try {
                for (RosterListener listener : rosterListeners) {
                    listener.entriesUpdated(addresses);
                }
            } catch (RuntimeException e) {
                log.error("Internal error:", e);
            }
        }

        public void entriesDeleted(Collection<String> addresses) {
            try {
                for (RosterListener listener : rosterListeners) {
                    listener.entriesDeleted(addresses);
                }
            } catch (RuntimeException e) {
                log.error("Internal error:", e);
            }
        }

        public void presenceChanged(Presence presence) {
            try {
                for (RosterListener listener : rosterListeners) {
                    listener.presenceChanged(presence);
                }
            } catch (RuntimeException e) {
                log.error("Internal error:", e);
            }
        }
    };

    /**
     * Adds a listener to this roster. The listener will be fired anytime one or
     * more changes to the roster are pushed from the server.
     * 
     * @param rosterListener
     *            a roster listener.
     */
    public void addRosterListener(RosterListener rosterListener) {
        if (!rosterListeners.contains(rosterListener)) {
            rosterListeners.add(rosterListener);
        }
    }

    /**
     * Removes a listener from this roster. The listener will be fired anytime
     * one or more changes to the roster are pushed from the server.
     * 
     * @param rosterListener
     *            a roster listener.
     */
    public void removeRosterListener(RosterListener rosterListener) {
        rosterListeners.remove(rosterListener);
    }

    public void disposeConnection() {
        if (this.connection != null) {
            this.connection = null;
        }
    }

    public void prepareConnection(XMPPConnection connection) {
        this.connection = connection;
    }

    public void startConnection() {
        assert this.connection != null;
        assert this.roster == null;

        this.roster = this.connection.getRoster();
        // TODO This is too late, we might miss Roster events... reload?
        this.roster.addRosterListener(rosterListener);
    }

    public void stopConnection() {
        assert this.connection != null;
        assert this.roster != null;

        this.roster.removeRosterListener(rosterListener);
        this.roster = null;
    }

    /**
     * Returns all currently known online presences associated with a JID or an
     * unavailable presence if the user is not online or an empty list if no
     * roster is available.
     */
    public Iterable<Presence> getPresences(JID from) {
        if (from == null)
            throw new IllegalArgumentException("JID cannot be null");

        if (roster == null)
            return Collections.emptyList();

        return Util.asIterable(roster.getPresences(from.toString()));
    }
}
