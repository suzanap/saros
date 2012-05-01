package de.fu_berlin.inf.dpp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.bytestreams.BytestreamSession;
import org.picocontainer.annotations.Nullable;

import bmsi.util.Diff;
import bmsi.util.DiffPrint;
import de.fu_berlin.inf.dpp.Saros;
import de.fu_berlin.inf.dpp.activities.SPath;
import de.fu_berlin.inf.dpp.editor.internal.EditorAPI;
import de.fu_berlin.inf.dpp.net.JID;
import de.fu_berlin.inf.dpp.ui.dialogs.RememberDecisionMessageDialog;

/**
 * Static Utility functions
 */
public class Utils {

    private static final Logger log = Logger.getLogger(Utils.class);

    private Utils() {
        // no instantiation allowed
    }

    protected static final Base64 base64Codec = new Base64();
    protected static final URLCodec urlCodec = new URLCodec();

    protected static String escape(String toEscape, BinaryEncoder encoder) {

        byte[] toEncode;
        try {
            toEncode = toEscape.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            toEncode = toEscape.getBytes();
        }

        byte[] encoded = {};
        try {
            encoded = encoder.encode(toEncode);
        } catch (EncoderException e) {
            log.error("can not escape", e);
        }

        try {
            return new String(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            return new String(encoded);
        }
    }

    protected static String unescape(String toUnescape, BinaryDecoder decoder) {

        byte[] toDecode;
        try {
            toDecode = toUnescape.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            toDecode = toUnescape.getBytes();
        }

        byte[] decoded = {};
        try {
            decoded = decoder.decode(toDecode);
        } catch (DecoderException e) {
            log.error("can not unescape", e);
        }

        try {
            return new String(decoded, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            return new String(decoded);
        }
    }

    public static String escapeBase64(String toEscape) {
        return escape(toEscape, base64Codec);
    }

    public static String unescapeBase64(String toUnescape) {
        return unescape(toUnescape, base64Codec);
    }

    public static String urlEscape(String toEscape) {
        return escape(toEscape, urlCodec);
    }

    public static String urlUnescape(String toUnescape) {
        return unescape(toUnescape, urlCodec);
    }

    public static void close(Socket socketToClose) {
        if (socketToClose == null)
            return;

        try {
            socketToClose.close();
        } catch (IOException e) {
            // ignore
        }
    }

    /**
     * Returns a runnable that upon being run will wait the given time in
     * milliseconds and then run the given runnable.
     * 
     * The returned runnable supports being interrupted upon which the runnable
     * returns with the interrupted flag being raised.
     * 
     */
    public static Runnable delay(final int milliseconds, final Runnable runnable) {
        return new Runnable() {
            public void run() {
                try {
                    Thread.sleep(milliseconds);
                } catch (InterruptedException e) {
                    log.error("Code not designed to be interruptable", e);
                    Thread.currentThread().interrupt();
                    return;
                }
                runnable.run();
            }
        };
    }

    /**
     * Returns a callable that upon being called will wait the given time in
     * milliseconds and then call the given callable.
     * 
     * The returned callable supports being interrupted upon which an
     * InterruptedException is being thrown.
     * 
     */
    public static <T> Callable<T> delay(final int milliseconds,
        final Callable<T> callable) {
        return new Callable<T>() {
            public T call() throws Exception {

                Thread.sleep(milliseconds);
                return callable.call();
            }
        };
    }

    public static <T> Callable<T> retryEveryXms(final Callable<T> callable,
        final int retryMillis) {
        return new Callable<T>() {
            public T call() {
                T t = null;
                while (t == null && !Thread.currentThread().isInterrupted()) {
                    try {
                        t = callable.call();
                    } catch (InterruptedIOException e) {
                        // Workaround for bug in Limewire RUDP
                        // https://www.limewire.org/jira/browse/LWC-2838
                        return null;
                    } catch (InterruptedException e) {
                        log.error("Code not designed to be interruptable", e);
                        Thread.currentThread().interrupt();
                        return null;
                    } catch (Exception e) {
                        // Log here for connection problems.
                        t = null;
                        try {
                            Thread.sleep(retryMillis);
                        } catch (InterruptedException e2) {
                            log.error("Code not designed to be interruptable",
                                e);
                            Thread.currentThread().interrupt();
                            return null;
                        }
                    }
                }
                return t;
            }
        };
    }

    /**
     * Returns an iterable which will return the given iterator ONCE.
     * 
     * Subsequent calls to iterator() will throw an IllegalStateException.
     * 
     * @param <T>
     * @param it
     *            an Iterator to wrap
     * @return an Iterable which returns the given iterator ONCE.
     */
    public static <T> Iterable<T> asIterable(final Iterator<T> it) {
        return new Iterable<T>() {

            boolean returned = false;

            public Iterator<T> iterator() {
                if (returned)
                    throw new IllegalStateException(
                        "Can only call iterator() once.");

                returned = true;

                return it;
            }
        };
    }

    public static PacketFilter orFilter(final PacketFilter... filters) {

        return new PacketFilter() {

            public boolean accept(Packet packet) {

                for (PacketFilter filter : filters) {
                    if (filter.accept(packet)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static String read(InputStream input) throws IOException {

        try {
            byte[] content = IOUtils.toByteArray(input);

            try {
                return new String(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return new String(content);
            }
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * Utility method similar to {@link ObjectUtils#equals(Object, Object)},
     * which causes a compile error if the second parameter is not a subclass of
     * the first.
     */
    public static <V, K extends V> boolean equals(V object1, K object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }

    /**
     * Return a new Runnable which runs the given runnable but catches all
     * RuntimeExceptions and logs them to the given log.
     * 
     * Errors are logged and re-thrown.
     * 
     * This method does NOT actually run the given runnable, but only wraps it.
     * 
     * @param log
     *            The log to print any exception messages thrown which occur
     *            when running the given runnable. If null, the
     *            {@link Utils#log} is used.
     * 
     */
    public static Runnable wrapSafe(@Nullable Logger log,
        final Runnable runnable) {

        if (log == null) {
            log = Utils.log;
        }
        final Logger logToUse = log;

        StackTrace tmp = null;
        // Assign tmp = new StackTrace() iff assertions are enabled.
        assert (tmp = new StackTrace()) != null;
        final StackTrace stackTrace = tmp;
        return new Runnable() {
            @SuppressWarnings("null")
            public void run() {
                try {
                    runnable.run();
                } catch (RuntimeException e) {
                    logToUse.error("Internal Error:", e);
                    if (stackTrace != null) {
                        logToUse.error("Original caller:", stackTrace);
                    }
                } catch (Error e) {
                    logToUse.error("Internal Fatal Error:", e);
                    if (stackTrace != null) {
                        logToUse.error("Original caller:", stackTrace);
                    }

                    // Re-throw errors (such as an OutOfMemoryError)
                    throw e;
                }
            }
        };
    }

    /**
     * Run the given runnable in a new thread (with the given name) and log any
     * RuntimeExceptions to the given log.
     * 
     * @return The Thread which has been created and started to run the given
     *         runnable
     * 
     * @nonBlocking
     */
    public static Thread runSafeAsync(@Nullable String name, final Logger log,
        final Runnable runnable) {

        Thread t = new Thread(wrapSafe(log, runnable));
        if (name != null)
            t.setName(name);
        t.start();
        return t;
    }

    /**
     * Run the given runnable in a new thread and log any RuntimeExceptions to
     * the given log.
     * 
     * @return The Thread which has been created and started to run the given
     *         runnable.
     * 
     * @nonBlocking
     */
    public static Thread runSafeAsync(final Logger log, final Runnable runnable) {
        return runSafeAsync(null, log, runnable);
    }

    /**
     * Run the given runnable in the SWT-Thread, log any RuntimeExceptions to
     * the given log and block until the runnable returns.
     * 
     * @blocking
     */

    public static void runSafeSWTSync(final Logger log, final Runnable runnable) {
        try {
            Display.getDefault().syncExec(wrapSafe(log, runnable));
        } catch (SWTException e) {
            if (!PlatformUI.getWorkbench().isClosing()) {
                throw e;
            }
        }
    }

    /**
     * Class used for reporting the results of a Callable<T> inside a different
     * thread.
     * 
     * It is kind of like a Future Light.
     */
    public static class CallableResult<T> {
        public T result;
        public Exception e;
    }

    /**
     * Runs the given callable in the SWT Thread returning the result of the
     * computation or throwing an exception that was thrown by the callable.
     */
    public static <T> T runSWTSync(final Callable<T> callable) throws Exception {

        final CallableResult<T> result = new CallableResult<T>();

        Utils.runSafeSWTSync(log, new Runnable() {
            public void run() {
                try {
                    result.result = callable.call();
                } catch (Exception e) {
                    result.e = e;
                }
            }
        });

        if (result.e != null)
            throw result.e;
        else
            return result.result;
    }

    public static String escapeForLogging(String s) {
        if (s == null)
            return null;

        return StringEscapeUtils.escapeJava(s);
        /*
         * // Try to put nice symbols for non-readable characters sometime
         * return s.replace(' ', '\uc2b7').replace('\t',
         * '\uc2bb').replace('\n','\uc2b6').replace('\r', '\uc2a4');
         */
    }

    /**
     * Run the given runnable in the SWT-Thread and log any RuntimeExceptions to
     * the given log.
     * 
     * @nonBlocking
     */
    public static void runSafeSWTAsync(final Logger log, final Runnable runnable) {
        try {
            Display.getDefault().asyncExec(wrapSafe(log, runnable));
        } catch (SWTException e) {
            if (!PlatformUI.getWorkbench().isClosing()) {
                throw e;
            }
        }
    }

    /**
     * Run the given runnable (in the current thread!) and log any
     * RuntimeExceptions to the given log and block until the runnable returns.
     * 
     * @blocking
     */
    public static void runSafeSync(Logger log, Runnable runnable) {
        wrapSafe(log, runnable).run();
    }

    /**
     * Run the given runnable (in the current thread!) and log any
     * RuntimeExceptions to the given log and block until the runnable returns.
     * 
     * @blocking
     */
    public static void runSafeSyncFork(Logger log, Runnable runnable) {
        Thread t = runSafeAsync(null, log, runnable);
        try {
            t.join();
        } catch (InterruptedException e) {
            log.warn("Waiting for the forked thread in runSafeSyncFork was"
                + " interrupted unexpectedly");
        }
    }

    /**
     * @swt Needs to be called from the SWT-UI thread, otherwise
     *      <code>null</code> is returned.
     */
    public static IViewPart findView(String id) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null)
            return null;

        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window == null)
            return null;

        IWorkbenchPage page = window.getActivePage();
        if (page == null)
            return null;

        return page.findView(id);
    }

    /**
     * Return a string representation of the given paths suitable for debugging
     * by joining their OS dependent full path representation by ', '
     */
    public static String toOSString(final Set<SPath> paths) {
        StringBuilder sb = new StringBuilder();
        for (SPath path : paths) {
            if (sb.length() > 0)
                sb.append(", ");

            sb.append(path.getFullPath().toOSString());
        }
        return sb.toString();
    }

    /**
     * Crude check whether we are on the SWT thread
     */
    public static boolean isSWT() {
        if (Display.getCurrent() != null) {
            return true;
        }
        try {
            boolean platformSWT = PlatformUI.getWorkbench().getDisplay()
                .getThread() == Thread.currentThread();
            if (platformSWT) {
                log.warn("Running in PlatformSWT Thread"
                    + " which is not found with Display.getCurrent()");
            }
            return platformSWT;
        } catch (SWTException e) {
            return false;
        }
    }

    public static String getEclipsePlatformInfo() {
        return Platform.getBundle("org.eclipse.core.runtime").getVersion()
            .toString();
    }

    public static String getPlatformInfo() {

        String javaVersion = System.getProperty("java.version",
            "Unknown Java Version");
        String javaVendor = System.getProperty("java.vendor", "Unknown Vendor");
        String os = System.getProperty("os.name", "Unknown OS");
        String osVersion = System.getProperty("os.version", "Unknown Version");
        String hardware = System.getProperty("os.arch", "Unknown Architecture");

        StringBuilder sb = new StringBuilder();

        sb.append("  Java Version: " + javaVersion + "\n");
        sb.append("  Java Vendor: " + javaVendor + "\n");
        sb.append("  Eclipse Runtime Version: " + getEclipsePlatformInfo()
            + "\n");
        sb.append("  Operating System: " + os + " (" + osVersion + ")\n");
        sb.append("  Hardware Architecture: " + hardware);

        return sb.toString();
    }

    public static String prefix(JID jid) {
        if (jid == null) {
            return "[Unknown] ";
        } else {
            return "[" + jid.toString() + "] ";
        }
    }

    /**
     * Given a *File*name, this function will ensure that if the filename
     * contains directories, these directories are created.
     * 
     * Returns true if the directory of the given file already exists or the
     * file has no parent directory.
     * 
     * Returns false, if the directory could not be created.
     */
    public static boolean mkdirs(String filename) {
        File parent = new File(filename).getParentFile();

        if (parent == null || parent.isDirectory()) {
            return true;
        }

        if (!parent.mkdirs()) {
            log.error("Could not create Dir: " + parent.getPath());
            return false;
        } else
            return true;
    }

    /**
     * Tries to open the given URL string in the default external browser. The
     * Desktop API is deliberately not used for this because it only works with
     * Java 1.6.
     * 
     * @param urlString
     *            the URL to show as a String
     * @return true if the browser could be opened, false otherwise
     */
    public static boolean openExternalBrowser(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            log.error("Couldn't parse URL from string " + urlString, e);
            return false;
        }

        IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench()
            .getBrowserSupport();
        IWebBrowser browser;
        try {
            browser = browserSupport.getExternalBrowser();
            browser.openURL(url);
            return true;
        } catch (Exception e) {
            log.error("Couldn't open external browser", e);
            return false;
        }

    }

    /**
     * Tries to open the given URL string in Eclipse's internal browser. However
     * if the user specified in the preferences to use an external browser
     * instead, the external browser is tried to open.
     * 
     * @param urlString
     *            the URL to show as a String
     * @param title
     *            a string displayed in the browsers title area
     * @return true if the browser could be opened, false otherwise
     */
    public static boolean openInternalBrowser(String urlString, String title) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            log.error("Couldn't parse URL from string " + urlString, e);
            return false;
        }

        IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench()
            .getBrowserSupport();
        IWebBrowser browser;
        try {
            browser = browserSupport.createBrowser(
                IWorkbenchBrowserSupport.AS_EDITOR
                    | IWorkbenchBrowserSupport.LOCATION_BAR
                    | IWorkbenchBrowserSupport.NAVIGATION_BAR, null, title, "");
            browser.openURL(url);
            return true;
        } catch (Exception e) {
            log.error("Couldn't open internal Browser", e);
            return false;
        }
    }

    public static String getMessage(Throwable e) {
        if (e instanceof XMPPException) {
            return e.toString();
        }
        if (e instanceof ExecutionException && e.getCause() != null) {
            return getMessage(e.getCause());
        }
        return e.getMessage();
    }

    public static final int CHUNKSIZE = 16 * 1024;

    /**
     * Compresses the given byte array using a Java Deflater.
     */
    public static byte[] deflate(byte[] input, IProgressMonitor subMonitor) {
        subMonitor.beginTask("Deflate bytearray", input.length / CHUNKSIZE + 1);

        Deflater compressor = new Deflater(Deflater.DEFLATED);
        compressor.setInput(input);
        compressor.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

        byte[] buf = new byte[CHUNKSIZE];
        while (!compressor.finished() && !subMonitor.isCanceled()) {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
            subMonitor.worked(1);
        }
        IOUtils.closeQuietly(bos);

        subMonitor.done();

        return bos.toByteArray();
    }

    /**
     * Uncompresses the given byte array using a Java Inflater.
     * 
     * @throws IOException
     *             If the operation fails (because the given byte array does not
     *             contain data accepted by the inflater)
     */
    public static byte[] inflate(byte[] input, IProgressMonitor subMonitor)
        throws IOException {

        subMonitor.beginTask("Inflate bytearray", input.length / CHUNKSIZE + 1);

        ByteArrayOutputStream bos;
        Inflater decompressor = new Inflater();
        decompressor.setInput(input, 0, input.length);
        bos = new ByteArrayOutputStream(input.length);
        byte[] buf = new byte[CHUNKSIZE];

        try {
            while (!decompressor.finished() && !subMonitor.isCanceled()) {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
                subMonitor.worked(1);
            }
            return bos.toByteArray();
        } catch (java.util.zip.DataFormatException ex) {
            log.error("Failed to inflate bytearray", ex);
            throw new CausedIOException(ex);
        } finally {
            IOUtils.closeQuietly(bos);
            subMonitor.done();
        }
    }

    /**
     * Print the difference between the contents of the given file and the given
     * inputBytes to the given log.
     */
    public static void logDiff(Logger log, JID from, SPath path,
        byte[] inputBytes, IFile file) {
        try {
            if (file == null) {
                log.error("No file given", new StackTrace());
                return;
            }

            if (!file.exists()) {
                log.info("File on disk is missing: " + file);
                return;
            }

            if (inputBytes == null) {
                log.info("File on disk is to be deleted:" + file);
                return;
            }

            // get stream from old file
            InputStream oldStream = file.getContents();
            InputStream newStream = new ByteArrayInputStream(inputBytes);

            // read Lines from
            Object[] oldContent = readLinesAndEscapeNewlines(oldStream);
            Object[] newContent = readLinesAndEscapeNewlines(newStream);

            // Calculate diff of the two files
            Diff diff = new Diff(oldContent, newContent);
            Diff.Change script = diff.diff_2(false);

            // log diff
            DiffPrint.UnifiedPrint print = new DiffPrint.UnifiedPrint(
                oldContent, newContent);
            Writer writer = new StringWriter();
            print.setOutput(writer);
            print.print_script(script);

            String diffAsString = writer.toString();
            if (diffAsString.trim().length() == 0) {
                log.error("No inconsistency found in file [" + from.getName()
                    + "] " + path);
            } else {
                log.info("Diff of inconsistency: \nPath: " + path + "\n"
                    + diffAsString);
            }
        } catch (CoreException e) {
            log.error("Can't read file content", e);
        } catch (IOException e) {
            log.error("Can't convert file content to String", e);
        }
    }

    /**
     * Reads the given stream into an array of lines while retaining and
     * escaping the line delimiters.
     */
    public static String[] readLinesAndEscapeNewlines(InputStream stream)
        throws IOException {

        List<String> result = new ArrayList<String>();
        String data = IOUtils.toString(stream);
        Matcher matcher = Pattern.compile("\\r\\n|\\r|\\n").matcher(data);
        int previousEnd = 0;
        while (matcher.find()) {
            // Extract line and escape line delimiter.
            result.add(data.substring(previousEnd, matcher.start())
                + escapeForLogging(matcher.group()));
            previousEnd = matcher.end();
        }
        // If there is no line delimiter after the last line...
        if (previousEnd != data.length()) {
            result.add(data.substring(previousEnd, data.length()));
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * Returns a string representation of the throughput when processing the
     * given number of bytes in the given time in milliseconds.
     */
    public static String throughput(long length, long deltaMs) {
        if (deltaMs == 0) {
            return " (" + formatByte(length) + " in < 1 ms)";
        } else {
            return " (" + formatByte(length) + " in "
                + formatDuration(deltaMs / 1000) + " at "
                + formatByte(length / deltaMs * 1000) + "/s)";
        }
    }

    /**
     * Turn an integer representing a file size into a human readable
     * representation based on 1KB = 1000 Byte, 1000 KB=1MB, etc. (SI)
     */
    public static String formatByte(long bytes) {
        int unit = 1000;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = ("kMGTPE").charAt(exp - 1);
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * Formats a given duration in seconds (e.g. achived by using a StopWatch)
     * as HH:MM:SS
     * 
     * @param seconds
     * @return
     */
    public static String formatDuration(long seconds) {
        String format = "";

        if (seconds <= 0L) {
            return "";
        }
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = (seconds % 60);

        format += hours > 0 ? String.format("%02d", hours) + "h " : "";
        format += minutes > 0 ? String.format(hours > 0 ? "%02d" : "%d",
            minutes) + "m " : "";
        format += seconds > 0 ? String
            .format(minutes > 0 ? "%02d" : "%d", secs) + "s" : "";
        return format;
    }

    /**
     * Serializes an {@link Serializable}. Errors are logged to
     * {@link Utils#log} . .
     * 
     * @param o
     *            {@link Serializable} to serialize
     * @return the serialized data or <code>null</code> (when not serializable)
     */
    public static byte[] serialize(Serializable o) {
        if (o == null) {
            return null;
        }
        byte[] data = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.close();
            data = bos.toByteArray();
        } catch (NotSerializableException e) {
            log.error("'" + o + "' is not serializable: ", e);
        } catch (IOException e) {
            log.error("Unexpected error during serialization: ", e);
        }
        return data;
    }

    /**
     * Restores a serialized {@link Serializable}. Errors are logged to
     * {@link Utils#log}.
     * 
     * @param serialized
     *            serialized {@link Object}
     * @return deserialized {@link Object} or <code>null</code>
     */
    public static Object deserialize(byte[] serialized) {
        Object o = null;
        try {
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(serialized));
            o = in.readObject();
            in.close();
        } catch (IOException e) {
            log.error("Unexpected error during deserialization: ", e);
        } catch (ClassNotFoundException e) {
            log.error("Class not found in system: ", e);
        }

        return o;
    }

    /**
     * Indicate the User that there was an error. It pops up an ErrorDialog with
     * given title and message.
     */
    public static void popUpFailureMessage(final String title,
        final String message, boolean failSilently) {
        if (failSilently)
            return;

        Utils.runSafeSWTSync(log, new Runnable() {
            public void run() {
                MessageDialog.openError(EditorAPI.getShell(), title, message);
            }
        });
    }

    /**
     * Ask the User a given question. It pops up an QuestionDialog with given
     * title and message.
     * 
     * @return boolean indicating whether the user said Yes or No
     */
    public static boolean popUpYesNoQuestion(final String title,
        final String message, boolean failSilently) {
        if (failSilently)
            return false;

        try {
            return Utils.runSWTSync(new Callable<Boolean>() {
                public Boolean call() {
                    return MessageDialog.openQuestion(EditorAPI.getShell(),
                        title, message);
                }
            });
        } catch (Exception e) {
            log.error("An internal error ocurred while trying"
                + " to open the question dialog.");
            return false;
        }
    }

    /**
     * Ask the User a given question. It pops up an QuestionDialog with given
     * title and message. Additionally custom button labels are applicable.
     * 
     * @param title
     *            dialog title
     * @param message
     *            displayed message
     * @param dialogButtonLabels
     *            custom button labels
     * @param failSilently
     *            don`t open the dialog
     * 
     * @return boolean indicating whether the user said Yes or No
     */
    public static boolean popUpCustomQuestion(final String title,
        final String message, final String[] dialogButtonLabels,
        boolean failSilently) {
        if (failSilently)
            return false;

        try {
            return Utils.runSWTSync(new Callable<Boolean>() {
                public Boolean call() {
                    MessageDialog md = new MessageDialog(EditorAPI.getShell(),
                        title, null, message, MessageDialog.QUESTION,
                        dialogButtonLabels, 0);
                    md.open();
                    return md.getReturnCode() == 0;
                }
            });
        } catch (Exception e) {
            log.error("An internal error ocurred while trying"
                + " to open the question dialog.");
            return false;
        }
    }

    /**
     * Ask the User a given question. It pops up an {@link MessageDialog} with
     * given title and message. It stores the decision in the
     * {@link PreferenceStore} it the checkbox is selected.
     * 
     * @param saros
     *            is needed to set the selection to preference store
     * @param preferenceName
     *            constant where to store in the preference store
     * 
     * @return boolean indicating whether the user said Yes or No
     */
    public static boolean popUpRememberDecisionDialog(final String title,
        final String message, final Saros saros, final String preferenceName) {
        try {
            return Utils.runSWTSync(new Callable<Boolean>() {
                public Boolean call() {
                    RememberDecisionMessageDialog dialog = new RememberDecisionMessageDialog(
                        EditorAPI.getShell(), title, null, message,
                        MessageDialog.QUESTION, new String[] {
                            IDialogConstants.YES_LABEL,
                            IDialogConstants.NO_LABEL }, 0);
                    int result = dialog.open();
                    if (dialog.isRememberDecision()) {
                        saros.getPreferenceStore().setValue(preferenceName,
                            Boolean.toString(result == 0));
                    }

                    return result == 0;
                }
            });
        } catch (Exception e) {
            log.error("An internal error ocurred while trying"
                + " to open the question dialog.");
            return false;
        }
    }

    /**
     * Closes a bytestreamSession without error output.
     * 
     * @param stream
     */
    public static void closeQuietly(BytestreamSession stream) {
        if (stream == null)
            return;
        try {
            stream.close();
        } catch (Exception e) {
            //
        }
    }

    /**
     * Concat <code>strings</code>. If <code>separator</code> is not
     * <code><b>null</b></code> or empty the separator will be put between the
     * strings but not after the last one
     * 
     * Example: <br />
     * <code>
     * separator = "-"; <br />
     * strings = ['a','b','c']; <br />
     * join(separator, strings) => 'a-b-c'</code>
     * 
     * @param separator
     * @param objects
     * @return
     */
    public static String join(String separator, Object... objects) {
        return join(separator, Arrays.asList(objects));
    }

    /**
     * Concatenates all elements of this collection. For each element the
     * toString method will be invoked and the separator will be appended but
     * not for the last element of the collection. The order of the output
     * depends on the iterator that is returned by the collection.
     * 
     * @param separator
     * @param objects
     * @return
     */

    public static String join(String separator, Collection<?> objects) {
        int length = objects.size();

        if (length == 0)
            return "";

        if (separator == null)
            separator = "";

        StringBuilder result = new StringBuilder(512);

        Iterator<?> it = objects.iterator();

        for (int i = 0; i < length - 1; i++)
            result.append(it.next()).append(separator);

        result.append(it.next());

        return result.toString();
    }
}
