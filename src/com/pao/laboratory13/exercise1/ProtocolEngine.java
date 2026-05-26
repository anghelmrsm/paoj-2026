package com.pao.laboratory13.exercise1;

public class ProtocolEngine {
    private enum State { INIT, AUTH, OPEN, CLOSED }

    private State state = State.INIT;
    private int historyCount;

    public String execute(String input) {
        String line = input == null ? "" : input.trim();
        String[] tokens = line.isEmpty() ? new String[0] : line.split("\\s+");
        if (tokens.length == 0) {
            return "ERR E_PARSE UNKNOWN_COMMAND";
        }
        String operation = tokens[0];
        switch (operation) {
            case "AUTH":
                return auth(tokens);
            case "OPEN":
                return open(tokens);
            case "SEND":
                return message(tokens, false);
            case "BROADCAST":
                return message(tokens, true);
            case "HISTORY":
                return history(tokens);
            case "CLOSE":
                return close(tokens);
            default:
                return "ERR E_PARSE UNKNOWN_COMMAND";
        }
    }

    private String auth(String[] tokens) {
        if (tokens.length < 2) {
            return "ERR E_PARSE AUTH";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        state = State.AUTH;
        historyCount = 0;
        return "OK AUTH user=" + tokens[1];
    }

    private String open(String[] tokens) {
        if (tokens.length != 1) {
            return "ERR E_PARSE OPEN";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state == State.OPEN) {
            return "ERR E_STATE ALREADY_OPEN";
        }
        if (state != State.AUTH) {
            return "ERR E_STATE NOT_OPEN";
        }
        state = State.OPEN;
        return "OK OPEN";
    }

    private String message(String[] tokens, boolean broadcast) {
        String operation = broadcast ? "BROADCAST" : "SEND";
        if (tokens.length < 2) {
            return "ERR E_PARSE " + operation;
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state != State.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }
        historyCount++;
        return broadcast ? "OK OPEN broadcast" : "OK OPEN sent";
    }

    private String history(String[] tokens) {
        if (tokens.length != 1) {
            return "ERR E_PARSE HISTORY";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state != State.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }
        return "OK OPEN history=" + historyCount;
    }

    private String close(String[] tokens) {
        if (tokens.length != 1) {
            return "ERR E_PARSE CLOSE";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state != State.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }
        state = State.CLOSED;
        return "OK CLOSED";
    }
}
