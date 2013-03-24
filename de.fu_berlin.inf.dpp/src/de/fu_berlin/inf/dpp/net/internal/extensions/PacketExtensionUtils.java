/*
 * DPP - Serious Distributed Pair Programming
 * (c) Freie Universität Berlin - Fachbereich Mathematik und Informatik - 2006
 * (c) Riad Djemili - 2006
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 1, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package de.fu_berlin.inf.dpp.net.internal.extensions;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

import de.fu_berlin.inf.dpp.observables.SessionIDObservable;
import de.fu_berlin.inf.dpp.util.Utils;

/**
 * Holds various simple helper methods to create and parse simple Smack packet
 * extensions.
 * 
 * @author rdjemili
 */

/*
 * Stefan Rossbach: delete this utility class. the XStreamExtensionProvider(s)
 * should offer the package filter for their extensions !
 */
public class PacketExtensionUtils {

    private static final Logger log = Logger
        .getLogger(PacketExtensionUtils.class);

    /**
     * @return {@link PacketFilter} that only accepts messages which belong to
     *         the current session.
     */
    public static PacketFilter getSessionIDFilter(
        final XStreamExtensionProvider<? extends SarosSessionPacketExtension> extProv,
        final SessionIDObservable sessionID) {

        return new AndFilter(extProv.getPacketFilter(), new PacketFilter() {
            @Override
            public boolean accept(Packet packet) {
                SarosSessionPacketExtension extension = extProv
                    .getPayload(packet);

                if (extension == null) {
                    log.error("Invalid payload in packet: " + packet);
                    return false;
                }

                if (!Utils.equals(extension.getSessionID(),
                    sessionID.getValue()))
                    return false;

                return true;
            }
        });
    }
}
