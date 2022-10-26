package app.utils.machine_state.history_state;

import DTO.DTO_enigma.DTO_enigma_outputs.DTOencodeDecode;

public class EncodingState {
    String input;
    String output;
    long nanoSec;

    public EncodingState(DTOencodeDecode dtOencodeDecode){
        input = dtOencodeDecode.getInput();
        output = dtOencodeDecode.getOutput();
        nanoSec = dtOencodeDecode.getNanoSec();
    }

    public long getNanoSec() {
        return nanoSec;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }
}
