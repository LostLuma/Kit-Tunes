package net.pixaurora.kit_tunes.impl.error;

import net.pixaurora.kit_tunes.impl.ui.text.Component;

public class ScrobblerSetupTimeoutException extends KitTunesException {
    private static final long serialVersionUID = 1L;

    private static final Component MESSAGE = Component.translatable("kit_tunes.error.scrobbler_setup.timeout");

    public ScrobblerSetupTimeoutException() {
        super("Scrobbler setup timed out.");
    }

    @Override
    public Component userMessage() {
        return MESSAGE;
    }

    @Override
    public boolean isPrinted() {
        return false;
    }
}
